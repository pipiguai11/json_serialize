package com.lhw.serialize.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhw.serialize.message.JsonMessage;
import com.lhw.serialize.model.JsonMessageObject;
import com.lhw.serialize.services.IFastJsonService;
import com.lhw.serialize.util.PrintUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FastjsonServiceImpl implements IFastJsonService {

    @Override
    public void parseJsonArray() {
        //直接解析，然后用JSONArray接收
        JSONArray jsonArray = JSON.parseArray(JsonMessage.JSON_DATA);
        assert jsonArray.size() != 0;

        //通过JSONArray的getObject方法直接获取到任意位置的对象，通过指定角标和类对象
        // 结果JsonMessageObject(userName=lhw, age=18, address=gz)
        JsonMessageObject jmo = jsonArray.getObject(0,JsonMessageObject.class);
        PrintUtil.printlnResult("JsonArray",jmo);

        //获取到整个jsonArray中的第一个json字符串对象
        // 结果：{"address":"gz","userName":"lhw","age":18}
        String temp = jsonArray.getObject(0,String.class);
        PrintUtil.printlnResult("temp",temp);

        //通过getJsonObject方法获取到Array中的任意一个位置的Object，然后调用JSONObject的方法取值
        // 结果：hw
        JSONObject jsonObject = jsonArray.getJSONObject(1);
        String username = jsonObject.getString("userName");
        PrintUtil.printlnResult("JsonArray getJsonObject getString",username);

        //解析时指定类对象，然后用List接收（推荐使用这种方式，更简便）
        // 结果：[JsonMessageObject(userName=lhw, age=18, address=gz), JsonMessageObject(userName=hw, age=19, address=hz)]
        List<JsonMessageObject> jsonMessageObjects = JSON.parseArray(JsonMessage.JSON_DATA, JsonMessageObject.class);
        PrintUtil.printlnResult("JavaBeanList",jsonMessageObjects);
    }

    @Override
    public void parseJsonObject() {

    }

    @Override
    public void toJsonArray() {

    }

    @Override
    public void toJsonObject() {

    }
}
