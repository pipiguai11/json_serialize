package com.lhw.serialize;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest
class JsonSerializeApplicationTests {

    public static final String JSON_DATA_PATH = "templates/file/jsonData.txt";

    /**
     * 从template/file目录中读取文件并输出
     */
    @Test
    void contextLoads() {
        try {
            InputStream in = new ClassPathResource(JSON_DATA_PATH).getInputStream();
            byte[] b = new byte[4];
            StringBuffer sb = new StringBuffer();
            int len = -1;
            while ((len = in.read(b)) != -1){
                sb.append(new String(b, StandardCharsets.UTF_8));
            }
            System.out.println(sb);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
