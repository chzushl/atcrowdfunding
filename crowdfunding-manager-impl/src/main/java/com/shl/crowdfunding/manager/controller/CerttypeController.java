package com.shl.crowdfunding.manager.controller;

import com.shl.crowdfunding.bean.Cert;
import com.shl.crowdfunding.manager.service.CertService;
import com.shl.crowdfunding.manager.service.CerttypeService;
import com.shl.crowdfunding.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/certtype")
public class CerttypeController {
    @Autowired
    private CerttypeService certtypecService;

    @Autowired
    private CertService certService;

    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        List<Cert> allCert = certService.queryAllCert();
        map.put("allCert", allCert);

        List<Map<String, Object>> certAccttype = certtypecService.queryCertAccttype();
        map.put("certAccttype", certAccttype);
        return "certtype/index";
    }

    @ResponseBody
    @RequestMapping("/insertAcctTypeCert")
    public Object insertAcctTypeCert(Integer certid, String accttype) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("certid", certid);
            paramMap.put("accttype", accttype);
            int count = certtypecService.insertAcctTypeCert(paramMap);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("/deleteAcctTypeCert")
    public Object deleteAcctTypeCert( Integer certid, String accttype ) {
        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("certid", certid);
            paramMap.put("accttype", accttype);
            int count = certtypecService.deleteAcctTypeCert(paramMap);
            result.setSuccess(count==1);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
}