package com.tabletype.model;

import java.util.*;



public interface TableTypeDAO_interface {
    public void insert(TableTypeVO tabletypeVO);
    public void update(TableTypeVO tabletypeVO);
    public void delete(Integer tableId);
    public TableTypeVO findByPrimaryKey(Integer tableId);
    public List<TableTypeVO> getAll(); 
    //萬用複合查詢(傳入參數型態Map)(回傳 List)
//  public List<EmpVO> getAll(Map<String, String[]> map); 
}