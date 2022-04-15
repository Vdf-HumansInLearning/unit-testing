package com.vdf.stocks;

import com.vdf.stocks.exception.InvalidRequestException;
import com.vdf.stocks.model.Stock;
import com.vdf.stocks.service.AuditService;
import com.vdf.stocks.service.StocksService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PortfolioService {

    private StocksService stocksService;

    private AuditService auditService;

    private final Map<String, Stock> stocks = new HashMap<>();

    public void setStockService(StocksService stocksService) {
        this.stocksService = stocksService;
    }

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    public Map<String, Stock> getStocks() {
        return stocks;
    }

    public void buyStocks(Stock stock) {
        List<Stock> initialStock = new ArrayList<>();
        initialStock.addAll(stocks.values().stream().map(Stock::clone).collect(Collectors.toList()));
        if(stocks.get(stock.getStockId()) == null) {
            Stock addedStock = new Stock(stock.getStockId(), stock.getName(), stock.getQuantity());
           stocks.put(stock.getStockId(), addedStock);
        } else {
           stocks.get(stock.getStockId()).updateQuatity(stock.getQuantity());
        }
        auditService.auditStockChange(initialStock, stocks.values());
    }

    public void sellStocks(Stock stock) throws InvalidRequestException {
        Collection<Stock> initialStock = stocks.values();
        if(stocks.get(stock.getStockId()) == null) {
            throw new InvalidRequestException("Stock with given id does not exist in the portfolio");
        } else {
            stocks.get(stock.getStockId()).updateQuatity(stock.getQuantity());
        }
        auditService.auditStockChange(initialStock, stocks.values());
    }

    public double getMarketValue(){
        double marketValue = 0.0;
        for(Stock stock: stocks.values()){
            marketValue += stocksService.getPrice(stock.getStockId()) * stock.getQuantity();
        }
        return marketValue;
    }
}
