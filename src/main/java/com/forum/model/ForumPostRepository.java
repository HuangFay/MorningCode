// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.forum.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository<ForumPostVO, Integer> {

	
}