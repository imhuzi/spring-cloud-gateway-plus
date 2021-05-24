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

/**
 * The type Param check utils.
 *
 * @author xiaoyu
 */
public class ParamCheckUtils {
    
    /**
     * Dubbo body is empty boolean.
     *
     * @param body the body
     * @return the boolean
     */
    public static boolean dubboBodyIsEmpty(final String body) {
        return null == body || "".equals(body) || "{}".equals(body) || "null".equals(body);
    }
}
