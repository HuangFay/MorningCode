package com.morning.news.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morning.ordd.model.OrddVO;

@Service("newsService")
public class NewsService {
	
	@Autowired
	NewsRepository newsRepository;
	
	public List<NewsVO> getAll() {
		return newsRepository.findAll();
	}
	
	public NewsVO getOneNews(Integer id) {
		Optional<NewsVO> optional = newsRepository.findById(id);

		return optional.orElse(null);
	}
	
	public void editNews(NewsVO newsVO) {
		newsRepository.save(newsVO);
	}
	
	public void deleteNews(Integer newsId) {
		if (newsRepository.existsById(newsId))
			newsRepository.deleteById(newsId);
	}
	
	
	//前台
	 public NewsVO getLatestNews() {
	        return newsRepository.findTopByOrderByNewsIdDesc();
	    }
	
}
