package com.shl.crowdfunding.potal.service.impl;

import com.shl.crowdfunding.bean.Member;
import com.shl.crowdfunding.potal.mapper.MemberMapper;
import com.shl.crowdfunding.potal.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member querMemberLogin(Map<String, Object> loginHashMap) {
        Member member = memberMapper.querMemberLogin(loginHashMap);
        if(member==null){
            throw new RuntimeException("账号密码错误");
        }
        return member;
    }

    @Override
    public void updateAcctType(Member loginMember) {
        memberMapper.updateAcctType(loginMember);
    }

    @Override
    public void updateBasicinfo(Member loginMember) {
        memberMapper.updateBasicinfo(loginMember);
    }

    @Override
    public void updateEmail(Member member) {
        memberMapper.updateEmail(member);
    }

    @Override
    public void updateAuthstatus(Member loginMember) {
        memberMapper.updateAuthstatus(loginMember);
    }

    @Override
    public Member getMemberById(Integer memberid) {
        return memberMapper.selectByPrimaryKey(memberid);
    }

    @Override
    public List<Map<String, Object>> queryCertByMemberid(Integer memberid) {
        return memberMapper.queryCertByMemberid(memberid);
    }
}
