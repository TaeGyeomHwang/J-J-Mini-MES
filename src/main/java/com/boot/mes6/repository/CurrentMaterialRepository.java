package com.boot.mes6.repository;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.entity.CurrentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentMaterialRepository extends JpaRepository<CurrentMaterial, Long> {

    Optional<CurrentMaterial> findByCurrentMaterialName(MaterialName materialName);

}
