package com.kosta.lsj.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kosta.lsj.vo.BoardVo;

public class BoardDaoImpl implements BoardDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(dburl, "webdb", "1234");
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC 드라이버 로드 실패!");
		}
		return conn;
	}

	public List<BoardVo> getList(String keyField, String keyWord, String startDate, String endDate, int start, int end) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		String query = "";

		try {
			conn = getConnection();

			if ((keyWord.equals("null") || keyWord.equals("")) && !keyField.equals("regDate")) {
				/*
					SELECT tmp2.NO, tmp2.TITLE, tmp2.HIT, TO_CHAR(tmp2.REG_DATE, 'YYYY-MM-DD HH:MI') REG_DATE , tmp2.USER_NO, tmp2.NAME 
					FROM (
							SELECT ROWNUM AS RNUM, tmp.* 
							FROM ( select u.NAME, b.*
					  			   from Board b, USERS u
					  			   where b.USER_NO = u.NO
					  			   and b.NO = b.REF 
					  			   order by ref desc, pos 
					  			 ) tmp
							WHERE ROWNUM <= ?+?
					)tmp2 
					WHERE RNUM > ?
				 */
				
				query = "SELECT tmp2.NO, tmp2.TITLE, tmp2.HIT, TO_CHAR(tmp2.REG_DATE, 'YYYY-MM-DD HH:MI') REG_DATE , tmp2.USER_NO, tmp2.NAME \r\n"
					  + "FROM ( \r\n"
					  + "		SELECT ROWNUM AS RNUM, tmp.* \r\n"
					  + "		FROM ( select u.NAME, b.*\r\n"
					  + "			   from Board b, USERS u \r\n"
					  + "			   where b.USER_NO = u.NO \r\n"
					  +	"			   and b.NO = b.REF \r\n"
					  + "			   order by ref desc, pos "
					  + "			 ) tmp\r\n"
					  + "		WHERE ROWNUM <= ?+? \r\n"
					  + "	   )tmp2 \r\n"
					  + "WHERE RNUM > ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, start);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
				
			}else if(keyField.equals("regDate")){
				query = "SELECT tmp2.NO, tmp2.TITLE, tmp2.HIT, TO_CHAR(tmp2.REG_DATE, 'YYYY-MM-DD HH:MI') REG_DATE , tmp2.USER_NO, tmp2.NAME \r\n"
					  + "FROM ( \r\n"
					  + "		SELECT ROWNUM AS RNUM, tmp.* \r\n"
					  + "		FROM ( select u.NAME, b.*\r\n"
					  + "			   from Board b, USERS u \r\n"
					  + "			   where b.USER_NO = u.NO \r\n"
					  +	"			   and b.NO = b.REF \r\n"
					  + "			   and TO_CHAR(REG_DATE, 'YYYY-MM-DD') BETWEEN ? AND ? \r\n"				
					  + "			   order by ref desc, pos "
					  + "			 ) tmp\r\n"
					  + "		WHERE ROWNUM <= ?+? \r\n"
					  + "	   )tmp2 \r\n"
					  + "WHERE RNUM > ?";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
				pstmt.setInt(3, start);
				pstmt.setInt(4, end);
				pstmt.setInt(5, start);
			}else {
				
				query = "SELECT tmp2.NO, tmp2.TITLE, tmp2.HIT, TO_CHAR(tmp2.REG_DATE, 'YYYY-MM-DD HH:MI') REG_DATE , tmp2.USER_NO, tmp2.NAME \r\n"
					  + "FROM ( \r\n"
					  + "		SELECT ROWNUM AS RNUM, tmp.* \r\n"
					  + "		FROM ( select u.NAME, b.*\r\n"
					  + "			   from Board b, USERS u \r\n"
					  + "			   where b.USER_NO = u.NO \r\n"
					  +	"			   and b.NO = b.REF \r\n"
					  +	"			   and "+ keyField + " like ? \r\n"	
					  + "			   order by ref desc, pos "
					  + "			 ) tmp\r\n"
					  + "		WHERE ROWNUM <= ?+? \r\n"
					  + "	   )tmp2 \r\n"
					  + "WHERE RNUM > ?";
					
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + keyWord + "%");
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
				pstmt.setInt(4, start);
					
			}

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");

				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return list;

	}

	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "SELECT b.TITLE, b.CONTENT, b.HIT, TO_CHAR(b.REG_DATE, 'YYYY-MM-DD HH:MI') REG_DATE, \r\n"
						 + "	   b.User_NO, b.POS, b.topREF, b.REF, b.DEPTH, u.NAME \r\n"
						 + "FROM Board b, USERS u\r\n"
						 + "WHERE b.USER_NO = u.NO\r\n"
						 + "AND b.NO = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				int pos = rs.getInt("pos");
				int topRef = rs.getInt("topRef");
				int ref = rs.getInt("ref");
				int depth = rs.getInt("depth");

				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, pos, topRef, ref, depth, userName);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		System.out.println(boardVo);
		return boardVo;

	}

	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "INSERT INTO BOARD\r\n"
						 + "(NO, TITLE, CONTENT, HIT, REG_DATE, USER_NO, POS, TOPREF, REF, DEPTH)\r\n"
						 + "VALUES(seq_board_no.nextval, ?, ?, 0, SYSDATE, ?, 0, seq_board_no.nextval, seq_board_no.nextval, 0)\r\n";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}

	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ? or ref = ? or topRef = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);
			pstmt.setInt(2, no);
			pstmt.setInt(3, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}

	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}

	@Override
	public int getTotalRecord(String keyField, String keyWord, String startDate, String endDate) {
		int totalCount = 0;
		String query = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			if ((keyWord.equals("null") || keyWord.equals("")) && !keyField.equals("regDate")) {
				query = "SELECT count(b.NO) \r\n"
					  + "FROM BOARD b , USERS u \r\n"
					  + "WHERE b.USER_NO = u.NO \r\n"
					  +	"AND b.NO = b.REF \r\n";
				pstmt = conn.prepareStatement(query);
			}else if(keyField.equals("regDate")) {
				query = "SELECT count(b.NO) \r\n"
					  + "FROM BOARD b , USERS u \r\n"
					  + "WHERE b.USER_NO = u.NO \r\n"
					  + "AND b.NO = b.REF \r\n"
					  + "AND TO_CHAR(REG_DATE, 'YYYY-MM-DD') BETWEEN ? AND ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, startDate);
				pstmt.setString(2, endDate);
			}else {
				query = "SELECT count(b.NO) \r\n"
					  + "FROM BOARD b , USERS u \r\n"
					  + "WHERE b.USER_NO = u.NO \r\n"
					  + "AND b.NO = b.REF \r\n"
					  + "AND " + keyField + " like ? ";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + keyWord + "%");
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}

		return totalCount;
	}
	
	@Override
	public int updateHit(int no) {
		int isUpdated = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			
			String query = "UPDATE BOARD "
						 + "SET HIT = HIT + 1 "
						 + "WHERE NO = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			isUpdated = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		return isUpdated;
	}

	@Override
	public int replyBoard(BoardVo vo) {
		int isInserted = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		System.out.println(vo.toString());
		try {
			conn = getConnection();
			
			String query = "INSERT INTO BOARD VALUES ("
						 + "							seq_board_no.nextval, "		//	no 
						 + "							?, "						// title
						 + "							?, "						// content
						 + "							0, "						// hit
						 + "							SYSDATE, "					// reg_date
						 + "							?, "						// user_no
						 + "							?, "						// pos
						 + "							?, "						// topRef
						 + "							?, "						// ref
						 + "							? "							// depth						 
						 + ")";
			pstmt2 = conn.prepareStatement(query);
			pstmt2.setString(1, vo.getTitle());
			pstmt2.setString(2, vo.getContent());
			pstmt2.setInt(3, vo.getUserNo());
			pstmt2.setInt(4, vo.getPos()+1);
			pstmt2.setInt(5, vo.getTopRef());
			pstmt2.setInt(6, vo.getRef());
			pstmt2.setInt(7, vo.getDepth()+1);
			isInserted = pstmt2.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return isInserted;
	}

	@Override
	public int replyUpBoard(int topRef, int pos) {
		int isUpdated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			String sql = "UPDATE BOARD set pos = pos + 1 where topRef = ? and pos > ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, topRef);
			pstmt.setInt(2, pos);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return isUpdated;
	}

	@Override
	public List<BoardVo> getReplyBoard(int no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<BoardVo> replyList = new ArrayList<>();
		
		try {
			conn = getConnection();
			
			String query = "SELECT b.No, u.NAME, b.CONTENT, TO_CHAR(b.REG_DATE, 'YYYY-MM-DD HH:MI') REG_DATE, "
						 + "	   b.POS, b.TOPREF, b.REF, b.DEPTH, b.USER_NO\r\n"
						 + "FROM Board b, USERS u\r\n"
						 + "WHERE b.USER_NO = u.NO\r\n"
						 + "AND POS > 0\r\n"
						 + "AND topREF = ?\r\n"
						 + "ORDER BY TOPREF desc, pos";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardVo replyVo = new BoardVo();
				replyVo.setNo(rs.getInt("no"));
				replyVo.setUserName(rs.getString("name"));
				replyVo.setContent(rs.getString("content"));
				replyVo.setRegDate(rs.getString("reg_date"));
				replyVo.setPos(rs.getInt("pos"));
				replyVo.setTopRef(rs.getInt("topRef"));
				replyVo.setRef(rs.getInt("ref"));
				replyVo.setDepth(rs.getInt("depth"));
				replyVo.setUserNo(rs.getInt("user_no"));
				
				replyList.add(replyVo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		return replyList;
	}

	@Override
	public int[] getReplyCount(List<BoardVo> list) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int[] replyCountList = new int[list.size()];
		
		try {
			conn = getConnection();
			
			String query = "SELECT COUNT(*) CNT \r\n"
						 + "FROM BOARD b \r\n"
						 + "WHERE b.topREF = ? \r\n"
						 + "AND b.NO != b.REF ";
			pstmt = conn.prepareStatement(query);
			for(int i=0; i<list.size(); i++) {
				pstmt.setInt(1, list.get(i).getNo());	
				rs = pstmt.executeQuery();
				rs.next();
				int count = rs.getInt("cnt");
				replyCountList[i] = count;
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return replyCountList;
	}
}
