package com.datatorrent.contrib.rule;

public class StockBean {

  private double oPrice;
  private double cPrice;
  private String symbol;
  private long timestamp;
  private String date;

  public StockBean(){}

  public StockBean(double oPrice, double cPrice, String symbol, long timestamp)
  {
    super();
    this.oPrice = oPrice;
    this.cPrice = cPrice;
    this.symbol = symbol;
    this.timestamp = timestamp;
  }

  public StockBean(double oPrice, double cPrice, String symbol, String date)
  {
    super();
    this.oPrice = oPrice;
    this.cPrice = cPrice;
    this.symbol = symbol;
    this.date = date;
  }


  public double getoPrice()
  {
    return oPrice;
  }


  public void setoPrice(double oPrice)
  {
    this.oPrice = oPrice;
  }


  public double getcPrice()
  {
    return cPrice;
  }


  public void setcPrice(double cPrice)
  {
    this.cPrice = cPrice;
  }


  public String getSymbol()
  {
    return symbol;
  }
  public void setSymbol(String symbol)
  {
    this.symbol = symbol;
  }
  public long getTimestamp()
  {
    return timestamp;
  }
  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }


  public String getDate()
  {
    return date;
  }


  public void setDate(String date)
  {
    this.date = date;
  }


  @Override
  public String toString()
  {
    return "StockBean [oPrice=" + oPrice + ", cPrice=" + cPrice + ", symbol=" + symbol + ", timestamp=" + timestamp + "]";
  }

}
