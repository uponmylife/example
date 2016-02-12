package geo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Application {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        JobDetail job = newJob(SimpleJob1.class).withIdentity("job1", "group1").build();
        CronScheduleBuilder cronSchedule = cronSchedule("0/5 * * * * ?");
        CronTrigger cronTrigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule).build();
        scheduler.scheduleJob(job, cronTrigger);

        JobDetail job2 = newJob(SimpleJob1.class).withIdentity("job2", "group1").build();
        SimpleScheduleBuilder simpleSchedule = simpleSchedule().withIntervalInSeconds(1).repeatForever();
        Trigger simpleTrigger = newTrigger().withIdentity("trigger2", "group1").withSchedule(simpleSchedule).build();
        scheduler.scheduleJob(job2, simpleTrigger);

        scheduler.start();

        print(scheduler);
        Thread.sleep(2000);
        scheduler.deleteJob(new JobKey("job1", "group1"));
        print(scheduler);
    }

    private static void print(Scheduler scheduler) throws SchedulerException {
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                System.out.printf("group=%s, name=%s, trigger=", jobKey.getGroup(), jobKey.getName(),
                        getTriggerInfo(triggers.get(0).getTriggerBuilder().build()));
            }

        }
    }

    private static String getTriggerInfo(Trigger trigger) {
        if (trigger instanceof CronTrigger)
            return "CronTrigger: expression=" + ((CronTrigger) trigger).getCronExpression();
        else if (trigger instanceof SimpleTrigger)
            return "SimpleTrigger: interval=" + ((SimpleTrigger) trigger).getRepeatInterval();

        return null;
    }
}
