package com.lhw.serialize.config;

import com.lhw.serialize.message.JsonMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JsonDataLoad {

    @Value("${filePath}")
    private String filePath ;

    private static final String TEMPLATE_FILE_PATH = "templates/file/jsonData.txt";

    @Bean
    public JsonMessage jsonMessage() throws IOException {
        return new JsonMessage(TEMPLATE_FILE_PATH);
    }

}
