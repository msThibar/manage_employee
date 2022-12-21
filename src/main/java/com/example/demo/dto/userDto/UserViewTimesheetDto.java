package com.example.demo.dto.userDto;

import com.example.demo.models.Timesheet;
import com.example.demo.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserViewTimesheetDto {
    private String name;
    private String date;
    private String checkinTime;
    private String checkoutTime;
    private String status;
    private String error;
}
