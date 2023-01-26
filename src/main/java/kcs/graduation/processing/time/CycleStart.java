package kcs.graduation.processing.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CycleStart {
    public void autoEventStart(Date date, TimerTask task) {
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        timer.schedule(task, calendar.getTime());
    }

    public void everySecondExecute(TimerTask task) {
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        timer.schedule(task, calendar.getTime(), 1000L);
    }
}
