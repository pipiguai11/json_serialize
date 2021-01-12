package com.lhw.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lhw.serialize.model.Classroom;
import com.lhw.serialize.model.User;
import com.lhw.serialize.util.FileUtil;
import com.lhw.serialize.util.PrintUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest
class JsonSerializeApplicationTests {

    public static final String JSON_DATA_PATH = "templates/file/jsonData.txt";

    private static Classroom classroom ;


    @Test
    void contextLoads() {
    }

    /**
     * 测试格式化输出json字符串
     */
    @Test
    void testSerializerFeature(){
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
         * beanToArray: 将value值放到一个数组中并输出
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
         * sortField： 根据Key排序输出，也即是根据key的字母序排序后输出
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

    @Test
    void testToJSONByte(){
        init();
        classroom.setMap(null);
        classroom.setPersonList(null);
        byte[] b = JSON.toJSONBytes(classroom,SerializerFeature.WriteMapNullValue);
        StringBuffer sb = new StringBuffer(new String(b));
        System.out.println(sb);

        byte[] b2 = JSON.toJSONBytes(classroom,SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue);
        StringBuffer sb2 = new StringBuffer(new String(b2));
        System.out.println(sb2);

    }

//    @Test
    private String testImgByteToJson() throws IOException {
        String filePath = "templates/temp/1.jpg";
        InputStream in = new ClassPathResource(filePath).getInputStream();
        String suffix = filePath.split("\\.")[1];
        assert in != null;
        byte[] b = new byte[in.available()];
        in.read(b);
        System.out.println("文件字节数组 ： " + Arrays.toString(b));

        String temp = Base64.getEncoder().encodeToString(b);
        System.out.println("temp : " + temp);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("byte", temp);
        jsonObject.put("suffix",suffix);
        System.out.println("JSONObject 对象 ： " + jsonObject.toJSONString());

        return jsonObject.toJSONString();
    }

    /**
     * 解析图片字节数组json
     * @throws IOException
     */
    @Test
    void testDownloadFile() throws IOException {
        String json = this.testImgByteToJson();
        JSONObject jo = JSON.parseObject(json);
        String str = jo.getString("byte");
        String suffix = jo.getString("suffix");
        byte[] b = Base64.getDecoder().decode(str);
        System.out.println("Base64解码byte数组 ： " + Arrays.toString(b));

        String filename = UUID.randomUUID().toString() + "." + suffix;
        String filePath = "E:\\temp\\" + filename;
        File file = new File(filePath);
        OutputStream out = new FileOutputStream(file);
        out.write(b);
        out.flush();
        out.close();
    }


    /**
     * 从template/file目录中读取文件并输出
     */
    private String getJsonString(){
        try {
            InputStream in = new ClassPathResource(JSON_DATA_PATH).getInputStream();
            byte[] b = new byte[4];
            StringBuffer sb = new StringBuffer();
            int len = -1;
            while ((len = in.read(b)) != -1){
                sb.append(new String(b, StandardCharsets.UTF_8));
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
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
