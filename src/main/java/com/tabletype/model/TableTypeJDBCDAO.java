package com.tabletype.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;






public class TableTypeJDBCDAO implements TableTypeDAO_interface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/morningcode?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "22837480";
	
	private static final String INSERT_STMT = 
			"INSERT INTO table_type (table_type,table_type_number) VALUES (?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT table_id,table_type, table_type_number FROM table_type";
		private static final String GET_ONE_STMT = 
			"SELECT table_id, table_type, table_type_number FROM table_type where table_id = ?";
		private static final String DELETE = 
			"DELETE FROM table_type where table_id = ?";
		private static final String UPDATE = 
			"UPDATE table_type set table_type=?, table_type_number=?";
		@Override
		public void insert(TableTypeVO tabletypeVO) {
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(driver);
				
				con = DriverManager.getConnection(url, userid,passwd);
				pstmt = con.prepareStatement(INSERT_STMT);
			
				pstmt.setInt(1, tabletypeVO.getTableType());
				pstmt.setInt(2, tabletypeVO.getTableTypeNumber());
				pstmt.executeUpdate();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
			
			}catch (SQLException se) {
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
			
			Connection con = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName(driver);
				
				con = DriverManager.getConnection(url, userid,passwd);
				pstmt = con.prepareStatement(UPDATE);
			
				pstmt.setInt(1, tabletypeVO.getTableType());
				pstmt.setInt(2, tabletypeVO.getTableTypeNumber());
				
				pstmt.executeUpdate();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
			
			}catch (SQLException se) {
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

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(DELETE);

				pstmt.setInt(1, tableId);

				pstmt.executeUpdate();

				// Handle any driver errors
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
				// Handle any SQL errors
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

				Class.forName(driver);
				con = DriverManager.getConnection(url, userid, passwd);
				pstmt = con.prepareStatement(GET_ONE_STMT);

				pstmt.setInt(1, tableId);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					// empVo 也稱為 Domain objects
					tableVO = new TableTypeVO();
					tableVO.setTableId(rs.getInt("table_id"));
					tableVO.setTableType(rs.getInt("table_type"));
					tableVO.setTableTypeNumber(rs.getInt("table_type_number"));
					
				}

				// Handle any driver errors
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
				// Handle any SQL errors
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
			TableTypeVO tabletypeVO = null;
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName(driver);
				
				con = DriverManager.getConnection(url, userid,passwd);
				pstmt = con.prepareStatement(GET_ALL_STMT);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					tabletypeVO = new TableTypeVO();
					tabletypeVO.setTableId(rs.getInt("table_id"));
					tabletypeVO.setTableType(rs.getInt("table_type"));
					tabletypeVO.setTableTypeNumber(rs.getInt("table_type_number"));
					list.add(tabletypeVO);
				}
				
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load database driver. "
						+ e.getMessage());
			
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
		public static void main(String[] args) {
			TableTypeJDBCDAO dao = new TableTypeJDBCDAO();
			
			
			
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
//			
//			for (TableTypeVO aEmp : list) {
//				System.out.print(aEmp.getTableId() + ",");
//				System.out.print(aEmp.getTableType() + ",");
//				System.out.print(aEmp.getTableTypeNumber() + ",");
//				
//			}
//			
//		
			
//			TableTypeVO tableVO = dao.findByPrimaryKey(1);
//			System.out.println(tableVO.getTableId());
		}



}
