package com.lhw.serialize.controller;

import com.lhw.serialize.services.IFastJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FastjsonController {

    @Autowired
    private IFastJsonService fastJsonService;

    @GetMapping("/parse/array")
    public String parseArray(){
        fastJsonService.parseJsonArray();
        return "success";
    }

    @GetMapping("/parse/array/v2")
    public String parseArrayV2(){
        fastJsonService.parseJsonArray2();
        return "success";
    }

    @GetMapping("/parse/array/v3")
    public String parseArrayV3(){
        fastJsonService.parseJsonArray3();
        return "success";
    }

    @GetMapping("parse/object")
    public String parseObject(){
        fastJsonService.parseJsonObject();
        return "success";
    }

    @GetMapping("toString/json")
    public String toJsonString(){
        fastJsonService.toJsonString();
        return "success";
    }

    @GetMapping("toByte/json")
    public String toJsonByte(){
        fastJsonService.toJsonByte();
        return "success";
    }

    @GetMapping("download/file/byte")
    public String downloadFile(){
        fastJsonService.byteObjectJsonStringHandle();
        return "success";
    }

}
