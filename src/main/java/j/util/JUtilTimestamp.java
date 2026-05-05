package j.util;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class JUtilTimestamp{
	public static final String WEEK_DAY_1="星期一";
	public static final String WEEK_DAY_2="星期二";
	public static final String WEEK_DAY_3="星期三";
	public static final String WEEK_DAY_4="星期四";
	public static final String WEEK_DAY_5="星期五";
	public static final String WEEK_DAY_6="星期六";
	public static final String WEEK_DAY_7="星期日";
	
	public static final long millisOfSecond=1000L;
	public static final long millisOfMinute=60000L;
	public static final long millisOfHour=3600000L;
	public static final long millisOfDay=86400000L;
	public static final long millisOfWeek=86400000*7L;

	/**
	 * 时间输出格式化（区分大小写）
	 * yyyy	四位年份
	 * MM	两位数月份
	 * dd	两位数日
	 * HH	24小时制小时
	 * mm	分钟
	 * ss	秒
	 */

	/**
	 *
	 * @param time
	 * @return 如 20080808
	 */
	public static int getDateAsYYYYMMDD(Timestamp time){
		return getDateAsYYYYMMDD(time, null);
	}

	/**
	 *
	 * @param time
	 * @param zoneId
	 * @return 如 20080808
	 */
	public static int getDateAsYYYYMMDD(Timestamp time, ZoneId zoneId){
		Instant instant = Instant.ofEpochMilli(time.getTime());

		// 转换为系统默认时区的 LocalDateTime
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId==null?ZoneId.systemDefault():zoneId);

		//yyyy-MM-dd HH:mm:ss
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatted = dateTime.format(formatter);

		return Integer.parseInt(JUtilString.replaceAll(formatted.substring(0, 10), "-", ""));
	}

	/**
	 *
	 * @param time
	 * @return 如 2008-08-08
	 */
	public static String getDateYYYYMMDD(Timestamp time){
		return getDateYYYYMMDD(time, null);
	}

	/**
	 *
	 * @param time
	 * @param zoneId
	 * @return 如 2008-08-08
	 */
	public static String getDateYYYYMMDD(Timestamp time, ZoneId zoneId){
		Instant instant = Instant.ofEpochMilli(time.getTime());

		// 转换为系统默认时区的 LocalDateTime
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId==null?ZoneId.systemDefault():zoneId);

		//yyyy-MM-dd HH:mm:ss
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatted = dateTime.format(formatter1);

		return formatted.substring(0, 10);
	}

	/**
	 *
	 * @param time
	 * @return 如 2008-08-08
	 */
	public static String getDateMMDDYYYY(Timestamp time){
		return getDateMMDDYYYY(time, null);
	}

	/**
	 *
	 * @param time
	 * @return 如 2008-08-08
	 * @return
	 */
	public static String getDateMMDDYYYY(Timestamp time, ZoneId zoneId){
		Instant instant = Instant.ofEpochMilli(time.getTime());

		// 转换为系统默认时区的 LocalDateTime
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId==null?ZoneId.systemDefault():zoneId);

		//MM-dd-yyyy HH:mm:ss
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		String formatted = dateTime.format(formatter);

		return formatted.substring(0, 10);
	}

	/**
	 *
	 * @param time
	 * @param zoneId
	 * @param format
	 * @return
	 */
	public static String getDateFormatted(Timestamp time, ZoneId zoneId, String format){
		// 或使用 Instant.now()
		Instant instant = Instant.ofEpochMilli(time.getTime());

		// 转换为系统默认时区的 LocalDateTime
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId==null?ZoneId.systemDefault():zoneId);

		//MM-dd-yyyy HH:mm:ss
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		String formatted = dateTime.format(formatter);

		return formatted.substring(0, 10);
	}

	/**
	 * 得到符合中国习惯的周一~周日顺序号（1~7）
	 * @param time
	 * @return
	 */
	public static int getWeekDayUsualOrderCn(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        calendar=null;
        if(dayNum == Calendar.SUNDAY){
            return 7;
        }else if(dayNum == Calendar.MONDAY){
            return 1;
    	}else if(dayNum == Calendar.TUESDAY){
            return 2;
    	}else if(dayNum == Calendar.WEDNESDAY){
            return 3;
    	}else if(dayNum == Calendar.THURSDAY){
            return 4;
    	}else if(dayNum == Calendar.FRIDAY){
            return 5;
    	}else{
            return 6;
    	}
    }
	
	/**
	 * 得到符合中国习惯的周一~周日的日期
	 * @param time
	 * @return
	 */
	public static String[] getDatesOfWeekCn(long time) {
		Timestamp t=new Timestamp(time);
		String[] dates=new String[7];
		int weekDay=getWeekDayUsualOrderCn(time);
		for(int i=1; i<=7; i++) {
			Timestamp _time=JUtilTimestamp.addToTime(t, i-weekDay);
			dates[i-1]=_time.toString().substring(0,10);
		}
		return dates;
	}
	
	
	/**
	 * 得到指定时间是星期几的中文名
	 * @param time
	 * @return
	 */
    public static String getWeekDayCn(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        calendar=null;
        if(dayNum == Calendar.SUNDAY){
            return "星期日";
        }else if(dayNum == Calendar.MONDAY){
            return "星期一";
    	}else if(dayNum == Calendar.TUESDAY){
            return "星期二";
    	}else if(dayNum == Calendar.WEDNESDAY){
            return "星期三";
    	}else if(dayNum == Calendar.THURSDAY){
            return "星期四";
    	}else if(dayNum == Calendar.FRIDAY){
            return "星期五";
    	}else{
            return "星期六";
    	}
    }
    
    /**
	 * 得到当前是星期几的中文名
     * @return
     */
    public static String getWeekDayCn(){
        return getWeekDayCn(System.currentTimeMillis());
    }  
    
    
    public static String getWeekDayEn(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        calendar=null;
        if(dayNum == Calendar.SUNDAY){
            return "Sunday";
        }else if(dayNum == Calendar.MONDAY){
            return "Monday";
    	}else if(dayNum == Calendar.TUESDAY){
            return "Tuesday";
    	}else if(dayNum == Calendar.WEDNESDAY){
            return "Wednesday";
    	}else if(dayNum == Calendar.THURSDAY){
            return "Thursday";
    	}else if(dayNum == Calendar.FRIDAY){
            return "Friday";
    	}else{
            return "Saturday";
    	}
    }
    
    /**
	 * 得到当前是星期几的中文名
     * @return
     */
    public static String getWeekDayEn(){
        return getWeekDayEn(System.currentTimeMillis());
    }  
    
    /**
     * 得到星期几、时、分、秒、年、月、日、每月的第几周等，其中月份的返回值是实际月份数-1
     * @param time
     * @param type
     * @return
     */
    public static int getValue(long time,int type){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int result = calendar.get(type);
        return result;
    }
    
    /**
     * 是否闰年
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year){
    	if((year%4==0&&year%100!=0)||(year%100==0&&year%400==0)){
    		return true;
    	}
    	return false;
    }
    

    
    /**
     * 得到某月的天数
     * @param year
     * @return
     */
    public static int getDaysOfMonth(int year,int month){
    	if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
    		return 31;
    	}else if(month==2){
    		if(isLeapYear(year)){
    			return 29;
    		}else{
    			return 28;    		
    		}
    	}else{
    		return 30;
    	}
    }

	/**
	 *
	 * @param now
	 * @return
	 */
	public static int getDaysElapsedOfYear(long now) {
		int year=JUtilTimestamp.getValue(now, Calendar.YEAR);
		int month=JUtilTimestamp.getValue(now, Calendar.MONTH)+1;
		int days=0;
		for(int i=1; i<month; i++) {
			days+=JUtilTimestamp.getDaysOfMonth(year, i);
		}
		days+=JUtilTimestamp.getValue(now, Calendar.DAY_OF_MONTH);
		return days;
	}
    
    /**
     * 
     * @param original
     * @param daysAdd
     * @return
     */
    public static Timestamp addToTime(Timestamp original,int daysAdd){
    	if(original==null||daysAdd==0) return original;
    	return new Timestamp(original.getTime()+daysAdd*3600000*24L);
    }

    
    /**
     * 
     * @param original
     * @param daysAdd
     * @return
     */
    public static Timestamp addToTime(Timestamp original,double daysAdd){
    	if(original==null||daysAdd==0) return original;
    	return new Timestamp(original.getTime()+(long)(daysAdd*3600000*24L));
    }


	/**
	 * 
	 * @return
	 */
	public static String timestamp(){
		return timestamp(System.currentTimeMillis());
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String timestamp(long time){
		String t=(new Timestamp(time)).toString();
		if(t.length()==19) t+=".000";
		else if(t.length()==21) t+="00";
		else if(t.length()==22) t+="0";
		return t;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isTimestamp(String value){
		if(value==null) return false;
		return (value.matches("^\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}$")
					||value.matches("^\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}.\\d{1,3}$"));
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static boolean isHHMM(String value){
		if(value==null) return false;
		if(!value.matches("^\\d{2}:\\d{2}$")) return false;
		String[] cells = JUtilString.getTokens(value, ":");
		int h = Integer.parseInt(cells[0]);
		if(h<0 || h>24) return false;

		int m = Integer.parseInt(cells[1]);
		if(m<0 || h>59) return false;

		return true;
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	public static boolean isHHMMSS(String value){
		if(value==null) return false;
		if(!value.matches("^\\d{2}:\\d{2}:\\d{2}$")) return false;
		String[] cells = JUtilString.getTokens(value, ":");
		int h = Integer.parseInt(cells[0]);
		if(h<0 || h>24) return false;

		int m = Integer.parseInt(cells[1]);
		if(m<0 || h>59) return false;

		int s = Integer.parseInt(cells[2]);
		if(s<0 || s>59) return false;

		return true;
	}


	/**
	 * 
	 * @param now
	 * @return
	 */
	public static Timestamp getMondayOfWeek(long now){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(now));
        
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        calendar=null;
        if(dayNum == Calendar.SUNDAY){
        	return new Timestamp(now-millisOfDay*6);
        }else if(dayNum == Calendar.MONDAY){
        	return new Timestamp(now);
    	}else if(dayNum == Calendar.TUESDAY){
    		return new Timestamp(now-millisOfDay*1);
    	}else if(dayNum == Calendar.WEDNESDAY){
    		return new Timestamp(now-millisOfDay*2);
    	}else if(dayNum == Calendar.THURSDAY){
    		return new Timestamp(now-millisOfDay*3);
    	}else if(dayNum == Calendar.FRIDAY){
    		return new Timestamp(now-millisOfDay*4);
    	}else{
    		return new Timestamp(now-millisOfDay*5);
    	}  
	}
	
	/**
	 * 
	 * @param now
	 * @return
	 */
	public static Timestamp nextWeek(Timestamp now){
		if(now==null) return null;
		
		return new Timestamp(now.getTime()+millisOfDay*7);
	}
	
	/**
	 * 0000-00-00
	 * @param now
	 * @return
	 */
	public static Timestamp nextMonth(Timestamp now){
		if(now==null) return null;
		
		String _now=now.toString().substring(0,19);
		int year=Integer.parseInt(_now.substring(0,4));
		int month=Integer.parseInt(_now.substring(5,7));
		int day=Integer.parseInt(_now.substring(8,10));
		
		month+=1;
		if(month>12){
			year++;
			month=1;
		}
		
		int allDaysOfNextMonth=getDaysOfMonth(year,month);
		while(day>allDaysOfNextMonth) day--;
		
		_now=year+"-"+(month<10?"0":"")+month+"-"+day+_now.substring(10);
		
		return Timestamp.valueOf(_now);
	}
	
	/**
	 * 0000-00-00
	 * @param now
	 * @return
	 */
	public static Timestamp nextSeason(Timestamp now){
		if(now==null) return null;
		
		for(int i=0;i<3;i++) now=nextMonth(now);
		
		return now;
	}
	
	/**
	 * 0000-00-00
	 * @param now
	 * @return
	 */
	public static Timestamp nextYear(Timestamp now){
		if(now==null) return null;
		
		for(int i=0;i<12;i++) now=nextMonth(now);
		
		return now;
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String[] getHowMuchTimeInDDHHMMSSFormat(long time){
		long t_day=(long)Math.floor(time/millisOfDay);
		String s_day=(t_day<10?"0":"")+t_day;
		
		time=time%millisOfDay;
		long t_hour=(long)Math.floor(time/millisOfHour);
		String s_hour=(t_hour<10?"0":"")+t_hour;
		
		time=time%millisOfHour;
		long t_minute=(long)Math.floor(time/millisOfMinute);
		if(t_minute<10) t_minute='0'+t_minute;
		String s_minute=(t_minute<10?"0":"")+t_minute;
		
		time=time%millisOfMinute;
		long t_second=(long)Math.floor(time/millisOfSecond);
		String s_second=(t_second<10?"0":"")+t_second;
		
		return new String[]{s_day,s_hour,s_minute,s_second};
	}

	/**
	 * 本地时间转化为UTC时间
	 * @param localTime 本地时间
	 * @return
	 */
	public static long localToUTC(String localTime){
		return localToUTC(localTime, null);
	}

	/**
	 * 本地时间转化为UTC时间
	 * @param localTime 本地时间
	 * @param timeZone 本地时间所属时区
	 * @return
	 */
	public static long localToUTC(String localTime, TimeZone timeZone){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"+(localTime.length()>19?".SSSSSS":""));
			sdf.setTimeZone(timeZone==null ? TimeZone.getDefault() : timeZone);
			return sdf.parse(localTime).getTime();
		}catch (Exception e){
			return 0;
		}
	}

	/**
	 * UTC时间转化为本地时间
	 * @param utcTime utc时间
	 * @param timeZone 本地时区
	 * @return
	 */
	public static String utcToLocal(String utcTime, TimeZone timeZone){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"+(utcTime.length()>19?".SSSSSS":""));
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			long utcMillis = sdf.parse(utcTime).getTime();

			return toString(utcMillis, timeZone==null ? TimeZone.getDefault() : timeZone);
		}catch (Exception e){
			return utcTime;
		}
	}

	/**
	 * UTC时间转化为本地时间
	 * @param utcMillis utc时间
	 * @return
	 */
	public static String utcToLocal(long utcMillis){
		return toString(utcMillis, null);
	}

	/**
	 * UTC时间转化为本地时间
	 * @param utcMillis utc时间
	 * @param timeZone 本地时区
	 * @return
	 */
	public static String utcToLocal(long utcMillis, TimeZone timeZone){
		return toString(utcMillis, timeZone==null ? TimeZone.getDefault() : timeZone);
	}

	/**
	 * 将utc时间ms显示为指定时区时间（YYYY:MM:DD HH:MM:SS.SSS）
	 * @param utcMillis
	 * @return
	 */
	public static String toString(long utcMillis, TimeZone timeZone){
		Calendar utcCalendar = Calendar.getInstance(timeZone);
		utcCalendar.setTimeInMillis(utcMillis);

		StringBuffer s=new StringBuffer();
		s.append(utcCalendar.get(Calendar.YEAR)).append("-");

		int month=utcCalendar.get(Calendar.MONTH) + 1;
		if(month<10) s.append("0");
		s.append(month).append("-");

		int date=utcCalendar.get(Calendar.DATE);
		if(date<10) s.append("0");
		s.append(date);

		s.append(" ");

		int hour=utcCalendar.get(Calendar.HOUR_OF_DAY);
		if(hour<10) s.append("0");
		s.append(hour).append(":");

		int minute=utcCalendar.get(Calendar.MINUTE);
		if(minute<10) s.append("0");
		s.append(minute).append(":");

		int second=utcCalendar.get(Calendar.SECOND);
		if(second<10) s.append("0");
		s.append(second);

		int millisecond=utcCalendar.get(Calendar.MILLISECOND);
		if(millisecond>0){
			s.append(".");
			if(millisecond<10) s.append("00");
			else if(millisecond<100) s.append("0");
			s.append(millisecond);
		}

		return s.toString();
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static String getTimeZone(HttpServletRequest request){
		String timeZone="UTC";
		if(request!=null){
			timeZone=request.getHeader("timeZone");
			if(JUtilString.isBlank(timeZone)) timeZone=JUtilBase.getCookie(request, "timeZone");
		}
		try{
			if(TimeZone.getTimeZone(timeZone) == null) timeZone=TimeZone.getDefault().getID();
		}catch (Exception e){
			timeZone = "UTC";
		}
		return timeZone;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static TimeZone getTimeZoneInstance(HttpServletRequest request){
		return TimeZone.getTimeZone(getTimeZone(request));
	}


	public static void main(String[] args){
		System.out.println(getDateMMDDYYYY(new Timestamp(System.currentTimeMillis())));
	}
}
