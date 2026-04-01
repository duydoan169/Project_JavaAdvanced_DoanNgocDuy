package service;

import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.TableDAO;
import model.*;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private static final OrderService instance = new OrderService();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final TableDAO tableDAO = new TableDAO();

    private OrderService() {}

    public static OrderService getInstance() { return instance; }

    public void placeOrder(int tableId, List<OrderItem> items) {
        try {
            int customerId = SessionManager.getCurrentUser().getUserId();

//            Order existing = orderDAO.getOpenOrderByCustomer(customerId);
//
//            if (existing != null) {
//                System.out.println("Bạn đang có một đơn hàng chưa thanh toán");
//                return;
//            }

//            Table table = tableDAO.findTableById(tableId);
//            if (table == null) {
//                System.out.println("Bàn không tồn tại");
//                return;
//            }
//
//            if (table.getStatus() == TableStatus.OCCUPIED) {
//                System.out.println("Bàn đang có người");
//                return;
//            }
//
//
//            if (items == null || items.isEmpty()) {
//                System.out.println("Đơn hàng phải có ít nhất 1 món");
//                return;
//            }

            Table table = tableDAO.findTableById(tableId);
            orderDAO.createOrder(tableId, customerId, items);

            tableDAO.updateTable(tableId, table.getCapacity(), TableStatus.OCCUPIED);
            TableService.getInstance().refreshTables();
            System.out.println("Đặt món thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public Order getActiveOrder() {
        try {
            int customerId = SessionManager.getCurrentUser().getUserId();
            return orderDAO.getOpenOrderByCustomer(customerId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    public List<Order> getAllOrders() {
        try {
            int customerId = SessionManager.getCurrentUser().getUserId();
            return orderDAO.getAllOrdersByCustomerId(customerId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    public List<OrderItem> getOrderItems(int orderId) {
        try {
            return orderItemDAO.getItemsByOrderId(orderId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    public void cancelItem(int orderItemId) {
        try {
            orderItemDAO.cancelItem(orderItemId);
            System.out.println("Hủy món thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void updateItemStatus(int orderItemId, OrderItemStatus status) {
        try {
            orderItemDAO.updateItemStatus(orderItemId, status);
            System.out.println("Cập nhật trạng thái thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void checkout(int orderId) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                System.out.println("Đơn hàng không tồn tại");
                return;
            }

            if (order.getStatus() == OrderStatus.PAID) {
                System.out.println("Đơn hàng đã được thanh toán");
                return;
            }

            List<OrderItem> items = orderItemDAO.getItemsByOrderId(orderId);
            double total = items.stream()
                    .mapToDouble(i -> i.getUnitPrice() * i.getQuantity())
                    .sum();

            for (OrderItem item : items) {
                MenuItem menuItem = MenuItemService.getInstance().getMenuItemById(item.getItemId());
                if (menuItem != null && menuItem.getItemType() == ItemType.DRINK){
                    MenuItemService.getInstance().editMenuItem(menuItem.getItemId(), menuItem.getProductName(), menuItem.getItemType(), menuItem.getPrice(), menuItem.getStock() - item.getQuantity(), menuItem.isAvailable());
                }
                orderItemDAO.updateItemStatus(item.getOrderItemId(), OrderItemStatus.SERVED);
            }

            orderDAO.updateOrderCheckOut(orderId, OrderStatus.PAID, total);

            tableDAO.updateTable(order.getTableId(),
                    tableDAO.findTableById(order.getTableId()).getCapacity(),
                    TableStatus.EMPTY);

            TableService.getInstance().refreshTables();
            MenuItemService.getInstance().refreshMenuItems();

            System.out.printf("Thanh toán thành công! Tổng tiền: %,.0f VND%n", total);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}