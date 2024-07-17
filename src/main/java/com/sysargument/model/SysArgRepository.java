package com.sysargument.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SysArgRepository extends JpaRepository<SysArgVO, Integer> {

    List<SysArgVO> findBysysArgument(String sysArgument);

}
