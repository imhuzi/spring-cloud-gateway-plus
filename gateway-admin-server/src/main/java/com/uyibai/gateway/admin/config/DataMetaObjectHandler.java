package com.uyibai.gateway.admin.config;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        LocalDateTime time = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, time);
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, time);
        this.strictInsertFill(metaObject, "status", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
    }
}
