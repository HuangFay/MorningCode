package com.reservation.model;

import com.morning.mem.model.MemVO;
import com.tabletype.model.TableTypeVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ResRepository extends JpaRepository<ResVO, Integer>{


    List<ResVO> findByMemVO(MemVO memVO);

}
