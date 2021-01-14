package com.lhw.serialize.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class User2 {

    @JSONField(name = "userName",ordinal = 2)
    private String newName;
    @JSONField(name = "age",ordinal = 1)
    private int newAge;
    @JSONField(name = "address",ordinal = 3,serialize = false)
    private String newAddress;

}
