package com.lhw.serialize.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lhw.serialize.message.JsonMessage;
import com.lhw.serialize.model.JsonMessageObject;
import com.lhw.serialize.services.IFastJsonService;
import com.lhw.serialize.util.PrintUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FastjsonServiceImpl implements IFastJsonService {

    @Override
    public void parseJsonArray() {
        //直接解析，然后用JSONArray接收
        JSONArray jsonArray = JSON.parseArray(JsonMessage.JSON_DATA);
        assert jsonArray.size() != 0;

        /**
         * 通过JSONArray的getObject方法直接获取到任意位置的对象，通过指定角标和类对象
         */
        // 结果JsonMessageObject(userName=lhw, age=18, address=gz)
        JsonMessageObject jmo = jsonArray.getObject(0,JsonMessageObject.class);
        PrintUtil.printlnResult("JsonArray.getObject","获取java对象",jmo);


        /**
         * 获取到整个jsonArray中的第一个json字符串对象
         */
        // 结果：{"address":"gz","userName":"lhw","age":18}
        String str1 = jsonArray.getObject(0,String.class);
        PrintUtil.printlnResult("jsonArray.getObject","获取java对象（字符串）",str1);
        // 结果：{"address":"gz","userName":"lhw","age":18}
        String str2 = jsonArray.getString(0);
        PrintUtil.printlnResult("jsonArray.getString","获取字符串对象，等价于上面的写法",str2);


        /**
         * 通过getJsonObject方法获取到Array中的任意一个位置的Object，然后调用JSONObject的方法取值
         */
        // 结果：hw
        JSONObject jsonObject = jsonArray.getJSONObject(1);
        String username = jsonObject.getString("userName");
        PrintUtil.printlnResult("JsonArray.getJsonObject.getString","获取JSONObject并通过该对象获取属性信息",username);


        /**
         * 解析时指定类对象，然后用List接收（推荐使用这种方式，更简便）
         */
        // 结果：[JsonMessageObject(userName=lhw, age=18, address=gz), JsonMessageObject(userName=hw, age=19, address=hz)]
        List<JsonMessageObject> jsonMessageObjects = JSON.parseArray(JsonMessage.JSON_DATA, JsonMessageObject.class);
        PrintUtil.printlnResult("JSON.parseArray","解析json字符串数组，并转成java对象list",jsonMessageObjects);
    }

    /**
     * 简单解析一个json，通过getBoolean方法获取布尔值
     */
    @Override
    public void parseJsonArray2() {
        String json = "{\"boo\":true}";
        String jsons = "[{\"boo1\":true,\"boo2\":false},{\"boo1\":false,\"boo2\":true}]";
        JSONObject temp = JSON.parseObject(json);
        JSONArray temp2 = JSON.parseArray(jsons);
        temp2.forEach(t ->{
            JSONObject jb = (JSONObject)t;
            System.out.println(jb.getBoolean("boo1"));  //true，false
        });
        System.out.println("-------------------------------");
        System.out.println(temp.getBoolean("boo"));  //true
        System.out.println(temp2.getJSONObject(1).getBoolean("boo2"));  //true
    }

    /**
     * 解析json，调用其他方法获取各种不同的类型值
     */
    @Override
    public void parseJsonArray3() {
        System.out.println("-------------------parse array-----------------------");
        List<String> list = Arrays.asList("hanwen", "true", "1", "10086", "2021-01-08", "2.0");
        String json = JSON.toJSONStringWithDateFormat(list,"yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        JSONArray jsonArray = JSON.parseArray(json);
        System.out.println(" String : " + jsonArray.getString(0));
        System.out.println(" boolean : " + jsonArray.getBoolean(1));
        System.out.println(" integer : " + jsonArray.getInteger(2));
        System.out.println(" bigInteger : " + jsonArray.getBigInteger(3));
        System.out.println(" date : " + sdf.format(jsonArray.getDate(4)));
        System.out.println(" double : " + jsonArray.getDouble(5));  //2.0
        System.out.println(" float : " + jsonArray.getFloat(5));  //2.0


        System.out.println("-------------------parse object---------------------");
        String objectJson = "{\"string\":\"lhw\",\"boolean\":true,\"integer\":10," +
                "\"date\":\"2020-01-08\",\"double\":2.1,\"float\":2.2}";
        Object parse = JSON.parse(objectJson);
        JSONObject jsonObject = JSON.parseObject(objectJson);
        System.out.println(parse);
        System.out.println(" String : " + jsonObject.getString("string"));
        System.out.println(" boolean : " + jsonObject.getBoolean("boolean"));
        System.out.println(" integer : " + jsonObject.getInteger("integer"));
        System.out.println(" date : " + sdf.format(jsonObject.getDate("date")));
        System.out.println(" double : " + jsonObject.getDouble("double"));
        System.out.println(" float : " + jsonObject.getFloat("float"));

    }


    @Override
    public void parseJsonObject() {

    }

    @Override
    public void toJsonArray() {
        JSON.toJSONString(JsonMessage.JSON_DATA, SerializerFeature.WriteMapNullValue);
    }

    @Override
    public void toJsonObject() {

    }
}
