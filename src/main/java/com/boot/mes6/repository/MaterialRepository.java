package com.boot.mes6.repository;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialInOut, Long> {

    //2024-06-21 기준 여기있는 메서드 아무것도 안씀
    //의사코드 작성 완료 후 필요하면 사용
    List<MaterialInOut> findByMaterialNameAndMaterialOrderDateBetweenOrderByMaterialNo(MaterialName name, LocalDateTime start, LocalDateTime end);

    List<MaterialInOut> findByOrderOrderNo(Long orderNo);  // 추가된 메소드

    // 특정 수주번호에 해당하는 material_order_date를 오늘 날짜로 업데이트하는 네이티브 쿼리
    @Transactional
    @Modifying
    @Query(value = "UPDATE material_in_out SET material_order_date = :orderDate WHERE order_no = :orderNo", nativeQuery = true)
    void updateMaterialOrderDateToTodayByOrderNo(Long orderNo, LocalDateTime orderDate);

}
