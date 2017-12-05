package com.example.cc.mypiechart;

/**
 * Created by cc on 2017/12/5.
 */

public class PieEntry {

    private float number;//数值
    private int colorRes;
    private boolean selected;
    private float startC;//扇形其实角度
    private float endC;//扇形终止角度

    public PieEntry(float number, int colorRes, boolean selected) {
        this.number = number;
        this.colorRes = colorRes;
        this.selected = selected;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public int getColorRes() {
        return colorRes;
    }

    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public float getStartC() {
        return startC;
    }

    public void setStartC(float startC) {
        this.startC = startC;
    }

    public float getEndC() {
        return endC;
    }

    public void setEndC(float endC) {
        this.endC = endC;
    }
}
