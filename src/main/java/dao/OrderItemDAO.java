package dao;

import model.OrderItem;
import model.OrderItemStatus;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public List<OrderItem> getItemsByOrderId(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "select oi.*, mi.product_name from order_items oi " +
                "join menu_items mi on oi.item_id = mi.item_id " +
                "where oi.order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("item_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price"),
                            OrderItemStatus.valueOf(rs.getString("status")),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    item.setItemName(rs.getString("product_name"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public void updateItemStatus(int orderItemId, OrderItemStatus status) throws SQLException {
        String sql = "update order_items set status = ? where order_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, orderItemId);
            ps.executeUpdate();
        }
    }

    public void cancelItem(int orderItemId) throws SQLException {
        String sql = "delete from order_items where order_item_id = ? and status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            ps.executeUpdate();
        }
    }


    //cehf logic
    public List<OrderItem> getAllActiveItems() throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "select oi.*, mi.product_name from order_items oi " +
                "join menu_items mi on oi.item_id = mi.item_id " +
                "where oi.status != 'SERVED' " +
                "order by oi.created_at asc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrderItem item = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        OrderItemStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                item.setItemName(rs.getString("product_name"));
                items.add(item);
            }
        }
        return items;
    }
}