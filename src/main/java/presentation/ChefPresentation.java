package presentation;

import model.OrderItem;
import model.OrderItemStatus;
import service.ChefService;
import utils.InputUtils;

import java.util.List;

public class ChefPresentation {
    private static final ChefService chefService = ChefService.getInstance();

    public static void start() {
        while (true) {
            System.out.println("=========CHEF MENU=========");
            System.out.println("1. Xem danh sách món");
            System.out.println("2. Cập nhật trạng thái món");
            System.out.println("0. Đăng xuất");
            System.out.println("===========================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> viewActiveItems();
                case "2" -> updateItemStatus();
                case "0" -> { return; }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    private static void viewActiveItems() {
        List<OrderItem> items = chefService.getActiveItems();
        if (items == null || items.isEmpty()) {
            System.out.println("Không có món nào cần xử lý");
            return;
        }

        System.out.println("==============MENU==============");
        System.out.printf("%-5s %-5s %-20s %-5s %s%n",
                "ID", "Bàn", "Tên món", "SL", "Trạng thái");
        System.out.println("--------------------------------");
        for (OrderItem item : items) {
            System.out.printf("%-5d %-5d %-20s %-5d %s%n",
                    item.getOrderItemId(),
                    item.getOrderId(),
                    item.getItemName(),
                    item.getQuantity(),
                    item.getStatus()
            );
        }
        System.out.println("================================");
    }

    private static void updateItemStatus() {
        viewActiveItems();

        int targetId;
        while (true) {
            try {
                targetId = Integer.parseInt(InputUtils.readLine("Nhập ID món muốn cập nhật: "));
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID phải là số");
            }
        }

        System.out.println("1. PENDING  → COOKING");
        System.out.println("2. COOKING  → READY");
        System.out.println("3. READY    → SERVED");
        System.out.println("0. Quay lại");

        switch (InputUtils.readLine("Chọn trạng thái mới: ")) {
            case "1" -> chefService.updateItemStatus(targetId, OrderItemStatus.COOKING);
            case "2" -> chefService.updateItemStatus(targetId, OrderItemStatus.READY);
            case "3" -> chefService.updateItemStatus(targetId, OrderItemStatus.SERVED);
            case "0" -> { return; }
            default  -> System.out.println("Lựa chọn không hợp lệ");
        }
    }
}
