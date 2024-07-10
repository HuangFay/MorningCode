package com.tabletype.model;

import java.sql.*;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class TableTypeJNDIDAO implements TableTypeDAO_interface{
	
	public TableTypeJNDIDAO() {
		
	}
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB3");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = 
			"INSERT INTO table_type (table_type,table_type_number) VALUES (?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT table_id,table_type, table_type_number FROM table_type";
		private static final String GET_ONE_STMT = 
			"SELECT table_id, table_type, table_type_number FROM table_type where table_id = ?";
		private static final String DELETE = 
			"DELETE FROM table_type where table_id = ?";
		private static final String UPDATE = 
			"UPDATE table_type set table_type=?, table_type_number=? where table_id = ?";
		@Override
		public void insert(TableTypeVO tabletypeVO) {
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(INSERT_STMT);
			
				pstmt.setInt(1, tabletypeVO.getTableType());
				pstmt.setInt(2, tabletypeVO.getTableTypeNumber());
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				
			}finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
		@Override
		public void update(TableTypeVO tabletypeVO) {
			// TODO Auto-generated method stub
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(UPDATE);
			
				pstmt.setInt(1, tabletypeVO.getTableType());
				pstmt.setInt(2, tabletypeVO.getTableTypeNumber());
				pstmt.setInt(3, tabletypeVO.getTableId());
				
				
				pstmt.executeUpdate();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				
			}finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
		@Override
		public void delete(Integer tableId) {
			Connection con = null;
			PreparedStatement pstmt = null;

			try {

				con = ds.getConnection();
				pstmt = con.prepareStatement(DELETE);

				pstmt.setInt(1, tableId);

				pstmt.executeUpdate();

				// Handle any driver errors
			
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}

			
			
		}
		@Override
		public TableTypeVO findByPrimaryKey(Integer tableId) {
			TableTypeVO tableVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ONE_STMT);


				pstmt.setInt(1, tableId);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					
					tableVO = new TableTypeVO();
					tableVO.setTableId(rs.getInt("table_id"));
					tableVO.setTableType(rs.getInt("table_type"));
					tableVO.setTableTypeNumber(rs.getInt("table_type_number"));
					
				}

				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				// Clean up JDBC resources
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return tableVO;
		}
		@Override
		public List<TableTypeVO> getAll() {
			List<TableTypeVO> list = new ArrayList<TableTypeVO>();
			TableTypeVO tableTypeVO = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ALL_STMT);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					tableTypeVO = new TableTypeVO();
					tableTypeVO.setTableId(rs.getInt("table_id"));
					tableTypeVO.setTableType(rs.getInt("table_type"));
					tableTypeVO.setTableTypeNumber(rs.getInt("table_type_number"));
					list.add(tableTypeVO);
				}
				
			}catch (SQLException se) {
				throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				
			}finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException se) {
						se.printStackTrace(System.err);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
			return list;
		}
//		public static void main(String[] args) {
//			TableTypeJDBCDAO dao = new TableTypeJDBCDAO();
//			
//			
			
//新增
			
//			TableTypeVO tableVO = new TableTypeVO();
//			
//			tableVO.setTableType(8);
//			tableVO.setTableTypeNumber(4);
//			dao.insert(tableVO);
			
//修改
//			TableTypeVO tableVO2 = new TableTypeVO();
//			tableVO2.setTableId(1);
//			tableVO2.setTableType(5);
//			tableVO2.setTableTypeNumber(2);
//			dao.update(tableVO2);
			
//刪除
//			dao.delete(3);
			
			
//查詢
//List<TableTypeVO> list = dao.getAll();
			
//			for (TableTypeVO aEmp : list) {
//				System.out.print(aEmp.getTableId() + ",");
//				System.out.print(aEmp.getTableType() + ",");
//				System.out.print(aEmp.getTableTypeNumber() + ",");
//				
//			}
//			
//		
//		}
	
		



}


