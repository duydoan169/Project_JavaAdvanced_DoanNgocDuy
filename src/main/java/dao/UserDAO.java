package dao;

import model.Role;
import model.User;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role")),
                        rs.getBoolean("is_active"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return users;
    }

    public User findUserById(int userId) throws SQLException{
        String sql = "select * from users where user_id = ?";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role")),
                            rs.getBoolean("is_active"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    public User findUserByName(String username) throws SQLException{
        String sql = "select * from users where username = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role")),
                            rs.getBoolean("is_active"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    public void createUser(String username, String password, Role role) throws SQLException {
        String sql = "insert into users (username, password, role) values (? , ? , ?)";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, String.valueOf(role));
            ps.executeUpdate();
        }
    }

    public void setUserStatus(int userId, boolean bool) throws SQLException {
        String sql = "update users set is_active = ? where user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, bool);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}
