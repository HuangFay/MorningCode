package com.morning.news.model;
import org.springframework.data.jpa.repository.JpaRepository;



public interface NewsRepository extends JpaRepository<NewsVO, Integer> {

	
	NewsVO findTopByOrderByNewsIdDesc();
}
