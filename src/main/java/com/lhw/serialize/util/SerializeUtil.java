package com.lhw.serialize.util;

import com.alibaba.fastjson.serializer.*;
import com.lhw.serialize.model.Classroom;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;

public class SerializeUtil {

    public static SerializeConfig globalInstance = new SerializeConfig();

    static {
        globalInstance.put(Double.class, new MyDoubleSerializer(new DecimalFormat("0.00")));
        globalInstance.put(Classroom.class,new MyClassroomSerializer());
    }

    public static class MyDoubleSerializer extends DoubleSerializer{

        private DecimalFormat decimalFormat;

        public MyDoubleSerializer(DecimalFormat decimalFormat){
            this.decimalFormat = decimalFormat;
        }

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features){
            SerializeWriter out = serializer.out;
            if (object == null){
                out.writeNull(SerializerFeature.WriteNullNumberAsZero);
                return;
            }
            double result = (Double) object;
            if (Double.isNaN(result) || Double.isInfinite(result)){
                out.writeNull();
            }else {
                if (decimalFormat != null){
                    String doubleFormat = decimalFormat.format(result);
                    out.write("\"" + doubleFormat + "\"");
                }else {
                    out.writeDouble(result,true);
                }
            }
        }
    }

    public static class MyClassroomSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            SerializeWriter out = serializer.out;
            if (object == null){
                out.writeNull(SerializerFeature.WriteMapNullValue);
                return;
            }

            Classroom classroom = (Classroom)object;
            out.write("{");
            writeValue(out,"className",classroom.getClassName());
            out.write(",");
            writeValue(out,"classAddress",classroom.getClassAddress());
            out.write(",");
            writeValue(out,"testField1",classroom.getTestField1());
            out.write(",");
            writeValue(out,"testField2",classroom.getTestField2());
            out.write(",");
            writeValue(out,"personNum",classroom.getPersonNum());
            out.write("}");
        }

        public void writeKey(SerializeWriter out, String key){
            out.write("\"" + key + "\"");
            out.write(":");
        }

        public void writeValue(SerializeWriter out,String key, Object o){
            if (o == null){
                writeKey(out,key);
                out.writeNull();
                return;
            } else if (o instanceof Integer) {
                writeKey(out,key);
                out.writeInt((Integer)o);
            } else if (o instanceof String) {
                writeKey(out,key);
                out.writeString((String)o);
            } else if (o instanceof Long) {
                writeKey(out,key);
                out.writeLong((Long)o);
            } else if (o instanceof Double) {
                writeKey(out,key);
                out.writeDouble((Double)o,true);
            } else if (o instanceof Float) {
                writeKey(out,key);
                out.writeFloat((Float)o,true);
            } else {
               throw new RuntimeException("no support value : " + o);
            }
        }
    }

}
