package com.reservation.model;

import com.tabletype.model.TableTypeVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ResRepository extends JpaRepository<ResVO, Integer>{

    //搜需欄位找出相對應的ResCVO

}
