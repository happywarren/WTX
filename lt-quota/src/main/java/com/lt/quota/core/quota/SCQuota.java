package com.lt.quota.core.quota;


import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.http.HttpData;
import com.lt.quota.core.http.ISocketListener;
import com.lt.quota.core.http.MitakeHttpParams;
import com.lt.quota.core.http.MitakeSocket;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.tradeclient.tcp.client.bean.QuotaServer;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.StringTools;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SCQuota implements Runnable{

    private static final int TIMEOUT = 100;
    private ProductTimeCache productTimeCache;

    private static Logger logger = LoggerFactory.getLogger(QuotaServer.class);
    private static String SC = "http://hejqh.zo26.cn/main/getFuturesOnlineData.json?commoindity=SC&device=1&type=2"; //获取SC行情
    private static String DAX = "http://hejqh.zo26.cn/futuresquote/getFuturesQuote.json?commodityNo=DAX&type=real"; //获取dax行情
    private static long preTime =0;
    private static String scHost[] = {"","http://118.178.213.103","80",""};
    private static MitakeSocket mitakeSocket = null;


    @Override
    public void run() {
        JSONObject quota  = HttpTools.doGet(SC,null);
        if(quota != null){
           Integer code =  quota.getInteger("code");
           Boolean success = quota.getBoolean("success");
           if(code == 100 && success){
              // String data = quota.
               String data = quota.getString("data");
               String [] xdata =  data.split(",");
               if(xdata.length == 16){
                   QuotaBean quotaBean = new QuotaBean();
                   quotaBean.setProductName(xdata[0]);
                   String time = DateTools.formatDate(new Date(Long.valueOf(xdata[1])),DateTools.FORMAT_FULL);
                   quotaBean.setTimeStamp(time);
                   quotaBean.setOpenPrice(xdata[2]);
                   quotaBean.setPreSettlePrice(xdata[3]);
                   quotaBean.setChangeValue(xdata[4]);
                   quotaBean.setChangeRate(xdata[5]);
                   quotaBean.setHighPrice(xdata[6]);
                   quotaBean.setLowPrice(xdata[7]);
                   quotaBean.setPositionQty(xdata[8]);
                   quotaBean.setLastPrice(xdata[9]);
                   quotaBean.setBidPrice1(xdata[10]);
                   quotaBean.setAskPrice1(xdata[11]);
                   quotaBean.setBidQty1(xdata[12]);
                   quotaBean.setAskQty1(xdata[13]);
                   long curtime = Long.valueOf(xdata[1]);
                   if(curtime > preTime){
                       preTime = curtime;
                       CleanInstance.getInstance().setMarketDataQueue(quotaBean);
                       System.out.println(quotaBean.toString());
                   }

               }
           }
        }

    }


    @PostConstruct
    public  void start(){
        //ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();
       // executors.scheduleAtFixedRate(this,0,30,TimeUnit.MILLISECONDS);
    }

    public static JSONObject doGet(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        if(StringTools.isBlankOrEmpty(url)){
            return null;
        }


        JSONObject jsonData = null;
        try {

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT).build();
            URI uri = null;
            URIBuilder uriBuilder = new URIBuilder();
            if (params != null && params.size() > 0) {
                Set<String> keys = params.keySet();
                Iterator<String> it = keys.iterator();
                for (; it.hasNext();) {
                    String key = it.next();
                    String val = params.get(key);
                    uriBuilder.setParameter(key, val);
                }
            }
            uri = uriBuilder.build();

            HttpGet httpGet = new HttpGet(url + uri);
            httpGet.setConfig(requestConfig);

            jsonData = httpClient.execute(httpGet, createResponseHandler());
        } catch (Exception e1) {
            e1.printStackTrace();
            return jsonData;
        } finally {
        }
        return jsonData;
    }

    private static ResponseHandler<JSONObject> createResponseHandler() {
        ResponseHandler<JSONObject> jsonHandler = new ResponseHandler<JSONObject>() {

            public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                // StatusLine statusCode = response.getStatusLine();
                // if (statusCode.getStatusCode() >= 300) {
                // throw new HttpResponseException(statusCode.getStatusCode(),
                // statusCode.getReasonPhrase());
                // }
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                ContentType contentType = ContentType.getOrDefault(httpEntity);
                Charset charset = contentType.getCharset();
                if (charset == null) {
                    charset = Consts.UTF_8;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
                        "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                for (; (line = reader.readLine()) != null;) {
                    buffer.append(line + "\n");
                }
                // System.out.println(buffer.toString());
                return JSONObject.parseObject(buffer.toString());
            }
        };
        return jsonHandler;
    }
}
