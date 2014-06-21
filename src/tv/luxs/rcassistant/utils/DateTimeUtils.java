package tv.luxs.rcassistant.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtils {
	
	//日期字符串转换为Calendar对象
	public static Calendar getCalendar(String dateTime) throws ParseException{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(dateTime);
		cal.setTime(date);
		return cal;
	}
	
	//将Calendar转换为字符串
	public static String getDateTimeString(Calendar cal){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setLenient(false);
		String s = format.format(cal.getTime());
		return s;
	}
	
	//将Calendar转换为字符串
	public static String getDateString(Calendar cal){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
		String s = format.format(cal.getTime());
		return s;
	}

	//获得当前时间
	public static Calendar getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		return cal;
	}

	//获得当前年
	public static String getCurrentYear(){
		int temp = (Calendar.getInstance()).get(Calendar.YEAR);
		String tempStr = temp + "";
		return tempStr;
	}
	
	//获得当前月
	public static String getCurrentMonth(){
		int temp = (Calendar.getInstance()).get(Calendar.MONTH);
		String tempStr = temp + 1 + "";
		return tempStr;
	}
	
	//获得当前日
	public static String getCurrentDay(){
		int temp = (Calendar.getInstance()).get(Calendar.DAY_OF_MONTH);
		String tempStr = temp + "";
		return tempStr;
	}
	
	//获得当前小时
	public static String getCurrentHour(){
		int temp = (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY);
		String tempStr = temp + "";
		return tempStr;
	}
	
	//获得当前分
	public static String getCurrentMinutes(){
		int temp = (Calendar.getInstance()).get(Calendar.MINUTE);
		String tempStr = temp + "";
		return tempStr;
	}
	
	//获得当前秒
	public static String getCurrentSecond(){
		int temp = (Calendar.getInstance()).get(Calendar.SECOND);
		String tempStr = temp + "";
		return tempStr;
	}
	
	//获得上一年
	public static Calendar getPreYear(Calendar calendar){
		calendar.add(Calendar.YEAR, -1);
		return calendar;
	}
	//获得下一年
	public static Calendar getNextYear(Calendar calendar){
		calendar.add(Calendar.YEAR, 1);
		return calendar;
	}
	//获得上一月
	public static Calendar getPreMonth(Calendar calendar){
		calendar.add(Calendar.MONTH, -1);
		return calendar;
	}
	//获得下一月
	public static Calendar getNextMonth(Calendar calendar){
		calendar.add(Calendar.MONTH, 1);
		return calendar;
	}
	//获得上一小时
	public static Calendar getPreHour(Calendar calendar){
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		return calendar;
	}
	//获得下一小时
	public static Calendar getNextHour(Calendar calendar){
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		return calendar;
	}
	//获得上一小时
	public static Calendar getPreMinute(Calendar calendar){
		calendar.add(Calendar.MINUTE, -1);
		return calendar;
	}
	//获得下一小时
	public static Calendar getNextMinute(Calendar calendar){
		calendar.add(Calendar.MINUTE, 1);
		return calendar;
	}
	
	//获得指定月的天数
	public static int getDayCountOfmonth(int year,int month){
		Calendar time=Calendar.getInstance(); 
		time.clear(); 
		time.set(Calendar.YEAR,year); 
		time.set(Calendar.MONTH,month-1);//Calendar对象默认一月为0            
		int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
		return day;
	}
	
	//获得指定月的天数
	public static int getDaysOfMonth(Calendar calendar){
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	//指定日期获得星期几
	public static int getDayForWeek(Calendar calendar){  
	    return calendar.get(Calendar.DAY_OF_WEEK);  
	}  
	
	//将json date 转为日期字符串
	public static String getDateFromJsonDate(String jsonDate){
		try{
		 String tempDate = jsonDate.replace("/Date(", "");
		 tempDate = tempDate.replace("+0800)/", "");
		 long time = Long.parseLong(tempDate);//秒
         GregorianCalendar gc = new GregorianCalendar(); 
         gc.setTimeInMillis(time);
         java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy年MM月dd日");
         String resultDate = format.format(gc.getTime());
System.out.print("resultDate:" + resultDate);
         return resultDate;
		}catch (Exception e) {
			return " ";
		}
	}
}
