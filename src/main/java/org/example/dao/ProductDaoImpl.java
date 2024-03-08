package org.example.dao;

import org.example.model.Product;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProductDaoImpl implements ProductDAO {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/product_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";
    private static final String COUNTER_FILE_PATH = System.getProperty("user.home") + File.separator + "backup_counter.txt";
    private int backupCounter = 1;

    @Override
    public void insert(List<Product> products) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS product (" +
                    "id SERIAL PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "unit_price DECIMAL(10, 2)," +
                    "qty INTEGER," +
                    "imported_date DATE" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSql);
            }

            String sql = "INSERT INTO product (name, unit_price, qty, imported_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Product p : products) {
                    pstmt.setString(1, p.getName());
                    pstmt.setDouble(2, p.getPrice());
                    pstmt.setInt(3, p.getQty());
                    pstmt.setDate(4, Date.valueOf(p.getImportedDate()));
                    pstmt.executeUpdate();
                }
            }
        }
    }

    @Override
    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the "product" table exists
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "product", null);
            if (!tables.next()) {
                // Table does not exist, create it
                String createTableSql = "CREATE TABLE product (" +
                        "id SERIAL PRIMARY KEY," +
                        "name VARCHAR(20)," +
                        "unit_price DOUBLE PRECISION," +
                        "qty INTEGER," +
                        "imported_date DATE" +
                        ")";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSql);
                }
            }
            // Now you can execute your original query
            String sql = "SELECT * FROM product";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Product product = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("unit_price"),
                                rs.getInt("qty"),
                                rs.getDate("imported_date").toLocalDate()
                        );
                        products.add(product);
                    }
                }
            }
        }
        products.sort(Comparator.comparing(Product::getId));
        return products;
    }

    @Override
    public Product getById(int id) {
        Product product = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM product WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        product = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("unit_price"),
                                rs.getInt("qty"),
                                rs.getDate("imported_date").toLocalDate()
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> getByName(String name) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM product WHERE LOWER(name) ILIKE LOWER(?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + name + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Product product = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("unit_price"),
                                rs.getInt("qty"),
                                rs.getDate("imported_date").toLocalDate()
                        );
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void deleteProductById(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM product WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProducts(List<Product> unSavedUpdatedProducts) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE product SET name = ?, unit_price = ?, qty = ?, imported_date = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Product p : unSavedUpdatedProducts) {
                    pstmt.setString(1, p.getName());
                    pstmt.setDouble(2, p.getPrice());
                    pstmt.setInt(3, p.getQty());
                    pstmt.setDate(4, Date.valueOf(p.getImportedDate()));
                    pstmt.setInt(5, p.getId());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void backupProductData() throws IOException, InterruptedException {

        // Read the current counter value from the file
        int backupCounter = readCounterFromFile();

        String homeDirectory = System.getProperty("user.home");
        String backupFolder = homeDirectory + File.separator + "backup-product-data";
        // Create the backup folder if it does not exist
        new File(backupFolder).mkdirs();
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-dd-MM_hh-mm-ss a"));
        String backupFileName = backupCounter + "-product-backup_" + timeStamp + ".sql";
        String backupPath = backupFolder + File.separator + backupFileName;

        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\15\\bin\\pg_dump.exe",
                "--host", "localhost",
                "--port", "5432",
                "--username", "postgres",
                "--format", "plain",
                "--file", backupPath,
                "product_db"
        );

        try {
            final Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", "1234");
            Process p = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                System.err.println(line);
                line = r.readLine();
            }
            r.close();
            p.waitFor();
            System.out.println(p.exitValue());

            // Increment the counter and save it to the file for the next backup
            backupCounter++;
            writeCounterToFile(backupCounter);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void restoreProductData(int number) {
        String homeDirectory = System.getProperty("user.home");
        String backupFolder = homeDirectory + File.separator + "backup-product-data";
        File folder = new File(backupFolder);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No backup files found");
            return;
        }
        if (number < 1 || number > files.length) {
            System.out.println("Invalid backup number");
            return;
        }
        String backupFileName = files[number - 1].getName();
        String backupPath = backupFolder + File.separator + backupFileName;

        // Drop the existing table
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DROP TABLE IF EXISTS product";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error dropping table: " + e.getMessage());
        }

        // Restore the backup
        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\15\\bin\\psql.exe",
                "--host", "localhost",
                "--port", "5432",
                "--username", "postgres",
                "--dbname", "product_db",
                "--file", backupPath
        );

        try {
            final Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", "1234");
            Process p = pb.start();
            final BufferedReader r = new BufferedReader(
                    new InputStreamReader(p.getErrorStream()));
            String line = r.readLine();
            while (line != null) {
                System.err.println(line);
                line = r.readLine();
            }
            r.close();
            p.waitFor();
            System.out.println(p.exitValue());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private int readCounterFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE_PATH))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            // Handle exceptions (e.g., file not found, invalid format)
        }
        return 1; // Default value if counter file doesn't exist or cannot be read
    }

    private void writeCounterToFile(int counter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE_PATH))) {
            writer.write(String.valueOf(counter));
        } catch (IOException e) {
            // Handle file write errors
        }
    }

    @Override
    public void resetBackupFileCounter() {
        writeCounterToFile(1);
    }

    @Override
    public List<Product> getProductsByPage(int currentPage, int pageSize) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM product ORDER BY id OFFSET ? LIMIT ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, (currentPage - 1) * pageSize);
                pstmt.setInt(2, pageSize);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Product product = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("unit_price"),
                                rs.getInt("qty"),
                                rs.getDate("imported_date").toLocalDate()
                        );
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return products;
    }

    @Override
    public int getTotalPages(int pageSize) {
        int totalProducts = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT COUNT(*) FROM product";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        totalProducts = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return (int) Math.ceil((double) totalProducts / pageSize);
    }
}
