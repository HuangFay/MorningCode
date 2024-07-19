package com.morning.order.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM `order` WHERE ord_id = ?1", nativeQuery = true)
    void deleteByOrdId(int ordId);

    @Query("FROM OrderVO o WHERE o.memVO.memNo = ?1 ORDER BY o.ordId")
    List<OrderVO> findByMemNo(Integer memNo);
}
