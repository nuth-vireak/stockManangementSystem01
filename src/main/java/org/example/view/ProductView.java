package org.example.view;

import org.example.model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.File;
import java.util.List;

public class ProductView {

    public void renderDisplayMenu(List<Product> products, int currentPage, int totalPages) {
        renderProductListTable(products, currentPage, totalPages);

        Table tableOption = new Table(6, BorderStyle.DESIGN_PAPYRUS, ShownBorders.SURROUND_HEADER_FOOTER_AND_COLUMNS);

        tableOption.setColumnWidth(0, 16, 25);
        tableOption.setColumnWidth(1, 16, 25);
        tableOption.setColumnWidth(2, 16, 25);
        tableOption.setColumnWidth(3, 16, 25);
        tableOption.setColumnWidth(4, 16, 25);
        tableOption.setColumnWidth(5, 16, 25);

        tableOption.addCell("F) First");
        tableOption.addCell("P) Previous");
        tableOption.addCell("N) Next");
        tableOption.addCell("L) Last");
        tableOption.addCell("G) Go to");
        tableOption.addCell("");

        tableOption.addCell("*) Display");
        tableOption.addCell("W) Write");
        tableOption.addCell("R) Read");
        tableOption.addCell("U) Update");
        tableOption.addCell("D) Delete");
        tableOption.addCell("S) Search");
        tableOption.addCell("Se) Set row");
        tableOption.addCell("Sa) Save");
        tableOption.addCell("Un) Unsaved");
        tableOption.addCell("Ba) Backup");
        tableOption.addCell("Re) Restore");
        tableOption.addCell("E) Exit");

        System.out.println(tableOption.render());

        System.out.println("--------------------------");
    }

    public void renderUnsavedInsertedProductListTable(List<Product> unSavedProducts) {

        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 20, 25);
        table.setColumnWidth(1, 25, 25);
        table.setColumnWidth(2, 25, 25);
        table.setColumnWidth(3, 20, 25);

        table.addCell("Unsaved Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 4);
        table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        unSavedProducts.forEach(product -> {
            table.addCell(product.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getPrice()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getQty()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getImportedDate()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });

        System.out.println(table.render());
    }

    public void renderProductListTable(List<Product> products, int currentPage, int totalPages) {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 20, 25);
        table.setColumnWidth(1, 25, 25);
        table.setColumnWidth(2, 25, 25);
        table.setColumnWidth(3, 20, 25);
        table.setColumnWidth(4, 20, 25);

        table.addCell("Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        table.addCell("ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        if (!products.isEmpty()) {
            products.forEach(product -> {
                table.addCell(String.valueOf(product.getId()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(product.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(String.valueOf(product.getPrice()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(String.valueOf(product.getQty()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(String.valueOf(product.getImportedDate()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
        } else {
            table.addCell("No data available", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        }

        table.addCell("Page " + currentPage + " of " + totalPages, new CellStyle(CellStyle.HorizontalAlign.LEFT), 5);

        System.out.println(table.render());
    }

    public void renderProductDetailsTable(Product product) {
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        table.setColumnWidth(0, 40, 80);
        table.addCell("Product Detail", new CellStyle(CellStyle.HorizontalAlign.CENTER), 1);

        table.addCell(" ".repeat(4) + "ID : " + product.getId());
        table.addCell(" ".repeat(4) + "Name : " + product.getName());
        table.addCell(" ".repeat(4) + "Unit Price : " + product.getPrice());
        table.addCell(" ".repeat(4) + "Qty : " + product.getQty());
        table.addCell(" ".repeat(4) + "Imported Date : " + product.getImportedDate());

        System.out.println(table.render());
    }

    public void renderProductTable(List<Product> productDAOByName) {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 20, 25);
        table.setColumnWidth(1, 25, 25);
        table.setColumnWidth(2, 25, 25);
        table.setColumnWidth(3, 20, 25);
        table.setColumnWidth(4, 20, 25);

        table.addCell("ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        productDAOByName.forEach(product -> {
            table.addCell(String.valueOf(product.getId()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(product.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getPrice()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getQty()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getImportedDate()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });

        System.out.println(table.render());
    }

    public void renderUnsavedUpdatedProductListTable(List<Product> unSavedUpdatedProducts) {
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 20, 25);
        table.setColumnWidth(1, 25, 25);
        table.setColumnWidth(2, 25, 25);
        table.setColumnWidth(3, 20, 25);

        table.addCell("Unsaved Updated Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        unSavedUpdatedProducts.forEach(product -> {
            table.addCell(product.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getPrice()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getQty()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(String.valueOf(product.getImportedDate()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });

        System.out.println(table.render());
    }

    public void renderDatabaseBackupFileTable() {
        String homeDirectory = System.getProperty("user.home");
        String backupFolder = homeDirectory + File.separator + "backup-product-data";
        File file = new File(backupFolder);
        File[] files = file.listFiles();

        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 8, 80);
        table.setColumnWidth(1, 50, 80);

        table.addCell("Backup List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 2);

        if (files != null) {
            int id = 1;
            for (File f : files) {
                if (f.isFile()) {
                    table.addCell(String.valueOf(id), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    table.addCell(f.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    id++;
                }
            }
        }

        System.out.println(table.render());
    }
}
