package org.example;

import org.example.controller.ProductController;
import org.example.model.ProductModel;
import org.example.view.ProductView;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        ProductView view = new ProductView();
        ProductModel model = new ProductModel();
        ProductController productController = new ProductController(model, view);
        productController.userOption();
    }
}