package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter
@Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long planNo;

    private Long planProductionAmount;

    private LocalDateTime planStartDate;

    private LocalDateTime planExpectFinishDate;

    private LocalDateTime planFinishDate;
}
