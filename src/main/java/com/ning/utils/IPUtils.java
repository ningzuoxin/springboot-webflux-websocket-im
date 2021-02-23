package com.ning.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPUtils {

    public static void main(String[] args) {
        try {
            // 使用InetAddress.getLocalHost().getHostAddress()打印
            System.out.println("InetAddress.getLocalHost().getHostAddress():" + InetAddress.getLocalHost().getHostAddress());
            System.out.println("打印所有IP列表信息：");
            System.out.println("本机地址：" + IPUtils.getIpAdd());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据网卡获得IP地址
     *
     * @return
     * @throws SocketException
     * @throws UnknownHostException
     */
    public static String getIpAdd() throws SocketException, UnknownHostException {
        String ip = "";
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            String name = intf.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    // 获得IP
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                            if (!"127.0.0.1".equals(ip)) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        }
        return ip;
    }

}

