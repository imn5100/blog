package com.shaw.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesUtil {

    /**
     * 获取messages.properties里的key对应的value值
     */
    public PropertiesUtil() {
        init();
    }

    static CompositeConfiguration comp_config;

    /**
     * 这么做能保证config文件只在此类加载的时候读取一遍
     * 把init方法放开是为了当配置文件修改后，需要主动重新加载配置文件（有文件的情况却不重启web容器）
     */
    public static void init() {
        comp_config = new CompositeConfiguration();
        try {
            comp_config.addConfiguration(new PropertiesConfiguration("config.properties"));

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getConfiguration() {
        if (comp_config == null) {
            init();
        }
        return comp_config;
    }

}
