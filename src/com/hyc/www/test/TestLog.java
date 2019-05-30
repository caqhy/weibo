/*
 * Copyright (c) 2019.  黄钰朝
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyc.www.test;


import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于测试日志打印
 * @date 2019-05-02 17:12
 */
public class TestLog {
    public static void main(String[] args) {
        log("日志信息");

    }

    public static void log(String logInfo){
        // 1. create log
        Logger log = Logger.getLogger(TestLog.class);
        // 2. get log config file
        // 3. start log
        log.debug(logInfo);
    }
}
