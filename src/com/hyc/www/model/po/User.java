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

package com.hyc.www.model.po;

import com.hyc.www.dao.annotation.Field;
import com.hyc.www.dao.annotation.Table;
import com.hyc.www.model.po.abs.BaseEntity;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @program www
 * @description 用户实体类
 * @date 2019-05-01 23:27
 */
@Table(name = "user")
public class User extends BaseEntity {
    @Field(name = "email")
    private String email;
    @Field(name = "weibo_id")
    private String weiboId;
    @Field(name = "phone")
    private String phone;
    @Field(name = "password")
    private String password;
    @Field(name = "gender")
    private String gender;
    @Field(name = "signature")
    private String signature;
    @Field(name = "name")
    private String name;
    @Field(name = "photo")
    private String photo;
    @Field(name = "chat_background")
    private String chatBackground;
    @Field(name = "location")
    private String location;
    @Field(name = "online_status")
    private String onlineStatus;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getChatBackground() {
        return chatBackground;
    }

    public void setChatBackground(String chatBackground) {
        this.chatBackground = chatBackground;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
