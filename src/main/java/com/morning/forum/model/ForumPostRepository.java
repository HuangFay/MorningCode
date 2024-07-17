package com.morning.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository<ForumPostVO, Integer> {

	// 取得文章列表 (過濾可顯示的文章, 根據時間排序)
    List<ForumPostVO> findByPostStatusOrderByPostTimeDesc(Integer postStatus);
	
//	// 用原生SQL查詢做操作 (條件: 狀態可顯示, 文章內容數字截斷, 根據時間排序)
//	@Query(value = "SELECT post_id, mem_no, post_title, " +
//            "CASE WHEN LENGTH(post_content) > :length THEN CONCAT(SUBSTRING(post_content, 1, :length), '...') ELSE post_content END AS post_content, " +
//            "post_time, post_status " +
//            "FROM forum_posts " +
//            "WHERE post_status = 1 " +
//            "ORDER BY post_time DESC", nativeQuery = true)
//    List<ForumPostVO> findByPostTimeDesc(@Param("length") int length);
}