package org.example.dao;

import org.example.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    void insert(List<Product> product) throws SQLException;
    List<Product> getAll() throws SQLException;
    Product getById(int id);
    List<Product> getByName(String name);
    void deleteProductById(int id);
    void updateProducts(List<Product> unSavedUpdatedProducts);
    void backupProductData() throws IOException, InterruptedException;
    void restoreProductData(int number);
    void resetBackupFileCounter();
    List<Product> getProductsByPage(int currentPage, int pageSize);
    int getTotalPages(int pageSize);
}
