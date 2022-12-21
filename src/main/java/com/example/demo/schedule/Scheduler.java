package com.example.demo.schedule;

import com.example.demo.repositories.TimesheetRepository;
import com.example.demo.service.MailService;
import com.example.demo.service.TimesheetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Scheduler {
    private final Logger logger= LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    TimesheetService timesheetService;
    @Autowired
    MailService mailService;
    /*@Scheduled(fixedDelay = 1*1000*30)
    public void Test(){
        logger.info("fixed delay, time: "+System.currentTimeMillis()/1000);
        try {
            Thread.sleep(1000*5);
        } catch (InterruptedException e) {
            logger.info(e.toString());
        }
    }
    @Scheduled(fixedRate = 1*1000*30)
    public void Test2(){
        logger.info("fixed date, time: "+System.currentTimeMillis()/1000);
        try {
            Thread.sleep(1000*5);
        } catch (InterruptedException e) {
            logger.info(e.toString());
        }
    }*/
    //@Scheduled(fixedRate = 1*1000*60*60)
    @Scheduled(cron = "0 0 12 * * TUE-SAT")
    public void UpdateStatusTimesheet(){
        logger.info("UpdateStatusTimesheet running");
        timesheetService.updateStatus();
    }
    @Scheduled(cron = "0 0 18 * * MON-FRI")
    public void notifyCheckout(){
        logger.info("notifyCheckout running");
        List<String> list= timesheetService.findUserForgotCheckout();
        list.forEach(s -> mailService.notifyCheckout(s));
    }
}
