package com.morning.order.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.morning.mem.model.MemVO;

@Entity  
@Table(name = "`order`")
public class OrderVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
		
	private Integer ordId;             
	private MemVO memVO;           
    private Byte ordType;              
    private Integer ordAmount;         
    private Byte ordStatus;            
    private Timestamp ordBuilddate;    
    private Timestamp ordReserveTime;  
    private Byte ordPaymentStatus;

	public OrderVO() { 
	}
	
	@Id  
	@Column(name = "ord_id")  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getOrdId() {
		return ordId;
	}

	public void setOrdId(Integer ordId) {
		this.ordId = ordId;
	}
	
	@ManyToOne
	@JoinColumn(name = "mem_no") 
	public MemVO getMemVO() {
		return memVO;
	}

	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}
	
	@Column(name = "ord_type")
	@NotNull(message = "請選擇選項")
	public Byte getOrdType() {
		return ordType;
	}

	public void setOrdType(Byte ordType) {
		this.ordType = ordType;
	}
	
	@Column(name = "ord_amount")
	@NotNull(message = "請輸入1~9999數字")
	@Min(value = 0, message = "請輸入1~9999數字")
	@Max(value = 9999, message = "請輸入1~9999數字")
	public Integer getOrdAmount() {
		return ordAmount;
	}

	public void setOrdAmount(Integer ordAmount) {
		this.ordAmount = ordAmount;
	}
	
	@Column(name = "ord_status")
	@NotNull(message = "請選擇選項")
	public Byte getOrdStatus() {
		return ordStatus;
	}

	public void setOrdStatus(Byte ordStatus) {
		this.ordStatus = ordStatus;
	}
	
	@Column(name = "ord_builddate")
	public Timestamp getOrdBuilddate() {
		return ordBuilddate;
	}
	
	public void setOrdBuilddate(Timestamp ordBuilddate) {
		this.ordBuilddate = ordBuilddate;
	}
	
	@Column(name = "ord_reserve_time")
//	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Timestamp getOrdReserveTime() {
		return ordReserveTime;
	}

	public void setOrdReserveTime(Timestamp ordReserveTime) {
		this.ordReserveTime = ordReserveTime;
	}

	@Column(name = "ord_payment_status")
	@NotNull(message = "請選擇選項")
	public Byte getOrdPaymentStatus() {
		return ordPaymentStatus;
	}

	public void setOrdPaymentStatus(Byte ordPaymentStatus) {
		this.ordPaymentStatus = ordPaymentStatus;
	}
}
	