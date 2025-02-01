package org.example.repo;

import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.config.Configs.*;

public class UserRepositoryImpl implements UserRepository {

    private Connection conn; // Shared connection across methods

    public UserRepositoryImpl() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        System.out.println("Trying to connect to db");
        try {
            // Reusing the same connection for all database operations
            conn = DriverManager.getConnection(IN_MEMORY_DB_URL, IN_MEMORY_DB_USER, IN_MEMORY_DB_PASSWORD);
            System.out.println("Connected to db");

            // Create table if it doesn't exist
            String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY, " +
                    "name VARCHAR(100))";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTable);
                System.out.println("Table created or already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to db");
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        String query = "MERGE INTO users KEY(id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findUserById(int id) {
        String query = "SELECT id, name FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, name FROM users";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE users SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
