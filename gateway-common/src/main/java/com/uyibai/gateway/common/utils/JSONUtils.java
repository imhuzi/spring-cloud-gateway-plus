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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
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
    private static Logger logger = LoggerFactory.getLogger(JSONUtils.class);

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
            logger.error("JSONSeriallizerUtil toObject error :", e);
            return null;
        }
    }

    public static <T> T toBean(byte[] bytes, Class<T> cls) {
        try {
            return getObjectMaper().readValue(bytes, cls);
        } catch (IOException e) {
            logger.error("JSONSeriallizerUtil toObject error :", e);
            return null;
        }
    }

    public static <T> T toBean(String jsonString, TypeReference<T> valueTypeRef) {
        try {
            return getObjectMaper().readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            logger.error("JSONSeriallizerUtil toObject error :", e);
            return null;
        }
    }


    public static Map<String, Object> unserializeBytes(String content, Class<Map> mapClass) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        return toBean(content, HashMap.class);
    }


    public static Map<String, byte[]> unserializeBytes(byte[] content) {
        if (content == null || content.length < 1) {
            return null;
        }
        return toBean(content, HashMap.class);
    }

    public static Map<String, Object> unserializeToMap(byte[] content) {
        if (content == null) {
            return null;
        }
        Map map = toBean(content, Map.class);
        return map;
    }

    public static ArrayList unserializeToList(byte[] bytes) {
        if (bytes.length < 1) {
            return null;
        }
        return toBean(bytes, ArrayList.class);
    }


    public static Map<String, Object> unserialize(String content) {
        if (content == null || content.length() < 1) {
            return null;
        }
        return toBean(content, HashMap.class);
    }


    public static String serialize(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        return toString(map);
    }

    public static String serialize(Object object) {
        if (object == null) {
            return null;
        }

        return toString(object);
    }


    public static String serialize(List<String> list) {
        if (list == null) {
            return null;
        }

        return toString(list);
    }

    public static byte[] toBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return getObjectMaper().writeValueAsBytes(obj);
        } catch (IOException e) {
            logger.error("JSONSeriallizerUtil toBytes error :", e);
            return null;
        }
    }

    /**
     * 将对象序列化成JSON String
     *
     * @param obj Object
     * @return String
     * @throws IOException
     */
    public static String toString(Object obj) {

        try {
            return getObjectMaper().writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("JSONSeriallizerUtil toString error :", e);
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

        HashMap kk = new HashMap(4);
        kk.put("kk", 121);
        kk.put("kk1", 1291);

        String res = JSONUtils.serialize(kk);
        System.out.println(res);
//        RedisLink redisLink = new RedisLink("192.168.1.231",6379,"redis");
//        int size = 20;
////        ArrayList data = new ArrayList(size);
//        User employee = new User();
//        employee.setUid(UUID.randomUUID().toString());
//        employee.setUsername(UUID.randomUUID().toString());
//        employee.setHeadurl(UUID.randomUUID().toString());
//        employee.setCreatetime(UUID.randomUUID().toString());
//        employee.setFiletoken(UUID.randomUUID().toString());
//        employee.setIs_recommend(UUID.randomUUID().toString());
//        employee.setLastlogintime(UUID.randomUUID().toString());
//        employee.setMatchphone(UUID.randomUUID().toString());
//        for (int i = 0; i < size; i++) {
//            User employee = new User();
//            employee.setUid(UUID.randomUUID().toString());
//            employee.setUsername(UUID.randomUUID().toString());
//            employee.setHeadurl(UUID.randomUUID().toString());
//            employee.setCreatetime(UUID.randomUUID().toString());
//            employee.setFiletoken(UUID.randomUUID().toString());
//            employee.setIs_recommend(UUID.randomUUID().toString());
//            employee.setLastlogintime(UUID.randomUUID().toString());
//            employee.setMatchphone(UUID.randomUUID().toString());
//            data.add(employee);
//        }
//
//        User employee = new User();
//        employee.setUid(UUID.randomUUID().toString());
//        employee.setUsername(UUID.randomUUID().toString());
//        employee.setHeadurl(UUID.randomUUID().toString());
//        employee.setCreatetime(UUID.randomUUID().toString());
//        employee.setFiletoken(UUID.randomUUID().toString());
//        employee.setIs_recommend(UUID.randomUUID().toString());
//        employee.setLastlogintime(UUID.randomUUID().toString());
//        employee.setMatchphone(UUID.randomUUID().toString());
//
//
//        String str = JSONUtils.toJsonString(data);
//        System.out.println(str);
//
//        System.out.println("反序列化java object");
//        ArrayList d = JSONUtils.toObject(str, ArrayList.class);
//        System.out.println(d);
//
//        JSONUtils.toJsonString(employee);
        System.out.println("-start serialize2 list------------------------------");
//        List<String> strList = new ArrayList<>();
//        strList.add("kkkk");
//        strList.add("zzzz");
//        strList.add("4444");
//        strList.add("kkkk");
//        String str2 = serialize2(strList);
//        System.out.println("serialize2:" + serialize2(strList));
//        System.out.println("unserialize2" + unserialize2(str2.getBytes()));
//        System.out.println("-end serialize2 list------------------------------");

//        System.out.println("-start serializemap------------------------------");
//        Map<String, Object> objectMap = new HashedMap();
//        objectMap.put("u1", new User());
//        objectMap.put("u2", employee);
//        System.out.println("serializeMap:" + serializemap(objectMap).length);
//        Map<String, byte[]> bb = unserializeBytes(serializemap(objectMap));
//        System.out.println(bb.get("u2"));
//        System.out.println("-end serializemap------------------------------");

//        System.out.println("-start serialize obj------------------------------");
//        byte[] userBytes = serialize(employee);
//        System.out.println(userBytes.length);
//        User user1 = unserializeBytes(userBytes,User.class);
//        System.out.println(employee.getUid());
//        System.out.println("-end serialize obj------------------------------");

//        System.out.println("-start serialize Map------------------------------");
//        String strMap = serialize(objectMap);
//        System.out.println(userBytes.length);
//        Map str1Map = unserializeBytes(strMap);
//        System.out.println(str1Map);
//        System.out.println("-end serialize Map------------------------------");

//        String rStr = redisLink.getJedis().get("effectlist:ios:iPhone6,2:10.0.2:0.12.140.320");
//        Map m = PHPSerializerOldUtil.unserializeToMap(rStr.getBytes());
//        System.out.println(m);
//        String str2 = serialize(m);
//        System.out.println(str2);


    }

}

