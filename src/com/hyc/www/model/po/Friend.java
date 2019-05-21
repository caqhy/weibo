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

import com.alibaba.fastjson.annotation.JSONField;
import com.hyc.www.dao.annotation.Table;
import com.hyc.www.model.po.abs.BaseEntity;

import java.math.BigInteger;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @program www
 * @description 朋友表
 * @date 2019-05-02 01:56
 */
@Table(name = "friend")
public class Friend extends BaseEntity {
    @JSONField(name = "user_id")
    private BigInteger userId;
    @JSONField(name = "friend_id")
    private BigInteger friendId;
    @JSONField(name = "chat_id")
    private BigInteger chatId;
    private String photo;
    @JSONField(name = "group_id")
    private BigInteger groupId;
    private String alias;
    private String description;

    public BigInteger getChatId() {
        return chatId;
    }

    public void setChatId(BigInteger chatId) {
        this.chatId = chatId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getFriendId() {
        return friendId;
    }

    public void setFriendId(BigInteger friendId) {
        this.friendId = friendId;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
