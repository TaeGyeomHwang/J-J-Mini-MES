package com.boot.mes6.entity;

import com.boot.mes6.constant.MaterialName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "current_material")
@Getter
@Setter
public class CurrentMaterial {

    @Id
    @Enumerated(EnumType.STRING)
    private MaterialName current_material_name;

    @Column(nullable = false)
    private Long current_material_amount;

}
