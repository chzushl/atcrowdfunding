package com.shl.crowdfunding.manager.service;

import com.shl.crowdfunding.bean.Cert;
import com.shl.crowdfunding.bean.MemberCert;
import com.shl.crowdfunding.util.Page;
import com.shl.crowdfunding.vo.Data;

import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Title: CertService.java<／p>
 * <p>Description: 资质模块的业务接口<／p>
 * <p>Copyright: Copyright (c) 2017<／p>
 * <p>Company: Atguigu<／p>
 * @author admin
 * @date 2017年8月1日
 * @version 1.0
 */
public interface CertService {
	public Cert queryCert(Map<String, Object> paramMap);

	public Page<Cert> pageQuery(Map<String, Object> paramMap);

	public int queryCount(Map<String, Object> paramMap);

	public void insertCert(Cert cert);

	public Cert queryById(Integer id);

	public int updateCert(Cert cert);

	public int deleteCert(Integer id);

	public int deleteCerts(Data ds);

	public List<Cert> queryCertByAccttype(String accttype);

	public List<Cert> queryAllCert();

	public List<Map<String, Object>> queryAllAccttypeCert();

	public int insertAccttypeCert(Map<String, Object> map);

	public int deleteAccttypeCert(Map<String, Object> map);

    void saveMemberCert(List<MemberCert> certimgs);
}
