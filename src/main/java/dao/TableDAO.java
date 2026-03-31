package dao;

import model.Table;
import model.TableStatus;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    public List<Table> getAllTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        String sql = "select * from tables";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tables.add(new Table(
                        rs.getInt("table_id"),
                        rs.getInt("capacity"),
                        TableStatus.valueOf(rs.getString("status"))
                ));
            }
        }
        return tables;
    }

    public Table findTableById(int tableId) throws SQLException{
        String sql = "select * from tables where table_id = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tableId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    return new Table(
                            rs.getInt("table_id"),
                            rs.getInt("capacity"),
                            TableStatus.valueOf(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    public void createTable(int cap) throws SQLException{
        String sql = "insert into tables (capacity) values (?)";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, cap);
            ps.executeUpdate();
        }
    }

    public void deleteTable(int tableId) throws SQLException{
        String sql = "delete from tables where table_id = ?";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, tableId);
            ps.executeUpdate();
        }
    }

    public void updateTable(int tableId, int capacity, TableStatus tableStatus) throws SQLException{
        String sql = "update tables set capacity = ?, status = ? where table_id = ?";

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, capacity);
            ps.setString(2, String.valueOf(tableStatus));
            ps.setInt(3, tableId);
            ps.executeUpdate();
        }
    }
}
