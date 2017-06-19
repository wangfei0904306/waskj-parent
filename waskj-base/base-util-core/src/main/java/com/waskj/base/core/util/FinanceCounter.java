package com.waskj.base.core.util;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 利息计算器
 * 
 * @author G.Sun
 *
 */
public class FinanceCounter {

	/**
	 * 到期还本息 repayment of principal and interest at maturity 不计算第一天买入时间
	 * 
	 * @param investTime
	 *            投资起始时间
	 * @param endTime
	 *            投资截止时间
	 * @param counterTime
	 *            要计算利息的起始时间
	 * @param rate
	 *            年化利率
	 * @param money
	 *            金额
	 * @return 四舍五入，保留小数点2位
	 * @throws ParseException
	 */
	public static BigDecimal repayOfPriAndInte(Date investTime, Date endTime, Date counterTime, BigDecimal rate,
			BigDecimal money) throws ParseException {
		int n = 0;
		if (counterTime.getTime() > investTime.getTime()) {
			n = UtilDateTime.getBetweenDays(counterTime, endTime, "yyyy-MM-dd") - 1; // 不计算第一天
		} else {
			n = UtilDateTime.getBetweenDays(investTime, endTime, "yyyy-MM-dd");
		}
		System.out.println(n);
		return money.multiply(rate).multiply(new BigDecimal(n)).divide(new BigDecimal(365), 2,
				BigDecimal.ROUND_HALF_UP);
	}

	public static void main(String[] args) {
		try {
			BigDecimal result = repayOfPriAndInte(DateUtils.parseDate("2016-07-05 00:00:00", "yyyy-MM-dd HH:mm:ss"),
					DateUtils.parseDate("2016-08-05 00:00:00", "yyyy-MM-dd HH:mm:ss"),
					DateUtils.parseDate("2016-08-03 15:00:00", "yyyy-MM-dd HH:mm:ss"), new BigDecimal(0.1),
					new BigDecimal(10000));
			System.out.println(result.doubleValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
