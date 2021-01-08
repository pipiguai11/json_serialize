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

    @GetMapping("/parse/object")
    public String parseObject(){
        fastJsonService.parseJsonArray3();
        return "success";
    }

}
