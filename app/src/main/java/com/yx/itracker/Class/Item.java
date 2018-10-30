package com.yx.itracker.Class;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private int id;
    private BigInteger productId;
    private List<String> images;
    private String name;
    //private String category;
    private int quantity;
    private BigDecimal sellPrice;
    private BigDecimal buyPrice;
    private String notes;

    public Item(int id, BigInteger productId, List<String> images, String name, int quantity, BigDecimal sellPrice, BigDecimal buyPrice, String notes) {
        this.id = id;
        this.productId = productId;
        this.images = images;
        this.name = name;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.notes = notes;
    }

    public BigInteger getProductId() {
        return productId;
    }

    public int getId() {
        return id;
    }

    public List<String> getImages() {
        return images;
    }

    public String getFirstImage() {
        return images.get(0);
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String printPretty() {
        return (productId.toString() + "," +
                Integer.toString(images.size()) + "," +
                name + "," +
                String.valueOf(quantity) + "," +
                sellPrice.toString() + "," +
                buyPrice.toString() + "," +
                notes
        );
    }


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", productId=" + productId +
                ", images=" + images +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", sellPrice=" + sellPrice +
                ", buyPrice=" + buyPrice +
                ", notes='" + notes + '\'' +
                '}';
    }
}
