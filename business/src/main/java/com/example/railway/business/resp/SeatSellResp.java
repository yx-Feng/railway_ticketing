package com.example.railway.business.resp;

public class SeatSellResp {

    private Integer carriageIndex;

    private String row;

    private String col;

    private String seatType;

    private String sell;

    public Integer getCarriageIndex() {
        return carriageIndex;
    }

    public void setCarriageIndex(Integer carriageIndex) {
        this.carriageIndex = carriageIndex;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    @Override
    public String toString() {
        return "SeatSellResp{" +
                "carriageIndex=" + carriageIndex +
                ", row='" + row + '\'' +
                ", col='" + col + '\'' +
                ", seatType='" + seatType + '\'' +
                ", sell='" + sell + '\'' +
                '}';
    }
}
