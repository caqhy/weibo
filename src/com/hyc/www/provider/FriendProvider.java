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

package com.hyc.www.provider;

import com.hyc.www.controller.constant.RequestMethod;
import com.hyc.www.factory.ServiceProxyFactory;
import com.hyc.www.model.dto.ServiceResult;
import com.hyc.www.model.po.Chat;
import com.hyc.www.model.po.Friend;
import com.hyc.www.model.po.Message;
import com.hyc.www.provider.annotation.Action;
import com.hyc.www.provider.annotation.ActionProvider;
import com.hyc.www.server.ChatServer;
import com.hyc.www.service.ChatService;
import com.hyc.www.service.FriendService;
import com.hyc.www.service.MessageService;
import com.hyc.www.service.TweetService;
import com.hyc.www.service.constants.MessageType;
import com.hyc.www.service.constants.ServiceMessage;
import com.hyc.www.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;

import static com.hyc.www.service.constants.ServiceMessage.CANNOT_DELETE_HYC;
import static com.hyc.www.service.constants.Status.ERROR;
import static com.hyc.www.util.BeanUtils.jsonToJavaObject;
import static com.hyc.www.util.ControllerUtils.returnJsonObject;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 负责好友相关业务流程
 * @date 2019-05-06 07:11
 */
@ActionProvider(path = "/friend")
public class FriendProvider extends BaseProvider {
    private final FriendService friendService = (FriendService) new ServiceProxyFactory().getProxyInstance(new FriendServiceImpl());
    private final ChatService chatService = (ChatService) new ServiceProxyFactory().getProxyInstance(new ChatServiceImpl());
    private final MessageService messageService = (MessageService) new ServiceProxyFactory().getProxyInstance(new MessageServiceImpl());
    private final TweetService tweetService = (TweetService)new ServiceProxyFactory().getProxyInstance(new TweetServiceImpl());

    /**
     * 提供添加好友的业务流程
     *
     * @name addFriend
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/9
     */
    @Action(method = RequestMethod.ADD_DO)
    synchronized public void addFriend(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Friend friend = (Friend) jsonToJavaObject(req.getInputStream(), Friend.class);
        ServiceResult result;
        //加好友
        result = friendService.addFriend(friend);
        if (ERROR.equals(result.getStatus())) {
            returnJsonObject(resp, result);
            return;
        }
        //添加好友后判断是否对方也将自己添加为好友，如果双向添加，则建立聊天关系，否则发送好友通知
        if (friendService.isFriend(friend)) {

            Chat chat = (Chat) chatService.createFriendChat(friend).getData();
            //发送打招呼消息
            Message message = new Message();
            message.setChatId(chat.getId());
            message.setSenderId(friend.getUserId());
            message.setContent(ServiceMessage.AGREE_FRIEND.message);
            message.setTime(new Timestamp(System.currentTimeMillis()));
            message.setType(MessageType.USER.toString());
            ChatServer.sendNotify(message, friend.getFriendId());
            ChatServer.sendNotify(message, friend.getUserId());
            messageService.insertMessage(message);
            //初始化朋友圈
            tweetService.initNews(friend);
        } else {
            //生成的加好友通知，发送实时通知并存到数据库
            Message message = (Message) result.getData();
            ChatServer.sendNotify(message, friend.getFriendId());
            messageService.insertMessage(message);
        }
        returnJsonObject(resp, result);
    }

    /**
     * 提供获取好友列表的服务
     *
     * @name listFriend
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/9
     */
    @Action(method = RequestMethod.LIST_DO)
    public void listFriend(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("user_id");
        ServiceResult result;
        result = friendService.listFriend(new BigInteger(userId));
        returnJsonObject(resp, result);
    }

    /**
     * 提供获取好友列表的服务
     *
     * @name listFriend
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/9
     */
    @Action(method = RequestMethod.UPDATE_DO)
    public void updateFriend(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Friend friend = (Friend) jsonToJavaObject(req.getInputStream(), Friend.class);
        ServiceResult result;
        result = friendService.updateFriend(friend);
        returnJsonObject(resp, result);
    }

    /**
     * 提供删除好友的服务
     *
     * @name deleteFriend
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2019/5/9
     */
    @Action(method = RequestMethod.DELETE_DO)
    synchronized public void deleteFriend(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String friendId = req.getParameter("friend_id");
        String userId = req.getParameter("user_id");

        ServiceResult result;
        //系统账号不可删除
        //删除好友之前将聊天关系移除,请求中的好友对象只含有userId和friendId，所以需要重新获取
        Friend friend = friendService.getByUidAndFriendId(new BigInteger(userId), new BigInteger(friendId));
        if (friend == null) {
            result = new ServiceResult();
            result.setMessage(ServiceMessage.FRIEND_NOT_EXIST.message);
            result.setStatus(ERROR);
            returnJsonObject(resp, result);
            return;
        }
        if (UserServiceImpl.SYSTEM_ID.equals(friend.getFriendId())) {
            returnJsonObject(resp, new ServiceResult(ERROR, ServiceMessage.CANNOT_DELETE_SYSTEM.message, null));
            return;
        }
        if (UserServiceImpl.HYC_ID.equals(friend.getFriendId())) {
            returnJsonObject(resp, new ServiceResult(ERROR, CANNOT_DELETE_HYC.message, null));
            return;
        }
        //调用聊天服务将聊天关系解除
        Chat chat = new Chat();
        chat.setId(friend.getChatId());
        chatService.removeChat(chat);
        //解除好友关系
        result = friendService.removeFriend(friend);
        returnJsonObject(resp, result);
    }

}
