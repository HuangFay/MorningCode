package com.reservationcontrol.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationControlJDBCDAO implements ReservationControlDAO_interface {

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/morningcode?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "22837480";
		
	private static final String INSERT_STMT = 
			"INSERT INTO reservation_control (table_id,reservation_control_date,reservation_control_table) VALUES (?, ?,?)";
		private static final String GET_ALL_STMT = 
			"SELECT reservation_control_id,table_id, reservation_control_date,reservation_control_table FROM reservation_control";
		private static final String GET_ONE_STMT = 
			"SELECT reservation_control_id, table_id, reservation_control_date,reservation_control_table FROM reservation_control where reservation_control_id = ?";
		private static final String DELETE = 
			"DELETE FROM reservation_control where reservation_control_id = ?";
		private static final String UPDATE = 
			"UPDATE reservation_control set table_id=?, reservation_control_date=?,reservation_control_table=?";
	@Override
	public void insert(ResCVO reservationControlVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url, userid,passwd);
			pstmt = con.prepareStatement(INSERT_STMT);
		
			pstmt.setInt(1, reservationControlVO.getTableTypeVO().getTableId());
			pstmt.setDate(2, reservationControlVO.getReservationControlDate());
			pstmt.setString(3, reservationControlVO.getReservationControlTable());
			
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
	public void update(ResCVO reservationControlVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url, userid,passwd);
			pstmt = con.prepareStatement(UPDATE);
		
			pstmt.setInt(1, reservationControlVO.getTableTypeVO().getTableId());
			pstmt.setDate(2, reservationControlVO.getReservationControlDate());
			pstmt.setString(3, reservationControlVO.getReservationControlTable());
			
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
	public void delete(Integer reservationControlId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, reservationControlId);

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
	public ResCVO findByPrimaryKey(Integer reservationControlId) {
		ResCVO reservationControlVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, reservationControlId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				reservationControlVO = new ResCVO();
				reservationControlVO.setReservationControlId(rs.getInt("reservationControlId"));
				reservationControlVO.getTableTypeVO().setTableId(rs.getInt("table_Id"));;
				reservationControlVO.setReservationControlDate(rs.getDate("reservation_control_date"));
				reservationControlVO.setReservationControlTable(rs.getString("reservation_control_table"));
				
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
		return reservationControlVO;
	}

	@Override
	public List<ResCVO> getAll() {
		List<ResCVO> list = new ArrayList<ResCVO>();
		ResCVO reservationControlVO =null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				reservationControlVO = new ResCVO();
				reservationControlVO.setReservationControlId(rs.getInt("reservationControlId"));
				reservationControlVO.getTableTypeVO().setTableId(rs.getInt("table_id"));;
				;
				reservationControlVO.setReservationControlDate(rs.getDate("reservation_control_date"));
				reservationControlVO.setReservationControlTable(rs.getString("reservation_control_table"));
				
				list.add(reservationControlVO); // Store the row in the list
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
		
		return list;
	}
	

}
