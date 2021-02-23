package com.ning.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

public class PropertiesUtil {

    public static Properties getProperties(String location) {
        Properties props = null;
        try {
            props = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(location), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

}
