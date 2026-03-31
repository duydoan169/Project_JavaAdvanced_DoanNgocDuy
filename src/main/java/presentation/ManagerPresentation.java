package presentation;

import model.ItemType;
import model.MenuItem;
import model.Table;
import model.User;
import service.MenuItemService;
import service.TableService;
import service.UserService;
import utils.InputUtils;

import java.util.List;

public class ManagerPresentation {
    public static void start(){
        while (true) {
            System.out.println("=========MANAGER MENU=========");
            System.out.println("1. Quản lý thực đơn");
            System.out.println("2. Quản lý bàn ăn");
            System.out.println("3. Quản lý người dùng");
            System.out.println("0. Quay lại");
            System.out.println("==============================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> manageMenu();
                case "2" -> manageTables();
                case "3" -> manageUsers();
                case "0" -> {
                    return;
                }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }


    // MENU MANAGEMENT

    private static void manageMenu(){
        while (true) {
            System.out.println("=========MENU MANAGER=========");
            System.out.println("1. Xem thực đơn");
            System.out.println("2. Thêm món");
            System.out.println("3. Sửa món");
            System.out.println("4. Xóa món");
            System.out.println("0. Quay lại");
            System.out.println("==============================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> showMenuItems();
                case "2" -> addMenuItem();
                case "3" -> editMenuItem();
                case "4" -> removeMenuItem();
                case "0" -> {
                    return;
                }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    public static void showMenuItems() {
        List<MenuItem> items = MenuItemService.getInstance().getMenuItemList();
        System.out.println("====================================MENU====================================");
        System.out.printf("%-5s %-20s %-6s %-12s %-6s %s%n",
                "ID", "Tên món", "Loại", "Giá", "Tồn", "Trạng thái");
        System.out.println("----------------------------------------------------------------------------");
        for (MenuItem item : items) {
            System.out.printf("%-5d %-20s %-6s %-12.0f %-6s %s%n",
                    item.getItemId(),
                    item.getProductName(),
                    item.getItemType(),
                    item.getPrice(),
                    item.getStock() == -1 ? " " : item.getStock(),
                    item.isAvailable() ? "Đang bán" : "Ngừng bán"
            );
        }
        System.out.println("============================================================================");
    }

    private static void addMenuItem() {
        String name;
        while (true) {
            name = InputUtils.readLine("Nhập tên món: ");
            if (name.length() >= 3) break;
            System.out.println("Tên món phải có ít nhất 3 ký tự");
        }

        ItemType itemType;
        while (true) {
            String type = InputUtils.readLine("Nhập loại (FOOD/DRINK): ").toUpperCase();
            try {
                itemType = ItemType.valueOf(type);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Loại phải là FOOD hoặc DRINK");
            }
        }

        double price;
        while (true) {
            try {
                price = Double.parseDouble(InputUtils.readLine("Nhập giá: "));
                if (price > 0) break;
                System.out.println("Giá phải lớn hơn 0");
            } catch (NumberFormatException e) {
                System.out.println("Giá phải là số");
            }
        }

        int stock = -1;
        if (itemType == ItemType.DRINK) {
            while (true) {
                try {
                    stock = Integer.parseInt(InputUtils.readLine("Nhập số lượng tồn kho: "));
                    if (stock >= 0) break;
                    System.out.println("Số lượng không được âm");
                } catch (NumberFormatException e) {
                    System.out.println("Số lượng phải là số");
                }
            }
        }

        MenuItemService.getInstance().addMenuItem(name, itemType, price, stock);
    }

    private static void editMenuItem(){
        int targetId;
        while (true){
            try{
                targetId = Integer.parseInt(InputUtils.readLine("Nhập ID món muốn sửa: "));
                break;
            }catch (NumberFormatException e){
                System.out.println("ID phải là số");
            }
        }

        MenuItem targetItem = MenuItemService.getInstance().getMenuItemById(targetId);
        if (targetItem == null){
            System.out.println("Món không tồn tại");
            return;
        }

        System.out.println("================================DISH INFO================================");
        System.out.printf("%-5s %-20s %-6s %-12s %-6s %s%n",
                "ID", "Tên món", "Loại", "Giá", "Tồn", "Trạng thái");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-5d %-20s %-6s %-12.0f %-6s %s%n",
                targetItem.getItemId(),
                targetItem.getProductName(),
                targetItem.getItemType(),
                targetItem.getPrice(),
                targetItem.getStock() == -1 ? " " : targetItem.getStock(),
                targetItem.isAvailable() ? "Đang bán" : "Ngừng bán"
        );
        System.out.println("=========================================================================");

        String name;
        while (true) {
            name = InputUtils.readLine("Nhập tên món (để trống để bỏ qua): ");
            if (name.isEmpty()) {
                name = targetItem.getProductName();
                break;
            }
            if (name.length() >= 3) break;
            System.out.println("Tên món phải có ít nhất 3 ký tự");
        }

        ItemType itemType;
        while (true) {
            String type = InputUtils.readLine("Nhập loại (FOOD/DRINK) (để trống để bỏ qua): ").toUpperCase();
            if (type.isEmpty()) {
                itemType = targetItem.getItemType();
                break;
            }
            try {
                itemType = ItemType.valueOf(type);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Loại phải là FOOD hoặc DRINK");
            }
        }

        double price;
        while (true) {
            String input = InputUtils.readLine("Nhập giá (để trống để bỏ qua): ");
            if (input.isEmpty()) {
                price = targetItem.getPrice();
                break;
            }
            try {
                price = Double.parseDouble(input);
                if (price > 0) break;
                System.out.println("Giá phải lớn hơn 0");
            } catch (NumberFormatException e) {
                System.out.println("Giá phải là số");
            }
        }

        int stock = targetItem.getStock();
        if (itemType == ItemType.DRINK) {
            while (true) {
                String input = InputUtils.readLine("Nhập số lượng tồn kho (để trống để bỏ qua): ");
                if (input.isEmpty()) {
                    stock = targetItem.getStock();
                    break;
                }
                try {
                    stock = Integer.parseInt(input);
                    if (stock >= 0) break;
                    System.out.println("Số lượng không được âm");
                } catch (NumberFormatException e) {
                    System.out.println("Số lượng phải là số");
                }
            }
        }

        MenuItemService.getInstance().editMenuItem(targetId, name, itemType, price, stock, targetItem.isAvailable());
    }

    private static void removeMenuItem(){
        int targetId;
        while (true){
            try{
                targetId = Integer.parseInt(InputUtils.readLine("Nhập ID món muốn xóa: "));
                break;
            }catch (NumberFormatException e){
                System.out.println("ID phải là số");
            }
        }

        MenuItem targetItem = MenuItemService.getInstance().getMenuItemById(targetId);
        if (targetItem == null){
            System.out.println("Món không tồn tại");
            return;
        }
        System.out.println("================================DISH INFO================================");
        System.out.printf("%-5s %-20s %-6s %-12s %-6s %s%n",
                "ID", "Tên món", "Loại", "Giá", "Tồn", "Trạng thái");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-5d %-20s %-6s %-12.0f %-6s %s%n",
                targetItem.getItemId(),
                targetItem.getProductName(),
                targetItem.getItemType(),
                targetItem.getPrice(),
                targetItem.getStock() == -1 ? " " : targetItem.getStock(),
                targetItem.isAvailable() ? "Đang bán" : "Ngừng bán"
        );
        System.out.println("=========================================================================");

        String deleteConfirmation = InputUtils.readLine("Bạn có chắc muốn xóa? (y/n): ");
        if (deleteConfirmation.equalsIgnoreCase("y")){
            MenuItemService.getInstance().removeMenuItem(targetId);
        }else {
            System.out.println("Đã hủy xóa món ăn");
        }
    }


    // TABLE MANAGEMENT

    public static void manageTables() {
        while (true) {
            System.out.println("=========TABLE MANAGER=========");
            System.out.println("1. Xem danh sách bàn");
            System.out.println("2. Thêm bàn");
            System.out.println("3. Sửa bàn");
            System.out.println("4. Xóa bàn");
            System.out.println("0. Quay lại");
            System.out.println("===============================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> showTables();
                case "2" -> addTable();
                case "3" -> editTable();
                case "4" -> removeTable();
                case "0" -> { return; }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    public static void showTables() {
        List<Table> tables = TableService.getInstance().getTableList();
        System.out.println("===============TABLE LIST===============");
        System.out.printf("%-5s %-10s %s%n", "ID", "Sức chứa", "Trạng thái");
        System.out.println("----------------------------------------");
        for (Table t : tables) {
            System.out.printf("%-5d %-10d %s%n",
                    t.getTableId(),
                    t.getCapacity(),
                    t.getStatus()
            );
        }
        System.out.println("========================================");
    }

    private static void addTable() {
        int capacity;
        while (true) {
            try {
                capacity = Integer.parseInt(InputUtils.readLine("Nhập sức chứa: "));
                if (capacity > 0) break;
                System.out.println("Sức chứa phải lớn hơn 0");
            } catch (NumberFormatException e) {
                System.out.println("Sức chứa phải là số");
            }
        }
        TableService.getInstance().addTable(capacity);
    }

    private static void editTable() {
        int targetId;
        while (true) {
            try {
                targetId = Integer.parseInt(InputUtils.readLine("Nhập ID bàn muốn sửa: "));
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID phải là số");
            }
        }

        Table targetTable = TableService.getInstance().getTableById(targetId);
        if (targetTable == null) {
            System.out.println("Bàn không tồn tại");
            return;
        }
        System.out.println("===============TABLE INFO===============");
        System.out.printf("%-5s %-10s %s%n", "ID", "Sức chứa", "Trạng thái");
        System.out.println("----------------------------------------");
        System.out.printf("%-5d %-10d %s%n",
                targetTable.getTableId(),
                targetTable.getCapacity(),
                targetTable.getStatus()
        );
        System.out.println("========================================");

        int capacity;
        while (true) {
            String input = InputUtils.readLine("Nhập sức chứa mới (để trống để bỏ qua): ");
            if (input.isEmpty()) {
                capacity = targetTable.getCapacity();
                break;
            }
            try {
                capacity = Integer.parseInt(input);
                if (capacity > 0) break;
                System.out.println("Sức chứa phải lớn hơn 0");
            } catch (NumberFormatException e) {
                System.out.println("Sức chứa phải là số");
            }
        }

        TableService.getInstance().editTable(targetId, capacity, targetTable.getStatus());
    }

    private static void removeTable() {
        int targetId;
        while (true) {
            try {
                targetId = Integer.parseInt(InputUtils.readLine("Nhập ID bàn muốn xóa: "));
                break;
            } catch (NumberFormatException e) {
                System.out.println("ID phải là số");
            }
        }

        Table targetTable = TableService.getInstance().getTableById(targetId);
        if (targetTable == null) {
            System.out.println("Bàn không tồn tại");
            return;
        }

        System.out.println("===============TABLE INFO===============");
        System.out.printf("%-5s %-10s %s%n", "ID", "Sức chứa", "Trạng thái");
        System.out.println("----------------------------------------");
        System.out.printf("%-5d %-10d %s%n",
                targetTable.getTableId(),
                targetTable.getCapacity(),
                targetTable.getStatus()
        );
        System.out.println("========================================");

        String confirm = InputUtils.readLine("Bạn có chắc muốn xóa? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            TableService.getInstance().removeTable(targetId);
        }else {
            System.out.println("Đã hủy xóa bàn ăn");
        }
    }


    // USER MANAGEMENT
    private static void manageUsers(){
        while (true) {
            System.out.println("=========USER MANAGER=========");
            System.out.println("1. Xem danh sách người dùng");
            System.out.println("2. Thêm tài khoản đầu bếp");
            System.out.println("3. Vô hiệu hóa tài khoản người dùng");
            System.out.println("0. Quay lại");
            System.out.println("==============================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> showUserList();
                case "2" -> addChef();
                case "3" -> banUser();
                case "0" -> {
                    return;
                }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    private static void showUserList() {
        List<User> users = UserService.getInstance().getUserList();
        System.out.println("====================USER LIST====================");
        System.out.printf("%-5s %-20s %-10s %s%n", "ID", "Tên đăng nhập", "Vai trò", "Trạng thái");
        System.out.println("-------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-5d %-20s %-10s %s%n",
                    u.getUserId(),
                    u.getUsername(),
                    u.getRole(),
                    u.isActive() ? "Hoạt động" : "Bị cấm"
            );
        }
        System.out.println("=================================================");
    }

    private static void addChef(){
        String username;
        while (true) {
            username = InputUtils.readLine("Nhập tên: ");
            if (username.length() >= 3) break;
            System.out.println("Tên phải có ít nhất 3 ký tự");
        }

        String password;
        while (true) {
            password = InputUtils.readLine("Nhập mật khẩu: ");
            if (password.length() >= 6) break;
            System.out.println("Mật khẩu phải có ít nhất 6 ký tự");
        }

        UserService.getInstance().registerChef(username, password);
    }

    private static void banUser(){
        showUserList();

        while (true){
            try {
                int target = Integer.parseInt(InputUtils.readLine("Nhập ID người dùng: "));
                User targetUser = UserService.getInstance().getUserById(target);

                if (targetUser == null){
                    return;
                }

                if (targetUser.isActive()){
                    UserService.getInstance().banUser(target);
                }else {
                    if(InputUtils.readLine("Người dùng này đang bị ban, unban người dùng? (y/n): ").equalsIgnoreCase("y")){
                        UserService.getInstance().unbanUser(target);
                        return;
                    }else {
                        System.out.println("Đã hủy unban người dùng");
                        return;
                    }
                }
            }catch (NumberFormatException e){
                System.out.println("ID Phải là số");
            }
        }
    }
}


