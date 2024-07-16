package com.sysargument.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Transactional

public interface SysArgRepository extends JpaRepository<SysArgumentVO, Integer> {


}
