package com.aiqing.wallet.currency;

import com.aiqing.wallet.bean.BaseResponse;

import java.util.List;

public class CurrencyBean extends BaseResponse<CurrencyBean> {
    List<Currency> list;
    public String currency;

    public static class Currency {
        int ID;
        String Name;
        String FullName;
        String Symbol;
        String Img;
        String Type;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getSymbol() {
            return Symbol;
        }

        public void setSymbol(String symbol) {
            Symbol = symbol;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String img) {
            Img = img;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }
    }

    public List<Currency> getList() {
        return list;
    }

    public void setList(List<Currency> list) {
        this.list = list;
    }
}
