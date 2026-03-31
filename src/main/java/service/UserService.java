package service;

import dao.TableDAO;
import dao.UserDAO;
import model.Table;
import model.User;
import model.Role;
import utils.PasswordUtils;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private static final UserService instance = new UserService();
    private static List<User> users;
    private final UserDAO userDAO = new UserDAO();

    private UserService() {}

    public static UserService getInstance() { return instance; }

    public List<User> getUserList() {
        try {
            if (users == null) users = userDAO.getAllUsers();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return users;
    }

    public void refreshUsers() {
        try {
            users = userDAO.getAllUsers();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void banUser(int userId) {
        try {
            userDAO.setUserStatus(userId, false);
            refreshUsers();
            System.out.println("Ban người dùng thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void unbanUser(int userId) {
        try {
            userDAO.setUserStatus(userId, true);
            refreshUsers();
            System.out.println("Unban người dùng thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void addUser(String username, String password, Role role) {
        try {
            if (userDAO.findUserByName(username) != null) {
                System.out.println("Người dùng đã tồn tại");
                return;
            }
            userDAO.createUser(username, PasswordUtils.hash(password), role);
            refreshUsers();
            System.out.println("Tạo tài khoản thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void registerCustomer(String username, String password){
            addUser(username, password, Role.CUSTOMER);
    }

    public void registerChef(String username, String password) { addUser(username, password, Role.CHEF); }

    public User login(String username, String password){
        try {
            User user = userDAO.findUserByName(username);
            if (user == null){
                System.out.println("Người dùng không tồn tại");
                return null;
            }

            if (!PasswordUtils.verify(password, user.getPassword())){
                System.out.println("Thông tin không chính xác");
                return null;
            }

            if (!user.isActive()) {
                System.out.println("Tài khoản đã bị Ban");
                return null;
            }

            SessionManager.setCurrentUser(user);
            System.out.println("Đăng nhập thành công");
            return user;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    public User getUserById(int userId) {
        try {
            User user = userDAO.findUserById(userId);
            if (user == null){
                System.out.println("Người dùng không tồn tại");
                return null;
            }

            if (user.getRole() == Role.MANAGER){
                System.out.println("Không thể ban quản lí");
                return null;
            }
            return user;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}