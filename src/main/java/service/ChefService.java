package service;

import dao.MenuItemDAO;
import dao.OrderItemDAO;
import model.ItemType;
import model.MenuItem;
import model.OrderItem;
import model.OrderItemStatus;

import java.sql.SQLException;
import java.util.List;

public class ChefService {
    private static final ChefService instance = new ChefService();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    private ChefService() {}

    public static ChefService getInstance() { return instance; }

    public List<OrderItem> getActiveItems() {
        try {
            return orderItemDAO.getAllActiveItems();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

//    public void editItemStatus(int orderItemId, OrderItemStatus newStatus) {
//        try {
//            List<OrderItem> items = orderItemDAO.getAllActiveItems();
//            OrderItem target = items.stream()
//                    .filter(i -> i.getOrderItemId() == orderItemId)
//                    .findFirst()
//                    .orElse(null);
//
//            if (target == null) {
//                System.out.println("Món không tồn tại");
//                return;
//            }
//
//            OrderItemStatus current = target.getStatus();
//            boolean validTransition =
//                    (current == OrderItemStatus.PENDING  && newStatus == OrderItemStatus.COOKING) ||
//                            (current == OrderItemStatus.COOKING  && newStatus == OrderItemStatus.READY)   ||
//                            (current == OrderItemStatus.READY    && newStatus == OrderItemStatus.SERVED);
//
//            if (!validTransition) {
//                System.out.println("Không thể chuyển trạng thái từ " + current + " sang " + newStatus);
//                return;
//            }
//
//            orderItemDAO.editItemStatus(orderItemId, newStatus);
//            System.out.println("Cập nhật trạng thái thành công");
//        } catch (SQLException e) {
//            System.out.println("Database error: " + e.getMessage());
//        }
//    }

    public void editItemStatus(int orderItemId, OrderItemStatus newStatus) {
        try {
            OrderItem target = orderItemDAO.findOrderItemByItemId(orderItemId);
            if (target == null) {
                System.out.println("Món không tồn tại");
                return;
            }

            MenuItem menuItem = new MenuItemDAO().findMenuItemById(target.getItemId());
            if (menuItem != null && menuItem.getItemType() == ItemType.DRINK) {
                if (newStatus == OrderItemStatus.COOKING) {
                    System.out.println("Đồ uống không cần nấu");
                    return;
                }
            }

            orderItemDAO.updateItemStatus(orderItemId, newStatus);

            System.out.println("Cập nhật trạng thái thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public OrderItem findOrderItemByItemId(int orderItemId){
        try {
            return orderItemDAO.findOrderItemByItemId(orderItemId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
