package cn.zhima.flame_project.wx.controller;

import cn.zhima.flame_project.wx.service.MessageService;
import cn.zhima.flame_project.wx.tools.CheckUtil;
import cn.zhima.flame_project.wx.tools.MessageUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author 冫Soul丶
 */
@RestController
public class WxController {
    final MessageService messageService;

    public WxController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 微信Get请求验证自定义的token
     */
    @GetMapping("/verify_wx_token")
    public void login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        System.out.println("-----开始校验签名-----");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        try (PrintWriter out = response.getWriter()) {
            if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
                out.print(echostr);
                out.close();
                System.out.println("-----签名校验通过-----");
            }else {
                System.out.println("-----签名校验未通过-----");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信Post请求交互
     */
    @PostMapping("/verify_wx_token")
    public String handler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<String, String> map = MessageUtil.parseXml(request);
            String msgType = map.get("MsgType");
            System.out.println(map);
            if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
                return messageService.processEvent(map);
            } else {
                return messageService.processMessage(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
