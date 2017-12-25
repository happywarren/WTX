package com.lt.util.utils.speedycloud;

import java.io.InputStream;

public class SpeedyCloudS3 extends AbstractS3API {

    public SpeedyCloudS3(String host,String access_key, String secret_key) {
        super(host,access_key, secret_key);
    }

    public String list(String bucket) {
        return this.request("GET", "/" + bucket);
    }

    public String createBucket(String bucket) {
        return this.request("PUT", "/" + bucket);
    }

    public String deleteBucket(String bucket) {
        return this.request("DELETE", "/" + bucket);
    }

    public String queryBucketAcl(String bucket) {
        return this.request("GET", String.format("/%s?acl", bucket));
    }

    public String queryObjectAcl(String bucket, String key) {
        return this.request("GET", String.format("/%s/%s?acl", bucket, key));
    }

    public String deleteKey(String bucket, String key) {
        return this.request("DELETE", String.format("/%s/%s", bucket, key));
    }

    public String deleteVersioningKey(String bucket, String key, String versionId) {
        return this.request("DELETE", String.format("/%s/%s?versionId=%s", bucket, key, versionId));
    }

    public String getKeyVersions(String bucket) {
        return this.request("GET", String.format("/%s?versions", bucket));
    }

    public String queryObjectInfo(String bucket,String key){
    	return this.request("GET", String.format("/%s/%s", bucket, key));
    }
    public String configureBucketVersioning(String bucket, String status) {
        String path = bucket + "?versioning";
        String versioningBody = String.format("<?xml version=\"1.0\"encoding=\"UTF-8\"?><VersioningConfiguration xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Status>%s</Status ></VersioningConfiguration>", status);
        return this.putString("PUT", path, versioningBody);
    }

    public String getBucketVersioningStatus(String bucket) {
        return this.request("GET", String.format("/%s?versioning", bucket));
    }

    public String putObjectFromFile(String bucket, String key, String path) {
        return this.putKeyFromFile("PUT", String.format("/%s/%s", bucket, key), path);
    }
    public String putObjectFromImgFile(String bucket, String key, String path) {
    	return this.putKeyFromImgFile("PUT", String.format("/%s/%s", bucket, key), path);
    }
    
    public String putObjectFromImgData(String bucket, String key, InputStream inStream) {
    	return this.putKeyFromImgData("PUT", String.format("/%s/%s", bucket, key), inStream);
    }
    
    public String putObjectFromInputStream(String bucket, String key, String path) {
    	return this.putKeyFromImgFile("PUT", String.format("/%s/%s", bucket, key), path);
    }

    public String putObjectFromString(String bucket, String key, String s) {
        return this.putKeyFromString("PUT", String.format("/%s/%s", bucket, key), s);
    }

    public String updateBucketAcl(String bucket, String acl) {
        return this.requestUpdate("PUT", String.format("/%s?acl", bucket), acl);
    }

    public String updateKeyAcl(String bucket, String key, String acl) {
        return this.requestUpdate("PUT", String.format("/%s/%s?acl", bucket, key), acl);
    }

    public String updateVersioningKeyAcl(String bucket, String key, String versionId, String acl) {
        return this.requestUpdate("PUT", String.format("/%s/%s?acl&versionId=%s", bucket, key, versionId), acl);
    }
}