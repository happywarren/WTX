package com.lt.util.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleUtils {
	/** 默认除法运算精度: 8 */
	public static final int DEFAULT_SCALE = 8;
	
	/** 除法运算精度: 8 */
	public static final int SCALE_FOUR = 8;

	public static double add(double a, double b, double... ds) {
		double result = add(a, b);
		for (double item : ds) {
			result = add(result, item);
		}
		return result;
	}

	/**
	 * 加法, 保留2位小数
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		return div(ba.add(bb).setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue(),1);
	}

	/**
	 * 减法
	 *@param a
	 *@param b
	 *@return
	 */
	public static double sub(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		return ba.subtract(bb).setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double sub(double a, double b, double... ds) {
		double result = sub(a, b);
		for (double item : ds) {
			result = sub(result, item);
		}
		return result;
	}

	/**
	 * 乘法
	 *@param a
	 *@param b
	 *@return
	 */
	public static double mul(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		return ba.multiply(bb).setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}


	
	/**
	 * 除法 a/b, 
	 *@param a
	 *@param b
	 *@return
	 */
	public static double div(double a, double b) {
		BigDecimal ba = new BigDecimal(Double.toString(a));
		BigDecimal bb = new BigDecimal(Double.toString(b));
		return ba.divide(bb,DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	
	/**
	 * 
	 *	
	 * 描述: 计算平仓价格所用, 向上取整
	 * @author  thomas.su
	 * @created 2015年6月24日 下午1:59:46
	 * @since   v1.0.0 
	 * @param value
	 * @return
	 * @return  Double
	 */
	public static Double format(Double value) {
		return ceil(value, 2);
	}

	/**
	 * 
	 *	
	 * 描述:3位精度，向上取整
	 *
	 * @author  jiupeng
	 * @created 2015年7月10日 下午3:15:21
	 * @since   v1.0.0 
	 * @param d
	 * @return
	 * @return  Double
	 */
	public static Double ceilThree(Double d) {
		return ceil(d, 3);
	}

	/**
	 * 
	 *	
	 * 描述:向上取整，指定小数点后精度
	 *
	 * @author  jiupeng
	 * @created 2015年7月10日 下午3:15:35
	 * @since   v1.0.0 
	 * @param d
	 * @param acc	小数点后保留精度
	 * @return
	 * @return  Double
	 */
	public static Double ceil(Double d, int acc) {
		if (d == null) {
			return 0.0d;
		}
		if (acc <= 0) {
			return d;
		}
		int su = 1;
		for (int i = 0; i < acc; i++) {
			su = su * 10;
		}
		Double result = div(Math.ceil(mul(d, su)), su);
		return result;

	}



	/**
	 *	
	 * 描述:四舍五入
	 *
	 * @author  C.C
	 * @created 2015年7月17日 上午11:36:24
	 * @since   v1.0.0 
	 * @param d
	 * @param acc 小数点后保留精度
	 * @return
	 * @return  Double
	 */
	public static Double rund(Double d) {
		if (d == null) {
			return 0.0d;
		}
		return new BigDecimal(String.valueOf(d)).setScale(DEFAULT_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 描述:四舍五入   thomas
	 * @param d
	 * @param acc
	 * @return
	 */
	public static String rundFormat(Double d,int acc){
		if (d == null) {
			return "0.0";
		}
		if (acc < 0) {
			return String.valueOf(d);
		}
		
		 NumberFormat nf = NumberFormat.getNumberInstance();
	     nf.setMaximumFractionDigits(acc);
	     return nf.format(d);
		
	}

	/**
	 * 
	 *	
	 * 描述:对double取整
	 *
	 * @author  zhengb@cainiu.com
	 * @created 2015年8月25日 下午4:45:14
	 * @updator 
	 * @updated 
	 * @version   0.0.1
	 * @param d
	 * @return
	 * @return  Integer
	 */
	public static Integer Doublefloor(Double d,boolean upTrueDownFalse){
		DecimalFormat df = new DecimalFormat("0"); 
		if(upTrueDownFalse){
			df.setRoundingMode(RoundingMode.HALF_UP);
		}else{
			df.setRoundingMode(RoundingMode.FLOOR);
		}
		return Integer.parseInt(df.format(d));
	}
	
//	/**
//	 * 
//	 * TODO 四舍五入, 默认保留2位小数
//	 * @author XieZhibing
//	 * @date 2017年1月5日 上午11:28:20
//	 * @param value
//	 * @return
//	 */
//	public static double scaleFormat(double value) {
//		return scaleFormat(value, DEFAULT_SCALE);
//	}
//	/**
//	 * 
//	 * TODO 四舍五入, 默认保留 scale 位小数
//	 * @author XieZhibing
//	 * @date 2017年1月5日 上午11:28:57
//	 * @param value
//	 * @param scale
//	 * @return
//	 */
	public static double scaleFormat(Double value, int scale) {
		value = value == null?0.0:value;
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 保留scale位小数，第scale位舍去
	 * @param value
	 * @param scale
	 * @return
	 */
	public static double scaleFormatEnd(Double value, int scale) {
		value = value == null?0.0:value;
		BigDecimal bigDecimal = new BigDecimal(value+ 0.000000001);
		return bigDecimal.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
	}

	public static String doubleFormat(Double value,int scale){

		String pattern = "#.";
		for(int i=0; i < scale ;i++){
			pattern = pattern+"0";
		}

		DecimalFormat    df   = new DecimalFormat(pattern);
		df.setRoundingMode(RoundingMode.DOWN);
		return df.format(value);
	}

	public static void main(String[] args) {
		System.out.println(DoubleUtils.scaleFormatEnd(DoubleUtils.div(10, 0.1389),2));
		System.out.println(DoubleUtils.scaleFormatEnd(71.99,2));
		System.out.println(DoubleUtils.scaleFormatEnd(43630.203, 2));
		System.out.println(DoubleUtils.scaleFormatEnd(43630.205, 2));
		System.out.println(DoubleUtils.scaleFormatEnd(43630.21, 2));
		System.out.println(DoubleUtils.scaleFormatEnd(9999999.35, 2));
		System.out.println(DoubleUtils.scaleFormatEnd(11154333.36, 2));
//		System.out.println(DoubleUtils.scaleFormat(43630.203, 2));
	}
}
