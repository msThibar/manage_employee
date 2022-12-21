package com.example.demo.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String status;
    @Column(name = "isApproved")
    private boolean isApproved;
    @Column(name= "ApprovedBy")
    private String ApprovedBy;
    @OneToOne
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheet;

}
