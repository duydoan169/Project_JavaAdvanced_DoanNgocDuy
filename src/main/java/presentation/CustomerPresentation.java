package presentation;

import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Table;
import model.OrderItemStatus;
import model.OrderStatus;
import model.TableStatus;
import service.MenuItemService;
import service.OrderService;
import service.TableService;
import utils.InputUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerPresentation {
    private static final MenuItemService menuItemService = MenuItemService.getInstance();
    private static final TableService tableService = TableService.getInstance();
    private static final OrderService orderService = OrderService.getInstance();

    public static void start() {
        while (true) {
            System.out.println("=========CUSTOMER MENU=========");
            System.out.println("1. Chọn bàn và đặt món");
            System.out.println("2. Xem đơn hàng");
            System.out.println("3. Hủy món");
            System.out.println("4. Thanh toán");
            System.out.println("0. Thoát");
            System.out.println("===============================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> placeOrder();
                case "2" -> viewAllOrders();
                case "3" -> cancelItem();
                case "4" -> checkout();
                case "0" -> { return; }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    private static void placeOrder() {
        Order activeOrder = orderService.getActiveOrder();
        if (activeOrder != null) {
            System.out.println("Bạn đang có một đơn hàng chưa thanh toán ở bàn " + activeOrder.getTableId());
            System.out.println("Vui lòng thanh toán trước khi đặt bàn mới");
            return;
        }

        List<Table> tables = tableService.getTableList();
        System.out.println("===============TABLE LIST===============");
        System.out.printf("%-5s %-10s %s%n", "ID", "Sức chứa", "Trạng thái");
        System.out.println("----------------------------------------");
        for (Table t : tables) {
            if (t.getStatus() == TableStatus.EMPTY) {
                System.out.printf("%-5d %-10d %s%n",
                        t.getTableId(), t.getCapacity(), t.getStatus());
            }
        }
        System.out.println("[0] Quay lại");
        System.out.println("========================================");

        int tableId;
        while (true) {
            try {
                tableId = Integer.parseInt(InputUtils.readLine("Chọn bàn: "));
                if (tableId == 0) {
                    System.out.println("Đã thoát");
                    return;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID phải là số");
            }
        }

        Table selectedTable = tableService.getTableById(tableId);

        if (selectedTable == null) {
            System.out.println("ID bàn không tồn tại");
            return;
        }
        if (selectedTable.getStatus() == TableStatus.OCCUPIED) {
            System.out.println("Bàn đang có người");
            return;
        }

        List<OrderItem> items = new ArrayList<>();
        List<MenuItem> menu = menuItemService.getMenuItemList();

        while (true) {
            System.out.println("====================================MENU====================================");
            System.out.printf("%-5s %-20s %-6s %-12s %s%n",
                    "ID", "Tên món", "Loại", "Giá", "Tồn");
            System.out.println("----------------------------------------------------------------------------");
            for (MenuItem item : menu) {
                if (item.isAvailable()) {
                    System.out.printf("%-5d %-20s %-6s %-12.0f %s%n",
                            item.getItemId(),
                            item.getProductName(),
                            item.getItemType(),
                            item.getPrice(),
                            item.getStock() == -1 ? " " : item.getStock()
                    );
                }
            }
            System.out.println("[0] Xong");
            System.out.println("============================================================================");

            int itemId;
            while (true) {
                try {
                    itemId = Integer.parseInt(InputUtils.readLine("Chọn món: "));
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("ID phải là số");
                }
            }

            if (itemId == 0) break;

            MenuItem selected = MenuItemService.getInstance().getMenuItemById(itemId);

            if (selected == null) {
                System.out.println("Món không tồn tại");
                continue;
            }

            if (!selected.isAvailable()) {
                System.out.println("Món không còn được bán");
                continue;
            }

            int quantity;
            while (true) {
                try {
                    quantity = Integer.parseInt(InputUtils.readLine("Số lượng: "));
                    if (quantity > 0) break;
                    System.out.println("Số lượng phải lớn hơn 0");
                } catch (NumberFormatException e) {
                    System.out.println("Số lượng phải là số");
                }
            }

            if (selected.getStock() != -1 && quantity > selected.getStock()) {
                System.out.println("Không đủ hàng, còn lại: " + selected.getStock());
                continue;
            }

            items.add(new OrderItem(0, itemId, quantity, selected.getPrice()));
            System.out.println("Đã thêm: " + selected.getProductName() + " x" + quantity);
        }

        if (items.isEmpty()) {
            System.out.println("Chưa chọn món nào");
            return;
        }

        System.out.println("=========ORDER CONFIRMATION=========");
        double total = 0;
        for (OrderItem item : items) {
            MenuItem m = menuItemService.getMenuItemById(item.getItemId());
            String name = m != null ? m.getProductName() : "Unknown";
            double subtotal = item.getUnitPrice() * item.getQuantity();
            total += subtotal;
            System.out.printf("%-20s x%-3d %,.0f%n", name, item.getQuantity(), subtotal);
        }
        System.out.println("------------------------------------");
        System.out.printf("Tổng: %,.0f VND%n", total);
        System.out.println("====================================");

        if (InputUtils.readLine("Xác nhận đặt món? (y/n): ").equalsIgnoreCase("y")) {
            orderService.placeOrder(tableId, items);
        } else {
            System.out.println("Đã hủy");
        }
    }

    private static void viewAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders == null || orders.isEmpty()) {
            System.out.println("Bạn chưa có đơn hàng nào");
            return;
        }

        System.out.println("=============ORDER LIST=============");
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            System.out.printf("[%d] Đơn #%d | Bàn %d | %s | %s%n",
                    i + 1,
                    o.getOrderId(),
                    o.getTableId(),
                    o.getStatus(),
                    o.getCreatedAt().toLocalDate()
            );
        }
        System.out.println("[0] Quay lại");
        System.out.println("====================================");

        String input = InputUtils.readLine("Chọn đơn hàng để xem chi tiết: ");
        if (input.equals("0")) return;

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Lựa chọn không hợp lệ");
            return;
        }

        if (choice < 1 || choice > orders.size()) {
            System.out.println("Lựa chọn không hợp lệ");
            return;
        }

        viewOrderDetail(orders.get(choice - 1));
    }

    private static void viewOrderDetail(Order order) {
        List<OrderItem> items = orderService.getOrderItems(order.getOrderId());
        if (items == null || items.isEmpty()) {
            System.out.println("Không có món nào");
            return;
        }

        System.out.println("=========ORDER DETAIL #" + order.getOrderId() + "=========");
        System.out.printf("%-5s %-20s %-5s %-12s %s%n",
                "STT", "Tên món", "SL", "Giá", "Trạng thái");
        System.out.println("------------------------------------------");
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            System.out.printf("%-5d %-20s %-5d %-12.0f %s%n",
                    i + 1,
                    item.getItemName(),
                    item.getQuantity(),
                    item.getUnitPrice() * item.getQuantity(),
                    item.getStatus()
            );
        }
        System.out.println("------------------------------------------");
        System.out.printf("Trạng thái đơn: %s%n", order.getStatus());
        System.out.println("==========================================");
    }

    private static void cancelItem() {
        Order activeOrder = orderService.getActiveOrder();
        if (activeOrder == null) {
            System.out.println("Không có đơn hàng đang hoạt động");
            return;
        }

        List<OrderItem> items = orderService.getOrderItems(activeOrder.getOrderId());
        if (items == null || items.isEmpty()) {
            System.out.println("Không có món nào trong đơn");
            return;
        }

        System.out.println("=========CANCEL ITEM=========");
        boolean hasPending = false;
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            if (item.getStatus() == OrderItemStatus.PENDING) {
                System.out.printf("[%d] %s x%d%n",
                        i + 1, item.getItemName(), item.getQuantity());
                hasPending = true;
            }
        }

        if (!hasPending) {
            System.out.println("Không có món nào đang chờ để hủy");
            return;
        }

        System.out.println("[0] Quay lại");
        System.out.println("=========================");

        String input = InputUtils.readLine("Chọn món muốn hủy: ");
        if (input.equals("0")) return;

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("ID phải là số");
            return;
        }

        if (choice < 1 || choice > items.size()) {
            System.out.println("Lựa chọn không hợp lệ");
            return;
        }

        OrderItem selected = items.get(choice - 1);
        if (selected.getStatus() != OrderItemStatus.PENDING) {
            System.out.println("Chỉ có thể hủy món đang chờ");
            return;
        }

        orderService.cancelItem(selected.getOrderItemId());
    }

    private static void checkout() {
        Order activeOrder = orderService.getActiveOrder();
        if (activeOrder == null) {
            System.out.println("Bạn không có đơn hàng đang hoạt động");
            return;
        }

        viewOrderDetail(activeOrder);

        if (InputUtils.readLine("Xác nhận thanh toán? (y/n): ").equalsIgnoreCase("y")) {
            orderService.checkout(activeOrder.getOrderId());
        } else {
            System.out.println("Đã hủy thanh toán");
        }
    }
}