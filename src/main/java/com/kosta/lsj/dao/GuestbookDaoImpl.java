package com.kosta.lsj.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.kosta.lsj.vo.GuestbookVo;

public class GuestbookDaoImpl implements GuestbookDao{

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public Connection connectionDB() {
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		String user = "webdb";
		String pass = "1234";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC 드라이버 로드 실패!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public List<GuestbookVo> getList() {
		
		List<GuestbookVo> list = new ArrayList<>();
		try {
			String sql = "SELECT * FROM GUESTBOOK ORDER BY NO DESC";
			conn = connectionDB();
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery(sql);
			
			while (rs.next()) {
				GuestbookVo authorVO = new GuestbookVo(rs.getInt("no"), rs.getString("name"), rs.getString("password"), rs.getString("content"), rs.getString("reg_date"));
				list.add(authorVO);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(rs != null) {
					rs.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return list;
	}

	public int insert(GuestbookVo vo) {
		int isInserted = 0;

		try {
			String sql = "INSERT INTO GUESTBOOK VALUES (SEQ_GUESTBOOK_NO.NEXTVAL, ?, ?, ?, SYSDATE)";
			conn = connectionDB();
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());
			isInserted = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return isInserted;
	}

	public int delete(GuestbookVo vo) {

		int isDeleted = 0;

		try {
			String sql = "DELETE FROM GUESTBOOK WHERE NO = ? AND PASSWORD = ? ";
			conn = connectionDB();
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, vo.getNo()); 
			pstmt.setString(2, vo.getPassword());
			isDeleted = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
		return isDeleted;
	}
	
}
