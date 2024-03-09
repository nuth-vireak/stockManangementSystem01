package org.example.controller;

import org.example.dao.ProductDAO;
import org.example.dao.ProductDaoImpl;
import org.example.model.Product;
import org.example.model.ProductModel;
import org.example.view.ProductView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    static enum TEXT_COLOR{
        RESET("\u001B\0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m");
        private String colorCode;
        TEXT_COLOR(String colorCode){
            this.colorCode = colorCode;
        }
        public String getColorCode() {
            return colorCode;
        }
    }

    int currentPage = 1;
    int pageSize = 5; // Number of items per page

    private final Scanner input = new Scanner(System.in);
    private final ProductDAO productDAO = new ProductDaoImpl();

    private final ProductModel model;
    private final ProductView view;

    public ProductController(ProductModel model, ProductView view) {
        this.model = model;
        this.view = view;
    }

    private void writeProductToList() {
        System.out.print(TEXT_COLOR.BLUE.getColorCode()+"Enter product name : ");
        String name = input.nextLine();
        System.out.print(TEXT_COLOR.BLUE.getColorCode()+"Enter product price : ");
        double price = input.nextDouble();
        System.out.print(TEXT_COLOR.BLUE.getColorCode()+"Enter product quantity : ");
        int qty = input.nextInt();
        input.nextLine();

        Product product = new Product(name, price, qty, LocalDate.now());
        model.addProduct(product);
    }

    public void userOption() throws SQLException, IOException, InterruptedException {

        String option;

        do {
            List<Product> products = productDAO.getProductsByPage(currentPage, pageSize);
            int totalPages = productDAO.getTotalPages(pageSize);
            view.renderDisplayMenu(products, currentPage, totalPages);
            System.out.print(TEXT_COLOR.BLUE.getColorCode()+"-> Choose a options() : ");
            option = input.nextLine();

            switch (option) {
                case "f":
                    if (currentPage > 1) {
                        currentPage = 1;
                    } else {
                        System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Already on the first page");
                    }
                    break;
                case "p":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Already on the first page");
                    }
                    break;
                case "n":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Already on the last page");
                    }
                    break;
                case "l":
                    if (currentPage < totalPages) {
                        currentPage = totalPages;
                    } else {
                        System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Already on the last page");
                    }
                    break;
                case "g":
                    System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Go to");
                    break;
                case "*":
                    System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Display");
                    break;
                case "w":
                    writeProductToList();
                    break;
                case "r":
                    viewProductDetails();
                    System.out.print(TEXT_COLOR.BLUE.getColorCode()+"Press any key to continue...");
                    input.nextLine();
                    break;
                case "u":
                    updateProduct();
                    break;
                case "d":
                    deleteProductById();
                    break;
                case "s":
                    searchProduct();
                    break;
                case "se":
                    System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Set Row");
                    break;
                case "sa":
                    insertUnsavedOrUnsavedUpdateProduct();
                    break;
                case "un":
                    view.renderUnsavedInsertedProductListTable(model.getUnSavedInsertedProducts());
                    displayUnsavedProductList();
                    break;
                case "ba":
                    System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Backup");
                    backupProductDataFromDatabase();
                    break;
                case "re":
                    System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Restore");
                    displayDatabaseBackupFileTable();
                    restoreProductDataFromBackup();
                    break;
                case "e":
                    System.out.println(TEXT_COLOR.RED.getColorCode()+"Exit");
                    break;
                default:
                    System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Invalid option");
                    break;
            }
        } while (!option.equals("e"));
    }

    private void displayDatabaseBackupFileTable() {
        view.renderDatabaseBackupFileTable();
//        productDAO.resetBackupFileCounter();
    }

    private void restoreProductDataFromBackup() {

        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Enter Number to restore or 0 to cancel : ");
        int number = input.nextInt();
        input.nextLine();
        if (number == 0) {
            return;
        }
        productDAO.restoreProductData(number);
    }

    private void backupProductDataFromDatabase() throws IOException, InterruptedException {
        productDAO.backupProductData();
    }


    private void updateProduct() {
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Input ID to update product : ");
        int id = input.nextInt();
        input.nextLine();
        Product productToUpdate = productDAO.getById(id);

        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Update product Name To : ");
        String name = input.nextLine();
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Update product Price To : ");
        double price = input.nextDouble();
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Update product Quantity To : ");
        int qty = input.nextInt();
        input.nextLine();

        productToUpdate.setName(name);
        productToUpdate.setPrice(price);
        productToUpdate.setQty(qty);
        productToUpdate.setImportedDate(LocalDate.now());

        model.addUnSavedUpdatedProduct(productToUpdate);

    }

    private void deleteProductById() {
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Input ID to show product : ");
        int id = input.nextInt();
        input.nextLine();
        Product productDAOById = productDAO.getById(id);
        view.renderProductDetailsTable(productDAOById);
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Enter Y to confirm or B for back to display : ");
        String option = input.nextLine();

        switch (option) {
            case "y":
                productDAO.deleteProductById(id);
                break;
            case "b":
                break;
            default:
                System.out.println(TEXT_COLOR.GREEN.getColorCode()+"Invalid option");
                break;
        }
    }

    private void searchProduct() {
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"-> Input Product's name to search : ");
        String name = input.nextLine();
        List<Product> productsDAOByName = productDAO.getByName(name);
        view.renderProductTable(productsDAOByName);
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"Press 1 to search again and 0 to cancel : ");
        int option = input.nextInt();
        input.nextLine();

        switch (option) {
            case 1:
                searchProduct();
                break;
            case 0:
                break;
            default:
                System.out.println(TEXT_COLOR.GREEN.getColorCode()+"Invalid option");
                break;
        }
    }

    private void viewProductDetails() {
        System.out.print(TEXT_COLOR.GREEN.getColorCode()+"Enter ID to show product details : ");
        int id = input.nextInt();
        input.nextLine();
        Product product = productDAO.getById(id);
        view.renderProductDetailsTable(product);
    }

    private void insertUnsavedOrUnsavedUpdateProduct() throws SQLException {
        System.out.println(TEXT_COLOR.GREEN.getColorCode()+"Do you want to save Unsaved Inserted or Unsaved Updated? Please choose one of them!");
        System.out.println(TEXT_COLOR.GREEN.getColorCode()+"\"Ui\" for Unsaved Inserted and \"Uu\" for Unsaved Updated and \"B\" for back to main menu : ");
        String option = input.nextLine();

        if (option.equalsIgnoreCase("Ui")) {
            if (!model.getUnSavedInsertedProducts().isEmpty()) {
                productDAO.insert(model.getUnSavedInsertedProducts());
                model.getUnSavedInsertedProducts().clear();
                System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Unsaved Inserted");
                insertUnsavedOrUnsavedUpdateProduct();
            } else {
                System.out.println(TEXT_COLOR.BLUE.getColorCode()+"No unsaved inserted products to save.");
                insertUnsavedOrUnsavedUpdateProduct();
            }
        } else if (option.equalsIgnoreCase("Uu")) {
            if (!model.getUnSavedUpdatedProducts().isEmpty()) {
                model.getUnSavedUpdatedProducts().forEach(updatedProduct -> {
                    Product product = productDAO.getById(updatedProduct.getId());
                    if (product != null) {
                        productDAO.updateProducts(Collections.singletonList(updatedProduct));
                    } else {
                        System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Cannot update product with id " + updatedProduct.getId() + " because it does not exist in the database");
                    }
                });
                model.getUnSavedUpdatedProducts().clear();
                insertUnsavedOrUnsavedUpdateProduct();
            } else {
                System.out.println(TEXT_COLOR.BLUE.getColorCode()+"No unsaved updated products to save.");
                insertUnsavedOrUnsavedUpdateProduct();
            }
        } else {
            System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Back to main menu");
        }
    }

    private void displayUnsavedProductList() {
        System.out.print("\"I\" for unsaved Insertion and \"U\" for unsaved Update and \"B\" for back to main menu : ");
        String option = input.nextLine();

        switch (option) {
            case "i":
                view.renderUnsavedInsertedProductListTable(model.getUnSavedInsertedProducts());
                displayUnsavedProductList();
                break;
            case "u":
                view.renderUnsavedUpdatedProductListTable(model.getUnSavedUpdatedProducts());
                displayUnsavedProductList();
                break;
            case "b":
                break;
            default:
                System.out.println(TEXT_COLOR.BLUE.getColorCode()+"Invalid option");
                break;
        }

    }
}
