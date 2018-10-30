package com.yx.itracker.Class;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class ItemCard {
    private int id;
    private BigInteger productId;
    private String firstImage;
    private String name;
    private int quantity;
    private BigDecimal sellPrice;
    private BigDecimal buyPrice;

    public ItemCard(int id, BigInteger productId, String firstImage, String name, int quantity, BigDecimal sellPrice, BigDecimal buyPrice) {
        this.id = id;
        this.productId = productId;
        this.firstImage = firstImage;
        this.name = name;
        this.quantity = quantity;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
    }

    public int getId() {
        return id;
    }

    public BigInteger getProductId() {
        return productId;
    }

    public String getFirstImage() {
        return firstImage;
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

    public BigDecimal getSellValue() {
        return sellPrice.multiply(new BigDecimal(quantity));
    }

}
