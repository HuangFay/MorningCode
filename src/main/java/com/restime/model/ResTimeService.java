package com.restime.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("resTimeService")
public class ResTimeService {
	@Autowired
	ResTimeRepository repository;
	public void addResTime(ResTimeVO resTimeVO) {
		repository.save(resTimeVO);
	}
	public void updateResTime(ResTimeVO resTimeVO) {
		repository.save(resTimeVO);
	}
	
	public void deleteResTime(Integer reservationTimeId) {
		if(repository.existsById(reservationTimeId))
			repository.deleteByreservationTimeId(reservationTimeId);;
	}
	
	public ResTimeVO getOneResTime(Integer reservationTimeId) {
		Optional<ResTimeVO> optional = repository.findById(reservationTimeId);
		
		return optional.orElse(null);
	}
	public List<ResTimeVO>getAll(){
		return repository.findAll();
	}
}
