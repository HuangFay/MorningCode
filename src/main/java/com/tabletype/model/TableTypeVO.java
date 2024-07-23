package com.tabletype.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="table_type")
public class TableTypeVO implements java.io.Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="table_id", updatable = false, insertable = false)
	private Integer tableId;
	@Column(name="table_type")
	private Integer tableType;
	@Column(name="table_type_number")
	private Integer tableTypeNumber;
	
	
	
	public Integer getTableId() {
		return tableId;
	}
	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}
	public Integer getTableType() {
		return tableType;
	}
	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}
	public Integer getTableTypeNumber() {
		return tableTypeNumber;
	}
	public void setTableTypeNumber(Integer tableTypeNumber) {
		this.tableTypeNumber = tableTypeNumber;
	}
	
	public String toString() {
		return "TableTypeVO [tableId=" + tableId + ", tableType=" + tableType + ", tableTypeNumber=" + tableTypeNumber + "]";
	}
	
	
}
