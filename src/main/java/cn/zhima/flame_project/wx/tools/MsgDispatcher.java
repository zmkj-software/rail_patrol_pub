package cn.zhima.flame_project.wx.tools;

import cn.zhima.flame_project.entity.SysUser;
import cn.zhima.flame_project.entity.WxUser;
import cn.zhima.flame_project.repository.SysUserRepository;
import cn.zhima.flame_project.repository.WxUserRepository;
import cn.zhima.flame_project.wx.entity.Image;
import cn.zhima.flame_project.wx.entity.ImageMessage;
import cn.zhima.flame_project.wx.entity.TextMessage;
import cn.zhima.flame_project.wx.service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * @author 冫Soul丶
 */
@Component
public class MsgDispatcher {
    final WxUserRepository wxUserRepository;
    final SysUserRepository sysUserRepository;

    public MsgDispatcher(WxUserRepository wxUserRepository, SysUserRepository sysUserRepository) {
        this.wxUserRepository = wxUserRepository;
        this.sysUserRepository = sysUserRepository;
    }

    public String processMessage(Map<String, String> map) {
        String toUserName = map.get("ToUserName");
        String fromUserName = map.get("FromUserName");
        String createTime = map.get("CreateTime");
        String msgType = map.get("MsgType");
        String msgId = map.get("MsgId");
        String content = map.get("Content");
        WxUser wxUser = wxUserRepository.findByWxUser(fromUserName);
        SysUser sysUser = null;
        if (wxUser != null) {
            sysUser = sysUserRepository.findById(wxUser.getUserId()).get();
        }
        switch (msgType) {
            case MessageUtil.REQ_MESSAGE_TYPE_TEXT:
                TextMessage txtmsg = new TextMessage();
                txtmsg.setToUserName(fromUserName);
                txtmsg.setFromUserName(toUserName);
                txtmsg.setCreateTime(new Date().getTime());
                txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                if (content.equals("我要一张图")){
                    String mediaId = MessageService.upload("C:/flame_project/alarmImage/logo2.png", "image");
                    ImageMessage imgMsg = new ImageMessage();
                    imgMsg.setToUserName(fromUserName);
                    imgMsg.setFromUserName(toUserName);
                    imgMsg.setCreateTime(new Date().getTime());
                    imgMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_IMAGE);
                    Image image = new Image();
                    image.setMediaId(mediaId);
                    imgMsg.setImage(image);
                    System.out.println("给你一张图");
                    return MessageUtil.imageMessageToXml(imgMsg);
                }else if (content.equals("更新菜单")){
                    MessageService.customMenu();
                    txtmsg.setContent("菜单已更新，稍等片刻或是退出公众号再次进入即可使用新菜单");
                    return MessageUtil.textMessageToXml(txtmsg);
                }else if (content.equals("删除菜单")){
                    MessageService.deleteMenu();
                    txtmsg.setContent("菜单已删除，稍等片刻或是退出公众号再次进入");
                    return MessageUtil.textMessageToXml(txtmsg);
                }
                int count = 1;
                String s = null;
                StringTokenizer stringTokenizer = new StringTokenizer(content,"/");
                while(stringTokenizer.hasMoreTokens()) {
                    if (count == 1&&stringTokenizer.nextToken().equals("我要二维码")){
                        count++;
                    }else if (count == 2){
                        s = stringTokenizer.nextToken();
                    }
                }
                if (s!=null){
                    String url = MessageService.createCode(s);
                    txtmsg.setContent("参数'"+s+"'的二维码:"+url);
                    System.out.println("获取参数为"+s+"的二维码");
                    return MessageUtil.textMessageToXml(txtmsg);
                }

                if (wxUser != null) {
                    if (wxUser.getStatus().equals("已验证")) {
                        txtmsg.setContent("您已绑定了 '" + sysUser.getUsername() + "' 的设备，可直接查看相关报警信息!");
                    } else if (wxUser.getStatus().equals("未验证")) {
                        if (sysUser.getPassword().equals(DigestUtils.md5DigestAsHex(content.getBytes()))) {
                            wxUser.setStatus("已验证");
                            wxUserRepository.save(wxUser);
                            txtmsg.setContent("验证成功！");
                        } else {
                            txtmsg.setContent("密码错误！");
                        }
                    }
                } else if (wxUser == null) {
                    txtmsg.setContent("非常感谢您的关注，本公众号将助您更快捷的查询复合火焰探测器的报警信息!\n\n\n检测您尚未绑定任何设备，扫描设备二维码绑定后才能查看相关报警信息!");
                }
                System.out.println("==============这是文本消息！");
                return MessageUtil.textMessageToXml(txtmsg);
            case MessageUtil.REQ_MESSAGE_TYPE_IMAGE:
                System.out.println("==============这是图片消息！");
                break;
            case MessageUtil.REQ_MESSAGE_TYPE_LINK:
                System.out.println("==============这是链接消息！");
                break;
            case MessageUtil.REQ_MESSAGE_TYPE_LOCATION:
                System.out.println("==============这是位置消息！");
                break;
            case MessageUtil.REQ_MESSAGE_TYPE_VIDEO:
                System.out.println("==============这是视频消息！");
                break;
            case MessageUtil.REQ_MESSAGE_TYPE_VOICE:
                System.out.println("==============这是语音消息！");
                break;
            default:
        }
        return null;
    }
}
