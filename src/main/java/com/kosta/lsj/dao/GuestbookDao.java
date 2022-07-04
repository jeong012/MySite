package com.kosta.lsj.dao;

import java.util.List;

import com.kosta.lsj.vo.GuestbookVo;

public interface GuestbookDao {
  
	public List<GuestbookVo> getList();

	public int insert(GuestbookVo vo);

	public int delete(GuestbookVo vo);

}
