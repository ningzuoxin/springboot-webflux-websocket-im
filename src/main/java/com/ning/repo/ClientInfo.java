package com.ning.repo;

import com.ning.handler.IMSession;

public class ClientInfo {

    // 设备编号
    public String deviceId;
    // 终端类型 1 安卓 2 IOS
    public String terminal;
    // 客户号
    public String customId;
    // 手机号
    public String phoneId;
    // 连接实例
    public IMSession session;

}
