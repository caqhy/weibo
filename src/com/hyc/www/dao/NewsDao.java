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

package com.hyc.www.dao;

import com.hyc.www.dao.annotation.Query;
import com.hyc.www.dao.annotation.Result;
import com.hyc.www.dao.annotation.ResultType;
import com.hyc.www.model.po.News;

import java.util.List;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 负责动态表的CRUD
 * @date 2019-05-07 18:47
 */
public interface NewsDao extends BaseDao {
    String TABLE = "news";
    String ALL_FIELD = "user_id,tweet_id,loved,shared,viewed,collected," + BASE_FIELD;

    /**
     * 通过用户id查询所有自己可见的所有微博动态
     *
     * @param userId 用户id
     * @param limit  每页查询记录数
     * @param offset 起始记录数
     * @name listNewsByUserId
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/7
     */
    @Result(entity = News.class, returns = ResultType.LIST)
    @Query("select " + ALL_FIELD + " from " + TABLE + " where user_id = ?  order by gmt_create desc limit ? offset ?  ")
    List<News> listNewsByUserId(Object userId, int limit, int offset);


    /**
     * 通过微博id和用户id查询一个微博动态
     *
     * @param tweetId 微博id
     * @param userId 用户id
     * @name getNewsByTweetId
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/7
     */
    @Result(entity = News.class, returns = ResultType.OBJECT)
    @Query(value = "select " + ALL_FIELD + " from " + TABLE + " where tweet_id = ? and user_id = ? ")
    News getNewsByTweetIdAndUserId(Object tweetId,Object userId);
}
