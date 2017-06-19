package com.waskj.base.core.quartz.api.schedule;

import java.text.ParseException;
import java.util.List;

import org.quartz.JobKey;
import org.quartz.SchedulerException;

import com.waskj.base.core.quartz.domain.schedule.JobInfo;


public interface QuartzScheduleService {
	/**
     * 获取所有job、状态、cron等
     */
    public List<JobInfo> getAllSchedule();
    /**
     * 修改定时器cron
     * @throws SchedulerException 
     * @throws ParseException 
     */
    public void modifyTriggerCron(String triggerName,String cronExpression) throws SchedulerException, ParseException;
    
    
    /**
     * 删除相关的job任务
     * @param jobkey
     * @return
     * @throws SchedulerException
     */
    public  boolean deleteJob(JobKey jobkey) throws SchedulerException;
    /**
     * 立即执行
     * @param triggerkey
     * @param trigger
     * @return
     * @throws SchedulerException
     * @throws ParseException 
     */
    public  void nowStart(JobKey jobkey) throws SchedulerException, ParseException;
    /**
     * 停止一个job任务
     * @param jobkey
     * @throws SchedulerException
     */
    public  void pauseJob(JobKey jobkey) throws SchedulerException;
    
    /**
     * 恢复相关的job任务
     * @param jobkey
     * @throws SchedulerException
     */
    public  void resumeJob(JobKey jobkey) throws SchedulerException;
    /**
     * 暂停调度中所有的job任务
     * @throws SchedulerException
     */

    public  void pauseAll() throws SchedulerException;
}
