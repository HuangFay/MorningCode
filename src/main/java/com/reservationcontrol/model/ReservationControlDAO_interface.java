package com.reservationcontrol.model;

import java.util.*;

public interface ReservationControlDAO_interface {
	public void insert (ResCVO reservationControlVO);
	public void update(ResCVO reservationControlVO);
	public void delete(Integer reservationControlId);
	public ResCVO findByPrimaryKey(Integer reservationControlId);
	public List<ResCVO> getAll();
	
	
}
