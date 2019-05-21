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

package com.hyc.www.controller.impl.listener;

import com.hyc.www.provider.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @program www
 * @description 负责监听servlet的初始化和销毁事件
 * @date 2019-05-01 08：44
 */

@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener {

    private static final ConcurrentHashMap<String, BaseProvider> providerMap = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        /**
         * 用来装载provider的容器
         */
        //注册服务
        providerMap.put("userProvider", new UserProvider());
        providerMap.put("chatProvider", new ChatProvider());
        providerMap.put("uploadProvider", new UploadProvider());
        providerMap.put("friendProvider", new FriendProvider());
        providerMap.put("momentProvider",new TweetProvider());
        providerMap.put("messageProvider",new MessageProvider());
        providerMap.put("remarkProvider",new RemarkProvider());
        //将controllerMap注入ServletContext
        ServletContext sc = sce.getServletContext();
        sc.setAttribute("providerMap", providerMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
