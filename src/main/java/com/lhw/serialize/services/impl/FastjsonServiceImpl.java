package com.lhw.serialize.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.*;
import com.lhw.serialize.message.JsonData;
import com.lhw.serialize.model.Classroom;
import com.lhw.serialize.model.User;
import com.lhw.serialize.services.IFastJsonService;
import com.lhw.serialize.util.FileUtil;
import com.lhw.serialize.util.PrintUtil;
import com.lhw.serialize.util.SerializeUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FastjsonServiceImpl implements IFastJsonService {

    private static Classroom classroom ;

    @Override
    public void parseJsonArray() {
        //直接解析，然后用JSONArray接收
        JSONArray jsonArray = JSON.parseArray(JsonData.JSON_DATA);
        assert jsonArray.size() != 0;

        /**
         * 通过JSONArray的getObject方法直接获取到任意位置的对象，通过指定角标和类对象
         */
        // 结果JsonMessageObject(userName=lhw, age=18, address=gz)
        User jmo = jsonArray.getObject(0, User.class);
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
        List<User> jsonDataObjects = JSON.parseArray(JsonData.JSON_DATA, User.class);
        PrintUtil.printlnResult("JSON.parseArray","解析json字符串数组，并转成java对象list", jsonDataObjects);
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

    }

    /**
     * 解析Json对象
     */
    @Override
    public void parseJsonObject() {
        System.out.println("-------------------parse object---------------------");
        String objectJson = "{\"string\":\"lhw\",\"boolean\":true,\"integer\":10," +
                "\"date\":\"2020-01-08\",\"double\":2.1,\"float\":2.2}";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    /**
     * 将对象转换为json字符串
     */
    @Override
    public void toJsonString() {
        init();
        String temp = JSON.toJSONString(classroom);
        String temp2 = JSON.toJSONString(classroom, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.SortField,SerializerFeature.MapSortField);
        PrintUtil.compareResultPrintln("all SerializerFeature",temp,temp2);

        classroom.setPersonList(null);
        classroom.setMap(null);

        /**
         * BeanToArray: 将value值放到一个数组中并输出
         */
        String json = JSON.toJSONString(classroom);
        String beanToArray = JSON.toJSONString(classroom,SerializerFeature.BeanToArray);
        PrintUtil.compareResultPrintln("beanToArray",json,beanToArray);

        /**
         * DateFormat：日期数据格式化，以下有两种方式，
         *      一种是调用toJSONString方法时传入SerializerFeature.WriteDateUseDateFormat，在此之前需要设置格式（JSON.DEFFAULT_DATE_FORMAT=""）
         *      另一种是直接调用JSON.toJSONStringWithDateFormat，传入序列化对象以及格式化字符串即可。
         */
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        String writeDateUserDateFormat = JSON.toJSONString(classroom,SerializerFeature.WriteDateUseDateFormat);
        String writeDateUserDateFormat2 = JSON.toJSONStringWithDateFormat(classroom,"yyyy-MM-dd");
        PrintUtil.compareResultPrintln("dateFormat",json,writeDateUserDateFormat);
        PrintUtil.compareResultPrintln("dateFormat2",json,writeDateUserDateFormat2);

        /**
         * SortField： 根据Key排序输出，也即是根据key的字母序排序后输出
         */
        String sortField = JSON.toJSONString(classroom,SerializerFeature.SortField);
        PrintUtil.compareResultPrintln("sortField",json,sortField);

        /**
         * WriteMapNullValue：允许输出空值，默认为false（也就是不输出空值）
         */
        String writeMapNullValue = JSON.toJSONString(classroom,SerializerFeature.WriteMapNullValue);
        PrintUtil.compareResultPrintln("writeMapNullValue",json,writeMapNullValue);

        /**
         * WriteNullStringAsEmpty：字符串为空时不输出null，而是输出""
         *      需要搭配SerializerFeature.WriteMapNullValue一起使用
         */
        String writeNullStringAsEmpty = JSON.toJSONString(classroom,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
        PrintUtil.compareResultPrintln("writeNullStringAsEmpty",json,writeNullStringAsEmpty);

        /**
         * WriteNullListAsEmpty： List为空时不输出null，而是输出[]
         *      需要搭配SerializerFeature.WriteMapNullValue一起使用
         */
        String writeNullListAsEmpty = JSON.toJSONString(classroom,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteMapNullValue);
        PrintUtil.compareResultPrintln("writeNullListAsEmpty",json,writeNullListAsEmpty);

        /**
         * PrettyFormat ： 美化json格式，将json以层级结构输出出来
         */
        String prettyFormat = JSON.toJSONString(classroom,SerializerFeature.PrettyFormat);
        PrintUtil.compareResultPrintln("PrettyFormat",json,prettyFormat);

        /**
         * UseSingleQuotes： 输出时使用单引号而不是双引号
         */
        String useSingleQuotes = JSON.toJSONString(classroom, SerializerFeature.UseSingleQuotes);
        PrintUtil.compareResultPrintln("UseSingleQuotes",json,useSingleQuotes);

    }

    /**
     * 将对象转换为json字节数组
     *      效果和上面的转成字符串一样，只是需要额外操作(字节转字符串)，但是数据内容是一致的。
     */
    @Override
    public void toJsonByte() {
        init();
        byte[] b = JSON.toJSONBytes(classroom,SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.SortField,SerializerFeature.MapSortField);
        StringBuffer sb = new StringBuffer(new String(b));
        System.out.println(sb);

        //其他的略

    }

    /**
     * 读取文件并将字节数组封装到JSONObject中
     *      通过json传输文件字节数组，以字符串的格式
     * @return
     */
    @Override
    public String fileToByteObjectJsonString() {
        JSONObject jsonObject = new JSONObject();
        InputStream in = null;
        try {
            String filePath = "templates/temp/1.jpg";
            in = FileUtil.readInputStreamFromTemplates(filePath);
            String suffix = filePath.split("\\.")[1];
            assert in != null;

            //inputStream的available方法可以获取到inputstream的长度，创建字节数组时指定长度
            byte[] b = new byte[in.available()];
            in.read(b);
            System.out.println("文件字节数组 ： " + Arrays.toString(b));

            //对字节进行Base64编码，用于后面获取时解码得到同一个字节数组
            String temp = Base64.getEncoder().encodeToString(b);
            System.out.println("temp : " + temp);

            jsonObject.put("byte", temp);
            jsonObject.put("suffix",suffix);
            System.out.println("JSONObject 对象 ： " + jsonObject.toJSONString());

        }catch (IOException e ){
            e.printStackTrace();
        }finally {
            try {
                if (in != null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return jsonObject.toJSONString();
    }

    /**
     * 获取到一个封装了字节数组的JSONObject对象
     *      解析字符串并得到原始的字节数组
     *      然后对这个字节数组进行操作
     */
    @Override
    public void byteObjectJsonStringHandle() {
        String json = this.fileToByteObjectJsonString();
        JSONObject jo = JSON.parseObject(json);
        String str = jo.getString("byte");
        String suffix = jo.getString("suffix");

        //同样通过Base64解码器对json中获取到的字符串解码得到原始的字节数组
        byte[] b = Base64.getDecoder().decode(str);
        System.out.println("Base64解码byte数组 ： " + Arrays.toString(b));

        //文件下载
        String filename = UUID.randomUUID().toString() + "." + suffix;
        String filePath = "E:\\temp\\" + filename;
        File file = new File(filePath);
        try {
            OutputStream out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void testMySerializeConfig() {
        init();
        classroom.setPersonList(null);
        classroom.setMap(null);
        String result = JSON.toJSONString(classroom, SerializeUtil.globalInstance);
        System.out.println(result);

        String douStr = JSON.toJSONString(2.11111,SerializeUtil.globalInstance);
        System.out.println(douStr);

        String douStr2 = JSON.toJSONString(2.9,SerializeUtil.globalInstance);
        System.out.println(douStr2);

    }

    @Override
    public void testMyFilter(){
        User user1 = new User("lhw",19,"hz");
        User user2 = new User("hw",18,"gz");

        //根据PropertyKey和PropertyValue来判断哪些属性需要序列化（name就是key，value就是value）
        //当apply方法返回true时，表示该key:value需要序列化
        PropertyFilter propertyFilter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                System.out.println("PropertyFilter 根据PropertyName和PropertyValue来判断是否序列化；");
                System.out.println("source-----------:" + object);
                System.out.println("name-----------:" + name);
                System.out.println("value-----------:" + value);
                //属性为username，且值包含字符串“hw”时进行序列化
                if ("userName".equalsIgnoreCase(name)){
                    String temp = (String)value;
                    return temp.contains("hw");
                }
                //属性为age，且值大于18时进行序列化
                if ("age".equals(name)){
                    int age = (int)value;
                    return age > 18;
                }
                //属性为address，且值等于“gz”时进行序列化
                if ("address".equalsIgnoreCase(name)){
                    String address = (String)value;
                    return "gz".equalsIgnoreCase(address);
                }
                return false;
            }
        };

        String str1 = JSON.toJSONString(user1,propertyFilter);
        String str2 = JSON.toJSONString(user2,propertyFilter);
        System.out.println(str1);
        System.out.println(str2);
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");

        //根据key匹配并修改key值
        //返回的字符串即为key的修改结果
        NameFilter nameFilter = new NameFilter() {
            @Override
            public String process(Object object, String name, Object value) {
                if ("username".equalsIgnoreCase(name)){
                    return "my" + name;
                }else if ("age".equalsIgnoreCase(name)){
                    return name + "aaaa";
                }else if ("address".equalsIgnoreCase(name)){
                    return name + "zxc";
                }
                return name;
            }
        };

        String nameFilterStr = JSON.toJSONString(user1,nameFilter);
        System.out.println("原对象 ： " + user1);
        System.out.println("nameFilter：");
        System.out.println(nameFilterStr);
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");

        //根据Key匹配并修改Value值
        //和NameFilter一样，返回的值即为修改的Value值
        ValueFilter valueFilter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if ("username".equalsIgnoreCase(name)){
                    return value + "name";
                }else if ("age".equalsIgnoreCase(name)){
                    return value + "age";
                }else if ("address".equalsIgnoreCase(name)){
                    return value + "add";
                }
                return value;
            }
        };

        String valueFilterStr = JSON.toJSONString(user1,valueFilter);
        System.out.println("原对象 ： " + user1);
        System.out.println("ValueFilter : ");
        System.out.println(valueFilterStr);
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");

        //序列化所有属性之前执行一下操作，如下在序列化之前先将address值添加before后缀
        BeforeFilter beforeFilter = new BeforeFilter() {
            @Override
            public void writeBefore(Object object) {
                System.out.println("object = " + object);
                User u = (User)object;
                System.out.println("user = " + u);
                u.setAddress(u.getAddress() + "before"); //注意：这里是会直接修改原对象的，传过来的是引用地址
            }
        };
        String beforeFilterStr = JSON.toJSONString(user1,beforeFilter);
        System.out.println("BeforeFilterStr : ");
        System.out.println("解析后的字符串： " + beforeFilterStr);  //{"address":"hzbefore","age":19,"userName":"lhw"}，解析前先执行了该beforeFilter
        System.out.println("原对象信息 ： " + user1);  //User(userName=lhw, age=19, address=hzbefore)
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");


        AfterFilter afterFilter = new AfterFilter() {
            @Override
            public void writeAfter(Object object) {
                User u = (User)object;
                System.out.println("user = " + u);
                u.setAddress(u.getAddress() + "after"); //注意：这里是会直接修改原对象的，传过来的是引用地址
            }
        };
        String afterFilterStr = JSON.toJSONString(user2,afterFilter);
        System.out.println("解析后的字符串： " + afterFilterStr);  //{"address":"gz","age":18,"userName":"hw"}，在调用afterFilter之前就解析好了
        System.out.println("原对象信息 ： " + user2);  //User(userName=hw, age=18, address=gzafter)，解析完之后调用修改了值

    }


    private void init(){
        classroom = new Classroom();
        classroom.setClassName("高一一班");
        classroom.setClassAddress("崇德楼406");
        classroom.setDate(new Date());
        classroom.setTestField1("");
        classroom.setTestField2(null);
        classroom.setInTeach(true);
        classroom.setPersonNum(3);
        List<User> users = new ArrayList<>();
        User user1 = new User("lin",18,"广州");
        User user2 = new User("han",19,"惠州");
        User user3 = new User("www",20,"深圳");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        classroom.setPersonList(users);
        Map<String,Object> map = new HashMap<>(16);
        map.put("testMap1","111");
        map.put("testMap2","222");
        map.put("testMap3","333");
        map.put("user1",user1);
        map.put("user2",user2);
        map.put("user3",user3);
        map.put("list",users);
        classroom.setMap(map);
    }

}
