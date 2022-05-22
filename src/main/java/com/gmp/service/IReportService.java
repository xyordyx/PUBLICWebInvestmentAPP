package com.gmp.service;

import com.gmp.investmentV1APP.JSON.*;
import com.gmp.investmentAPP.JSON.FinancialTransactions;
import com.gmp.investmentAPP.JSON.investmentAPPData;
import com.gmp.investmentAPP.JSON.InvoiceTransactions;
import com.gmp.hmviking.LoginJSON;
import com.gmp.persistence.model.HistoricalData;
import com.gmp.persistence.model.User;
import com.gmp.persistence.model.WeeklyDataModel;

import java.util.List;

public interface IReportService {
    investmentAPPData processFinancialTransactions(FinancialTransactions transactions,
                                              List<InvoiceTransactions> invoices, User userId, double currencyFactor);

    List<HistoricalData> getMonthlyAnalysis(User user);

    List<WeeklyDataModel> getDataModel(User user);

    FacturedoData getProcessedResultsFactu(FacturedoData factuData,LoginJSON loginJSON);

    Auctions getFactuFinalizedInvoices(LoginJSON loginJSON);
}
