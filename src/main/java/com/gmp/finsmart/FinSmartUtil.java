package com.gmp.finsmart;

import com.gmp.finsmart.JSON.*;

import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.ResponseJSON;
import com.gmp.web.dto.Investment;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.gmp.finsmart.FinSmartCIG.*;
import static com.gmp.hmviking.InvestmentUtil.getTime;


public class FinSmartUtil {

    private static String op = "[]";
    private static String op2 = "[\n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"2000.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fb4d\", \n" +
            "      \"companyName\": \"universidad catolica san pablo\", \n" +
            "      \"companyRuc\": \"20327998413\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 18, \n" +
            "    \"tem\": \"1.0103\", \n" +
            "    \"advanceAmount\": \"42954.53\", \n" +
            "    \"netAmount\": \"45215.29\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 18, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"45215.29\", \n" +
            "        \"code\": \"E001-238\", \n" +
            "        \"netAmount\": \"45215.29\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-03-05\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:27:36.149Z\", \n" +
            "    \"tea\": \"12.82\", \n" +
            "    \"availableBalancePercentage\": \"10.00\", \n" +
            "    \"_id\": \"6024279a1e736300090b4474\", \n" +
            "    \"publishAt\": \"2021-02-15T17:26:22.305Z\", \n" +
            "    \"debtorContact\": \"602427701e736300090b4473\", \n" +
            "    \"createdAt\": \"2021-02-10T18:36:10.246Z\", \n" +
            "    \"reservationAmount\": \"2260.76\"\n" +
            "  }\n" +
            "]\n";
    private static String op3 = "[\n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"1000.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fb4d\", \n" +
            "      \"companyName\": \"universidad catolica san pablo\", \n" +
            "      \"companyRuc\": \"20327998413\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 18, \n" +
            "    \"tem\": \"1.0103\", \n" +
            "    \"advanceAmount\": \"42954.53\", \n" +
            "    \"netAmount\": \"45215.29\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 18, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"45215.29\", \n" +
            "        \"code\": \"E001-238\", \n" +
            "        \"netAmount\": \"45215.29\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-03-05\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:27:36.149Z\", \n" +
            "    \"tea\": \"12.82\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"6024279a1e736300090b4474\", \n" +
            "    \"publishAt\": \"2021-02-15T17:26:22.305Z\", \n" +
            "    \"debtorContact\": \"602427701e736300090b4473\", \n" +
            "    \"createdAt\": \"2021-02-10T18:36:10.246Z\", \n" +
            "    \"reservationAmount\": \"2260.76\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"2000.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fb0b\", \n" +
            "      \"companyName\": \"petrex sa\", \n" +
            "      \"companyRuc\": \"20103744211\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 24, \n" +
            "    \"tem\": \"1.0742\", \n" +
            "    \"advanceAmount\": \"14230.55\", \n" +
            "    \"netAmount\": \"15811.72\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 24, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"17967.86\", \n" +
            "        \"code\": \"E001-984\", \n" +
            "        \"netAmount\": \"15811.72\", \n" +
            "        \"retentionAmount\": \"2156.14\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-03-11\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:27:02.972Z\", \n" +
            "    \"tea\": \"13.68\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"6023101cd468b10008a652d6\", \n" +
            "    \"publishAt\": \"2021-02-15T17:26:05.410Z\", \n" +
            "    \"debtorContact\": \"5fffa4120d587116a0ad0f27\", \n" +
            "    \"createdAt\": \"2021-02-09T22:43:40.187Z\", \n" +
            "    \"reservationAmount\": \"1581.17\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"1000.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 116, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"21373.35\", \n" +
            "    \"netAmount\": \"24015.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 116, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"25016.00\", \n" +
            "        \"code\": \"FPP2-543\", \n" +
            "        \"netAmount\": \"24015.00\", \n" +
            "        \"retentionAmount\": \"1001.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-06-11\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:32.608Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"60258ed11e736300090b468c\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:43.473Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:08:49.521Z\", \n" +
            "    \"reservationAmount\": \"2641.65\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 108, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"21613.50\", \n" +
            "    \"netAmount\": \"24015.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 108, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"25016.00\", \n" +
            "        \"code\": \"FPP2-542\", \n" +
            "        \"netAmount\": \"24015.00\", \n" +
            "        \"retentionAmount\": \"1001.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-06-03\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:20.467Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"1110.00\", \n" +
            "    \"_id\": \"60258e8e1e736300090b468b\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:51.075Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:07:42.622Z\", \n" +
            "    \"reservationAmount\": \"2401.50\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 91, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"17524.78\", \n" +
            "    \"netAmount\": \"19258.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 91, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"20060.00\", \n" +
            "        \"code\": \"FPP2-540\", \n" +
            "        \"netAmount\": \"19258.00\", \n" +
            "        \"retentionAmount\": \"802.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-05-17\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:03.135Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"110.00\", \n" +
            "    \"_id\": \"60258e151e736300090b4689\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:36.079Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:05:41.172Z\", \n" +
            "    \"reservationAmount\": \"1733.22\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 99, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"19982.70\", \n" +
            "    \"netAmount\": \"22203.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 99, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"23128.00\", \n" +
            "        \"code\": \"FPP2-541\", \n" +
            "        \"netAmount\": \"22203.00\", \n" +
            "        \"retentionAmount\": \"925.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-05-25\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:02.686Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"10.00\", \n" +
            "    \"_id\": \"60258e4a1e736300090b468a\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:27.060Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:06:34.283Z\", \n" +
            "    \"reservationAmount\": \"2220.30\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 86, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"16493.75\", \n" +
            "    \"netAmount\": \"18125.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 86, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"18880.00\", \n" +
            "        \"code\": \"FPP2-539\", \n" +
            "        \"netAmount\": \"18125.00\", \n" +
            "        \"retentionAmount\": \"755.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-05-12\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:25:40.146Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"1000.00\", \n" +
            "    \"_id\": \"60258de31e736300090b4688\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:22.792Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:04:51.387Z\", \n" +
            "    \"reservationAmount\": \"1631.25\"\n" +
            "  }\n" +
            "]\n";
    private static String op4 = "[\n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fb4d\", \n" +
            "      \"companyName\": \"universidad catolica san pablo\", \n" +
            "      \"companyRuc\": \"20327998413\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 18, \n" +
            "    \"tem\": \"1.0103\", \n" +
            "    \"advanceAmount\": \"42954.53\", \n" +
            "    \"netAmount\": \"45215.29\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 18, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"45215.29\", \n" +
            "        \"code\": \"E001-238\", \n" +
            "        \"netAmount\": \"45215.29\", \n" +
            "        \"retentionAmount\": \"0.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-03-05\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:27:36.149Z\", \n" +
            "    \"tea\": \"12.82\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"6024279a1e736300090b4474\", \n" +
            "    \"publishAt\": \"2021-02-15T17:26:22.305Z\", \n" +
            "    \"debtorContact\": \"602427701e736300090b4473\", \n" +
            "    \"createdAt\": \"2021-02-10T18:36:10.246Z\", \n" +
            "    \"reservationAmount\": \"2260.76\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fb0b\", \n" +
            "      \"companyName\": \"petrex sa\", \n" +
            "      \"companyRuc\": \"20103744211\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 24, \n" +
            "    \"tem\": \"1.0742\", \n" +
            "    \"advanceAmount\": \"14230.55\", \n" +
            "    \"netAmount\": \"15811.72\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 24, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"17967.86\", \n" +
            "        \"code\": \"E001-984\", \n" +
            "        \"netAmount\": \"15811.72\", \n" +
            "        \"retentionAmount\": \"2156.14\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-03-11\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:27:02.972Z\", \n" +
            "    \"tea\": \"13.68\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"6023101cd468b10008a652d6\", \n" +
            "    \"publishAt\": \"2021-02-15T17:26:05.410Z\", \n" +
            "    \"debtorContact\": \"5fffa4120d587116a0ad0f27\", \n" +
            "    \"createdAt\": \"2021-02-09T22:43:40.187Z\", \n" +
            "    \"reservationAmount\": \"1581.17\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 116, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"21373.35\", \n" +
            "    \"netAmount\": \"24015.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 116, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"25016.00\", \n" +
            "        \"code\": \"FPP2-543\", \n" +
            "        \"netAmount\": \"24015.00\", \n" +
            "        \"retentionAmount\": \"1001.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-06-11\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:32.608Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"60258ed11e736300090b468c\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:43.473Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:08:49.521Z\", \n" +
            "    \"reservationAmount\": \"2641.65\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 108, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"21613.50\", \n" +
            "    \"netAmount\": \"24015.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 108, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"25016.00\", \n" +
            "        \"code\": \"FPP2-542\", \n" +
            "        \"netAmount\": \"24015.00\", \n" +
            "        \"retentionAmount\": \"1001.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-06-03\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:20.467Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"60258e8e1e736300090b468b\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:51.075Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:07:42.622Z\", \n" +
            "    \"reservationAmount\": \"2401.50\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 91, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"17524.78\", \n" +
            "    \"netAmount\": \"19258.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 91, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"20060.00\", \n" +
            "        \"code\": \"FPP2-540\", \n" +
            "        \"netAmount\": \"19258.00\", \n" +
            "        \"retentionAmount\": \"802.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-05-17\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:03.135Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"60258e151e736300090b4689\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:36.079Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:05:41.172Z\", \n" +
            "    \"reservationAmount\": \"1733.22\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 99, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"19982.70\", \n" +
            "    \"netAmount\": \"22203.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 99, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"23128.00\", \n" +
            "        \"code\": \"FPP2-541\", \n" +
            "        \"netAmount\": \"22203.00\", \n" +
            "        \"retentionAmount\": \"925.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-05-25\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:26:02.686Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"60258e4a1e736300090b468a\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:27.060Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:06:34.283Z\", \n" +
            "    \"reservationAmount\": \"2220.30\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"0.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7faca\", \n" +
            "      \"companyName\": \"soluciones en telecomunicaciones will s.a.c.\", \n" +
            "      \"companyRuc\": \"20517573869\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 86, \n" +
            "    \"tem\": \"1.2408\", \n" +
            "    \"advanceAmount\": \"16493.75\", \n" +
            "    \"netAmount\": \"18125.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 86, \n" +
            "    \"currency\": \"pen\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"18880.00\", \n" +
            "        \"code\": \"FPP2-539\", \n" +
            "        \"netAmount\": \"18125.00\", \n" +
            "        \"retentionAmount\": \"755.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-05-12\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:25:40.146Z\", \n" +
            "    \"tea\": \"15.95\", \n" +
            "    \"availableBalancePercentage\": \"0.00\", \n" +
            "    \"_id\": \"60258de31e736300090b4688\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:22.792Z\", \n" +
            "    \"debtorContact\": \"5ffdc4de0981ba22982baf3f\", \n" +
            "    \"createdAt\": \"2021-02-11T20:04:51.387Z\", \n" +
            "    \"reservationAmount\": \"1631.25\"\n" +
            "  }, \n" +
            "  {\n" +
            "    \"status\": \"sale closed\", \n" +
            "    \"availableBalanceAmount\": \"10.00\", \n" +
            "    \"debtor\": {\n" +
            "      \"_id\": \"5fdfb84046ff262a53c7fb74\", \n" +
            "      \"companyName\": \"asica farms s.a.c.\", \n" +
            "      \"companyRuc\": \"20600483596\"\n" +
            "    }, \n" +
            "    \"minimumDuration\": 119, \n" +
            "    \"tem\": \"1.0274\", \n" +
            "    \"advanceAmount\": \"101947.50\", \n" +
            "    \"netAmount\": \"113275.00\", \n" +
            "    \"isConfirming\": false, \n" +
            "    \"toBeCollectedIn\": 119, \n" +
            "    \"currency\": \"usd\", \n" +
            "    \"physicalInvoices\": [\n" +
            "      {\n" +
            "        \"totalAmount\": \"115000.00\", \n" +
            "        \"code\": \"E001-301\", \n" +
            "        \"netAmount\": \"113275.00\", \n" +
            "        \"retentionAmount\": \"1725.00\"\n" +
            "      }\n," +
            "      {\n" +
            "        \"totalAmount\": \"115000.00\", \n" +
            "        \"code\": \"E001-123\", \n" +
            "        \"netAmount\": \"113275.00\", \n" +
            "        \"retentionAmount\": \"1725.00\"\n" +
            "      }\n" +
            "    ], \n" +
            "    \"paymentDate\": \"2021-06-14\", \n" +
            "    \"updatedAt\": \"2021-02-15T17:25:31.178Z\", \n" +
            "    \"tea\": \"13.05\", \n" +
            "    \"availableBalancePercentage\": \"100.00\", \n" +
            "    \"_id\": \"6026d3f71e736300090b487b\", \n" +
            "    \"publishAt\": \"2021-02-15T17:25:09.492Z\", \n" +
            "    \"debtorContact\": \"5ffdc4df0981ba22982bb13f\", \n" +
            "    \"createdAt\": \"2021-02-12T19:16:07.735Z\", \n" +
            "    \"reservationAmount\": \"11327.50\"\n" +
            "  }\n" +
            "]\n";


    private static final String amountBigger = "INVESTMENTS.INVESTMENT_AMOUNT_IS_BIGGER_THAN_TARGET_INVOICE_AVAILABLE_BALANCE";
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static DecimalFormat df2 = new DecimalFormat("#.#");

    public static List<String> parseString(String formField){
        return Arrays.stream(formField.split(","))
                .filter(s -> (s != null && s.length() > 0))
                .collect(Collectors.toList());
    }

    public static List<Integer> parseInt(String formField){
        return Arrays.stream(formField.split(","))
                .filter(s -> (s != null && s.length() > 0))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static ResponseJSON postToFinSmart(double amount, Investment investment, HashMap<String,Double> balance,
                                              LoginJSON json){
        String parameters;
        ResponseJSON responseJSON;
        parameters = generateJSONInvest(amount, investment.getCurrency(), investment.getOpportunity().getId(),balance);
        responseJSON = executeInvestment(parameters,json.getAccessToken());
        return responseJSON;
    }

    public static Investment generateAndSubmit(Investment investment, LoginJSON loginJSON,
                                               HashMap<String,Double> balance) {
        ResponseJSON responseJSON;
        double amountSubmit = 0;
        if(investment.getOpportunity().getAvailableBalanceAmount() >= investment.getAmount()){
            responseJSON = postToFinSmart(investment.getAmount(),investment,balance,loginJSON);
            if(responseJSON.isStatus()){
                amountSubmit = investment.getAmount();
                investment.setStatus("true");
            }else{
                if(responseJSON.getMessage().replace('"',' ').equals(amountBigger)){
                    //FEATURE:IF AMOUNT IS BIGGER INVEST 30% LESS
                    double adjustment = (investment.getAmount()-(investment.getAmount()*0.30));
                    if(adjustment > 100){
                        responseJSON = postToFinSmart(adjustment,investment,balance,loginJSON);
                    }
                    if(responseJSON.isStatus()){
                        amountSubmit = adjustment;
                        investment.setStatus("true");
                    }
                }else {
                    investment.setStatus("false");
                    investment.setMessage(responseJSON.getMessage());
                }
            }
        }else {
            //Feature: Auto investment to the current Invoice amount available
            if(investment.getOpportunity().getAvailableBalanceAmount() > 0){
                responseJSON = postToFinSmart(investment.getOpportunity().getAvailableBalanceAmount(),investment,
                        balance,loginJSON);
                if(responseJSON.isStatus()){
                    amountSubmit = investment.getOpportunity().getAvailableBalanceAmount();
                    investment.setAutoAdjusted(true);
                    investment.setAdjustedAmount(investment.getOpportunity().getAvailableBalanceAmount());
                    investment.setStatus("true");
                    investment.setMessage("");
                }
            }else{
                investment.setStatus("false");
                investment.setMessage("AMOUNT AVAILABLE IS 0.00");
            }
        }
        investment.setCompleted(true);
        System.out.println(Thread.currentThread().getName()+"Invest:"+getTime()+
                investment.getOpportunity().getPhysicalInvoices().get(0).getCode()+" "+
                investment.getOpportunity().getDebtor().getCompanyName()+" STATUS: "+
                investment.getStatus()+" Amount:"+amountSubmit+ " MSG:"+investment.getMessage());

        return investment;
    }

    public static String generateJSONInvest(double amount, String currency, String invoice_id,HashMap<String,Double> balance){
        //FEATURE TO INVEST WITH AVAILABLE BALANCE
        if(amount >= balance.get(currency)){
            return "{\"amount\":\""+df2.format(balance.get(currency))+"\",\"currency\":\""+currency+"\",\"invoice\":\""+invoice_id+
                    "\",\"type\":\"investment\"}";
        }else return "{\"amount\":\""+amount+"\",\"currency\":\""+currency+"\",\"invoice\":\""+invoice_id+
                "\",\"type\":\"investment\"}";
    }

    public static Investment waitForInvoiceInvest(HashMap<String,Opportunities> oppMap, Investment formInvestment){
        for (Map.Entry<String, Opportunities> it : oppMap.entrySet()) {
            if(!formInvestment.getSkipList().contains(it.getKey())) {
                if(it.getValue().getPhysicalInvoices().contains(formInvestment.getFormCode()) &&
                        it.getValue().getCurrency().equals(formInvestment.getCurrency())){
                    formInvestment.setOpportunity(it.getValue());
                    return formInvestment;
                }else formInvestment.getSkipList().add(it.getKey());
            }
        }
        return formInvestment;
    }

    public static Investment waitForInvoiceInvest(List<Opportunities> opportunities, Investment formInvestment){
        for(Opportunities op : opportunities){
            if(op.getPhysicalInvoices().contains(formInvestment.getFormCode()) &&
                    op.getCurrency().equals(formInvestment.getCurrency())){
                formInvestment.setOpportunity(op);
                return formInvestment;
            }
        }
        return formInvestment;
    }

    public static Investment waitForInvoiceInvest(String threadName, List<Opportunities> opportunities,
                                                  List<Investment> invList){
        String concat = "";
        for(Opportunities op : opportunities){
            concat = concat+op.getPhysicalInvoices().get(0).getCode()+"("+op.getAvailableBalanceAmount()+")"+" ";
            for(Investment inv : invList){
                if(op.getPhysicalInvoices().contains(inv.getFormCode()) && op.getCurrency().equals(inv.getCurrency())){
                    inv.setOpportunity(op);
                    return inv;
                }
            }
        }
        System.out.println(threadName+"Seeker:"+ getTime()+concat);
        return null;
    }

    public static List<Opportunities> getOpportunities(int i){
        Type founderListType = new TypeToken<ArrayList<Opportunities>>(){}.getType();
        if(i>=5 && i <=10){
            return gson.fromJson(op2, founderListType);
        }
        else if (i>10 && i <=15){
            return gson.fromJson(op3, founderListType);
        }
        else if (i>60 && i <=80){
            return gson.fromJson(op4, founderListType);
        }
        else return gson.fromJson(op, founderListType);
    }

    public static double calculateROI(double tem, int days, double amount){
        return BigDecimal.valueOf(((tem/30)*amount*days)*0.01)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Map<String, InvoiceTransactions> indexInvoices(List<InvoiceTransactions> invoices){
        Map<String,InvoiceTransactions> invoicesIndex = new HashMap<>();
        for(InvoiceTransactions inv : invoices){
            invoicesIndex.put(inv.get_id(),inv);
        }
        return invoicesIndex;
    }

    public static String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public static HashMap<String,Opportunities> processOpportunities
            (List<Opportunities> jsonList, HashMap<String,Opportunities> oppMap){
        for(Opportunities op : jsonList){
            oppMap.put(op.getId(),op);
        }
        return oppMap;
    }
}
