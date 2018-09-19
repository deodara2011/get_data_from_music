package data.util;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Configurator {
    public static SimpleDateFormat DATE_FORMAT=new SimpleDateFormat("yyyyMMddHHmmss");
    private static final String CONFIG_FILE_NAME="/config/get_data.properties";
	private volatile static Properties properties=null;
	
	public static void main(String[] args){
	    //getProperties().list(System.out);
	    System.out.println(DATE_FORMAT.format(new Date()));
	}
	
	//双重检验加锁+volatile保证多线程安全并实际初始化一次。
	public static Properties getProperties(){
		if(properties==null){
			synchronized(Configurator.class){
				if(properties==null){
					init();
				}
			}
		}
		return properties;
	}
	private static boolean init(){
		//InputStream inputStream=null;
		properties=new Properties();
		/*
		try(InputStreamReader inputStream=new InputStreamReader(
				new FileInputStream(fileName), "UTF-8")) {
				*/
		try(InputStreamReader inputStream=new InputStreamReader(
		        Configurator.class.getResourceAsStream(CONFIG_FILE_NAME), "UTF-8")) {
			properties.load(inputStream);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public static String getProperty(String key){
		return getProperties().getProperty(key);
	}
	public static String getProperty(String key,String defaultValue){
			return getProperties().getProperty(key,defaultValue);
	}
	public static int getProperty(String key,int defaultValue){
		String value=getProperty(key);
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			return defaultValue;
		}
	}
	public static double getProperty(String key,double defaultValue){
		String value=getProperty(key);
		try{
			return Double.parseDouble(value);
		}catch(Exception e){
			return defaultValue;
		}
	}
	public static int[] getInts(String key){
		String value=getProperty(key);
		try{
			String[] strs=value.split(",");
			int[] result=new int[strs.length];
			for(int i=0;i<strs.length;i++)
				result[i]=Integer.parseInt(strs[i]);
			return result;
		}catch(Exception e){
			return null;
		}
	}
	public static double[] getDoubles(String key){
		String value=getProperty(key);
		try{
			String[] strs=value.split(",");
			double[] result=new double[strs.length];
			for(int i=0;i<strs.length;i++)
				result[i]=Double.parseDouble(strs[i]);
			return result;
		}catch(Exception e){
			return null;
		}
	}
	public static Calendar[] getTimeRange(){
	    String timeStr=getProperty("params.timeRange");
	    if(timeStr == null) return null;
	    timeStr=timeStr.replaceAll("[\\[\\]]", "");
	    String[] strs=timeStr.split("\\s*,\\s*");
	    if(strs == null || strs.length < 2)
	        return null;
	    try {
	        Calendar[] timeRange=new Calendar[2];
	        timeRange[0]=getCalendar(strs[0]);
            timeRange[1]=getCalendar(strs[1]);
            return timeRange;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
	private static Calendar getCalendar(String s) throws ParseException{
	    Calendar cal=Calendar.getInstance();
        cal.setTime(DATE_FORMAT.parse(s));
        return cal;
	}
}
