package com.uyibai.gateway.admin.config;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uyibai.gateway.admin.api.model.definition.ArgsValueDefinition;

@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ArgsValueDefinitionTypeHandler extends AbstractJsonTypeHandler<List<ArgsValueDefinition>> {
    private static final Logger log = LoggerFactory.getLogger(JacksonTypeHandler.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Class<List<ArgsValueDefinition>> type;

    public ArgsValueDefinitionTypeHandler(Class<List<ArgsValueDefinition>> type) {
        if (log.isTraceEnabled()) {
            log.trace("JacksonTypeHandler(" + type + ")");
        }

        Assert.notNull(type, "Type argument cannot be null", new Object[0]);
        this.type = type;
    }

    protected List<ArgsValueDefinition> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<ArgsValueDefinition>>() { });
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    protected String toJson(List<ArgsValueDefinition> obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper should not be null", new Object[0]);
        objectMapper = objectMapper;
    }
}
