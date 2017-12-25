package com.lt.quota.core.quota.bean;


public class NewQuotaBean implements java.io.Serializable {

    /**
     * 卖一价
     */
    private String a;//askPrice1
    /**
     * 卖量1
     */
    private String b;//askQty1
    /**
     * 均价
     */
    private String c;//averagePrice
    /**
     * 买1价
     */
    private String d;//bidPrice1
    /**
     * 买量1
     */
    private String e;//bidQty1
    /**
     * 涨幅
     */
    private String f;//changeRate
    /**
     * 涨跌值
     */
    private String g;//changeValue
    /**
     * 最高价
     */
    private String h;//highPrice
    /**
     * 最新价
     */
    private String i;//lastPrice
    /**
     * 跌停价
     */
    private String j;//limitDownPrice
    /**
     * 涨停价
     */
    private String k;//limitUpPrice
    /**
     * 最低价
     */
    private String l;//lowPrice
    /**
     * 开盘价
     */
    private String m;//openPrice
    /**
     * 持仓量
     */
    private String n;//positionQty
    /**
     * 昨收盘价
     */
    private String o;//preClosePrice
    /**
     * 昨结算价
     */
    private String p;//preSettlePrice
    /**
     * 商品
     */
    private String q;//productName
    /**
     * 结算价
     */
    private String r;//settlePrice
    /**
     * 时间戳
     */
    private String s;//timeStamp
    /**
     * 当日总成交量
     */
    private Integer t;//totalQty

    /**
     * 内外盘
     */
    private Integer pl;

    private String u;//low13Week        13周最低价
    private String v;//high13Week       13周最高价
    private String w;//low26Week        26周最低价
    private String x;//high26Week       26周最高价
    private String y;//low52Week        52周最低价
    private String z;//high52Week       52周最高价
    private String aa;//marketCap       市值


    public NewQuotaBean() {
    }

    public NewQuotaBean(QuotaBean bean) {
        this.a = bean.getAskPrice1();
        this.b = bean.getAskQty1();
        this.c = bean.getAveragePrice();
        this.d = bean.getBidPrice1();
        this.e = bean.getBidQty1();
        this.f = bean.getChangeRate();
        this.g = bean.getChangeValue();
        this.h = bean.getHighPrice();
        this.i = bean.getLastPrice();
        this.j = bean.getLimitDownPrice();
        this.k = bean.getLimitUpPrice();
        this.l = bean.getLowPrice();
        this.m = bean.getOpenPrice();
        this.n = bean.getPositionQty();
        this.o = bean.getPreClosePrice();
        this.p = bean.getPreSettlePrice();
        this.q = bean.getProductName();
        this.r = bean.getSettlePrice();
        this.s = bean.getTimeStamp();
        this.t = bean.getTotalQty();
        this.u = bean.getLow13Week();
        this.v = bean.getHigh13Week();
        this.w = bean.getLow26Week();
        this.x = bean.getHigh26Week();
        this.y = bean.getLow52Week();
        this.z = bean.getHigh52Week();
        this.aa = bean.getMarketCap();
        this.pl = bean.getPlate();
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public Integer getPl() {
        return pl;
    }

    public void setPl(Integer pl) {
        this.pl = pl;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }


}
