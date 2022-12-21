package com.example.demo.dto.timesheetDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetViewDto {
    private String date;
    private String checkinTime;
    private String checkoutTime;
    private String status;
    private String error;
}
