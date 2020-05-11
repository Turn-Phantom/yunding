package com.online.yunding.server;

import com.online.yunding.config.ConfigParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @desc webSocket 服务
 * @date 2020-05-04
 */
@ServerEndpoint("/web/socket/{sid}")
@Component
public class WebSocketServer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 客户端对应socket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<>();

    private static Map<String, WebSocketServer> serverMap = new HashMap<>();

    // 连接的会话; 通过session发送消息
    private Session session;

    // 接受数据的sid
    private String sid;

    @Autowired
    private ConfigParams configParams;

    /** 连接建立成功调用的方法 */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        serverMap.put("yd001", this);
        logger.info("连接成功, sid为：" + sid);
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 连接关闭调用的方法 */
    @OnClose
    public void onClose() {
        serverMap.clear();
        logger.info("连接关闭");
    }

    /** 收到客户端消息后调用的方法; （无需客户端发送消息到服务端） */
    /*@OnMessage
    public void onMessage(String message, Session session) {
        logger.info("接受消息");
    }*/

    /** 错误处理 */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }

    /** 服务器推动消息 */
    public void sendMessage(String msg) throws IOException{
        serverMap.get("yd001").session.getBasicRemote().sendText(msg);
    }
}
