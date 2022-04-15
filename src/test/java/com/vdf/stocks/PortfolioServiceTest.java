package com.vdf.stocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

import com.vdf.stocks.model.Stock;
import com.vdf.stocks.service.AuditService;
import com.vdf.stocks.service.StocksService;
import java.util.Collection;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private AuditService auditService;

    @Mock
    private StocksService stocksService;

    @InjectMocks
    private PortfolioService portfolioService;

    @Captor
    private ArgumentCaptor<Collection<Stock>> initialStockCaptor;

    @Captor
    private ArgumentCaptor<Collection<Stock>> updatedStockCaptor;

    @BeforeEach
    void setup() {
        initMocks(this);
    }

    @Test
    void buyNewStock() {
        //given
        Stock stock = new Stock("123", "mystock", 10);

        //when
        portfolioService.buyStocks(stock);

        //then
        Map<String, Stock> stocks = portfolioService.getStocks();
        assertEquals(1, stocks.entrySet().size());
        assertNotNull(stocks.get(stock.getStockId()));
        Stock portfolioStock = stocks.get(stock.getStockId());
        assertEquals(stock.getStockId(), portfolioStock.getStockId());
        assertEquals(stock.getQuantity(), portfolioStock.getQuantity());
        assertEquals(stock.getName(), portfolioStock.getName());
    }

    @Test
    void buyNewStockAudit() {
        //given
        Stock stock = new Stock("123", "mystock", 10);

        //when
        portfolioService.buyStocks(stock);

        //then
        verify(auditService).auditStockChange(initialStockCaptor.capture(), updatedStockCaptor.capture());
        Collection<Stock> auditInitialStockCollection = initialStockCaptor.getValue();
        assertEquals(0, auditInitialStockCollection.size());
        Collection<Stock> auditUpdateStockCollection = updatedStockCaptor.getValue();
        Stock auditUpdateStock = (Stock) auditUpdateStockCollection.toArray()[0];
        assertEquals(stock.getStockId(), auditUpdateStock.getStockId());
        assertEquals(stock.getQuantity(), auditUpdateStock.getQuantity());
        assertEquals(stock.getName(), auditUpdateStock.getName());
    }

    @Test
    void buyExistingStock() {
        //given
        Stock initialStock = new Stock("123", "mystock", 10);
        portfolioService.buyStocks(initialStock);
        Stock stock = new Stock("123", "mystock", 100);

        //when
        portfolioService.buyStocks(stock);

        //then
        Map<String, Stock> stocks = portfolioService.getStocks();
        assertEquals(1, stocks.entrySet().size());
        assertNotNull(stocks.get(initialStock.getStockId()));
        Stock portfolioStock = stocks.get(initialStock.getStockId());
        assertEquals(initialStock.getStockId(), portfolioStock.getStockId());
        assertEquals(initialStock.getName(), portfolioStock.getName());
        assertEquals(initialStock.getQuantity() + stock.getQuantity(), portfolioStock.getQuantity());
    }

    @Test
    void buyAuditExistingStock() {
        //given
        Stock initialStock = new Stock("123", "mystock", 10);
        portfolioService.buyStocks(initialStock);
        Stock stock = new Stock("123", "mystock", 100);

        //when
        portfolioService.buyStocks(stock);

        //then
        verify(auditService, times(2)).auditStockChange(initialStockCaptor.capture(), updatedStockCaptor.capture());
        Collection<Stock> auditInitialStockCollection = initialStockCaptor.getValue();
        Stock auditInitialStock = (Stock) auditInitialStockCollection.toArray()[0];
        assertEquals(initialStock.getStockId(), auditInitialStock.getStockId());
        assertEquals(initialStock.getQuantity(), auditInitialStock.getQuantity());
        assertEquals(initialStock.getName(), auditInitialStock.getName());
        Collection<Stock> auditUpdateStockCollection = updatedStockCaptor.getValue();
        Stock auditUpdateStock = (Stock) auditUpdateStockCollection.toArray()[0];
        assertEquals(stock.getStockId(), auditUpdateStock.getStockId());
        assertEquals(stock.getQuantity() + initialStock.getQuantity(), auditUpdateStock.getQuantity());
        assertEquals(stock.getName(), auditUpdateStock.getName());
    }

    @Test
    void sellStocks() {
    }

    @Test
    void getMarketValue() {
    }
}