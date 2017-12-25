package com.lt.util.utils;

import java.util.Random;
import java.util.Set;

import javolution.util.FastSet;

/**
 * 随机码创建
 * @author guodw
 *
 */
public class CodeCreate {  
    /** 
     * 获得code 
     * @param len code长度 
     * @return 
     */  
    public static String createCodeLen(int len){  
        int random = createRandomInt();  
        return createCode(random, len);  
    }  
      
    private static String createCode(int random,int len){  
        Random rd = new Random(random);  
        final int  maxNum = 62;  
        StringBuffer sb = new StringBuffer();  
        int rdGet;//取得随机数  
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
                'x', 'y', 'z', 'A','B','C','D','E','F','G','H','I','J','K',  
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
                'X', 'Y' ,'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
          
        int count=0;  
        while(count < len){  
            rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1  
            if (rdGet >= 0 && rdGet < str.length) {  
                sb.append(str[rdGet]);  
                count ++;  
            }  
        }  
        return sb.toString();  
    }  
    
    /**
     * 创建
     * @param random
     * @param len
     * @return
     */
    private static String createCodeForNickName(int random,int len){  
    	Random rd = new Random(random);  
    	final int  maxNum = 62;  
    	StringBuffer sb = new StringBuffer();  
    	sb.append("用户");
    	int rdGet;//取得随机数  
    	char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
    	
    	int count=0;  
    	while(count < len){  
    		rdGet = Math.abs(rd.nextInt(maxNum));//生成的数最大为62-1  
    		if (rdGet >= 0 && rdGet < str.length) {  
    			sb.append(str[rdGet]);  
    			count ++;  
    		}  
    	}  
    	return sb.toString();  
    }  
      
    private static int createRandomInt(){  
        //得到0.0到1.0之间的数字，并扩大100000倍  
        double temp = Math.random()*100000;  
        //如果数据等于100000，则减少1  
        if(temp>=100000){  
            temp = 99999;  
        }  
        int tempint = (int)Math.ceil(temp);  
        return tempint;  
    }  
    /**
     * 生成随机昵称
     * @param len
     * @return
     */
    public static String createNickName(int len){
    	int random = createRandomInt();
    	return createCodeForNickName(random,len);
    }
    public static String createNickName(int source,int len){
    	if(source<500){
    		source = source+500;
    	}
       	int tmp = (int) (source*len+Math.random()*len);
        	return "用户"+tmp;
    }
    
    public static void main(String[] args){  
    	//System.out.println(createNickName(26236, 2));
    	Set<String> set = FastSet.newInstance();
    	
    	for (int i =1; i <20000; i++) {
    		String string = createNickName(i,2);
    		if(set.contains(string)){
    			System.out.println("----"+string);
    		}
    		set.add(string);
    		System.out.println(string);
		}
    	System.out.println(set.size());
    }  
}  