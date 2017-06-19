package com.waskj.base.core.util;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 * 
 * http://git.oschina.net/sphsyv/sypro
 * 
 * @author 孙宇
 *
 */
public class DateUtil {

	private static final String[] PARSEPATTERNS = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd HH:mm", "yyyy.MM.dd HH:mm", "yyyy-MM-dd HH", "yyyy/MM/dd HH", "yyyy.MM.dd HH", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd", "yyyyMMdd" };
	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取某个时间段内的月列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 *            : 1--返回“*年*月”; 2--返回“*月”
	 */
	public static List<String> getMonthStr(String startDate, String endDate, int type) {
		List<String> month = new ArrayList<String>();
		List<String> monthStr = new ArrayList<String>();
		Date s = stringToDate((startDate.substring(0, 7) + "-01"));
		Date e = stringToDate(endDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(s);
		while (cal.getTime().compareTo(e) <= 0) {
			String temp = ("" + (cal.get(Calendar.MONTH) + 1)).length() == 1 ? "0" + (cal.get(Calendar.MONTH) + 1) : "" + (cal.get(Calendar.MONTH) + 1);
			month.add(cal.get(Calendar.YEAR) + "年" + temp + "月");
			monthStr.add((cal.get(Calendar.MONTH) + 1) + "月");
			cal.add(Calendar.MONTH, 1);
		}
		if (type == 1) {
			return month;
		} else {
			return monthStr;
		}
	}

	/***
	 * 得到指定时间所在周的日期list
	 * 
	 * @param date
	 * @return
	 */
	public static List<String> getWeekListStr(String date) {
		List<String> weekDateList = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(date));
		int issumday = cal.get(Calendar.DAY_OF_WEEK);
		String begin = "";
		String end = "";
		if (issumday == 1) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
			begin = df.format(cal.getTime());
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			end = df.format(cal.getTime());
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
			begin = df.format(cal.getTime());
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			end = df.format(cal.getTime());
		}
		weekDateList = getDayStr(begin, end);
		return weekDateList;
	}

	/***
	 * 从周一到周天，顺次加入List
	 * 
	 * @param date
	 * @return
	 */
	public static List<String> getWeekStr() {
		List<String> weekDateList = new ArrayList<String>();
		weekDateList.add("星期一");
		weekDateList.add("星期二");
		weekDateList.add("星期三");
		weekDateList.add("星期四");
		weekDateList.add("星期五");
		weekDateList.add("星期六");
		weekDateList.add("星期日");
		return weekDateList;
	}

	/**
	 * 获取某个时间段内的日列表
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public static List<String> getDayStr(String startDate, String endDate) {
		List<String> day = new ArrayList<String>();
		try {
			FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");
			Date s = format.parse(startDate);
			Date e = format.parse(endDate);
			while (s.getTime() <= e.getTime()) {
				day.add(dateToString(s, "yyyy-MM-dd"));
				s = addDay(s, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 获取某个时间段内的日列表
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public static List<String> getMonthStr(String startDate, String endDate) {
		List<String> day = new ArrayList<String>();
		try {
			FastDateFormat format = FastDateFormat.getInstance("yyyy-MM");
			Date s = format.parse(startDate);
			Date e = format.parse(endDate);
			while (s.getTime() <= e.getTime()) {
				day.add(dateToString(s, "yyyy-MM"));
				s = addMonth(s, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}

	/**
	 * 获取一天内每两个小行的时间点，返回list
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public static List<Map<String, Object>> getTimeStr() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			int s = 0;
			int e = 24;
			boolean b = true;
			while(b)
			if (s % 2 == 0) {
				Map<String, Object> map = new HashMap<>();
				map.put("strat", s);
				map.put("end", s+2);
				s  = s + 2;
				list.add(map);
				if (s >= e) {
					b = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 将字符串转换成日期类型,自动匹配格式
	 * 
	 * @param date
	 */
	public static Date stringToDate(String date) {
		try {
			return DateUtils.parseDate(date, PARSEPATTERNS);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字符串格式转日期
	 * 
	 * @param date
	 * @param parsePatterns
	 */
	public static Date stringToDate(String date, String... parsePatterns) {
		try {
			return DateUtils.parseDate(date, parsePatterns);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @param pattern
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			FastDateFormat format = FastDateFormat.getInstance(pattern);
			return format.format(date);
		}
		return "";
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 */
	public static String dateToString(Date date) {
		return dateToString(date, PATTERN);
	}

	/**
	 * 增加n天后的日期
	 * 
	 * @param date
	 * @param n
	 */
	public static Date addDay(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, n);// 增加n天
		return calendar.getTime();
	}

	/**
	 * 增加n个月后的日期
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMonth(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, n);// 增加n个月
		return calendar.getTime();
	}

	/**
	 * 增加n个年后的日期
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addYear(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, n);// 增加n个年
		return calendar.getTime();
	}
	
	
	
	/**
	 * 增加n分钟后的日期
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMinute(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, n);// 增加n分钟
		return calendar.getTime();
	}

	/**
	 * 增加n秒后的日期
	 * 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addSecond(Date date, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, n);// 增加n秒
		return calendar.getTime();
	}

	/**
	 * 获取当前月第一天
	 * 
	 * @return
	 */
	public static Date firstDayOfMonth() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return c.getTime();
	}

	/**
	 * 获取某月的第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date firstDayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	public static Date lastDayOfMonth() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 获取某月的最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date lastDayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date firstDayOfYear(int year) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		return c.getTime();
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date lastDayOfYear(int year) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.roll(Calendar.DAY_OF_YEAR, -1);
		return c.getTime();
	}

	/**
	 * 取两个日期相差天数
	 * 
	 * @param early
	 * @param late
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date early, Date late) {
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// 得到两个日期相差的天数
		long days = (caled.getTime().getTime() - calst.getTime().getTime()) / (1000 * 3600 * 24);
		return Math.abs((int) days);
	}

	/**
	 * 取小时差
	 * 
	 * @param type
	 *            0 返回小时 1 返回分钟
	 * @return
	 */
	public static double getBetweenTime(int type, Date d1, Date d2) {
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d2);
		double btw = Math.abs(c.getTime().getTime() - c1.getTime().getTime());
		return type == 0 ? btw / 1000 / 60 / 60 : btw / 1000 / 60;
	}

	/**
	 * 小时转时分秒 return type=0 xx小时xx分xx秒 type=1 xx分xx秒
	 */
	public static String getFormatTime(int type, double d) {
		String str;
		if (type == 0) {
			long h = (long) d;
			double hm = Double.parseDouble(("0." + String.valueOf(d).split("\\.")[1])) * 60;
			int m = (int) hm;
			int s = (int) (Double.parseDouble(("0." + String.valueOf(hm).split("\\.")[1])) * 60);
			str = h + "小时" + m + "分钟" + s + "秒";
		} else {
			long m = (long) d;
			int s = (int) (Double.parseDouble(("0." + String.valueOf(d).split("\\.")[1])) * 60);
			str = m + "分钟" + s + "秒";
		}

		return str;
	}

	public static Date[] getSeasonDate(Date date) {
		Date[] season = new Date[3];

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int nSeason = getSeason(date);
		if (nSeason == 1) {// 第一季度
			c.set(Calendar.MONTH, Calendar.JANUARY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.FEBRUARY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MARCH);
			season[2] = c.getTime();
		} else if (nSeason == 2) {// 第二季度
			c.set(Calendar.MONTH, Calendar.APRIL);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MAY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.JUNE);
			season[2] = c.getTime();
		} else if (nSeason == 3) {// 第三季度
			c.set(Calendar.MONTH, Calendar.JULY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.AUGUST);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			season[2] = c.getTime();
		} else if (nSeason == 4) {// 第四季度
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.NOVEMBER);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.DECEMBER);
			season[2] = c.getTime();
		}
		return season;
	}

	/**
	 * 取第几季度
	 * 
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {

		int season = 0;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

}
