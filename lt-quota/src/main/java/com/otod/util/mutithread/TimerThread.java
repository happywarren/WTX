/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util.mutithread;

import com.otod.bean.WorkHandleParam;
import com.otod.util.mutithread.WorkHandle;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Administrator
 */
public class TimerThread extends Thread {

    private WorkHandleParam workHandleParam;
    private Timer timer = new Timer();
    //时间间隔
    private long periodDay;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private boolean startRun = false;

    public WorkHandleParam getWorkHandleParam() {
        return workHandleParam;
    }

    public boolean isStartRun() {
        return startRun;
    }

    public void setStartRun(boolean startRun) {
        this.startRun = startRun;
    }

    public void setWorkHandleParam(WorkHandleParam workHandleParam) {
        this.workHandleParam = workHandleParam;
    }

    public long getPeriodDay() {
        return periodDay;
    }

    public void setPeriodDay(long periodDay) {
        this.periodDay = periodDay;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public void run() {
        doWork();
    }

    public void doWork() {
        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.set(Calendar.HOUR_OF_DAY, hour);
        next.set(Calendar.MINUTE, minute);
        next.set(Calendar.SECOND, second);


        long mstime = 0;


        if (hour != 0 || minute != 0 || second != 0) {
            if (next.getTimeInMillis() - now.getTimeInMillis() >= 0) {
                mstime = next.getTimeInMillis() - now.getTimeInMillis();
                // System.out.println("今天" + mstime);

            } else {
                mstime = (next.getTimeInMillis() - now.getTimeInMillis()) + (24 * 60 * 60 * 1000);
                //  System.out.println("不是今天" + mstime);
            }
        } else {
            mstime = periodDay;
        }

        if (startRun) {
            WorkHandle workHandle = workHandleParam.workHandle;
            Object param = workHandleParam.param;
            workHandle.setParam(param);
            workHandle.doWork();
        }

        // System.out.println(mstime+"||"+periodDay);
        timer.schedule(new RemindTask(), mstime, periodDay);
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            WorkHandle workHandle = workHandleParam.workHandle;
            Object param = workHandleParam.param;
            workHandle.setParam(param);
            workHandle.doWork();
        }
    }
}
