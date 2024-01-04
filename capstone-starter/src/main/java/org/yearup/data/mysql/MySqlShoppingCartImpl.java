/*package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlShoppingCartImpl extends MySqlDaoBase implements ShoppingCartDao {


    @Autowired
    public MySqlShoppingCartImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();

        String sql = "SELECT shopping_cart.user_id, shopping_cart.quantity, products.* " +
                "FROM shopping_cart " +
                "JOIN products ON shopping_cart.product_id = products.product_id " +
                "WHERE shopping_cart.user_id = ?";
        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql) ){
            ps.setInt(1,userId);
            ResultSet row = ps.executeQuery();

            while(row.next()){
                ShoppingCartItem shoppingCartItem = mapRow(row);
                shoppingCart.add(shoppingCartItem);
            }
        }
        catch (SQLException exception){
            throw new RuntimeException(exception);
        }
        return shoppingCart;
    }
    @Override
    public void saveOrUpdate(ShoppingCart cart) {
        String insertQuery = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement insertPs = conn.prepareStatement(insertQuery);
             PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {

            for (ShoppingCartItem item : cart.getItems().values()) {
                if (item != null && item.getProduct() != null) {
                    int productId = item.getProduct().getProductId();
                    int quantity = item.getQuantity();

                    if (productId != null) {
                        updatePs.setInt(1, quantity);
                        updatePs.setInt(2, cart.getUserId()); // Using the userId from the cart object
                        updatePs.setInt(3, productId);
                        updatePs.addBatch();
                    } else {
                        insertPs.setInt(1, cart.getUserId()); // Using the userId from the cart object
                        insertPs.setInt(2, productId);
                        insertPs.setInt(3, quantity);
                        insertPs.addBatch();
                    }
                }
            }

            updatePs.executeBatch();
            insertPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void addProductToCart(int userId, int productId) {
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1;";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3,1);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);

            int updated = ps.executeUpdate();

            if(updated == 0){
                throw new RuntimeException("Item not found");
            }

        } catch (SQLException e) {
            // Log and handle the exception
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            // Log and handle the exception
            e.printStackTrace();
        }
    }

    private static ShoppingCartItem mapRow(ResultSet row) throws SQLException{
        int productId = row.getInt("product_id");
        int quantity = row.getInt("quantity");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        Product product = new Product(productId,name,price,categoryId,description,color,stock,isFeatured,imageUrl);
        shoppingCartItem.setProduct(product);
        shoppingCartItem.setQuantity(quantity);

        return shoppingCartItem;
    }

}

 */
