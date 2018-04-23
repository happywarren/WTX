package com.lt.quota.core.quota.third;


import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.otod.bean.quote.snapshot.Snapshot;
import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThirdSanpShot {

    public static Map<String,String> productListOuter = new ConcurrentHashMap<String,String>();
    public static  Map<String,String> productListInner = new ConcurrentHashMap<String,String>();

    private static SimpleDateFormat format = new SimpleDateFormat("yy");
    private static Date date = new Date();

    public int time; //时间
    public String symbol; //市场前缀
    public String cnName; //名称
    public float preSettlePrice;  //昨结算价
    public int fVol;   //现量，最近一笔成交量
    public int fOpenInt;   //持仓(未平仓合约)
    public float settlePrice;  //当日结算价
    public float preClosePrice; //昨收价
    public float openPrice; //今开价
    public float highPrice; //最高价
    public float lowPrice; //最低价
    public float lastPrice; //最新价
    public int totalQty; //当日总成交量
    public float amount; //当日总成交额
    public float bidPrice1; //买一价
    public int bidQty1; //买一量
    public float askPrice1; //卖一价
    public int askQty1; //卖一量
    public float changeValue; //涨跌值
    public float changeRate;  //涨跌幅

    @Override
    public String toString() {
        return "ThirdSanpShot{" +
                "time=" + time +
                ", symbol='" + symbol + '\'' +
                ", cnName='" + cnName + '\'' +
                ", preSettlePrice=" + preSettlePrice +
                ", fVol=" + fVol +
                ", fOpenInt=" + fOpenInt +
                ", settlePrice=" + settlePrice +
                ", preClosePrice=" + preClosePrice +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", lastPrice=" + lastPrice +
                ", totalQty=" + totalQty +
                ", amount=" + amount +
                ", bidPrice1=" + bidPrice1 +
                ", bidQty1=" + bidQty1 +
                ", askPrice1=" + askPrice1 +
                ", askQty1=" + askQty1 +
                '}';
    }

    public static String getOuterContractNo(String commodityNo){
        synchronized (productListOuter){
            return productListOuter.get(commodityNo);
        }
    }

    public static String getInnerContractNo(String commodityNo){
        synchronized(productListInner){
            return productListInner.get(commodityNo);
        }
    }

    public static ThirdSanpShot parseByeBuf(ByteBuf quotaBuf){

        Charset charset = Charset.forName("GBK");
        ThirdSanpShot snapShot = new ThirdSanpShot();
        int time = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readInt();
        byte[] szLabel = new byte[12];
        int index =12;
        quotaBuf.readBytes(szLabel);
        for(int i=0;i<szLabel.length; i++){
            if(szLabel[i] == 0){
                index =i;
                break;
            }
        }
        String label = new String(szLabel,0,index,charset);

        byte[] szName = new byte[16];
        quotaBuf.readBytes(szName);
        index =16;
        for(int i=0;i<szName.length;i++){
            if(szName[i] ==0){
                index=i;
                break;
            }
        }
        String name = new String(szName,0,index,charset);
        snapShot.time=time;
        snapShot.symbol=label;
        snapShot.cnName=name;
        snapShot.preSettlePrice = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat(); // 期货前一交易日的结算价
        snapShot.fVol= (int)quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat(); //
        snapShot.fOpenInt= (int)quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();  //持仓量
        snapShot.settlePrice= quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();  //当日结算价
        snapShot.preClosePrice = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();  //昨收价
        snapShot.openPrice= quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();      //今开价
        snapShot.highPrice = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();       //最高价
        snapShot.lowPrice= quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();       //最低价
        snapShot.lastPrice = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();   //最新价
        snapShot.totalQty = (int)quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();    //当日成交量
        snapShot.amount= quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();  //当日成交额
        float m_fBuyPrice[] =new float[5];  //买 五档
        float m_fBuyVolume[] = new float[5]; //买 量
        float m_fSellPrice[] = new float[5]; //卖 五挡
        float m_fSellVolume[] = new float[5]; //卖 量
        snapShot.bidPrice1= quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyPrice[1] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyPrice[2] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyPrice[3] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyPrice[4] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        snapShot.bidQty1 = (int)quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyVolume[1] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyVolume[2] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyVolume[3] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fBuyVolume[4] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        snapShot.askPrice1 = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellPrice[1] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellPrice[2] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellPrice[3] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellPrice[4] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        snapShot.bidQty1 = (int)quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellVolume[1] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellVolume[2] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellVolume[3] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();
        m_fSellVolume[4] = quotaBuf.order(ByteOrder.LITTLE_ENDIAN).readFloat();


        if(snapShot.cnName.startsWith("美原油05")){
           // System.out.println((System.currentTimeMillis()/1000-snapShot.time)+","+snapShot.toString());
            addQuotaBean(snapShot,1,"CL1805");
            String contractNo=  getOuterContractNo("CL");
            if(StringTools.isNotEmpty(contractNo)){
                String contract =  format.format(date)+name.replaceAll("美原油","");
                if(contractNo.equals(contract)){
                   //将行情加入进去
                    //计算涨跌值 涨跌幅
                }
            }
        }


       return snapShot;
    }

    public static void addQuotaBean(ThirdSanpShot snapshot,int plate,String productName){
        double chageRate =  DoubleUtils.mul(DoubleUtils.div((snapshot.lastPrice-snapshot.preClosePrice),snapshot.preClosePrice),100);
        chageRate =  DoubleUtils.scaleFormatEnd(chageRate,2);
        snapshot.changeRate = (float)chageRate;
        snapshot.changeValue = (float)DoubleTools.scaleFormat(snapshot.lastPrice-snapshot.openPrice,2);
        QuotaBean quotaBean =new QuotaBean();
        quotaBean.setAskPrice1(String.valueOf(snapshot.askPrice1));
        quotaBean.setAskQty1(String.valueOf((snapshot.askQty1)));
        quotaBean.setBidPrice1(String.valueOf(snapshot.bidPrice1));
        quotaBean.setBidQty1(String.valueOf(snapshot.bidQty1));
        quotaBean.setChangeValue(String.valueOf(snapshot.changeValue));
        quotaBean.setHighPrice(String.valueOf(snapshot.highPrice));
        quotaBean.setLastPrice(String.valueOf(snapshot.lastPrice));
        quotaBean.setLowPrice(String.valueOf(snapshot.lowPrice));
        quotaBean.setOpenPrice(String.valueOf(snapshot.openPrice));
        quotaBean.setPositionQty(String.valueOf(snapshot.fOpenInt));
        quotaBean.setPreClosePrice(String.valueOf(snapshot.preClosePrice));
        quotaBean.setProductName(productName);
        quotaBean.setSettlePrice(String.valueOf(snapshot.settlePrice));
        quotaBean.setLimitDownPrice("0.0");
        quotaBean.setLimitUpPrice("0.0");
        quotaBean.setPreSettlePrice("0.0");
        quotaBean.setAveragePrice("0.0");
        quotaBean.setChangeValue(String.valueOf(snapshot.changeValue));
        quotaBean.setChangeRate(String.valueOf(snapshot.changeRate));
       // System.out.println(System.currentTimeMillis()-);
        Date t = new Date((long)snapshot.time*1000);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateime = format2.format(t);
        quotaBean.setTimeStamp(dateime+".000");
        quotaBean.setPlate(plate);
        quotaBean.setTotalQty(snapshot.totalQty);
        System.out.println(format2.format(new Date())+","+quotaBean);
    }
}
