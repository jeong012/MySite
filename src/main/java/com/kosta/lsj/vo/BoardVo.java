package com.kosta.lsj.vo;

public class BoardVo {

	private int no;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	private String userName;
	private int pos;
	private int ref;
	private int topRef;
	private int depth;

	public BoardVo() {
	}
	
	public BoardVo(int no, String title, String content) {
		this.no = no;		
		this.title = title;
		this.content = content;
	}
	
	public BoardVo(String title, String content, int userNo) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}

	public BoardVo(String title, String content, int userNo, int topRef, int ref, int depth) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
		this.topRef = topRef;
		this.ref = ref;
		this.depth = depth;
	}

	public BoardVo(String title, String content, int userNo, int pos, int topRef, int ref, int depth) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
		this.pos = pos;
		this.topRef = topRef;
		this.ref = ref;
		this.depth = depth;
	}
	
	public BoardVo(int no, String title, int hit, String regDate, int userNo, String userName) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
	}
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, int pos, int topRef, int ref, int depth, String userName) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
		this.pos = pos;
		this.topRef = topRef;
		this.ref = ref;
		this.depth = depth;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getTopRef() {
		return topRef;
	}

	public void setTopRef(int topRef) {
		this.topRef = topRef;
	}

	public int getRef() {
		return ref;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", userNo=" + userNo + ", userName=" + userName + ", pos=" + pos + ", ref=" + ref
				+ ", topRef=" + topRef + ", depth=" + depth + "]";
	}

}
