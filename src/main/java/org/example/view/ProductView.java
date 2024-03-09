package org.example.view;

import org.example.model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.File;
import java.util.List;

public class ProductView {
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

    public void renderDisplayMenu(List<Product> products, int currentPage, int totalPages) {
        renderProductListTable(products, currentPage, totalPages);

        Table tableOption = new Table(6, BorderStyle.DESIGN_PAPYRUS, ShownBorders.SURROUND_HEADER_FOOTER_AND_COLUMNS);

        tableOption.setColumnWidth(0, 16, 25);
        tableOption.setColumnWidth(1, 16, 25);
        tableOption.setColumnWidth(2, 16, 25);
        tableOption.setColumnWidth(3, 16, 25);
        tableOption.setColumnWidth(4, 16, 25);
        tableOption.setColumnWidth(5, 16, 25);

        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"F) First");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"P) Previous");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"N) Next");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"L) Last");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"G) Go to");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"");

        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"*) Display");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"W) Write");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"R) Read");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"U) Update");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"D) Delete");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"S) Search");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"Se) Set row");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"Sa) Save");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"Un) Unsaved");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"Ba) Backup");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"Re) Restore");
        tableOption.addCell(TEXT_COLOR.GREEN.getColorCode()+"E) Exit");

        System.out.println(tableOption.render());

        System.out.println(TEXT_COLOR.BLUE.getColorCode()+"--------------------------");
    }

    public void renderUnsavedInsertedProductListTable(List<Product> unSavedProducts) {

        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 20, 25);
        table.setColumnWidth(1, 25, 25);
        table.setColumnWidth(2, 25, 25);
        table.setColumnWidth(3, 20, 25);

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Unsaved Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 4);
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

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

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        if (!products.isEmpty()) {
            products.forEach(product -> {
                table.addCell(String.valueOf(product.getId()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(product.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(String.valueOf(product.getPrice()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(String.valueOf(product.getQty()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(String.valueOf(product.getImportedDate()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            });
        } else {
            table.addCell(TEXT_COLOR.GREEN.getColorCode()+"No data available", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        }

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Page " + currentPage + " of " + totalPages, new CellStyle(CellStyle.HorizontalAlign.LEFT), 5);

        System.out.println(table.render());
    }

    public void renderProductDetailsTable(Product product) {
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.SURROUND_HEADER_AND_COLUMNS);
        table.setColumnWidth(0, 40, 80);
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Product Detail", new CellStyle(CellStyle.HorizontalAlign.CENTER), 1);

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+" ".repeat(4) + "ID : " + product.getId());
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+" ".repeat(4) + "Name : " + product.getName());
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+" ".repeat(4) + "Unit Price : " + product.getPrice());
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+" ".repeat(4) + "Qty : " + product.getQty());
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+" ".repeat(4) + "Imported Date : " + product.getImportedDate());

        System.out.println(table.render());
    }

    public void renderProductTable(List<Product> productDAOByName) {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 20, 25);
        table.setColumnWidth(1, 25, 25);
        table.setColumnWidth(2, 25, 25);
        table.setColumnWidth(3, 20, 25);
        table.setColumnWidth(4, 20, 25);

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

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

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Unsaved Updated Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        unSavedUpdatedProducts.forEach(product -> {
            table.addCell(TEXT_COLOR.GREEN.getColorCode()+product.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(TEXT_COLOR.GREEN.getColorCode()+String.valueOf(product.getPrice()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(TEXT_COLOR.GREEN.getColorCode()+String.valueOf(product.getQty()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(TEXT_COLOR.GREEN.getColorCode()+String.valueOf(product.getImportedDate()), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        });

        System.out.println(table.render());
    }

    public void renderDatabaseBackupFileTable() {
        String homeDirectory = System.getProperty(TEXT_COLOR.GREEN.getColorCode()+"user.home");
        String backupFolder = homeDirectory + File.separator + "backup-product-data";
        File file = new File(backupFolder);
        File[] files = file.listFiles();

        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        table.setColumnWidth(0, 8, 80);
        table.setColumnWidth(1, 50, 80);

        table.addCell(TEXT_COLOR.GREEN.getColorCode()+"Backup List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 2);

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
