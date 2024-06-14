package com.boot.mes6.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "facility")
@Getter
@Setter
public class Facility {

    @Id
    @Column(nullable = false)
    private Long facility_no;

    @Column(nullable = false)
    private String facility_name;

    @Column(nullable = false)
    private boolean is_onoff;
}
