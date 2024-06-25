package com.boot.mes6.repository;

import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.entity.MaterialInOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialInOut, Long> {

    //'같은 원자재'를 가진 애들 중 이력ID가 가장 크고(마지막) 그 가장 큰 애의 발주일이 같은 애들을 가져옴
    @Query(value = "SELECT * FROM material_in_out m1 " +
            "WHERE m1.material_name = :materialName " +
            "AND m1.material_receipt_date = (" +
            "    SELECT MAX(m2.material_receipt_date) " +
            "    FROM material_in_out m2 " +
            "    WHERE m2.material_name = :materialName)",
            nativeQuery = true)
    List<MaterialInOut> findLatestReceiptForMaterial(String materialName);

    //테이블에서 해당 원자재의 마지막 발주 접수일을 가져옴
    @Query(value = "SELECT material_receipt_Date FROM material_in_out WHERE material_name = :materialName ORDER BY material_no DESC LIMIT 1",
            nativeQuery = true)
    LocalDateTime findLatestMaterialInOut(String materialName);

    //원자재 발주 전인 애들 가져옴
    List<MaterialInOut> findByMaterialStatus(MaterialStatus materialStatus);

}
