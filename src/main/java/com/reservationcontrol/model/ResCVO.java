package com.reservationcontrol.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.tabletype.model.TableTypeVO;

@Entity
@Table(name = "reservation_control")
public class ResCVO implements java.io.Serializable {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="reservation_control_id", updatable = false, insertable = false)
	private Integer reservationControlId;
	
	@ManyToOne
	@JoinColumn(name="table_id")
	private TableTypeVO tableTypeVO;
	@Column(name="reservation_control_date")
//	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date reservationControlDate;
	@Column(name="reservation_control_table")
	private String reservationControlTable="000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	
	
	public Integer getReservationControlId() {
		return reservationControlId;
	}
	public void setReservationControlId(Integer reservationControlId) {
		this.reservationControlId = reservationControlId;
	}

	public Date getReservationControlDate() {
		return reservationControlDate;
	}
	public void setReservationControlDate(Date reservationControlDate) {
		this.reservationControlDate = reservationControlDate;
	}
	public String getReservationControlTable() {
		return reservationControlTable;
	}
	public void setReservationControlTable(String reservationControlTable) {
		this.reservationControlTable = reservationControlTable;
	}
	public TableTypeVO getTableTypeVO() {
		return tableTypeVO;
	}
	public void setTableTypeVO(TableTypeVO tableTypeVO) {
		this.tableTypeVO = tableTypeVO;
	}
	
	
	
}
