package com.tabletype.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("tableService")
public class TableTypeService {
	
	@Autowired
	TableTypeRepository repository;
	
	public void addTableType( TableTypeVO tableTypeVO) {
		repository.save(tableTypeVO);
			}
	public void updateTableType(TableTypeVO tableTypeVO) {
		repository.save(tableTypeVO);
	}
	
	//應該用不到
	public void deleteTableType(Integer tableId) {
		if (repository.existsById(tableId))
			repository.deleteBytableTypelId(tableId);
	}
	
	public TableTypeVO getOneTableType(Integer tableId) {
		Optional<TableTypeVO> optional = repository.findById(tableId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	public List<TableTypeVO>getAll(){
		return repository.findAll();
	}
	
	
	
	
//	private TableTypeDAO_interface dao;
//	
//	public TableTypeService() {
//		dao = new TableTypeJNDIDAO();
//	}
//	public TableTypeVO addTableType( Integer tableType,Integer tableTypeNumber) {
//		
//		TableTypeVO tableTypeVO = new TableTypeVO();
//		
//		tableTypeVO.setTableType(tableType);
//		tableTypeVO.setTableTypeNumber(tableTypeNumber);
//		dao.insert(tableTypeVO);
//		
//		return tableTypeVO;
//	}
//	
//	public TableTypeVO updateTableType(Integer tableId,Integer tableType,Integer tableTypeNumber) {
//		TableTypeVO tableTypeVO = new TableTypeVO();
//		tableTypeVO.setTableId(tableId);
//		tableTypeVO.setTableType(tableType);
//		tableTypeVO.setTableTypeNumber(tableTypeNumber);
//		
//		
//		dao.update(tableTypeVO);
//		return dao.findByPrimaryKey(tableId);
//		
//	}
//	
//	public void deleteTableType(Integer tableId) {
//		dao.delete(tableId);
//	}
//	public TableTypeVO getOneTableType(Integer tableId) {
//		return dao.findByPrimaryKey(tableId);
//	}
//
//	public List<TableTypeVO> getAll() {
//		return dao.getAll();
//	}
	
	
	
	
	
	
	
	
	
}
