package com.aiqing.wallet.home.function;


public class FunctionBean {
    public int drawable;
    public String text;
    public OnItemClickListener onItemClickListener;

    public FunctionBean(String text, int drawable, OnItemClickListener itemClickListener) {
        this.text = text;
        this.drawable = drawable;
        onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String text);
    }

    public void click() {
        onItemClickListener.onItemClick(text);
    }
}
