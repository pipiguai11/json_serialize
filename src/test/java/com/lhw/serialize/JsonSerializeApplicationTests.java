package com.lhw.serialize;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.*;
import com.lhw.serialize.model.Classroom;
import com.lhw.serialize.model.User;
import com.lhw.serialize.model.User2;
import com.lhw.serialize.util.FileUtil;
import com.lhw.serialize.util.PrintUtil;
import com.lhw.serialize.util.SerializeUtil;
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
     * 自定义序列化器
     */
    @Test
    void testMySerialize(){
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

    /**
     * 定制化序列化过滤属性
     */
    @Test
    void testSerializeFilter(){
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
                u.setAddress(u.getAddress() + "before");
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
                u.setAddress(u.getAddress() + "after");
            }
        };
        String afterFilterStr = JSON.toJSONString(user2,afterFilter);
        System.out.println("解析后的字符串： " + afterFilterStr);  //{"address":"gz","age":18,"userName":"hw"}，在调用afterFilter之前就解析好了
        System.out.println("原对象信息 ： " + user2);  //User(userName=hw, age=18, address=gzafter)，解析完之后调用修改了值


    }

    /**
     * fastjson处理超大json文本
     *      做了性能对比，对比用处理超大json文本的方式输出和常用的方式输出
     *      这里用的文件不是超大json文本，而是小文件，因此常用方式更省时，还没测试超大文本
     * @throws IOException
     */
    @Test
    void testHugeFile() throws IOException {
        JSONWriter jsonWriter = new JSONWriter(new FileWriter("E:\\temp\\" + new Random().nextInt(100) + ".txt"));
        JSONReader jsonReader = new JSONReader(new FileReader("D:\\ideaWorkSpace\\myDemo\\json_serialize\\src\\main\\resources\\templates\\file\\jsonData.txt"));

        //处理超大json数组
        long start = System.currentTimeMillis();
        jsonReader.startArray();
        jsonWriter.startArray();
        while (jsonReader.hasNext()){
            User u = jsonReader.readObject(User.class);
            jsonWriter.writeValue(u);
        }
        jsonWriter.flush();
        jsonWriter.endArray();
        jsonReader.endArray();
        System.out.println("处理超大json文本用时 ：" + (System.currentTimeMillis()-start));

        //性能对比
        String json = getJsonString();
        FileOutputStream fos = new FileOutputStream("E:\\temp\\" + new Random().nextInt(100) + ".txt");
        long start2 = System.currentTimeMillis();
        List<User> users = JSON.parseArray(json,User.class);
        String result = JSON.toJSONString(users);
        byte[] b = result.getBytes();
        fos.write(b);
        fos.flush();
        System.out.println("常用用时 ：" + (System.currentTimeMillis()-start2));

        //处理超大json对象
//        jsonReader.startObject();
//        jsonWriter.startObject();
//        while (jsonReader.hasNext()){
//            String key = jsonReader.readString();
//            User u = jsonReader.readObject(User.class);
//            jsonWriter.writeKey(key);
//            jsonWriter.writeValue(u);
//        }
//        jsonWriter.flush();
//        jsonWriter.endObject();
//        jsonReader.endObject();

        jsonWriter.close();
        jsonReader.close();
        fos.close();

    }

    @Test
    void testJSONFieldAnnotation(){
        String json = this.getJsonString();
        List<User2> user2 = JSON.parseArray(json,User2.class);
        System.out.println(user2);  //[User2(newName=lhw, newAge=18, newAddress=gz), User2(newName=hw, newAge=19, newAddress=hz)]

        //解析后的属性顺序根据User2类中定义的Ordinal顺序排序
        String str = JSON.toJSONString(user2);
        System.out.println(str);  //[{"age":18,"userName":"lhw"},{"age":19,"userName":"hw"}]

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

    /**
     * 初始化数据
     */
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
