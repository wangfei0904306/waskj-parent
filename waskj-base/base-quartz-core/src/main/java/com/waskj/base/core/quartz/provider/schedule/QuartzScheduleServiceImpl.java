package com.waskj.base.core.quartz.provider.schedule;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.waskj.base.core.quartz.api.schedule.QuartzScheduleService;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Service;

import com.waskj.base.core.quartz.domain.schedule.JobInfo;


@Service
public class QuartzScheduleServiceImpl implements QuartzScheduleService {

	@Resource(name = "schedulerFactory")
	private Scheduler scheduler;

	/**
	 * 获取所有job、状态、cron等
	 */
	public List<JobInfo> getAllSchedule() {
		List<JobInfo> list = new ArrayList<JobInfo>();
		try {

			GroupMatcher<TriggerKey> triggerMatcher = GroupMatcher.anyTriggerGroup();
			Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(triggerMatcher);

			for (TriggerKey tKey : triggerKeys) {
				JobInfo jobInfo = new JobInfo();
				jobInfo.setTriggerName(tKey.getName());
				String jobGroup = tKey.getGroup();

				Trigger t = scheduler.getTrigger(tKey);
				String jobName = t.getJobKey().getName();
				JobKey jobkey = new JobKey(jobName);

				JobDetail j = scheduler.getJobDetail(jobkey);
				String jobClass = j.getJobClass().getName();
				jobInfo.setJobGroup(jobGroup);
				jobInfo.setJobName(jobName);
				jobInfo.setJobClass(jobClass);
				jobInfo.setPrevTime(t.getPreviousFireTime() == null ? null : t.getPreviousFireTime());
				jobInfo.setNextTime(t.getNextFireTime() == null ? null : t.getNextFireTime());
				jobInfo.setInstanceName(scheduler.getSchedulerInstanceId());
				if (t instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) t;
					String cronExpression = cronTrigger.getCronExpression();
					jobInfo.setCronExpression(cronExpression);
				}
				TriggerState ts = scheduler.getTriggerState(tKey);
				String triggerState = ts.name();
				jobInfo.setJobStatus(triggerState);
				list.add(jobInfo);
			}
			ComparatorJobInfo comparator = new ComparatorJobInfo();
			Collections.sort(list, comparator);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public class ComparatorJobInfo implements Comparator<JobInfo> {
		public int compare(JobInfo arg0, JobInfo arg1) {
			// 首先比较年龄，如果年龄相同，则比较名字
			int flag = arg0.getJobStatus().compareTo(arg1.getJobStatus());
			if (flag == 0) {
				return arg0.getNextTime().compareTo(arg1.getNextTime());
			} else {
				return flag;
			}
		}

	}

	/**
	 * 修改定时器cron
	 * 
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public void modifyTriggerCron(String triggerName, String cronExpression) throws SchedulerException, ParseException {
		TriggerKey triggerKey = new TriggerKey(triggerName);
		Trigger trigger = scheduler.getTrigger(triggerKey);
		CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
		String dbCronExpression = cronTrigger.getCronExpression();

		// 如果相等，则表示用户并没有重新设定数据库中的任务时间，这种情况不需要重新rescheduleJob
		if (!dbCronExpression.equalsIgnoreCase(cronExpression)) {
			cronTrigger.setCronExpression(cronExpression);
			cronTrigger.setStartTime(new Date());
			scheduler.rescheduleJob(triggerKey, trigger);
		}

	}

	/**
	 * 删除相关的job任务
	 * 
	 * @param jobkey
	 * @return
	 * @throws SchedulerException
	 */
	public boolean deleteJob(JobKey jobkey) throws SchedulerException {
		return scheduler.deleteJob(jobkey);
	}

	/**
	 * 立即执行
	 * 
	 * @param triggerkey
	 * @param trigger
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public void nowStart(JobKey jobkey) throws SchedulerException, ParseException {
		scheduler.triggerJob(jobkey);
	}

	/**
	 * 停止一个job任务
	 * 
	 * @param jobkey
	 * @throws SchedulerException
	 */
	public void pauseJob(JobKey jobkey) throws SchedulerException {
		scheduler.pauseJob(jobkey);
	}

	/**
	 * 恢复相关的job任务
	 * 
	 * @param jobkey
	 * @throws SchedulerException
	 */
	public void resumeJob(JobKey jobkey) throws SchedulerException {
		scheduler.resumeJob(jobkey);
	}

	/**
	 * 暂停调度中所有的job任务
	 * 
	 * @throws SchedulerException
	 */
	public void pauseAll() throws SchedulerException {
		// scheduler.pauseAll();
		GroupMatcher<JobKey> groupmatcher = GroupMatcher.anyJobGroup();
		scheduler.pauseJobs(groupmatcher);
	}
}
