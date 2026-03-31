package dao;

import model.ItemType;
import model.MenuItem;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "select * from menu_items";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                menuItems.add(new MenuItem(
                    rs.getInt("item_id"),
                    rs.getString("product_name"),
                    ItemType.valueOf(rs.getString("item_type")),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getBoolean("is_available"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return menuItems;
    }

    public MenuItem findMenuItemById(int itemId) throws SQLException{
        String sql = "select * from menu_items where item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    return new MenuItem(
                            rs.getInt("item_id"),
                            rs.getString("product_name"),
                            ItemType.valueOf(rs.getString("item_type")),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getBoolean("is_available"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    public void createMenuItem(String productName, ItemType itemType, double price, int stock) throws SQLException{
        String sql = "insert into menu_items (product_name, item_type, price, stock) values (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, productName);
            ps.setString(2, String.valueOf(itemType));
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.executeUpdate();
        }
    }

    public void deleteMenuItem(int itemId) throws SQLException{
        String sql = "delete from menu_items where item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    public void updateMenuItem(int itemId, String productName, ItemType itemType, double price, int stock, boolean isAvailable) throws SQLException{
        String sql = "update menu_items set product_name = ?, item_type = ?, price = ?, stock = ?, is_available = ? where item_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            ps.setString(2, String.valueOf(itemType));
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setBoolean(5, isAvailable);
            ps.setInt(6, itemId);
            ps.executeUpdate();
        }
    }
}
