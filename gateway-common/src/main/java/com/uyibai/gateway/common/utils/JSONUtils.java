/*
 * ********************************************************************
 *    壹点壹滴互联(北京)教育科技有限公司         http://web.1d1d100.com/
 *
 *          Copyright ©  2018-2019.  All Rights Reserved.
 *
 *       ***************以技术引领幼教行业全面升级***************
 *       ***************用爱和责任推动幼儿教育公平***************
 *
 * ********************************************************************
 */

package com.uyibai.gateway.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于jackson的json工具类
 *
 * @author Huzi.Wang[imhuzi.wh@gmail.com]
 * @version V1.0.0
 * @date 2019-06-06 12:32
 **/
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class JSONUtils {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    /**
     * 线程安全，不影响系统性能 的单例模式
     */
    private static class MapperInstance {
        public static ObjectMapper mapper = new ObjectMapper();

        static {
            //序列化设置
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            //noinspection deprecation
            mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            mapper.registerModule(new JavaTimeModule());
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
    }

    public static ObjectMapper getObjectMaper() {
        return MapperInstance.mapper;
    }

    /**
     * 将 JSON String 反序列化成 T
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T toBean(String jsonString, Class<T> cls) {
        try {
            return getObjectMaper().readValue(jsonString, cls);
        } catch (IOException e) {
            logger.warn("JSONUtils toObject warn :", e);
            return null;
        }
    }

    public static <T> T toBean(byte[] bytes, Class<T> cls) {
        try {
            return getObjectMaper().readValue(bytes, cls);
        } catch (IOException e) {
            logger.warn("JSONUtils toObject warn :", e);
            return null;
        }
    }

    public static <T> T toBean(String jsonString, TypeReference<T> valueTypeRef) {
        try {
            return getObjectMaper().readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            logger.warn("JSONUtils toObject warn :", e);
            return null;
        }
    }

    public static <T> T toBean(String jsonString, Class<?> collectionClazz, Class<?>... elementClazzes) {
        JavaType javaType = getObjectMaper().getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        try {
            return getObjectMaper().readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("JSONUtils toObject warn :", e);
            return null;
        }
    }

    public static <T> T toBean(byte[] content, Class<?> collectionClazz, Class<?>... elementClazzes) {
        JavaType javaType = getObjectMaper().getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        try {
            return getObjectMaper().readValue(content, javaType);
        } catch (IOException e) {
            logger.warn("JSONUtils toObject warn :", e);
            return null;
        }
    }


    public static <T> T toList(String jsonString, Class<?> cls) {
        JavaType javaType = getObjectMaper().getTypeFactory().constructParametricType(ArrayList.class, cls);
        try {
            return getObjectMaper().readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("JSONUtils toObject warn :", e);
            return null;
        }
    }


    public static Map<String, ?> toMap(String content, Class<?> cls) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        return toBean(content, HashMap.class, String.class, cls);
    }


    public static Map<String, byte[]> byteToMap(byte[] content) {
        if (content == null || content.length < 1) {
            return null;
        }
        return toBean(content, HashMap.class, String.class, byte.class);
    }

    public static Map<String, ?> byteToMap(byte[] content, Class<?> cls) {
        if (content == null) {
            return null;
        }
        return toBean(content, String.class, cls);
    }

    public static <T> T byteToList(byte[] bytes, Class<?> cls) {
        if (bytes.length < 1) {
            return null;
        }
        return toBean(bytes, ArrayList.class, cls);
    }

    public static <T> T byteToBean(byte[] bytes, Class<T> cls) {
        if (bytes.length < 1) {
            return null;
        }
        return toBean(bytes, cls);
    }


    public static Map<String, ?> strToMap(String content, Class<?> cls) {
        if (content == null || content.length() < 1) {
            return null;
        }
        return toBean(content, HashMap.class, String.class, cls);
    }

    public static byte[] toBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return getObjectMaper().writeValueAsBytes(obj);
        } catch (IOException e) {
            logger.warn("JSONUtils toBytes warn :", e);
            return null;
        }
    }

    /**
     * 将对象序列化成JSON String
     *
     * @param obj Object
     * @return String
     */
    public static String toStr(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : getObjectMaper().writeValueAsString(obj);
        } catch (IOException e) {
            logger.warn("JSONUtils toString warn :", e);
            return "";
        }
    }


    /**
     * 增加 将 map 转成java类的方法
     *
     * @param map map
     * @param cls Class
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> cls) {
        return getObjectMaper().convertValue(map, cls);
    }


    public static void main(String[] args) throws Exception {

        HashMap<String, Object> kk = new HashMap(4);
        kk.put("kk", 121);
        kk.put("kk1", 1291);

        String res = JSONUtils.toStr(kk);
        System.out.println(res);
        System.out.println(JSONUtils.toMap(res,Object.class));
        System.out.println("-start serialize2 list------------------------------");


    }

}

