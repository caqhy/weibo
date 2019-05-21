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
import com.hyc.www.dao.RemarkDao;
import com.hyc.www.dao.UserDao;
import com.hyc.www.exception.DaoException;
import com.hyc.www.exception.ServiceException;
import com.hyc.www.factory.DaoProxyFactory;
import com.hyc.www.model.dto.ServiceResult;
import com.hyc.www.model.po.Tweet;
import com.hyc.www.model.po.Remark;
import com.hyc.www.model.po.User;
import com.hyc.www.model.vo.RemarkVO;
import com.hyc.www.service.RemarkService;
import com.hyc.www.service.constants.ServiceMessage;
import com.hyc.www.service.constants.Status;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static com.hyc.www.util.StringUtils.toLegalText;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 负责评论服务
 * @date 2019-05-14 01:16
 */
public class RemarkServiceImpl implements RemarkService {

    private final RemarkDao remarkDao = (RemarkDao) DaoProxyFactory.getInstance().getProxyInstance(RemarkDao.class);
    private final TweetDao tweetDao = (TweetDao) DaoProxyFactory.getInstance().getProxyInstance(TweetDao.class);
    private final UserDao userDao = (UserDao) DaoProxyFactory.getInstance().getProxyInstance(UserDao.class);

    /**
     * 添加一条评论
     *
     * @param remark 评论
     * @name addRemark
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/14
     */
    @Override
    public ServiceResult addRemark(Remark remark) {
        if (remark == null) {
            return new ServiceResult(Status.ERROR, ServiceMessage.PARAMETER_NOT_ENOUGHT.message, null);
        }
        StringBuilder message = new StringBuilder();
        //检查长度
        if (remark.getContent().length() > 200) {
            return new ServiceResult(Status.ERROR, ServiceMessage.CONTENT_TOO_LONG.message, remark);
        }
        //检查内容
        if (!isValidContent(remark.getContent())) {
            message.append(ServiceMessage.CONTENT_ILLEGAL.message);
        }
        //过滤非法字符
        remark.setContent(toLegalText(remark.getContent()));
        try {
            //检查是否存在
            Tweet tweet = tweetDao.getMomentById(remark.getMomentId());
            if (tweet == null) {
                return new ServiceResult(Status.ERROR, ServiceMessage.NOT_FOUND.message, remark);
            }
            //阻止插入id
            remark.setId(null);
            if (remarkDao.insert(remark) != 1) {
                return new ServiceResult(Status.ERROR, ServiceMessage.POST_FAILED.message, remark);
            }
            //评论成功后该朋友圈评论数加一
            tweet.setRemark(tweet.getRemark() + 1L);
            if (tweetDao.update(tweet) != 1) {
                throw new ServiceException("无法更新该朋友圈的评论数" + tweet.toString());
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, ServiceMessage.DATABASE_ERROR.message, remark);
        }
        return new ServiceResult(Status.SUCCESS, message.append(ServiceMessage.POST_SUCCESS.message).toString(), remark);
    }

    /**
     * 查询一条朋友圈的评论
     *
     * @param momentId 朋友圈id
     * @param page     页数
     * @name listRemark
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/14
     */
    @Override
    public ServiceResult listRemark(BigInteger momentId, int page) {
        //判空
        if (momentId == null) {
            return new ServiceResult(Status.SUCCESS, ServiceMessage.PARAMETER_NOT_ENOUGHT.message, null);
        }
        //根据页数信息生成查询参数
        int limit = 30;
        int offset = (page - 1) * limit;
        if (offset < 0) {
            return new ServiceResult(Status.ERROR, ServiceMessage.PAGE_INVALID.message, null);
        }
        List<RemarkVO> remarkVOList = new LinkedList<>();
        try {
            List<Remark> remarkList;
            remarkList = remarkDao.listRemarkDesc(momentId, limit, offset);
            //查找评论
            if (remarkList == null || remarkList.size() == 0) {
                return new ServiceResult(Status.SUCCESS, ServiceMessage.NO_REMARK.message, remarkVOList);
            }
            //添加评论者的头像，昵称
            for (Remark remark : remarkList) {
                User user = userDao.getUserById(remark.getUserId());
                remarkVOList.add(toRemarkVOObject(remark, user));
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, ServiceMessage.DATABASE_ERROR.message, remarkVOList);
        }
        return new ServiceResult(Status.SUCCESS, null, remarkVOList);
    }

    /**
     * 删除一条评论
     *
     * @param remarkId 评论id
     * @name removeRemark
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/14
     */
    @Override
    public ServiceResult removeRemark(BigInteger remarkId) {
        try {
            //判空
            if (remarkId == null) {
                throw new ServiceException(ServiceMessage.NOT_NULL.message);
            }
            //检查是否存在
            if (remarkDao.getRemarkById(remarkId) == null) {
                return new ServiceResult(Status.ERROR, ServiceMessage.NOT_FOUND.message, remarkId);
            }
            //删除
            Remark remark = remarkDao.getRemarkById(remarkId);
            if (remarkDao.delete(remark) != 1) {
                return new ServiceResult(Status.ERROR, ServiceMessage.PLEASE_REDO.message, remark);
            }
            //将评论数减一
            Tweet tweet = tweetDao.getMomentById(remark.getMomentId());
            tweet.setRemark(tweet.getRemark() - 1L);
            if (tweetDao.update(tweet) != 1) {
                throw new ServiceException("无法更新该朋友圈的评论数" + tweet.toString());
            }
        } catch (DaoException e) {
            e.printStackTrace();
            return new ServiceResult(Status.ERROR, ServiceMessage.DATABASE_ERROR.message, remarkId);
        }
        return new ServiceResult(Status.SUCCESS, ServiceMessage.OPERATE_SUCCESS.message, remarkId);
    }


    /**
     * 检查一段内容是否合法
     *
     * @param content 评论内容
     */
    private boolean isValidContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        //如果内容经过过滤后与原来不一样，说明含有非法内容
        String legalText = toLegalText(content);
        if (content.equals(legalText)) {
            return true;
        }
        return false;
    }

    /**
     * 建造一个评论视图层对象集合，需要一条评论，和该用户的信息
     *
     * @param remark 评论
     * @param user   评论者的信息
     * @name toRemarkVOObject
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/14
     */
    private RemarkVO toRemarkVOObject(Remark remark, User user) {
        RemarkVO remarkVO = new RemarkVO();
        remarkVO.setContent(remark.getContent());
        remarkVO.setUserId(remark.getUserId());
        remarkVO.setId(remark.getId());
        remarkVO.setUserName(user.getName());
        remarkVO.setUserPhoto(user.getPhoto());
        remarkVO.setLove(remark.getLove());
        remarkVO.setReply(remark.getReply());
        remarkVO.setTime(remark.getTime());
        remarkVO.setMomentId(remark.getMomentId());
        return remarkVO;
    }

}
