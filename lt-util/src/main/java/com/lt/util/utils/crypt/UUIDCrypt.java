package com.lt.util.utils.crypt;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UUIDCrypt {

	public static  String encryUUID(String s) {
		s = getRandom(5) + s + getRandom(5);
		char[] ch = s.toCharArray();
		String r = "";
		for (char c : ch) {
			switch (c) {
			case '0':
				r += "A";
				break;
			case '2':
				r += "B";
				break;
			case '4':
				r += "C";
				break;
			case '3':
				r += "D";
				break;
			case '1':
				r += "E";
				break;
			case '5':
				r += "F";
				break;
			case '8':
				r += "G";
				break;
			case '7':
				r += "H";
				break;
			case '6':
				r += "I";
				break;
			case '9':
				r += "J";
				break;
			}
		}
		return r;
	}

	public static String decryUUID(String s) {
		s = s.substring(5);
		s = s.substring(0, s.length() - 5);
		char[] ch = s.toCharArray();
		String r = "";
		for (char c : ch) {
			switch (c) {
			case 'A':
				r += "0";
				break;
			case 'B':
				r += "2";
				break;
			case 'C':
				r += "4";
				break;
			case 'D':
				r += "3";
				break;
			case 'E':
				r += "1";
				break;
			case 'F':
				r += "5";
				break;
			case 'G':
				r += "8";
				break;
			case 'H':
				r += "7";
				break;
			case 'I':
				r += "6";
				break;
			case 'J':
				r += "9";
				break;
			}

		}
		return r;
	}
	
	private static String getRandom(int point){
		String result = String.valueOf(Math.random());
	    String f = "#####0";
	    if(point > 0){
	    	f = "";
	    	for(int i=0;i<point-1;i++){
	    		f+="#";
	    	}
	    	f+="0";
	    }
	    BigDecimal rand = new BigDecimal(result);
	    BigDecimal one = new BigDecimal(1);
	    double d =  rand.divide(one,point,BigDecimal.ROUND_HALF_UP).doubleValue();
	    DecimalFormat df = new   DecimalFormat(f);
	    String t = df.format(d*Math.pow(10,point)); 
	    if(t.length() < point){
	    	int l = point - t.length();
	    	for(int i=0; i < l; i++){
	    		t += "0";
	    	}
	    }
	    return t;
	}

}
