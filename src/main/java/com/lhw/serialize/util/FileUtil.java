package com.lhw.serialize.util;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    /**
     * 从文件中读取json字符串
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readStringFromFile(String filePath) throws IOException {
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

    /**
     * 从templates/file文件夹中读取文件
     * @param path
     * @return
     */
    public static String readStringFromTemplates(String path){
        try {
            InputStream in = new ClassPathResource(path).getInputStream();
            byte[] b = new byte[4];
            StringBuffer sb = new StringBuffer();
            int len = -1;
            while ((len = in.read(b)) != -1){
                sb.append(new String(b, StandardCharsets.UTF_8));
            }
            in.close();
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 从templates/temp目录中读取文件，并返回流
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream readInputStreamFromTemplates(String path) throws IOException {
        return new ClassPathResource(path).getInputStream();
    }

}
