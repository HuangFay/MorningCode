package com.reservation.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("resService")
public class ResService {
	@Autowired
	ResRepository repository;
	
	public void addRes(ResVO resVO) {
		repository.save(resVO);
	}
	public void updateRes(ResVO resVO) {
		repository.save(resVO);
	}
	public ResVO getOneRes(Integer reservvationId) {
		Optional<ResVO> optional=repository.findById(reservvationId);
		return optional.orElse(null);
	}
	
	
	public List<ResVO>getAll(){
		return repository.findAll();
	}






}
