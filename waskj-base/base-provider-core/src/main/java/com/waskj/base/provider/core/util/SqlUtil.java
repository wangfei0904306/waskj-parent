package com.waskj.base.provider.core.util;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql工具类
 * 
 * @author 孙宇
 *
 */
public class SqlUtil {

	/**
	 * 一般用于sql语句获取in部分(两边加单引号)
	 * 
	 * @param ids
	 * @return
	 */
	public static String inForVarchar(String[] ids) {
		String _str = "";
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				_str += ",";
			}
			_str += "'" + ids[i] + "'";
		}
		return _str;
	}

	/**
	 * 格式化sql
	 * 
	 * @param sql
	 * @return
	 */
	public static String format(String sql) {
		return SQLUtils.format(sql, JdbcUtils.MYSQL);
	}

	/***
	 * 1、把sql串调用SqlUtil.format(sql); 2、SqlUtil.format(sql)取得头几行列的串 3、把串中有as的取as后面的别名 4、没有as的字段分是否有mysql函数再取出字段
	 * 
	 * @param sql
	 * @return
	 */
	public static List<String> getAliasList(String sql) {
		String aliasSql = sql;
		String formatSql = SqlUtil.format(aliasSql);
		int end = formatSql.indexOf("\nFROM");
		aliasSql = formatSql.substring(0, end) + ",";
		aliasSql = aliasSql.replaceAll("\n", "");
		aliasSql = aliasSql.replace("SELECT", "").replaceAll("AS", "as");
		String find = "(AS\\s.*?,)|(AS\\t.*?,)";
		Pattern pattern = Pattern.compile(find, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(aliasSql);
		List<String> aliasList = new ArrayList<String>();
		while (matcher.find()) {
			aliasList.add(matcher.group().replaceAll("\\t", " ").replaceAll("(?i)AS\\s", "").replaceAll(",", "").trim());
		}
		int countTimes = SqlUtil.counter(aliasSql, "as");
		// 把mysql的函数写string[]，用于比对.
		String[] sqlFun = { "ROUND", "round", "SUM", "sum", "COUNT", "count", "IFNULL", "ifnull", "AVG", "avg", "DATE_FORMAT", "date_format", "timediff" ,"TIMESTAMPDIFF", "group_concat"};
		String regEx = "[^0-9]";
		Pattern isNum = Pattern.compile(regEx);
		boolean addCol = true;
		if (aliasList.size() < countTimes) {
			for (String col : aliasSql.split(",")) {
				if (col.indexOf("as") <= -1) {
					for (String fun : sqlFun) {
						if (col.indexOf(fun) == 1) {
							addCol = false;
							break;
						} else if (col.indexOf(fun) < -1) {
							if (!NumberUtils.isNumber(isNum.matcher(col).replaceAll("").trim())) {
								aliasList.add(col);
								break;
							} else {
								int begin = col.indexOf(".") + 1;
								aliasList.add(col.substring(begin, col.length()).trim());
								break;
							}
						}
					}
					if (addCol) {
						if (!NumberUtils.isNumber(isNum.matcher(col).replaceAll("").trim())) {
							int begin = col.indexOf(".") + 1;
							aliasList.add(col.substring(begin, col.length()).trim());
						}
					}
				}
			}
		}
		return aliasList;
	}

	public static int counter(String text, String sub) {
		int count = 0, start = 0;
		while ((start = text.indexOf(sub, start)) >= 0) {
			start += sub.length();
			count++;
		}
		return count;
	}
}
