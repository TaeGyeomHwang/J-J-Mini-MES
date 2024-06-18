package com.boot.mes6.repository;

import com.boot.mes6.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryHwang extends JpaRepository<Order, Long> {
//    Order findByOrder_no(Long orderNo);
}
