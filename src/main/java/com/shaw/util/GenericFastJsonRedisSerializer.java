package com.shaw.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 *
 * @author shaw
 * @date 2016/10/29 0029
 * 自定义
 * fastJson序列化工具。json序列化相比 jdk 序列化稍慢，但是存储空间少。
 */
public class GenericFastJsonRedisSerializer implements RedisSerializer<Object> {
    static final byte[] EMPTY_ARRAY = new byte[0];
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final ParserConfig AUTO_TYPE_PARSE_CONFIG;

    static {
        AUTO_TYPE_PARSE_CONFIG = new ParserConfig();
        AUTO_TYPE_PARSE_CONFIG.setAutoTypeSupport(true);
    }


    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return JSON.parseObject(new String(bytes, DEFAULT_CHARSET), Object.class, AUTO_TYPE_PARSE_CONFIG);
    }
}