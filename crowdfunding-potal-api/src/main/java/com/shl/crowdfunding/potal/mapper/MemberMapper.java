package com.shl.crowdfunding.potal.mapper;


import com.shl.crowdfunding.bean.Member;

import java.util.List;
import java.util.Map;

public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

    Member querMemberLogin(Map<String, Object> loginHashMap);

    void updateAcctType(Member loginMember);

    void updateBasicinfo(Member loginMember);

    void updateEmail(Member member);

    void updateAuthstatus(Member loginMember);

    List<Map<String, Object>> queryCertByMemberid(Integer memberid);
}