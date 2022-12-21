package com.example.demo.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

public enum WorkingTime {
    START("08:30"), END("17:30");
    private String time;
    private WorkingTime(String time){
        this.time= time;
    }

    public String getTime() {
        return time;
    }
}
