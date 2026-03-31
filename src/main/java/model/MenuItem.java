package model;

import java.time.LocalDateTime;

public class MenuItem {
    private int itemId;
    private String productName;
    private ItemType itemType;
    private double price;
    private int stock;
    private boolean isAvailable;
    private LocalDateTime createdAt;

    public MenuItem() {}

    public MenuItem(int itemId, String productName, ItemType itemType, double price, int stock, boolean isAvailable, LocalDateTime createdAt) {
        this.itemId = itemId;
        this.productName = productName;
        this.itemType = itemType;
        this.price = price;
        this.stock = stock;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
