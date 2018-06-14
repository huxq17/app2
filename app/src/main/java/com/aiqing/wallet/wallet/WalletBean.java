package com.aiqing.wallet.wallet;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.home.details.RecordBean;

import java.util.List;

public class WalletBean extends BaseResponse<WalletBean.Wallet> {
    public static class Wallet {
        double sum;
        List<DataBean> list;

        public static class DataBean {
            double Balance;
            String Currency;
            String ExchangeRate;
            double Valuation;

            public double getBalance() {
                return Balance;
            }

            public void setBalance(double balance) {
                Balance = balance;
            }

            public String getCurrency() {
                return Currency;
            }

            public void setCurrency(String currency) {
                Currency = currency;
            }

            public String getExchangeRate() {
                return ExchangeRate;
            }

            public void setExchangeRate(String exchangeRate) {
                ExchangeRate = exchangeRate;
            }

            public double getValuation() {
                return Valuation;
            }

            public void setValuation(double valuation) {
                Valuation = valuation;
            }
        }

        public double getSum() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum = sum;
        }

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }
    }
}
