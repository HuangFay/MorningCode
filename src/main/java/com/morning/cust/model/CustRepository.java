package com.morning.cust.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustRepository extends JpaRepository<CustVO, Integer> {
    
    @Query(value = "delete from customization_options where cust_id =?1", nativeQuery = true)
    void deleteByCustId(int custId);

    @Query(value = "from CustVO where custStatus = ?1 order by custId")
    List<CustVO> findByCustStatus(byte custStatus);
}
