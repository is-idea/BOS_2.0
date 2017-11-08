package cn.itcast.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author 刘相磊
 * @version 1.0, 2017-11-08 19:24
 */
public class Test1 {

   public static void main(String[] args) throws SchedulerException {
       // 定时器对象
       Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
       // 定义一个工作对象
       JobDetail jobBuilder = JobBuilder.newJob(HelloJob.class).withIdentity("job1","group1").build();
       // 定义一个触发器
       SimpleTrigger build = TriggerBuilder.newTrigger()
               .withIdentity("trigger1", "group1").startNow()
               .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3))
               .build();
       scheduler.scheduleJob(jobBuilder,build);
       // 开启定时任务
       scheduler.start();
   }

}
