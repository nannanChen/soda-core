package com.soda.common;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SubData {
    private String CardID;
    private String TradeDate;
    private String TradeTime;
    private String subwayStation;
    private String industry;
    private String amount;
    private String type;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    private String lng;
    private String lat;

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getTradeDate() {
        return TradeDate;
    }

    public void setTradeDate(String tradeDate) {
        TradeDate = tradeDate;
    }

    public String getTradeTime() {
        return TradeTime;
    }

    public void setTradeTime(String tradeTime) {
        TradeTime = tradeTime;
    }

    public String getSubwayStation() {
        return subwayStation;
    }

    public void setSubwayStation(String subwayStation) {
        this.subwayStation = subwayStation;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
