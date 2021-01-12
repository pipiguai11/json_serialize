package com.lhw.serialize.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Classroom implements Serializable {

    private String className;
    private String classAddress;
    private String testField1;
    private String testField2;
    private int personNum;
    private boolean inTeach;
    private Date date;
    private List<User> personList;
    private Map<String,Object> map;

}
