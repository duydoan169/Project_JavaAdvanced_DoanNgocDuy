package service;

import dao.MenuItemDAO;
import model.MenuItem;
import model.ItemType;

import java.sql.SQLException;
import java.util.List;

public class MenuItemService {
    private static final MenuItemService instance = new MenuItemService();
    private static List<MenuItem> menuItems;
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();

    private MenuItemService() {}

    public static MenuItemService getInstance() { return instance; }

    public List<MenuItem> getMenuItemList() {
        try {
            if (menuItems == null) menuItems = menuItemDAO.getAllMenuItems();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return menuItems;
    }

    public void refreshMenuItems() {
        try {
            menuItems = menuItemDAO.getAllMenuItems();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void addMenuItem(String productName, ItemType itemType, double price, int stock) {
        try {
            menuItemDAO.createMenuItem(productName, itemType, price, stock);
            refreshMenuItems();
            System.out.println("Thêm món thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void removeMenuItem(int itemId) {
        try {
            if (menuItemDAO.findMenuItemById(itemId) == null) {
                System.out.println("Món ăn không tồn tại");
                return;
            }
            menuItemDAO.deleteMenuItem(itemId);
            refreshMenuItems();
            System.out.println("Xóa món thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void editMenuItem(int itemId, String productName, ItemType itemType, double price, int stock, boolean isAvailable) {
        try {
            if (menuItemDAO.findMenuItemById(itemId) == null) {
                System.out.println("Món ăn không tồn tại");
                return;
            }
            menuItemDAO.updateMenuItem(itemId, productName, itemType, price, stock, isAvailable);
            refreshMenuItems();
            System.out.println("Sửa món thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public MenuItem getMenuItemById(int itemId) {
        try {
            return new MenuItemDAO().findMenuItemById(itemId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
