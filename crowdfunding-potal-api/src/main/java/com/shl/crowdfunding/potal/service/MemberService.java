package com.shl.crowdfunding.potal.service;

import com.shl.crowdfunding.bean.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member querMemberLogin(Map<String, Object> loginHashMap);

    void updateAcctType(Member loginMember);

    void updateBasicinfo(Member loginMember);

    void updateEmail(Member member);

    void updateAuthstatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);
}
