package com.lt.util.utils.quartz;


import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;


/**
 * 
 * quartz-2.2.2.jar
 * 描述:简单的任务管理
 *
 * @author jiupeng
 * @created 2015年12月23日 上午10:32:47
 * @since v1.0.0
 */
public class QuartzManager {

	private static SchedulerFactory sf = new StdSchedulerFactory();

	private static String JOB_GROUP_NAME = "sch_group_default";

	private static String TRIGGER_GROUP_NAME = "sch_trigger_default";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 *            任务名
	 * @param job
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, Job job, String time, Map<String, Object> param)
			throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(job.getClass())
							.withIdentity(jobName, JOB_GROUP_NAME)
							.build();// 任务名，任务组，任务执行类
		
		if (param != null && !param.isEmpty()) {
			for (Entry<String, Object> entry : param.entrySet()) {
				jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
			}
		}
		
		// 触发器
		Trigger trigger = TriggerBuilder.newTrigger()
							.withIdentity(jobName, TRIGGER_GROUP_NAME)// 触发器名,触发器组
							.withSchedule(
									CronScheduleBuilder.cronSchedule(time)) //触发器时间设定
							.startNow()
							.build();
		
		sched.scheduleJob(jobDetail, trigger);
		// 启动
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param job
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Job job, String time, Map<String, String> param) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		JobDetail jobDetail = JobBuilder.newJob(job.getClass())
							.withIdentity(jobName, jobGroupName)
							.build();// 任务名，任务组，任务执行类
		
		if (param != null && !param.isEmpty()) {
			for (Entry<String, String> entry : param.entrySet()) {
				jobDetail.getJobDataMap().put(entry.getKey(), entry.getValue());
			}
		}
		
		// 触发器
		Trigger trigger = TriggerBuilder.newTrigger()
							.withIdentity(triggerName, triggerGroupName)// 触发器名,触发器组
							.withSchedule(
									CronScheduleBuilder.cronSchedule(time)) //触发器时间设定
							.startNow()
							.build();
		
		sched.scheduleJob(jobDetail, trigger);
		// 启动
		if (!sched.isShutdown())
			sched.start();
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyJobTime(String jobName, String time) throws SchedulerException, ParseException {
		Scheduler sched = sf.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, JOB_GROUP_NAME);
		
		CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);
		if(trigger != null){
			//表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
			
			//按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(scheduleBuilder).build();
			
			//按新的trigger重新设置job执行
			sched.rescheduleJob(triggerKey, trigger);
		}
		
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time)
			throws SchedulerException, ParseException {
		
		Scheduler sched = sf.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
		
		CronTrigger trigger = (CronTrigger)sched.getTrigger(triggerKey);
		if(trigger != null){
			//表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
			
			//按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(scheduleBuilder).build();
			
			//按新的trigger重新设置job执行
			sched.rescheduleJob(triggerKey, trigger);
		}
		
	}

	/**
	 * 
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName) throws SchedulerException {
		try {
			Scheduler sched = sf.getScheduler();
			
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, JOB_GROUP_NAME);
			sched.pauseTrigger(triggerKey);
			
			JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
			sched.pauseJob(jobKey);
			sched.deleteJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
			throws SchedulerException {
		try {
			Scheduler sched = sf.getScheduler();
			
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
			sched.pauseTrigger(triggerKey);
			
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
			sched.pauseJob(jobKey);
			sched.deleteJob(jobKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
