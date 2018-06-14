package com.aiqing.wallet.home.node;

import com.aiqing.wallet.bean.BaseResponse;
import com.aiqing.wallet.home.information.InformationBean;

public class NodePlanBean extends BaseResponse<NodePlanBean> {
    accumulated accumulated;
    asset asset;
    earning earning;

    public NodePlanBean.accumulated getAccumulated() {
        return accumulated;
    }

    public void setAccumulated(NodePlanBean.accumulated accumulated) {
        this.accumulated = accumulated;
    }

    public NodePlanBean.asset getAsset() {
        return asset;
    }

    public void setAsset(NodePlanBean.asset asset) {
        this.asset = asset;
    }

    public NodePlanBean.earning getEarning() {
        return earning;
    }

    public void setEarning(NodePlanBean.earning earning) {
        this.earning = earning;
    }

    /**
     * 累计
     */
    public static class accumulated {
        double EarningSelf;
        double EarningTeam;
        double EarningVip;

        public double getEarningSelf() {
            return EarningSelf;
        }

        public void setEarningSelf(double earningSelf) {
            EarningSelf = earningSelf;
        }

        public double getEarningTeam() {
            return EarningTeam;
        }

        public void setEarningTeam(double earningTeam) {
            EarningTeam = earningTeam;
        }

        public double getEarningVip() {
            return EarningVip;
        }

        public void setEarningVip(double earningVip) {
            EarningVip = earningVip;
        }
    }

    /**
     * 节点
     */
    public static class asset {
        int ID;
        int AccountID;
        int Currency;
        double BalanceA;
        String BalanceB;
        String UpdatedAt;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getAccountID() {
            return AccountID;
        }

        public void setAccountID(int accountID) {
            AccountID = accountID;
        }

        public int getCurrency() {
            return Currency;
        }

        public void setCurrency(int currency) {
            Currency = currency;
        }

        public double getBalanceA() {
            return BalanceA;
        }

        public void setBalanceA(double balanceA) {
            BalanceA = balanceA;
        }

        public String getBalanceB() {
            return BalanceB;
        }

        public void setBalanceB(String balanceB) {
            BalanceB = balanceB;
        }

        public String getUpdatedAt() {
            return UpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            UpdatedAt = updatedAt;
        }
    }

    /**
     * 最新收益
     */
    public static class earning {
        double ActualSelf;
        double ActualTeam;
        double ActualVip;

        public double getActualSelf() {
            return ActualSelf;
        }

        public void setActualSelf(double actualSelf) {
            ActualSelf = actualSelf;
        }

        public double getActualTeam() {
            return ActualTeam;
        }

        public void setActualTeam(double actualTeam) {
            ActualTeam = actualTeam;
        }

        public double getActualVip() {
            return ActualVip;
        }

        public void setActualVip(double actualVip) {
            ActualVip = actualVip;
        }
    }
}
