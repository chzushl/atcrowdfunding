package com.shl.crowdfunding.vo;

import com.shl.crowdfunding.bean.MemberCert;
import com.shl.crowdfunding.bean.User;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private List<User> userList = new ArrayList<User>();
	private List<User> datas = new ArrayList<User>();

	private List<Integer> ids ;
	
	private List<MemberCert> certimgs = new ArrayList<MemberCert>();

	public List<MemberCert> getCertimgs() {
		return certimgs;
	}

	public void setCertimgs(List<MemberCert> certimgs) {
		this.certimgs = certimgs;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<User> getDatas() {
		return datas;
	}

	public void setDatas(List<User> datas) {
		this.datas = datas;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

/*	public List<MemberCert> getCertimgs() {
		return certimgs;
	}*/

/*	public void setCertimgs(List<MemberCert> certimgs) {
		this.certimgs = certimgs;
	}*/

	

}
