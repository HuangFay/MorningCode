// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.forum.model;



import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ForumReplyRepository extends JpaRepository<ForumReplyVO, Integer> {

	@Modifying
	@Query(value = "delete from forum_reply where reply_id =?1", nativeQuery = true)
	void deleteByReplyId(int replyId);
	
	@Modifying
    @Transactional
    @Query(value = "INSERT INTO forum_reply (reply_content, reply_time, mem_no, post_id) VALUES (:replyContent, :replyTime, :memNo, :postId)", nativeQuery = true)
    void insertReply(@Param("replyContent") String replyContent, 
                     @Param("replyTime") Date replyTime, 
                     @Param("memNo") Integer memNo, 
                     @Param("postId") Integer postId);
}