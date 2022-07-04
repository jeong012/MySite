package com.kosta.lsj.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kosta.lsj.dao.BoardDao;
import com.kosta.lsj.dao.BoardDaoImpl;
import com.kosta.lsj.util.WebUtil;
import com.kosta.lsj.vo.BoardVo;
import com.kosta.lsj.vo.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String actionName = request.getParameter("a");
		if (actionName != null) {
			actionName = actionName.trim();
		}
		System.out.println("board:" + actionName);

		if ("list".equals(actionName)) {
			BoardDao dao = new BoardDaoImpl();

			String keyField = request.getParameter("keyField");
			if (keyField == null) { keyField = ""; }
			else { keyField = keyField.trim(); }

			String keyWord = request.getParameter("keyWord");
			if (keyWord == null) { keyWord = ""; } 
			else { keyWord = keyWord.trim(); }

			String startDate = request.getParameter("startDate");
			if (startDate == null) { startDate = ""; } 
			else { startDate = startDate.trim(); }

			String endDate = request.getParameter("endDate");
			if (endDate == null) { endDate = ""; } 
			else { endDate = endDate.trim(); }

			String nowPageS = request.getParameter("nowPage");
			if (nowPageS == null) { nowPageS = "1"; } 
			else { nowPageS = nowPageS.trim(); }
			int nowPageI = Integer.parseInt(nowPageS);

			int start = (nowPageI * 10) - 10;
			int end = 10;

			String nowBlockS = request.getParameter("nowBlock");
			if (nowBlockS == null) { nowBlockS = "1"; } 
			else { nowBlockS = nowBlockS.trim(); }
			int nowBlockI = Integer.parseInt(nowBlockS);

			// 리스트 가져오기
			List<BoardVo> list = dao.getList(keyField, keyWord, startDate, endDate, start, end);

			// 총 게시물 수 조회
			int totalRecord = dao.getTotalRecord(keyField, keyWord, startDate, endDate);

			// 답변 수 가져오기
			int[] replyCountList = dao.getReplyCount(list);

			// 리스트 화면에 보내기
			request.setAttribute("list", list);
			request.setAttribute("keyField", keyField);
			request.setAttribute("keyWord", keyWord);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("totalRecord", totalRecord);
			request.setAttribute("nowPage", nowPageI);
			request.setAttribute("start", start);
			request.setAttribute("end", end);
			request.setAttribute("nowBlockI", nowBlockI);
			request.setAttribute("replyCountList", replyCountList);
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");

		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no").trim());
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 어디에서 왔는지 구분하기 위함
			String flag = request.getParameter("flag");
			if(flag == null) { dao.updateHit(no); }

			String keyField = request.getParameter("keyField");
			if (keyField == null) { keyField = ""; }
			else { keyField = keyField.trim(); }

			String keyWord = request.getParameter("keyWord");
			if (keyWord == null) { keyWord = ""; } 
			else { keyWord = keyWord.trim(); }

			String startDate = request.getParameter("startDate");
			if (startDate == null) { startDate = ""; } 
			else { startDate = startDate.trim(); }

			String endDate = request.getParameter("endDate");
			if (endDate == null) { endDate = ""; } 
			else { endDate = endDate.trim(); }

			String nowPageS = request.getParameter("nowPage");
			if (nowPageS == null) { nowPageS = "1"; } 
			else { nowPageS = nowPageS.trim(); }
			int nowPageI = Integer.parseInt(nowPageS);

			List<BoardVo> replyList = dao.getReplyBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			request.setAttribute("replyList", replyList);
			request.setAttribute("nowPage", nowPageI);
			request.setAttribute("keyField", keyField);
			request.setAttribute("keyWord", keyWord);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");

		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no").trim());
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			String keyField = request.getParameter("keyField");
			if (keyField == null) { keyField = ""; }
			else { keyField = keyField.trim(); }

			String keyWord = request.getParameter("keyWord");
			if (keyWord == null) { keyWord = ""; } 
			else { keyWord = keyWord.trim(); }

			String startDate = request.getParameter("startDate");
			if (startDate == null) { startDate = ""; } 
			else { startDate = startDate.trim(); }

			String endDate = request.getParameter("endDate");
			if (endDate == null) { endDate = ""; } 
			else { endDate = endDate.trim(); }

			String nowPageS = request.getParameter("nowPage");
			if (nowPageS == null) { nowPageS = "1"; } 
			else { nowPageS = nowPageS.trim(); }
			int nowPageI = Integer.parseInt(nowPageS);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			request.setAttribute("nowPage", nowPageI);
			request.setAttribute("keyField", keyField);
			request.setAttribute("keyWord", keyWord);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");

		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no").trim());

			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();

			dao.update(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			UserVo authUser = getAuthUser(request);

			int userNo = authUser.getNo();

			String title = request.getParameter("title");
			String content = request.getParameter("content");

			BoardVo vo = new BoardVo(title, content, userNo);
			BoardDao dao = new BoardDaoImpl();
			dao.insert(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no").trim());
			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");

		} else if ("reply".equals(actionName)) {
			UserVo authUser = getAuthUser(request);
			int userNo = authUser.getNo();

			int no = Integer.parseInt(request.getParameter("no").trim());
			
			BoardDao dao = new BoardDaoImpl(); 
			BoardVo boardVo = dao.getBoard(no);
			
			String keyField = request.getParameter("keyField");
			if (keyField == null) { keyField = ""; } 
			else { keyField = keyField.trim(); }

			String keyWord = request.getParameter("keyWord");
			if (keyWord == null) { keyWord = ""; }
			else { keyWord = keyWord.trim(); }

			String startDate = request.getParameter("startDate");
			if (startDate == null) { startDate = ""; } 
			else { startDate = startDate.trim(); }

			String endDate = request.getParameter("endDate");
			if (endDate == null) { endDate = ""; } 
			else { endDate = endDate.trim(); }

			String nowPageS = request.getParameter("nowPage");
			if (nowPageS == null) { nowPageS = "1"; }
			else { nowPageS = nowPageS.trim(); }
			int nowPageI = Integer.parseInt(nowPageS);

			String title = "답변 : " + boardVo.getTitle();
			String content = request.getParameter("content");
			int pos = boardVo.getPos();
			int topRef = boardVo.getTopRef();
			int ref = boardVo.getNo();
			int depth = boardVo.getDepth();

			BoardVo vo = new BoardVo(title, content, userNo, pos, topRef, ref, depth);
			dao.replyUpBoard(topRef, pos);
			dao.replyBoard(vo);

			request.setAttribute("flag", "reply");
			request.setAttribute("nowPage", nowPageI);
			request.setAttribute("keyField", keyField);
			request.setAttribute("keyWord", keyWord);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			WebUtil.redirect(request, response, "/mysite/board?a=read&no="+boardVo.getTopRef());

		} else {
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}
