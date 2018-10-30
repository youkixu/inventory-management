package com.yx.itracker.Class;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCard {
    int id;
    String name;
    int quantity;
    BigDecimal price;
    String type;
    Date dateAdded;

    public OrderCard(int id, String name, int quantity, BigDecimal price, String type, Date dateAdded) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

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

    public String getDateAddedFormatted() {
        String pattern = "dd/MM/yy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(dateAdded);
    }
}
