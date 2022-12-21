package com.example.demo.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String date;
    private String checkinTime;
    private String checkoutTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "user_id")
    private User user;
    private String status;
    private String error;
    @OneToOne
    @Transient
    private Report report;

    public Timesheet(String date, User user, String error) {
        this.date = date;
        this.user = user;
        this.error = error;
    }

    @Override
    public String toString() {
        return "Timesheet{" +
                "date='" + date + '\'' +
                ", checkinTime='" + checkinTime + '\'' +
                ", checkoutTime='" + checkoutTime + '\'' +
                ", status='" + status + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
