package com.example.demo.service;

import com.example.demo.Enum.Week;
import com.example.demo.Enum.WorkingTime;
import com.example.demo.dto.ResponseCheckin;
import com.example.demo.dto.timesheetDto.TimesheetViewDto;
import com.example.demo.models.Timesheet;
import com.example.demo.models.User;
import com.example.demo.repositories.TimesheetRepository;
import com.example.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimesheetService {
    private final Logger logger= LoggerFactory.getLogger(TimesheetService.class);
    private final int MAXIMUM_LATE= 15;
    private final int MAXIMUM_EARLY= 15;
    private final String NOT_CHECKIN= "NOT_CHECKIN";
    @Autowired
    TimesheetRepository timesheetRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalculateService calculateService;
    @Autowired
    ModelMapper mapper;
    @Transactional
    public ResponseCheckin checkin(String code){
        Date date= new Date();
        SimpleDateFormat formatDate= new SimpleDateFormat("yyyy:MM:dd");
        String day= formatDate.format(date);
        SimpleDateFormat formatTime= new SimpleDateFormat("HH:mm");
        String time= formatTime.format(date);
        Optional<Timesheet> timesheetTmp = timesheetRepository.findCheckin(code, day);
        if(timesheetTmp.isPresent()){
            Timesheet timesheet= timesheetTmp.get();
            logger.info("checkout, id: "+timesheet.getId());
            timesheetRepository.updateCheckOut(time,timesheet.getId());
            return new ResponseCheckin(timesheet.getUser().getName(), time, day);
        }
        logger.info(time);
        User user= userRepository.findByCode(code).get();
        Timesheet timesheet= new Timesheet();
        timesheet.setCheckinTime(time);
        try {
            long late= calculateService.calMinute(time, WorkingTime.START.getTime(), formatTime);
            if(late>MAXIMUM_LATE) timesheet.setError("LATE,");
            timesheet.setStatus("'CHECKIN_LATE':'"+late+"'");
        } catch (ParseException e) {
            System.out.println(e);
        }
        timesheet.setDate(day);
        timesheet.setUser(user);
        timesheetRepository.save(timesheet);
        return new ResponseCheckin(user.getName(), time, day);
    }
    @Transactional
    public void updateStatus(){
        Date date= new Date(new Date().getTime()-1000*60*60*24*1);
        SimpleDateFormat formatDate= new SimpleDateFormat("yyyy:MM:dd");
        String day= formatDate.format(date);
        logger.info(day);
        List<Timesheet> list= timesheetRepository.findAllByDate(day);
        list.forEach(timesheet -> {
            try {
                if(timesheet.getCheckoutTime()==null) timesheet.setCheckoutTime(timesheet.getCheckinTime());
                if(timesheet.getError()==null) timesheet.setError("");
                long timeCheckoutEarly= calculateService.calMinute(WorkingTime.END.getTime(), timesheet.getCheckoutTime(), new SimpleDateFormat("HH:mm"));
                timesheet.setStatus(timesheet.getStatus()+",'CHECKOUT_EARLY':'"+timeCheckoutEarly+"'");
                if(timeCheckoutEarly>MAXIMUM_EARLY)     timesheet.setError(timesheet.getError()+"EARLY,");
                timesheetRepository.updateStatus(timesheet.getStatus(), timesheet.getCheckoutTime(), timesheet.getError(), timesheet.getId());
            } catch (ParseException e) {
                logger.error(e.toString());
            }
        });
        //thêm các timesheet không checkin
        List<Integer> listId= userRepository.findIds();
        listId.stream().forEach(id->{
            boolean f= true;
            for (Timesheet timesheet : list) {
                if(timesheet.getUser().getId()==id){
                    f= false;
                    break;
                }
            }
            if(f){
                logger.info(id+"");
                timesheetRepository.save(new Timesheet(day, new User(id), NOT_CHECKIN));
            }
        });

    }
    public List<String> findUserForgotCheckout(){
        SimpleDateFormat formatDate= new SimpleDateFormat("yyyy:MM:dd");
        String day= formatDate.format(new Date());
        return timesheetRepository.findUserForgotCheckout(day);
    }
    public List<TimesheetViewDto> findTimesheetErrorByUsername(String username){
        String month= new SimpleDateFormat("MM:yyyy").format(new Date());
        return findTimesheetErrorByUsernameAndMonth(month, username);
    }
    public List<TimesheetViewDto> findTimesheetErrorByUsernameAndMonth(String month, String username){
        logger.info("month: "+month+", username: "+username);
        return timesheetRepository.findErrorOfMonth(month, username).stream()
                .map(timesheet -> mapper.map(timesheet, TimesheetViewDto.class)).collect(Collectors.toList());
    }
    public HashMap<String, TimesheetViewDto> findTimesheetOfWeek(String username){
        SimpleDateFormat formatDate= new SimpleDateFormat("yyyy:MM:dd");
        String now= new SimpleDateFormat("E").format(new Date());
        Week day= Week.valueOf(now);
        String startWeek= formatDate.format(new Date(new Date().getTime()- (long) day.getStartWeek() *1000*60*60*24));
        String endWeek= formatDate.format(new Date(new Date().getTime()+ (long) (6 - day.getStartWeek()) *1000*60*60*24));
        return findTimesheetBetweenTwoDates(startWeek, endWeek, username);
    }

    public HashMap<String, TimesheetViewDto> findTimesheetBetweenTwoDates(String start, String end, String username){
        List<TimesheetViewDto> list= timesheetRepository.findTimesheetBetweenTwoDates(start, end, username).stream()
                .map(timesheet -> mapper.map(timesheet, TimesheetViewDto.class)).collect(Collectors.toList());
        HashMap<String, TimesheetViewDto> hash= new HashMap<>();
        list.forEach(timesheetViewDto -> {
            hash.put(timesheetViewDto.getDate(), timesheetViewDto);
        });
        return hash;
    }

}
