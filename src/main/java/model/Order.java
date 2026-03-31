package model;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int tableId;
    private int customerId;
    private OrderStatus status;
    private double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime checkoutAt;

    public Order() {}

    public Order(int tableId, int customerId) {
        this.tableId = tableId;
        this.customerId = customerId;
        this.status = OrderStatus.OPEN;
        this.totalPrice = 0.00;
    }

    public Order(int orderId, int tableId, int customerId, OrderStatus status, double totalPrice, LocalDateTime createdAt, LocalDateTime checkoutAt) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.customerId = customerId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.checkoutAt = checkoutAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCheckoutAt() {
        return checkoutAt;
    }

    public void setCheckoutAt(LocalDateTime checkoutAt) {
        this.checkoutAt = checkoutAt;
    }
}
