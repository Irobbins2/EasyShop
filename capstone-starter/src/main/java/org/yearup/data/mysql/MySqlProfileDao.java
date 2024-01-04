package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {
    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, profile.getUserId());
            statement.setString(2, profile.getFirstName());
            statement.setString(3, profile.getLastName());
            statement.setString(4, profile.getPhone());
            statement.setString(5, profile.getEmail());
            statement.setString(6, profile.getAddress());
            statement.setString(7, profile.getCity());
            statement.setString(8, profile.getState());
            statement.setString(9, profile.getZip());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Creating profile failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profile.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating profile failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating profile in the database", e);
        }
    }

    @Override
    public Profile getProfileById(int userId) {
        Profile profile = null;
        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    profile = mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching profile by ID from the database", e);
        }

        return profile;
    }

    @Override
    public void saveOrUpdate(Profile profile) {
        String sql = "UPDATE profiles SET first_name=?, last_name=?, phone=?, email=?, address=?, city=?, state=?, zip=? WHERE user_id=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getEmail());
            statement.setString(5, profile.getAddress());
            statement.setString(6, profile.getCity());
            statement.setString(7, profile.getState());
            statement.setString(8, profile.getZip());
            statement.setInt(9, profile.getUserId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Updating profile failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile in the database", e);
        }
    }

    @Override
    public void delete(Profile profile) {
        String sql = "DELETE FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, profile.getUserId());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Deleting profile failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting profile from the database", e);
        }
    }

    private Profile mapRow(ResultSet row) throws SQLException {
        return new Profile(
                row.getInt("user_id"),
                row.getString("first_name"),
                row.getString("last_name"),
                row.getString("phone"),
                row.getString("email"),
                row.getString("address"),
                row.getString("city"),
                row.getString("state"),
                row.getString("zip")
        );
    }
}

