package com.example.qt;

public class DayInfo
{
    private String day;
    private String date;
    private boolean inMonth;
    private boolean istoday;
    
    public DayInfo()
    {
    	this.istoday = false;
    }
    
    public String getDate(){
    	return date;
    }
    
    public void setDate(String date){
    	this.date = date;
    }
    /**
     * 날짜를 반환한다.
     * 
     * @return day 날짜
     */
    public String getDay()
    {
        return day;
    }
 
    /**
     * 날짜를 저장한다.
     * 
     * @param day 날짜
     */
    public void setDay(String day)
    {
        this.day = day;
    }
 
    /**
     * 이번달의 날짜인지 정보를 반환한다.
     * 
     * @return inMonth(true/false)
     */
    public boolean isInMonth()
    {
        return inMonth;
    }
 
    /**
     * 이번달의 날짜인지 정보를 저장한다.
     * 
     * @param inMonth(true/false)
     */
    public void setInMonth(boolean inMonth)
    {
        this.inMonth = inMonth;
    }
    
    public boolean isToday()
    {
    	return istoday;
    }
    
    public void isToday(boolean istoday)
    {
    	this.istoday = istoday;
    }
 
}