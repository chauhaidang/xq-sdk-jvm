package com.xq;

import org.apache.commons.lang3.StringUtils;

public class PrintService {
    public void print(MessageModel model) {
        String msg = StringUtils.trim(model.getMessage());
        System.out.println(msg);
    }
}