package com.morning.collect.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CollectService")
public class CollectService {

	@Autowired
    CollectRepository repository;

    //新增
    public void addFavorite(CollectVO collectVO) {
    	repository.save(collectVO);
    }
    
    //刪除
    public void deleteFavorite(Integer id) {
    	repository.deleteById(id);
    }
    
    //會員查詢收藏 全部
    public List<CollectVO> showFavorite(Integer memNo){
    	return repository.findByMemNo(memNo);
    }


}
