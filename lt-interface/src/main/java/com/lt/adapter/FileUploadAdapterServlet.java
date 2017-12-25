package com.lt.adapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.file.FileLoader;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.OSSObjectSample;
import com.lt.util.utils.speedycloud.SpeedyCloudUtils;

import javolution.util.FastMap;

/**   
* 项目名称：lt-interface   
* 类名称：FileUploadAdapterServlet   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月1日 上午11:31:35      
*/
public class FileUploadAdapterServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        long time = new Date().getTime();
        PrintWriter out = response.getWriter();
        String jsonStr = null ;
        try {
        	if(request.getContentLength()>8*1024*1024){
        		logger.info("=====图片太大,上传失败");
        		throw new LTException(LTResponseCode.F000001);
        	}
        	String contentType=request.getContentType();
            if(contentType!=null && contentType.startsWith("multipart/form-data")){
            	
            	/** 本地生成临时文件 beg*/
            	DiskFileItemFactory factory = new DiskFileItemFactory();  
                // 获取临时文件路径  
                String savePath = "src/main/webapp/img/" ;
                if(!new File(savePath).exists()){  
                    new File(savePath).mkdirs();  
                }  
                factory.setRepository(new File(savePath));  
                factory.setSizeThreshold(4096);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax(8 * 1024 * 1024);
				List<FileItem> list = upload.parseRequest(request);
				
				/** 本地生成临时文件 end*/
				
				Map<String,Object> photoPath = new HashMap<String,Object>();
				Map<String, Object> result = FastMap.newInstance();
				if(CollectionUtils.isNotEmpty(list)){
					for(FileItem fileItem : list){
						if(fileItem !=null && fileItem.getContentType()!=null && fileItem.getContentType().contains("image")){
							InputStream input = fileItem.getInputStream();
							String ex = fileItem.getName().substring(fileItem.getName().lastIndexOf("."), fileItem.getName().length());
							if (null != fileItem && fileItem.getSize() > 0) {
					            System.out.println("正在上传...");
					            String key = DateTools.parseToFormatDateString(new Date(),DateTools.TIME_STAMP)+ex;
					            //OSSObjectSample.uploadFile(OSSObjectSample.client, OSSObjectSample.bucketName, key, fileItem.getSize(),input);
						        ///String path = "http://"+OSSObjectSample.bucketName+OSSObjectSample.url+key;
						        //photoPath.put(fileItem.getFieldName(), path);
					            logger.info("key:{}"+key);
						        result = SpeedyCloudUtils.getSpeedyCloudUtils().uploadImage(key, input);
						        if("SUCCESS".equals((String)result.get("resultId"))){
						        	photoPath.put(fileItem.getFieldName(), (String)result.get("filePath"));
						        }
							}
							
							fileItem.delete();
						}
					}
				}

            	MainOperator mainOperator = new MainOperator();
            	jsonStr = mainOperator.operator(request, photoPath);
            }else{
            	logger.error("【获取不到文件流】");
            	jsonStr = LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
            }
        } catch (LTException e) {
            jsonStr = LTResponseCode.getCode(e.getMessage()).toJsonString();
        } catch (Exception e) {
        	e.printStackTrace();
            jsonStr = LTResponseCode.getCode(LTResponseCode.ER400).toJsonString();
        }
        long subTime = new Date().getTime() - time;
        logger.error("执行时间（" + (subTime) + "）毫秒");
        out.print(jsonStr);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
    public static synchronized String getFilePath(String urlStr) throws Exception{
    	try {
			InputStream inputStream = FileLoader.getFileLoader().loadResource(urlStr);
			String fileName = System.currentTimeMillis()+".jpg";
    		Map<String, Object> result = SpeedyCloudUtils.getSpeedyCloudUtils().uploadImage(fileName, inputStream);
    		if("SUCCESS".equals((String)result.get("resultId"))){
    			return (String) result.get("filePath");
    		}
		}catch(Exception e){
			e.printStackTrace();
		}
    	return  null;
    }
    
    public static synchronized String  downLoadFromUrl(String urlStr) throws IOException{  
    	String savePath = "/data/server/lt-interface/img" ;
    	//String fileName = System.currentTimeMillis()+".jpg";
    	String fileName = UUID.randomUUID()+".jpg";
    	//logger.info("***********下载图片"+fileName);
        URL url = new URL(urlStr);    
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
                //设置超时间为3秒  
        conn.setConnectTimeout(3*1000);  
        //防止屏蔽程序抓取而返回403错误  
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
  
        //得到输入流  
        InputStream inputStream = conn.getInputStream();    
        //获取自己数组  
        byte[] getData = readInputStream(inputStream);      
  
        //文件保存位置  
        File saveDir = new File(savePath);  
        if(!saveDir.exists()){  
            saveDir.mkdir();  
        }  
        File file = new File(savePath+File.separator+fileName);      
        FileOutputStream fos = new FileOutputStream(file);       
        fos.write(getData);     
        FileInputStream fileInputStream = new FileInputStream(file);
		String ex = OSSObjectSample.getFileNameNoEx(fileName);
		String key = DateTools.parseToFormatDateString(new Date(),DateTools.TIME_STAMP)+ex;
        OSSObjectSample.uploadFile(OSSObjectSample.client, OSSObjectSample.bucketName, key, file.length(),fileInputStream);
		 String filePath = "http://"+OSSObjectSample.bucketName+OSSObjectSample.url+key;
		if(file.exists()){
			//file.deleteOnExit();
		}
        if(fos!=null){  
            fos.close();     
        }  
        if(inputStream!=null){  
            inputStream.close();  
        }  
        if(fileInputStream!=null){
        	fileInputStream.close();
        }
        if(conn!=null){
        	conn.disconnect();
        }
        return filePath;
    }  
  
  
  
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {     
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }
    
    public static void main(String[] args) {
		try {
			new FileUploadAdapterServlet().downLoadFromUrl("https://idsafe-auth.udcredit.com/front/4.0/api/file_download/platform/web?name=i_20170620164702643992868.jpg&param=ENCRYPT_8664FD3F90B782E36068ADFD54112AFF64E16816F9A64C44FB1E2CFA659AD9");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
