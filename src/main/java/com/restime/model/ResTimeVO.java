package com.restime.model;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reservation_time")
public class ResTimeVO implements Serializable{
	@Id
	@Column(name="reservation_time_id")
	private Integer reservationTimeId;
	@Column(name="reservation_time")
	private Time reservationTime;
	public Integer getReservationTimeId() {
		return reservationTimeId;
	}
	public void setReservationTimeId(Integer reservationTimeId) {
		this.reservationTimeId = reservationTimeId;
	}
	public Time getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(Time reservationTime) {
		this.reservationTime = reservationTime;
	}
	
}
