package com.mq_test.mq_test.ws;

import com.alibaba.fastjson2.JSON;
import com.mq_test.mq_test.pojo.Result;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/rubbishReco/{cid}")
@Slf4j
public class RubbishRecoWs {

    //存放会话对象
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("cid") String cid) {
        log.info("{}客户端登入", cid);
        sessionMap.putIfAbsent(cid, session);
    }

    @OnMessage
    public void onMessage(String req, @PathParam("cid") String cid) {
//        TranslateDTO translateDTO = JSON.parseObject(req, TranslateDTO.class);
//        translateDTO.setCid(cid);
//        translateDTO.setFormatType("text");
//        translateDTO.setScene("general");
//        nlpService.translate(translateDTO);
//        log.info("收到来自客户端：{}的信息: {}", cid, req);
    }

    @OnClose
    public void onClose(@PathParam("cid") String cid) throws IOException {
        try {
            Session session = sessionMap.remove(cid);
            session.close();
        } catch (Exception ignored) {}
        log.info("{}客户端连接断开", cid);
    }
    
    @OnError
    public void onError(@PathParam("cid") String cid, Throwable error) {
        try {
            Session session = sessionMap.remove(cid);
            session.close();
        } catch (Exception ignored) {}

        log.info("傻逼{}客户端又出错", cid);
    }

    public void returnStringResult(String cid, String message) throws IOException {
        Session clientSession = sessionMap.get(cid);
        Result<String> res = Result.success(message);
        clientSession.getBasicRemote().sendText(JSON.toJSONString(res));
    }
}
