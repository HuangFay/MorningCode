package com.morning.collect.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CollectService")
public class CollectService {

	@Autowired
	CollectRepository repository;

	// 會員查詢收藏 全部
	public List<CollectVO> showFavorite(Integer memNo) {
		return repository.findByMemNo(memNo);
	}

	// 加入收藏
	public void addFavorite(CollectVO collectVO) {
		repository.save(collectVO);
	}

	// 取消收藏
	public void deleteFavorite(Integer memNo, Integer mealsId) {
		repository.deleteByMemNoAndMealsId(memNo, mealsId);
	}

	// 查詢會員收藏
	public boolean isFavoriteExists(Integer memNo, Integer mealsId) {
		return repository.existsByMemNoAndMealsId(memNo, mealsId);
	}

	// 用輸入的memNo去CollectVO查mealsId
	public List<Integer> getUserFavorites(Integer memNo) {
		return repository.findMealsIdsByMemNo(memNo);
	}

}
