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

package com.hyc.www.controller.constant;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 用于枚举控制层的系统消息
 * @date 2019-05-03 13:11
 */
public enum ControllerMessage {

    /**
     * 游客身份
     */
    YOU_ARE_VISITOR("您现在处于游客身份，该服务并未对游客开放，如需使用请先注册一个账号"),

    /*
     **************************************************************
     *              系统错误码
     **************************************************************
     */
    /**
     * 系统故障
     */
    SYSTEM_EXECEPTION("服务器发生了严重异常，无法提供服务"),
    /**
     * 请求参数错误
     */
    REQUEST_INVALID("您的请求参数不足或错误，系统无法处理您的请求");
    public String message;

    ControllerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
