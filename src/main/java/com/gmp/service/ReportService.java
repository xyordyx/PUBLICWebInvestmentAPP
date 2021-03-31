package com.gmp.service;

import com.gmp.facturedo.JSON.*;
import com.gmp.finsmart.JSON.*;
import com.gmp.finsmart.FinSmartUtil;
import com.gmp.hmviking.LoginJSON;
import com.gmp.persistence.dao.HistoricalDataRepository;
import com.gmp.persistence.dao.HistoricalDataStatusRepository;
import com.gmp.persistence.dao.LastInvestmentsStatusRepository;
import com.gmp.persistence.dao.WeeklyDataRepository;
import com.gmp.persistence.model.*;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.gmp.facturedo.FacturedoCIG.*;

@Service
@Transactional
public class ReportService implements IReportService{

    @Autowired
    private HistoricalDataStatusRepository statusRepository;

    @Autowired
    private HistoricalDataRepository dataRepository;

    @Autowired
    private LastInvestmentsStatusRepository lastInvestmentsStatusRepository;

    @Autowired
    private WeeklyDataRepository weeklyDataRepository;

    Calendar cal = Calendar.getInstance();
    Calendar profitExpected = Calendar.getInstance();

    private final double conversionFactor = 3.60;

    @Override
    public void updateInvestmentStatus(Investment investment, User userId, String systemId){
        List<LastInvestmentsStatus> statusList = lastInvestmentsStatusRepository.getLastRecordByUser(userId);
        if(statusList.size() >= 8){
            lastInvestmentsStatusRepository.deleteById(statusList.get(0).getId());
        }
        LastInvestmentsStatus newRecord = new LastInvestmentsStatus();
        newRecord.setAmount(investment.getAmount());
        newRecord.setCurrency(investment.getCurrency());
        newRecord.setInvoiceNumber(investment.getInvoiceNumber());
        newRecord.setStatus(investment.getStatus());
        newRecord.setMessage(investment.getMessage());
        newRecord.setAdjustedAmount(investment.getAdjustedAmount());
        newRecord.setAutoAdjusted(investment.isAutoAdjusted());
        if(systemId.equals("HMFINSMART")) {
            newRecord.setRazonSocial(investment.getOpportunity().getDebtor().getCompanyName());
        }else if(systemId.equals("HMFACTUREDO")) {
            newRecord.setRazonSocial(investment.getResults().getDebtor().getOfficial_name());
        }
        newRecord.setSystemId(systemId);
        newRecord.setDateTime(ZonedDateTime.now(ZoneId.of("GMT-5")));
        newRecord.setUser(userId);
        lastInvestmentsStatusRepository.save(newRecord);
    }

    @Override
    public List<LastInvestmentsStatus> getLastInvestmentsStatus(User user) {
        return lastInvestmentsStatusRepository.getAllByUser(user);
    }

    @Override
    public List<LastInvestmentsStatus> getAllByUserAndSystemId(User user, String systemId){
        return lastInvestmentsStatusRepository.getAllByUserAndSystemId(user, systemId);
    }

    @Override
    public List<HistoricalData> getMonthlyAnalysis(User user){
        return dataRepository.getAllByUser(user);
    }

    @Override
    public List<WeeklyDataModel> getDataModel(User userId) {
        return weeklyDataRepository.getAllByUser(userId);
    }

    public double getDoubleValue(double value){
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void restoreHistoricData(User userId){
        HistoricalDataStatus restore = statusRepository.getLastRecordByUserByType(userId,"restore");
        if(restore == null){
            cleanHistoricData(userId);
            HistoricalDataStatus dataStatus = new HistoricalDataStatus();
            dataStatus.setLastUpdate(ZonedDateTime.now(ZoneId.of("GMT-5")));
            dataStatus.setUser(userId);
            dataStatus.setType("restore");
            statusRepository.save(dataStatus);
        }else{
            Calendar lastDate = GregorianCalendar.from(restore.getLastUpdate());
            lastDate.add(Calendar.HOUR,24);
            Calendar now = GregorianCalendar.from(ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
            if(1 > lastDate.compareTo(now)){
                cleanHistoricData(userId);
                restore.setLastUpdate(ZonedDateTime.now(ZoneId.of("GMT-5")));
                statusRepository.save(restore);
            }
        }
    }

    @Transactional
    public void cleanHistoricData(User userId){
        dataRepository.deleteByUserID(userId);
        dataRepository.flush();
        weeklyDataRepository.deleteByUserID(userId);
        weeklyDataRepository.flush();
        statusRepository.deleteByUserIDAAndType(userId,"data");
        statusRepository.flush();
    }

    @Override
    public FinsmartData processFinancialTransactions(FinancialTransactions transactions,
                                                            List<InvoiceTransactions> invoices, User userId) {
        restoreHistoricData(userId);
        HistoricalDataStatus dataStatus = statusRepository.getLastRecordByUserByType(userId,"data");
        Map<String,InvoiceTransactions> invoicesIndexed = FinSmartUtil.indexInvoices(invoices);
        FinsmartData smartData = new FinsmartData();
        for (Transactions financialTransactions : transactions.getFinancialTransactions()) {
            double amountInvested = 0;
            double amountExpected = 0;
            double sum = 0;
            //TOTAL DEPOSITED
            if (financialTransactions.getType().equals("deposit") && financialTransactions.getStatus().equals("approved")) {
                if (financialTransactions.getCurrency().equals("pen")) {
                    smartData.setSolesTotalDeposited(smartData.getSolesTotalDeposited() + financialTransactions.getAmount());
                } else {
                    smartData.setDollarTotalDeposited(smartData.getDollarTotalDeposited() + financialTransactions.getAmount());
                }
                smartData.getDepositedIndex().put(financialTransactions.get_id(),financialTransactions);
                //TOTAL RETENTIONS
            }else if (financialTransactions.getType().equals("retention") && financialTransactions.getStatus().equals("approved")) {
                if (financialTransactions.getCurrency().equals("pen")) {
                    smartData.setSolesRetentions(smartData.getSolesRetentions() + financialTransactions.getAmount());
                } else {
                    smartData.setDollarRetentions(smartData.getDollarRetentions() + financialTransactions.getAmount());
                }
                smartData.getRetentionsIndex().put(financialTransactions.get_id(),financialTransactions);
                //TOTAL PROFIT
            }else if (financialTransactions.getType().equals("investment return") && financialTransactions.getStatus().equals("approved")) {
                if (financialTransactions.getCurrency().equals("pen")) {
                    sum = smartData.getSolesTotalProfit() + financialTransactions.getAmount();
                    smartData.setSolesTotalProfit(sum);
                } else {
                    sum = smartData.getDollarTotalProfit() + financialTransactions.getAmount();
                    smartData.setDollarTotalProfit(sum);
                }
                saveMonthlyProfitData(financialTransactions,dataStatus,userId);
                smartData.getTotalProfitIndex().put(financialTransactions.getInvoice().get_id(),financialTransactions);
                //TOTAL CURRENT INVESTED
            }
            else if (financialTransactions.getType().equals("investment") && financialTransactions.getStatus().equals("in progress")) {
                if(invoicesIndexed.containsKey(financialTransactions.getInvoice().get_id())) {
                    double tempProfit = FinSmartUtil.calculateROI(invoicesIndexed.get(financialTransactions.getInvoice().get_id()).getTem(),
                            invoicesIndexed.get(financialTransactions.getInvoice().get_id()).getMinimumDuration(),
                            financialTransactions.getAmount());
                    if (financialTransactions.getCurrency().equals("pen")) {
                        amountInvested = smartData.getSolesCurrentInvested() + financialTransactions.getAmount();
                        amountExpected = smartData.getSolesProfitExpected() + tempProfit;
                        smartData.setSolesCurrentInvested(amountInvested);
                        smartData.setSolesProfitExpected(amountExpected);
                    } else {
                        amountInvested = smartData.getDollarCurrentInvested() + financialTransactions.getAmount();
                        amountExpected = smartData.getDollarProfitExpected() + tempProfit;
                        smartData.setDollarCurrentInvested(amountInvested);
                        smartData.setDollarProfitExpected(amountExpected);
                    }
                    financialTransactions.setExpectedProfit(tempProfit);
                    financialTransactions.setInvoiceAppend(invoicesIndexed.get(financialTransactions.getInvoice().get_id()));
                    smartData.getCurrentInvestmentsIndex().put(financialTransactions.getInvoice().get_id(), financialTransactions);
                }
                saveMonthlyProfitExpectedData(financialTransactions,dataStatus,userId);
                saveMonthlyInvestedData(financialTransactions,dataStatus,userId);
            }if(financialTransactions.getStatus().equals("investment start")){
                if (financialTransactions.getCurrency().equals("pen")) {
                    smartData.setSolesCurrentInvested(smartData.getSolesCurrentInvested() + financialTransactions.getAmount());
                } else {
                    smartData.setDollarCurrentInvested(smartData.getDollarCurrentInvested() + financialTransactions.getAmount());
                }
                saveMonthlyInvestedData(financialTransactions,dataStatus,userId);
            }
        }
        if(dataStatus == null){dataStatus = new HistoricalDataStatus(); }
        dataStatus.setLastUpdate(ZonedDateTime.now(ZoneId.of("GMT-5")));
        dataStatus.setUser(userId);
        dataStatus.setType("data");
        statusRepository.save(dataStatus);
        //TOTAL PROFIT LESS RETENTIONS
        smartData.setSolesTotalProfit(smartData.getSolesTotalProfit() - smartData.getSolesRetentions());
        smartData.setDollarTotalProfit(smartData.getDollarTotalProfit() - smartData.getDollarRetentions());
        //TOTAL AVAILABLE
        smartData.setSolesAmountAvailable((smartData.getSolesTotalDeposited()+smartData.getSolesTotalProfit()) -
                smartData.getSolesCurrentInvested());
        smartData.setDollarAmountAvailable((smartData.getDollarTotalDeposited()+smartData.getDollarTotalProfit()) -
                smartData.getDollarCurrentInvested());

        return smartData;
    }

    public boolean saveMonthlyProfitData(Transactions financialTransactions, HistoricalDataStatus dataStatus, User userId){
        if(dataStatus != null){
            Instant instant = dataStatus.getLastUpdate().toInstant();
            Date date = Date.from(instant);
            if(date.after(financialTransactions.getCreatedAt())){
                return false;
            }
        }
        cal.setTime(financialTransactions.getCreatedAt());
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        double data = 0;
        HistoricalData updateData = dataRepository.getByMonthAndYearByUser(month,year,userId);
        if(financialTransactions.getCurrency().equals("usd")){
            data = financialTransactions.getAmount() * conversionFactor;
        }else data = financialTransactions.getAmount();
        if(updateData != null){
            data = data + updateData.getProfitAmount();
            updateData.setProfitAmount(getDoubleValue(data));
            dataRepository.save(updateData);
        }else{
            HistoricalData newData = new HistoricalData();
            newData.setProfitAmount(getDoubleValue(data));
            newData.setYear(year);
            newData.setMonth(month);
            newData.setUser(userId);
            dataRepository.save(newData);
        }
        return true;
    }

    public boolean saveMonthlyInvestedData(Transactions financialTransactions, HistoricalDataStatus dataStatus, User userId){
        if(dataStatus != null){
            Instant instant = dataStatus.getLastUpdate().toInstant();
            Date date = Date.from(instant);
            if(date.after(financialTransactions.getCreatedAt())){
                return false;
            }
        }
        cal.setTime(financialTransactions.getCreatedAt());
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        double data = 0;
        HistoricalData updateData = dataRepository.getByMonthAndYearByUser(month,year,userId);
        if(financialTransactions.getCurrency().equals("usd")){
            data = financialTransactions.getAmount() * conversionFactor;
        }else data = financialTransactions.getAmount();
        if(isBetween15Days(profitExpected)){
            setWeeklyInvested(profitExpected,data,userId);
        }
        if(updateData != null){
            data = data + updateData.getInvestedAmount();
            updateData.setInvestedAmount(getDoubleValue(data));
            dataRepository.save(updateData);
        }else{
            HistoricalData newData = new HistoricalData();
            newData.setInvestedAmount(getDoubleValue(data));
            newData.setYear(year);
            newData.setMonth(month);
            newData.setUser(userId);
            dataRepository.save(newData);
        }
        return true;
    }

    public boolean saveMonthlyProfitExpectedData(Transactions financialTransactions, HistoricalDataStatus dataStatus, User userId){
        if(dataStatus != null){
            Instant instant = dataStatus.getLastUpdate().toInstant();
            Date date = Date.from(instant);
            if(date.after(financialTransactions.getCreatedAt())){
                return false;
            }
        }
        profitExpected.setTime(financialTransactions.getInvoiceAppend().getPaymentDate());
        int month = profitExpected.get(Calendar.MONTH)+1;
        int year = profitExpected.get(Calendar.YEAR);

        double data = 0;
        HistoricalData updateData = dataRepository.getByMonthAndYearByUser(month,year,userId);
        if(financialTransactions.getCurrency().equals("usd")){
            data = financialTransactions.getExpectedProfit() * conversionFactor;
        }else data = financialTransactions.getExpectedProfit();
        if(isBetween15Days(profitExpected)){
            setWeeklyProfit(profitExpected,data,userId);
        }
        if(updateData != null){
            data = data + updateData.getPaymentAmount();
            updateData.setPaymentAmount(getDoubleValue(data));
            dataRepository.save(updateData);
        }else{
            HistoricalData newData = new HistoricalData();
            newData.setPaymentAmount(getDoubleValue(data));
            newData.setYear(year);
            newData.setMonth(month);
            newData.setUser(userId);
            dataRepository.save(newData);
        }
        return true;
    }

    public void setWeeklyProfit(Calendar date, Double profit, User userId){
        int dayOfYear = date.get(Calendar.DAY_OF_YEAR);
        WeeklyDataModel weeklyData = weeklyDataRepository.getByDayOfYearAndUser(dayOfYear,userId);
        if(weeklyData != null){
            weeklyData.setProfitAmount(weeklyData.getProfitAmount()+profit);
            weeklyDataRepository.save(weeklyData);
        }else{
            WeeklyDataModel dataModel = new WeeklyDataModel();
            dataModel.setProfitAmount(profit);
            dataModel.setDay(date.get(Calendar.DAY_OF_MONTH));
            dataModel.setDayOfYear(dayOfYear);
            dataModel.setMonth(date.get(Calendar.MONTH)+1);
            dataModel.setYear(date.get(Calendar.YEAR));
            dataModel.setUser(userId);
            weeklyDataRepository.save(dataModel);
        }
    }

    public void setWeeklyInvested(Calendar date, Double invested, User userId){
        int dayOfYear = date.get(Calendar.DAY_OF_YEAR);
        WeeklyDataModel weeklyData = weeklyDataRepository.getByDayOfYearAndUser(dayOfYear,userId);
        if(weeklyData != null){
            weeklyData.setInvestedAmount(weeklyData.getInvestedAmount()+invested);
            weeklyData.setTotal();
            weeklyDataRepository.save(weeklyData);
        }else{
            WeeklyDataModel dataModel = new WeeklyDataModel();
            dataModel.setInvestedAmount(invested);
            dataModel.setDay(date.get(Calendar.DAY_OF_MONTH));
            dataModel.setDayOfYear(dayOfYear);
            dataModel.setMonth(date.get(Calendar.MONTH)+1);
            dataModel.setYear(date.get(Calendar.YEAR));
            dataModel.setUser(userId);
            dataModel.setTotal();
            weeklyDataRepository.save(dataModel);
        }
    }

    public boolean isBetween15Days(Calendar investment){
        Calendar daysAfter = Calendar.getInstance();
        daysAfter.add(Calendar.DATE,15);
        Calendar daysBefore = Calendar.getInstance();
        daysBefore.add(Calendar.DATE,-15);
        if(daysBefore.before(investment) && daysAfter.after(investment)) {
            return true;
        }
        return false;
    }

    @Override
    public List<InvoiceTransactions> getFinalizedInvoices(List<InvoiceTransactions> invoices) {
        List<InvoiceTransactions> finalized = new ArrayList<InvoiceTransactions>();
        for(InvoiceTransactions inv : invoices){
            if(inv.getStatus().equals("finalized")){
                inv.setPastDueDays(getDuePastDays(inv));
                finalized.add(inv);
            }
        }
        return finalized;
    }

    @Override
    public Auctions getFactuFinalizedInvoices(LoginJSON loginJSON){
        Auctions act = getCompletedInvestJSON(loginJSON);
        for(Results results : act.getResults()){
            results.setBusinessRel(getBusinessRel(loginJSON,results.getOperation().getBusiness_relationship()));
        }
        return act;
    }

    public long getDuePastDays(InvoiceTransactions inv){
        long diffInMillis = Math.abs(inv.getActualPaymentDate().getTime() - inv.getPaymentDate().getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public FacturedoData getProcessedResultsFactu(FacturedoData factuData,LoginJSON loginJSON){
        List<Results> rst = getInvestmentsJSON(factuData.getLoginJSON()).getResults();
        for(Results results : rst){
            results.setToBeCollectedIn(getPendingToCollection(results.getPayment_date()));
            results.setTotalInvestedDays(getInvestedTotalDays(results.getPayment_date(),results.getInvestment_date()));
            results.setProfit(getExpectedProfit(results));
            if(results.getCurrency().equals("PEN")){
                factuData.setSolesProfitExpected(factuData.getSolesProfitExpected() + results.getProfit());
            }else factuData.setDollarProfitExpected(factuData.getDollarProfitExpected() + results.getProfit());
            results.setBusinessRel(getBusinessRel(loginJSON,results.getOperation().getBusiness_relationship()));
            factuData.getResultsInProgress().add(results);
        }
        return factuData;
    }

    public long getPendingToCollection(Date payment){
        Date date = new Date();
        long diffInMillis = Math.abs(payment.getTime() - date.getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public long getInvestedTotalDays(Date payment, Date creation){
        long diffInMillis = Math.abs(payment.getTime() - creation.getTime());
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public double getExpectedProfit(Results result){
        double IGV = 0.18;
        double fee = 0.1;
        double IR = 0.05;
        double total = (( (( Math.pow(( 1 + (result.getDiscount_rate()/100) ),(result.getTotalInvestedDays() / 30 )) ) - 1 ) /
                (1 - fee * (1 + IGV) ))* result.getAmount());
        double net = total - (total*fee) - ((total*fee)*IGV);
        return net - (IR*total);
    }
}