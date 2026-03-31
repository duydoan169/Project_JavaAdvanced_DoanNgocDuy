package presentation;
import model.User;
import service.UserService;
import utils.InputUtils;
import utils.SessionManager;

public class MainPresentation {
    public static void start() {
        while (true) {
            System.out.println("==============================");
            System.out.println("1. Đăng ký");
            System.out.println("2. Đăng nhập");
            System.out.println("0. Thoát");
            System.out.println("==============================");

            switch (InputUtils.readLine("Nhập lựa chọn: ")) {
                case "1" -> register();
                case "2" -> login();
                case "0" -> {
                    System.out.println("Đã thoát chương trình");
                    return;
                }
                default  -> System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }

    private static void register() {
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

        UserService.getInstance().registerCustomer(username, password);
    }

    private static void login() {
        String username = InputUtils.readLine("Nhập tên: ");
        String password = InputUtils.readLine("Nhập mật khẩu: ");

        User user = UserService.getInstance().login(username, password);
        if (user == null) return;
        switch (user.getRole()) {
            case MANAGER  -> ManagerPresentation.start();
            case CHEF     -> ChefPresentation.start();
            case CUSTOMER -> CustomerPresentation.start();
        }

        SessionManager.logout();
        System.out.println("Đã đăng xuất thành công");
    }
}