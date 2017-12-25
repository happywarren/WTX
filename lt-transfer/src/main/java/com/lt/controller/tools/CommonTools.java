package com.lt.controller.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.lt.model.fund.FundFlow;
import com.lt.util.utils.DateTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


/**   
* 项目名称：lt-transfer   
* 类名称：CommonTools   
* 类描述：公共工具类   
* 创建人：yuanxin   
* 创建时间：2017年5月16日 上午9:27:25      
*/
public class CommonTools {
	//生成文件路径
    private static String path = "/data/sql/";
    
    //文件路径+名称
    private static String filenameTemp;
    
	/**
	 * 根据对象拼接sql 
	 * @param <E>
	 * @param tableName 表名
	 * @param object 对象实体
	 * @return    
	 * @return:       String    
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 上午9:44:08
	 */
	public static <E> String createInsertSql(String tableName,List<E> objects) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		if(CollectionUtils.isEmpty(objects)){
			return "";
		}else{
			Object object = objects.get(0);
			
			StringBuilder sql = new StringBuilder("");
			StringBuilder title = new StringBuilder("insert into " + tableName);
			Class keys = object.getClass();
			Field[] fields = keys.getDeclaredFields();
			StringBuilder columns = new StringBuilder(" (");
			
			for(Field filed : fields){
				columns.append(","+ filed.getName());
			}
			
			title.append(columns.toString().replaceFirst(",", "")).append(") ");
			title.append(" values ");
			
//			StringBuilder values = new StringBuilder();
			
			for(Object objectSimple : objects){
				sql.append(title + createSqlValues(fields, objectSimple, keys) + ";\r\n");
//				values.append("," + createSqlValues(fields, objectSimple, keys)) ;
			}
			
//			title.append(values.toString().replaceFirst(",", ""));
			
			return sql.toString() ;
		}
		
	}
	
	/**
	 * 生成批量插入的单条数据
	 * @param fields
	 * @param object    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 上午11:33:25
	 */
	public static String createSqlValues(Field[] fields , Object object,Class keys){

		StringBuilder values = new StringBuilder("(");
		
		for(Field filed : fields){
			String setValue = filed.getName().substring(0, 1).toUpperCase() + filed.getName().substring(1);
			try {
				Method method = keys.getMethod("get" + setValue);
				// 获取字段类型
				String type = filed.getGenericType().toString(); 
				
				if(method.invoke(object) == null){
					values.append("," + "null");
				}else{
					if (type.equals("class java.lang.Double") ) {
						Double value = Double.parseDouble(method.invoke(object).toString());    //调用getter方法获取属性值
						values.append(",'" + value + "' ");
					}else if(type.equals("class java.util.Date")){
						Date value = (Date)method.invoke(object);    //调用getter方法获取属性值
						values.append(",'" + DateTools.formatDate(value, DateTools.FORMAT_LONG) + "' ");
					}else if(type.equals("class java.lang.Integer")){
						Integer value = (Integer)method.invoke(object);    //调用getter方法获取属性值
						values.append(",'" + value + "' ");
					}else if(type.equals("class java.lang.Long")){
						Long value = (Long)method.invoke(object);    //调用getter方法获取属性值
						values.append(",'" + value + "' ");
					}else{
						String value = method.invoke(object).toString();
						values.append(",'" + value + "' ");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return values.toString().replaceFirst(",", "") + ")" ;
	}
	
	
    
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName,String filecontent){
    	File file = new File(path);
    	if(!file.exists()){
    		file.mkdirs();
    	}
    	
        Boolean bool = false;
        filenameTemp = path+fileName+System.currentTimeMillis()+".sql";//文件路径+名称+文件类型
        file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
    }
	
	
	 /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        OutputStreamWriter  pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            
            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);
            
            fos = new FileOutputStream(file);
            pw = new OutputStreamWriter(fos, "UTF-8");
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    
    /**
     * 根据名称返回银行编码
     * @param bankName
     * @return    
     * @return:       String    
     * @throws 
     * @author        yuanxin
     * @Date          2017年5月16日 下午7:38:00
     */
    public static String getBankCode(String bankName){
    	
    	if(bankName == null ){
    		return "";
    	}
    	
    	if(bankName.contains("广发")){
    		return "0001";
    	}else if(bankName.contains("浦发")){
    		return "0002";
    	}else if(bankName.contains("兴业")){
    		return "0003";
    	}else if(bankName.contains("招商")){
    		return "0004";
    	}else if(bankName.contains("农业")){
    		return "0006";
    	}else if(bankName.contains("建设")){
    		return "0007";
    	}else if(bankName.contains("光大")){
    		return "0008";
    	}else if(bankName.contains("平安")){
    		return "0009";
    	}else if(bankName.contains("民生")){
    		return "00010";
    	}else if(bankName.contains("华夏")){
    		return "0011";
    	}else if(bankName.contains("工商")){
    		return "0012";
    	}else if(bankName.contains("中信")){
    		return "0013";
    	}else if(bankName.contains("交通")){
    		return "0014";
    	}else if(bankName.contains("邮政")){
    		return "0015";
    	}else if(bankName.contains("上海")){
    		return "0016";
    	}
    	
    	return "0005";
    }
    
    public static void main(String[] args) {
    	
    	
    	FundFlow flow = new FundFlow("1002325145755412", 1, "32510", "5844", "954", 1.2, 1.2, "225698554", "搞笑了", new Date(), new Date());
    	FundFlow flow2 = new FundFlow("asdfklajsldfkas", 1, "45656", "5844", "954", 1.2, 1.7, "2255988554", "吓死哥了", new Date(), new Date());
		
    	List<FundFlow> list = new ArrayList<FundFlow>();
    	
    	list.add(flow);
    	list.add(flow2);
    	list.add(flow);
    	String q1 = "" ;
    	
		try {
			q1 = createInsertSql("fund_flow_cash", list);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	createFile("user_sql", q1);
    	
	}
}
