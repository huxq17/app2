package com.aiqing.wallet.currency;

import com.aiqing.wallet.bean.BaseResponse;

import java.util.List;

public class RateBean extends BaseResponse<RateBean> {
    List<Rate> list;

    public static class Rate {
        int ID;
        int Type;
        int FromCurrency;
        int ToCurrency;
        String ExchangeRate;
        int FeeCurrency;
        String FeeAmount;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public int getFromCurrency() {
            return FromCurrency;
        }

        public void setFromCurrency(int fromCurrency) {
            FromCurrency = fromCurrency;
        }

        public int getToCurrency() {
            return ToCurrency;
        }

        public void setToCurrency(int toCurrency) {
            ToCurrency = toCurrency;
        }

        public String getExchangeRate() {
            return ExchangeRate;
        }

        public void setExchangeRate(String exchangeRate) {
            ExchangeRate = exchangeRate;
        }

        public int getFeeCurrency() {
            return FeeCurrency;
        }

        public void setFeeCurrency(int feeCurrency) {
            FeeCurrency = feeCurrency;
        }

        public String getFeeAmount() {
            return FeeAmount;
        }

        public void setFeeAmount(String feeAmount) {
            FeeAmount = feeAmount;
        }
    }

    public List<Rate> getList() {
        return list;
    }

    public void setList(List<Rate> list) {
        this.list = list;
    }
}
