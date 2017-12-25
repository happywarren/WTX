package com.lt.util.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;

/**
 * 该示例代码展示了如果在OSS中创建和删除一个Bucket，以及如何上传和下载一个文件。
 * 
 * 该示例代码的执行过程是：
 * 1. 创建一个Bucket（如果已经存在，则忽略错误码）；
 * 2. 上传一个文件到OSS；
 * 3. 下载这个文件到本地；
 * 4. 清理测试资源：删除Bucket及其中的所有Objects。
 * 
 * 尝试运行这段示例代码时需要注意：
 * 1. 为了展示在删除Bucket时除了需要删除其中的Objects,
 *    示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！
 * 2. 请使用您的API授权密钥填充ACCESS_ID和ACCESS_KEY常量；
 * 3. 需要准确上传用的测试文件，并修改常量uploadFilePath为测试文件的路径；
 *    修改常量downloadFilePath为下载文件的路径。
 * 4. 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
 *
 */
public class OSSObjectSample{
	
	public static String access_id = "QVnXfoPDZONogO78";
	public static String access_key = "OKvsD3ihBKj0wSAdXxkmHruDtRGPZM";

	public static String bucketName = "lt7";
	public static String url = ".oss-cn-hangzhou.aliyuncs.com/";
	public static String pkg = "photo/";
	public static String auditpkg = "audit/";
	public static OSSClient client;
	
    public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(dot,filename.length()); 
            } 
        } 
        return filename; 
    }
    
    // 创建Bucket.
    public static void ensureBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException{

        try {
            // 创建bucket
            client.createBucket(bucketName);
        } catch (ServiceException e) {
            if (!OSSErrorCode.BUCKET_ALREADY_EXISTS.equals(e.getErrorCode())) {
                // 如果Bucket已经存在，则忽略
                throw e;
            }
        }
    }

    // 删除一个Bucket和其中的Objects 
    public static void deleteBucket(OSSClient client, String bucketName)
            throws OSSException, ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing
                .getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    // 把Bucket设置为所有人可读
    public static void setBucketPublicReadable(OSSClient client, String bucketName)
            throws OSSException, ClientException {
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    // 上传文件
    public static void uploadFile(OSSClient client, String bucketName, String key,long l,InputStream input)
            throws OSSException, ClientException, FileNotFoundException {

        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(l);
        // 可以在metadata中标记文件类型
        objectMeta.setContentType("image/jpeg");
        client.putObject(bucketName, key, input, objectMeta);
    }

    // 下载文件
    public static void downloadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException {
        client.getObject(new GetObjectRequest(bucketName, key),
                new File(filename));
    }
    	    
    public OSSClient init(){
    	System.out.println("使用默认的OSS服务器地址创建OSSClient对象");
        // 使用默认的OSS服务器地址创建OSSClient对象。
        client = new OSSClient(OSSObjectSample.access_id, OSSObjectSample.access_key);
        OSSObjectSample.ensureBucket(client, OSSObjectSample.bucketName);
    	OSSObjectSample.setBucketPublicReadable(client, OSSObjectSample.bucketName);
		return client;
    }

	public String getAccess_id() {
		return access_id;
	}

	public void setAccess_id(String access_id) {
		OSSObjectSample.access_id = access_id;
	}

	public String getAccess_key() {
		return access_key;
	}

	public void setAccess_key(String access_key) {
		OSSObjectSample.access_key = access_key;
	}

	public String getAuditpkg() {
		return auditpkg;
	}

	public void setAuditpkg(String auditpkg) {
		OSSObjectSample.auditpkg = auditpkg;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		OSSObjectSample.bucketName = bucketName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		OSSObjectSample.url = url;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		OSSObjectSample.pkg = pkg;
	}

	public OSSClient getClient() {
		return client;
	}

	public void setClient(OSSClient client) {
		OSSObjectSample.client = client;
	}

}
