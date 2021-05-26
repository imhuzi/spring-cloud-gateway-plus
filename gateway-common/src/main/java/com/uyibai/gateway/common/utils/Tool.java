/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uyibai.gateway.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Tool
 */
public class Tool {

    public static String getInterface(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                service = service.substring(i + 1);
            }
            i = service.lastIndexOf(':');
            if (i >= 0) {
                service = service.substring(0, i);
            }
        }
        return service;
    }

    public static String getGroup(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                return service.substring(0, i);
            }
        }
        return null;
    }

    public static String getVersion(String service) {
        if (service != null && service.length() > 0) {
            int i = service.lastIndexOf(':');
            if (i >= 0) {
                return service.substring(i + 1);
            }
        }
        return null;
    }

    /**
     * 获取 接口 的 短名称， 如 com.alibaba.cloud.dubbo.service.DubboMetadataService
     * <p>
     * 返回 DubboMetadataService
     *
     * @param service
     * @return
     */
    public static String getInterfaceShortName(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                service = service.substring(i + 1);
            }
            i = service.lastIndexOf(':');
            if (i >= 0) {
                service = service.substring(0, i);
            }

            int k = service.lastIndexOf('.');
            if (k >= 0) {
                service = service.substring(k + 1);
            }
        }
        return service;
    }

    /**
     * 将 service 转为 path, 如 DubboMetadataService,/DubboMetadata
     *
     * @param appUriPrefix
     * @param routeUriPrefix
     * @param servicePath
     * @return
     */
    public static String getPath(String appUriPrefix, String routeUriPrefix, String servicePath) {
        if (StringUtils.isNotBlank(routeUriPrefix)) {
            return appUriPrefix + routeUriPrefix + "/*";
        }

        if (servicePath != null && servicePath.length() > 0) {
            int i = servicePath.lastIndexOf('.');
            String path = "";
            if (i >= 0) {
                path = servicePath.substring(i + 1).replaceAll("Service", "");
                path = "/" + path;
            } else {
                path = servicePath;
            }
            return appUriPrefix + path + "/*";
        }
        return null;
    }


}
