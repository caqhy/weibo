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

package com.hyc.www.service.impl;

import com.hyc.www.dao.TweetDao;
import com.hyc.www.dao.UserDao;
import com.hyc.www.exception.DaoException;
import com.hyc.www.factory.DaoProxyFactory;
import com.hyc.www.model.dto.ServiceResult;
import com.hyc.www.model.po.Tweet;
import com.hyc.www.model.po.User;
import com.hyc.www.service.UploadService;
import com.hyc.www.service.constants.Status;

import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigInteger;

import static com.hyc.www.service.constants.ServiceMessage.*;
import static com.hyc.www.util.UploadUtils.toFileName;
import static com.hyc.www.util.UploadUtils.toPhotoName;

/**
 * 负责文件上传服务
 *
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description
 * @date 2019-05-06 02:28
 */
public class UploadServiceImpl implements UploadService {

    private final UserDao userDao = (UserDao) DaoProxyFactory.getInstance().getProxyInstance(UserDao.class);
    private final TweetDao tweetDao = (TweetDao) DaoProxyFactory.getInstance().getProxyInstance(TweetDao.class);

    /**
     * 负责将文件写入文件，并将数据库表对应的记录上的photo属性值修改为文件名
     *
     * @param part      文件
     * @param id        记录id
     * @param tableName 表名
     * @name uploadPhoto
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/6
     */
    @Override
    public ServiceResult uploadPhoto(Part part, Object id, String tableName) {
        String fileName;
        try {
            fileName = toPhotoName(part);
            if ("user".equalsIgnoreCase(tableName)) {
                User user = new User();
                user.setId(new BigInteger(String.valueOf(id)));
                user.setPhoto(fileName);
                if (userDao.update(user) != 1) {
                    return new ServiceResult(Status.ERROR, UPDATE_USER_FAILED.message, user);
                }
            } else if ("moment".equalsIgnoreCase(tableName)) {
                Tweet tweet = new Tweet();
                tweet.setId(new BigInteger(String.valueOf(id)));
//                tweet.setPhoto(fileName);
                if (tweetDao.update(tweet) != 1) {
                    return new ServiceResult(Status.ERROR, DATABASE_ERROR.message, tweet);
                }
            } else {
                return new ServiceResult(Status.ERROR, UNSUPPROT_TABLE.message, tableName);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, DATABASE_ERROR.message, id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, UPLOAD_FAILED.message, id);
        }
        return new ServiceResult(Status.SUCCESS, UPDATE_INFO_SUCCESS.message, fileName);
    }

    /**
     * 负责将文件写入文件，并将数据库表对应聊天背景属性值修改为文件名
     *
     * @param part 文件
     * @param id   记录id
     * @name uploadBackground
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/9
     */
    @Override
    public ServiceResult uploadBackground(Part part, Object id) {
        String fileName;
        try {
            fileName = toPhotoName(part);
            User user = new User();
            user.setId(new BigInteger(String.valueOf(id)));
            user.setChatBackground(fileName);
            if (userDao.update(user) != 1) {
                return new ServiceResult(Status.ERROR, UPDATE_USER_FAILED.message, user);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, DATABASE_ERROR.message, id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, UPLOAD_FAILED.message, id);
        }
        return new ServiceResult(Status.SUCCESS, UPDATE_BACKGROUND_SUCCESS.message, fileName);
    }

    /**
     * 负责将文件写入文件，并返回文件名
     *
     * @param part 文件
     * @name uploadFile
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/14
     */
    @Override
    public ServiceResult uploadFile(Part part) {
        String fileName;
        try {
            fileName = toFileName(part);
        } catch (IOException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, UPLOAD_FAILED.message, part);
        }
        return new ServiceResult(Status.SUCCESS, UPLOAD_SUCCESS.message, fileName);
    }
}
