package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "current_time")
@Getter
@Setter
public class CurrentTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long current_time_no;

    @Column(nullable = false)
    private LocalDateTime current_time_value;
}
