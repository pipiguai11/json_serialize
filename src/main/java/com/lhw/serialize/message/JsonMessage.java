package com.lhw.serialize.message;

import com.lhw.serialize.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.io.*;

@Slf4j
public class JsonMessage {

    public static String JSON_DATA = "";

    public JsonMessage(){}

    public JsonMessage(String filePath) throws IOException {
        log.info("初始化json数据");
        if (Strings.isBlank(JSON_DATA)){
            JSON_DATA = FileUtil.readStringFromFile(filePath);
        }
    }

}
