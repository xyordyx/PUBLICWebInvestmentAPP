package com.gmp.service;

import com.gmp.facturedo.JSON.*;
import com.gmp.finsmart.JSON.FinancialTransactions;
import com.gmp.finsmart.JSON.FinsmartData;
import com.gmp.finsmart.JSON.InvoiceTransactions;
import com.gmp.hmviking.LoginJSON;
import com.gmp.persistence.model.HistoricalData;
import com.gmp.persistence.model.LastInvestmentsStatus;
import com.gmp.persistence.model.User;
import com.gmp.persistence.model.WeeklyDataModel;
import com.gmp.web.dto.Investment;

import java.util.List;

public interface IReportService {
    FinsmartData processFinancialTransactions(FinancialTransactions transactions,
                                              List<InvoiceTransactions> invoices, User userId, double currencyFactor);

    List<HistoricalData> getMonthlyAnalysis(User user);

    List<WeeklyDataModel> getDataModel(User user);

    FacturedoData getProcessedResultsFactu(FacturedoData factuData,LoginJSON loginJSON);

    Auctions getFactuFinalizedInvoices(LoginJSON loginJSON);
}
