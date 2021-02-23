package com.ning.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthModel implements Serializable {

    // 设备号
    private String deviceId;

    // 认证码
    private String authorization;

}
