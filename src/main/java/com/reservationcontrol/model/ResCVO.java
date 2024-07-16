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
	private Date reasrvationControlDate;
	@Column(name="reservation_control_table")
	private String reasrvationControlTable;
	
	
	public Integer getReservationControlId() {
		return reservationControlId;
	}
	public void setReservationControlId(Integer reservationControlId) {
		this.reservationControlId = reservationControlId;
	}

	public Date getReasrvationControlDate() {
		return reasrvationControlDate;
	}
	public void setReasrvationControlDate(Date reasrvationControlDate) {
		this.reasrvationControlDate = reasrvationControlDate;
	}
	public String getReasrvationControlTable() {
		return reasrvationControlTable;
	}
	public void setReasrvationControlTable(String reasrvationControlTable) {
		this.reasrvationControlTable = reasrvationControlTable;
	}
	public TableTypeVO getTableTypeVO() {
		return tableTypeVO;
	}
	public void setTableTypeVO(TableTypeVO tableTypeVO) {
		this.tableTypeVO = tableTypeVO;
	}
	
	
	
}
