package com.lhw.serialize.message;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JsonMessage {

    public static String JSON_DATA = "";

    private Logger log = LoggerFactory.getLogger(JsonMessage.class);

    public JsonMessage(String filePath) throws IOException {
        log.info("初始化json数据");
        if (Strings.isBlank(JSON_DATA)){
            JSON_DATA = readJsonStringFromFile(filePath);
        }
    }

    private String readJsonStringFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            throw new RuntimeException("文件不存在，请检查");
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            int len = -1;
            byte[] b = new byte[4];
            StringBuffer sb = new StringBuffer();
            while ((len = in.read(b)) != -1){
                sb.append(new String(b));
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (in != null){
                in.close();
            }
        }
        return "";
    }

}
