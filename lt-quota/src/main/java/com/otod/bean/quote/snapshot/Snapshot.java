/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ok
 */
public class Snapshot {

    public String symbol = "";
    public String cnName = "";
    public String exchCode="";
    public int date = 0;     //日期
    public int time = 0;     //时间
    public double open;      //
    public double high;
    public double low;
    public double close;
    public double change;
    public double changeRate;
    public double pClose;
    public int volume;
    public int value;
    public double tVolume;
    public double tValue;
    
    
    
    public void updateSnapshot(Snapshot snapshot) {
        this.date = snapshot.date;
        this.time = snapshot.time;
        this.open = snapshot.open;
        this.high = snapshot.high;
        this.low = snapshot.low;
        this.close=snapshot.close;
        this.change = snapshot.change;
        this.changeRate = snapshot.changeRate;
        this.pClose = snapshot.pClose;
        this.volume = snapshot.volume;
        this.value = snapshot.value;
        this.tVolume = snapshot.tVolume;
        this.tValue = snapshot.tValue;
        
    }
}
