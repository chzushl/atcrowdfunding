package com.shl.crowdfunding.listener.activity;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class YesListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        System.out.println("YesListener执行了....");
    }
}
