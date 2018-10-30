package com.yx.itracker.Class;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class Order {
    private Item item;
    private int quantity;
    private BigDecimal price;
    private String type;
    private Date dateAdded;

    public Order(Item item, int quantity, BigDecimal price, String type, Date dateAdded) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.dateAdded = dateAdded;
    }

    public Item getItem() { return item; }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    @Override
    public String toString() {
        return "Order{" +
                "item=" + item +
                ", quantity=" + quantity +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
