package com.lhw.serialize.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JsonMessageObject implements Serializable {

    private String userName;
    private Integer age;
    private String address;

}
