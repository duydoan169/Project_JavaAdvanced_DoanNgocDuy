package model;

public class Table {
    private int tableId;
    private int capacity;
    private TableStatus status;

    public Table() {
    }

    public Table(int tableId, int capacity, TableStatus status) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = status;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
    
}