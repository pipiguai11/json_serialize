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

}
