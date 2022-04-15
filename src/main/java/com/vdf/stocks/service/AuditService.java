package com.vdf.stocks.service;

import com.vdf.stocks.model.Stock;
import java.util.Collection;

public interface AuditService {

    void auditStockChange(Collection<Stock> initialState, Collection<Stock> updatedState);

}
