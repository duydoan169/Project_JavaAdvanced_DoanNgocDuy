package service;

import dao.TableDAO;
import model.Table;
import model.TableStatus;

import java.sql.SQLException;
import java.util.List;

public class TableService {
    private static final TableService instance = new TableService();
    private static List<Table> tables;
    private final TableDAO tableDAO = new TableDAO();

    private TableService() {}

    public static TableService getInstance() { return instance; }

    public List<Table> getTableList() {
        try {
            if (tables == null) tables = tableDAO.getAllTables();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return tables;
    }

    public void refreshTables() {
        try {
            tables = tableDAO.getAllTables();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }


    public void addTable(int cap) {
        try {
            tableDAO.createTable(cap);
            refreshTables();
            System.out.println("Tạo bàn ăn thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void removeTable(int tableId){
        try {
            Table table = tableDAO.findTableById(tableId);
            if (table == null) {
                System.out.println("ID bàn không tồn tại");
                return;
            }
            if (table.getStatus() == TableStatus.OCCUPIED) {
                System.out.println("Bàn hiện đang có người dùng");
                return;
            }
            tableDAO.deleteTable(tableId);
            refreshTables();
            System.out.println("Xóa bàn ăn thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public void editTable(int tableId, int capacity, TableStatus tableStatus){
        try {
            Table table = tableDAO.findTableById(tableId);
            if (table == null) {
                System.out.println("ID bàn không tồn tại");
                return;
            }

            if (table.getStatus() == TableStatus.OCCUPIED) {
                System.out.println("Bàn hiện đang có người dùng");
                return;
            }
            tableDAO.updateTable(tableId, capacity, tableStatus);
            refreshTables();
            System.out.println("Sửa bàn ăn thành công");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public Table getTableById(int tableId) {
        try {
            return new TableDAO().findTableById(tableId);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
