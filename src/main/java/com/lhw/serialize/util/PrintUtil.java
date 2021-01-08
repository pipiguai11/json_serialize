package com.lhw.serialize.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintUtil {

    public static void printlnResult(String message, Object o){
        log.info("开始打印【{}】的输出结果",message);
        System.out.println();
        System.out.println(o);
        System.out.println();
    }

}
