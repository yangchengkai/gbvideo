package com.yck.gbvideo.sip;

import org.springframework.stereotype.Component;

import javax.sip.*;

/**
 * @author YangChengKai
 * @create 2021/12/28 15:36
 * sip监听器 实现
 */
@Component
public class SipProcessor implements SipListener {

    @Override
    public void processRequest(RequestEvent requestEvent) {

    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {

    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {

    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {

    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {

    }
}
