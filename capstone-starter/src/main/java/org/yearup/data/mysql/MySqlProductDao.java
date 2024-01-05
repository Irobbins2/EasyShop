package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlProductDao extends MySqlDaoBase implements ProductDao {

    public MySqlProductDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE 1=1";

        if (categoryId != null) {
            sql += " AND category_id = ?";
        }

        if (minPrice != null) {
            sql += " AND price >= ?";
        }

        if (maxPrice != null) {
            sql += " AND price <= ?";
        }

        if (color != null && !color.isEmpty()) {
            sql += " AND color = ?";
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int parameterIndex = 1;
            if (categoryId != null) {
                statement.setInt(parameterIndex++, categoryId);
            }

            if (minPrice != null) {
                statement.setBigDecimal(parameterIndex++, minPrice);
            }

            if (maxPrice != null) {
                statement.setBigDecimal(parameterIndex++, maxPrice);
            }

            if (color != null && !color.isEmpty()) {
                statement.setString(parameterIndex, color);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = mapRow(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Product product = mapRow(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL query", e);
        }

        return products;
    }

    @Override
    public Product getById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }

        return null;
    }

    @Override
    public Product create(Product product) {
        String sql = "INSERT INTO products(name, price, category_id, description, color, image_url, stock, featured) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            setProductParameters(statement, product);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                int productId = retrieveGeneratedProductId(statement);
                return getById(productId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating product", e);
        }

        return null;
    }

    @Override
    public void update(int productId, Product product) {
        String sql = "UPDATE products" +
                " SET name = ?," +
                "     price = ?," +
                "     category_id = ?," +
                "     description = ?," +
                "     color = ?," +
                "     image_url = ?," +
                "     stock = ?," +
                "     featured = ?" +
                " WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setProductParameters(statement, product);
            statement.setInt(9, productId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Product with ID " + productId + " not found for update.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Product with ID " + productId + " not found for deletion.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    protected static Product mapRow(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }

    private void setProductParameters(PreparedStatement statement, Product product) throws SQLException {
        statement.setString(1, product.getName());
        statement.setBigDecimal(2, product.getPrice());
        statement.setInt(3, product.getCategoryId());
        statement.setString(4, product.getDescription());
        statement.setString(5, product.getColor());
        statement.setString(6, product.getImageUrl());
        statement.setInt(7, product.getStock());
        statement.setBoolean(8, product.isFeatured());
    }

    private int retrieveGeneratedProductId(PreparedStatement statement) throws SQLException {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        throw new SQLException("Creating product failed, no ID obtained.");
    }
}