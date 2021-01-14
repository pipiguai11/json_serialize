package com.lhw.serialize.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintUtil {

    public static void printlnResult(String methodName, String content , Object o){
        System.out.println();
        System.out.println("调用方法：【" + methodName + "】");
        System.out.println("方法描述：【" + content + "】");
        System.out.println("方法输出： " + o);
        System.out.println();
    }

    public static void compareResultPrintln(String message, Object o1, Object o2){
        System.out.println();
        System.out.println("------------------------------------------------------------------------");
        System.out.println("没有任何的格式化：");
        System.out.println(o1);
        System.out.println("执行【" + message + "】操作");
        System.out.println(o2);
        System.out.println();
    }

}
