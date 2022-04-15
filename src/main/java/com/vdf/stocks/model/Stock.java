package com.vdf.stocks.model;

public class Stock {

    private final String stockId;
    private final String name;
    private int quantity;

    public Stock(String stockId, String name, int quantity) {
        this.stockId = stockId;
        this.name = name;
        this.quantity = quantity;
    }

    public String getStockId() {
        return stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void updateQuatity(int quantity) {
        this.quantity += quantity;
    }

    public Stock clone() {
        return new Stock(stockId, name, quantity);
    }

}