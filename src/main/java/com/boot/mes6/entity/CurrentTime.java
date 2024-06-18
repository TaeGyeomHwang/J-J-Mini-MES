package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "current_time_record")
@Getter
@Setter
public class CurrentTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long currentTimeNo;

    @Column(nullable = false)
    private LocalDateTime currentTimeValue;
}
