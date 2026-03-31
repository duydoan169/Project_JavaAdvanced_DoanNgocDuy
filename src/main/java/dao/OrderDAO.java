package dao;

import model.*;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public void createOrder(int tableId, int customerId, List<OrderItem> items) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            int orderId;
            try {
                String orderSql = "insert into orders (table_id, customer_id, status) values (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, tableId);
                    ps.setInt(2, customerId);
                    ps.setString(3, "OPEN");
                    ps.executeUpdate();

                    try(ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) orderId = rs.getInt(1);
                        else throw new SQLException("Tạo đơn hàng thất bại");
                    }
                }

                String itemSql = "insert into order_items (order_id, item_id, quantity, unit_price) values (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                    for (OrderItem item : items) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getItemId());
                        ps.setInt(3, item.getQuantity());
                        ps.setDouble(4, item.getUnitPrice());
                        ps.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public Order getOpenOrderByCustomer(int customerId) throws SQLException {
        String sql = "select * from orders where customer_id = ? and status = 'OPEN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("order_id"),
                            rs.getInt("table_id"),
                            rs.getInt("customer_id"),
                            OrderStatus.valueOf(rs.getString("status")),
                            rs.getDouble("total_price"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("checkout_at") != null ? rs.getTimestamp("checkout_at").toLocalDateTime() : null
                    );
                }
            }
        }

        return null;
    }

    public List<Order> getAllOrdersByCustomerId(int customerId) throws SQLException{
        List<Order> orders = new ArrayList<>();
        String sql = "select * from orders where customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                            rs.getInt("order_id"),
                            rs.getInt("table_id"),
                            rs.getInt("customer_id"),
                            OrderStatus.valueOf(rs.getString("status")),
                            rs.getDouble("total_price"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("checkout_at") != null ? rs.getTimestamp("checkout_at").toLocalDateTime() : null
                    ));
                }
            }
        }
        return orders;
    }


    public void updateOrderCheckOut(int orderId, OrderStatus status, double total) throws SQLException{
        String sql = "update orders set checkout_at = ?, status = ?,  total_price = ? where order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, String.valueOf(status));
            ps.setDouble(3, total);
            ps.setInt(4, orderId);
            ps.executeUpdate();
        }
    }

    public Order getOrderById(int orderId) throws SQLException {
        String sql = "select * from orders where order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("order_id"),
                            rs.getInt("table_id"),
                            rs.getInt("customer_id"),
                            OrderStatus.valueOf(rs.getString("status")),
                            rs.getDouble("total_price"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("checkout_at") != null ? rs.getTimestamp("checkout_at").toLocalDateTime() : null
                    );
                }
            }
        }
        return null;
    }
}
