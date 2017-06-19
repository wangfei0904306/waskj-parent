package com.waskj.base.core.quartz.consumer.schedule;

import java.text.ParseException;
import java.util.List;

import com.waskj.base.core.quartz.api.schedule.QuartzScheduleService;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.waskj.base.consumer.core.result.JsonResult;
import com.waskj.base.core.quartz.domain.schedule.JobInfo;


@Controller
@RequestMapping({"/schedule/manage", "/center/schedule/manage"})
public class QuartzMagController {
	
	@Autowired
	private QuartzScheduleService quartzScheduleService;
	
	/**
	 * 查找志有job
	 * @param jobInfo
	 * @param model
	 * @return
	 */
	@RequestMapping("/quartzList")
	@ResponseBody
    public JsonResult list(JobInfo jobInfo,ModelMap model){
		JsonResult j = new JsonResult();
		try{
			j.setSuccess(true);
			List<JobInfo> list = quartzScheduleService.getAllSchedule();
			j.setObj(list);
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
	/**
	 * 修改cron时间
	 * @param jobName
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@RequestMapping("saveQuartz")
	@ResponseBody
    public JsonResult saveQuartz(String jobName,String triggerName,String cronExpression) throws SchedulerException, ParseException{
		JsonResult j = new JsonResult();
		try{
			quartzScheduleService.modifyTriggerCron(triggerName,cronExpression) ;
			j.setSuccess(true);
			j.setMsg("修改cron成功");
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
	/**
	 * 暂停job/恢复job
	 * @param jobName
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@RequestMapping("updateStatusQuartz")
	@ResponseBody
    public JsonResult updateStatusQuartz(String jobName) throws SchedulerException, ParseException{
		JsonResult j = new JsonResult();
		try{
			JobKey jobkey = new JobKey(jobName);
			quartzScheduleService.pauseJob(jobkey);
			j.setSuccess(true);
			j.setMsg("暂停成功");
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
	/**
	 * 恢复job
	 * @param jobName
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@RequestMapping("resumeQuartz")
	@ResponseBody
    public JsonResult resumeQuartz(String jobName) throws SchedulerException, ParseException{
		JsonResult j = new JsonResult();
		try{
			JobKey jobkey = new JobKey(jobName);
			quartzScheduleService.resumeJob(jobkey);
			j.setSuccess(true);
			j.setMsg("恢复成功");
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
	/**
	 * 删除job
	 * @param jobName
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@RequestMapping("delQuartz")
	@ResponseBody
    public JsonResult delQuartz(String jobName) throws SchedulerException, ParseException{
		JsonResult j = new JsonResult();
		try{
			JobKey jobkey = new JobKey(jobName);
			quartzScheduleService.deleteJob(jobkey);
			j.setSuccess(true);
			j.setMsg("删除成功");
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
	/**
	 * 立即执行
	 * @param jobName
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@RequestMapping("nowStart")
	@ResponseBody
    public JsonResult nowStart(String jobName,String triggerName,String cronExpression) throws SchedulerException, ParseException{
		JsonResult j = new JsonResult();
		try{
			JobKey jobkey = new JobKey(jobName);
			quartzScheduleService.nowStart(jobkey);
			j.setSuccess(true);
			j.setMsg("立即执行成功");
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
	/** 暂停全部
	 * @param jobName
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@RequestMapping("pauseAllQuartz")
	@ResponseBody
    public JsonResult pauseAllQuartz() throws SchedulerException, ParseException{
		JsonResult j = new JsonResult();
		try{
			quartzScheduleService.pauseAll();
			j.setSuccess(true);
			j.setMsg("暂停全部成功");
		}catch(Exception e){
			e.printStackTrace();
		}
        return j;
    }
		
}
