package com.kosta.lsj.dao;

import java.util.List;
import com.kosta.lsj.vo.BoardVo;

public interface BoardDao {
	public List<BoardVo> getList(String keyField, String keyWord, String startDate, String endDate, int start, int end);	// 게시물 전체 목록 조회
	public BoardVo getBoard(int no); 																					 	// 게시물 상세 조회
	public int insert(BoardVo vo);  																						// 게시물 등록
	public int delete(int no);       																						// 게시물 삭제
	public int update(BoardVo vo);   																						// 게시물 수정
	public int updateHit(int no);																							// 조회수 증가
	public int getTotalRecord(String keyField, String keyWord, String startDate, String endDate); 							// 총 게시물 수 조회
	public int replyBoard(BoardVo vo);																						// 게시글 답변 등록
	public int replyUpBoard(int topRef, int pos);																				// 답변과 연관된 게시글 위치값 증가
	public List<BoardVo> getReplyBoard(int no);																				// 게시글 관련 답글 조회
	public int[] getReplyCount(List<BoardVo> list);																			// 답변 수 조회
}
