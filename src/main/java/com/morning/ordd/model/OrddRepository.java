package com.morning.ordd.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrddRepository extends JpaRepository<OrddVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM orddetails WHERE ordd_id = ?1", nativeQuery = true)
    void deleteByOrddId(int orddId);

    @Query("FROM OrddVO WHERE ordId = ?1 ORDER BY orddId")
    List<OrddVO> findByOrdId(int ordId);
    
    //拿來找最新的訂單分數
    OrddVO findTopByOrderByOrddIdDesc();
}
