package com.shl.crowdfunding.manager.service;

import com.shl.crowdfunding.bean.Advert;
import com.shl.crowdfunding.util.Page;
import com.shl.crowdfunding.vo.Data;

import java.util.Map;

public interface AdvertService {
	public Advert queryAdvert(Map<String, Object> advertMap);

	public Page<Advert> pageQuery(Map<String, Object> advertMap);

	public int queryCount(Map<String, Object> advertMap);

	public int insertAdvert(Advert advert);

	public Advert queryById(Integer id);

	public int updateAdvert(Advert advert);

	public int deleteAdvert(Integer id);

	public int deleteAdverts(Data ds);
}
