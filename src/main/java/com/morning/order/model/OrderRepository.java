// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.morning.order.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

        @Transactional
        @Modifying
        @Query(value = "delete from `order` where ord_id =?1", nativeQuery = true)
        void deleteByOrdId(int ordId);

      //● (自訂)條件查詢
        @Query(value = "from OrderVO where ord_id=?1 order by ord_id")
        List<OrderVO> findByOthers(int ordId);
}


