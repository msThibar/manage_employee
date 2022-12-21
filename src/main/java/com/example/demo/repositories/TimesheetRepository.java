package com.example.demo.repositories;

import com.example.demo.models.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

    @Query("Select t From Timesheet t where t.user.code= ?1 and t.date= ?2")
    public Optional<Timesheet> findCheckin(String user_code, String date);
    List<Timesheet> findAllByDate(String date);
    @Modifying
    @Query("Update Timesheet t set t.status=?1, t.checkoutTime=?2, t.error=?3 where t.id=?4")
    public void updateStatus(String status, String checkoutTime, String error, int id);
    @Modifying
    @Query("Update Timesheet t set t.checkoutTime=?1 where t.id=?2")
    public void updateCheckOut(String checkout, int id);

    @Query("Select t.user.email From Timesheet t Where t.checkoutTime=null And t.date=?1")
    public List<String> findUserForgotCheckout(String day);
    //@Query(value = "Select t.user_id From timesheet t inner join user on user.id = t.user_id where t.date like '___08:2022' And user.username='admin0'", nativeQuery = true)
    // -> no column id ???
    @Query("Select t From Timesheet t Where t.date like ?1% And t.user.username=?2 And t.error is not null")
    public List<Timesheet> findErrorOfMonth(String month, String username);
    @Query("SELECT t FROM Timesheet t WHERE t.date BETWEEN :start AND :end AND t.user.username=:username and t.checkoutTime is not null")
    public List<Timesheet> findTimesheetBetweenTwoDates(String start, String end, String username);
    //public List<Timesheet> findTimesheetBetweenTwoDates(@Param("end") String end, @Param("start") String start, @Param("username") String username);
}
