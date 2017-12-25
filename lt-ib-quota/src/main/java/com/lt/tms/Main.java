package com.lt.tms;

import com.lt.tms.utils.FileUtils;
import com.lt.tms.utils.Utils;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/24 下午2:56
 * email:cndym@163.com
 */
public class Main {

    public static void main(String[] args) {
        String value = "TTMNPMGN=20.87228;NLOW=102.53;ACFSHR=10.21621;ALTCL=-99999.99;TTMPRCFPS=14.40608;TTMCFSHR=10.72221;ASFCF=-1163;AEPSNORM=8.3063;TTMRECTURN=18.5383;AATCA=106869;QCSHPS=14.84763;TTMFCF=38644;LATESTADATE=2017-07-01;APTMGNPCT=28.46053;TTMNIAC=46651;EV_Cur=814585.7;QATCA=112875;PR2TANBK=6.59027;TTMFCFSHR=7.28565;NPRICE=157.14;ASICF=-32429;REVTRENDGR=14.77854;QSCEX=-2360;PRICE2BK=6.13464;ALSTD=-99999.99;AOTLO=65824;TTMPAYRAT=26.54177;QPR2REV=17.89067;;TTMREVCHG=1.46127;TTMROAPCT=14.33706;QTOTCE=132425;APENORM=18.91817;QLTCL=81302;QSFCF=-549;TTMROIPCT=18.73583;DIVGRPCT=10.20822;QOTLO=8363;TTMEPSCHG=2.67222;YIELD=1.616393;TTMREVPS=42.13831;TTMEBT=62360;ADIV5YAVG=1.59573;Frac52Wk=1;NHIG=159.75;ASCEX=-13548;QTA=345173;TTMGROSMGN=38.50797;QTL=198717;AFPRD=22057;QCURRATIO=1.38834;TTMREV=223507;TTMINVTURN=55.22966;QCASH=76759;QLSTD=11980;TTMOPMGN=26.83809;TTMPR2REV=3.66567;QSICF=-820;TTMNIPEREM=402163.8;EPSCHNGYR=16.92664;TTMPRFCFPS=21.20129;TTMPTMGN=27.90069;AREVPS=39.20509;AEBTNORM=61372;ASOPI=60024;NetDebt_I=31580;PRYTDPCTR=22.60223;TTMEBITD=70206;AFEEPSNTM=10.1984;EPSTRENDGR=16.00656;QTOTD2EQ=81.81159;QSOPI=10768;QBVPS=25.6152;YLD5YAVG=1.62024;PR13WKPCT=7.295397;PR52WKPCT=51.38411;AROAPCT=14.92637;QTOTLTD=89864;TTMEPSXCLX=8.79096;QPRCFPS=-99999.99;QTANBVPS=24.04744;AROIPCT=20.19096;QEBIT=10583;QEBITDA=13122;MKTCAP=819302.8;TTMINTCOV=-99999.99;TTMROEPCT=36.02867;TTMREVPERE=1926785;AEPSXCLXOR=8.306303;QFPRD=9237;REVCHNGYR=7.20053;AFPSS=-29227;CURRENCY=USD;EV2EBITDA_Cur=62.07786;PEEXCLXOR=17.87518;QQUICKRATI=1.34965;ASINN=-99999.99;QFPSS=-7092;BETA=1.27468;ANIACNORM=45687;PR1WKPCT=2.398014;QLTD2EQ=67.8603;QSINN=-99999.99;PR4WKPCT=9.056842;AEBIT=58829";
        Map<String, String> valueMap = parseTickString47(value);
        System.out.println(valueMap.get("VOL10DAVG"));
        System.out.println(valueMap.get("MKTCAP"));
        System.out.println(valueMap.get("TTMDIVSHR"));
    }


    private static Map<String, String> parseTickString47(String value) {
        if (Utils.isEmpty(value)) {
            return null;
        }
        try {
            String[] valueArray = value.split(";");
            Map<String, String> valueMap = new HashMap<String, String>();
            for (String subValue : valueArray) {
                if (Utils.isEmpty(subValue)) {
                    continue;
                }
                String[] subArray = subValue.split("=");
                if (subArray.length != 2) {
                    continue;
                }
                valueMap.put(subArray[0], subArray[1]);
            }
            return valueMap;
        } catch (Exception e) {
            return null;
        }
    }


    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("Token cannot be generated.", e);
        }
    }
}
