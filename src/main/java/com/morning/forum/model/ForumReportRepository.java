package com.morning.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumReportRepository extends JpaRepository<ForumReportVO, Integer> {

    List<ForumReportVO> findByReportStatus(Integer reportStatus);
}