package cn.zhima.flame_project.wx.tools;

import cn.zhima.flame_project.entity.Alarm;
import cn.zhima.flame_project.entity.SysUser;
import cn.zhima.flame_project.entity.WxUser;
import cn.zhima.flame_project.repository.AlarmRepository;
import cn.zhima.flame_project.repository.SysUserRepository;
import cn.zhima.flame_project.repository.WxUserRepository;
import cn.zhima.flame_project.wx.entity.Article;
import cn.zhima.flame_project.wx.entity.NewsMessage;
import cn.zhima.flame_project.wx.entity.TextMessage;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 冫Soul丶
 */
@Component
public class EventDispatcher {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final
    WxUserRepository wxUserRepository;
    final
    AlarmRepository alarmRepository;
    final
    SysUserRepository sysUserRepository;

    public EventDispatcher(WxUserRepository wxUserRepository, AlarmRepository alarmRepository, SysUserRepository sysUserRepository) {
        this.wxUserRepository = wxUserRepository;
        this.alarmRepository = alarmRepository;
        this.sysUserRepository = sysUserRepository;
    }

    public String processEvent(Map<String, String> map) {
        String toUserName = map.get("ToUserName");
        String fromUserName = map.get("FromUserName");
        String createTime = map.get("CreateTime");
        String msgType = map.get("MsgType");
        String msgId = map.get("MsgId");
        String content = map.get("Content");
        String event = map.get("Event");
        WxUser wxUser = wxUserRepository.findByWxUser(fromUserName);
        SysUser sysUser = null;
        if (wxUser != null) {
            sysUser = sysUserRepository.findById(wxUser.getUserId()).get();
        }
        TextMessage txtmsg = new TextMessage();
        txtmsg.setToUserName(fromUserName);
        txtmsg.setFromUserName(toUserName);
        txtmsg.setCreateTime(new Date().getTime());
        txtmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        switch (event) {
            case MessageUtil.EVENT_TYPE_SUBSCRIBE:
                if (wxUser != null) {
                    if (wxUser.getStatus().equals("已验证")) {
                        txtmsg.setContent("非常感谢您的关注，本公众号将助您更快捷的查询复合火焰探测器的报警信息!\n\n\n检测您绑定了 '" + sysUser.getUsername() + "' 的设备，可直接查看相关报警信息!");
                    } else if (wxUser.getStatus().equals("未验证")) {
                        txtmsg.setContent("非常感谢您的关注，本公众号将助您更快捷的查询复合火焰探测器的报警信息!\n\n\n请输入 '" + sysUser.getUsername() + "' 的绑定密码:");
                    }
                } else if (wxUser == null) {
                    if ("".equals(map.get("EventKey")) || map.get("EventKey") == null) {
                        txtmsg.setContent("非常感谢您的关注，本公众号将助您更快捷的查询复合火焰探测器的报警信息!\n\n\n检测您尚未绑定任何设备，扫描设备二维码绑定后才能查看相关报警信息!");
                    } else {
                        String[] temp = map.get("EventKey").split("_");
                        WxUser newWxUser = new WxUser();
                        newWxUser.setUserId(sysUserRepository.findByUserName(temp[1]).getId());
                        newWxUser.setWxUser(fromUserName);
                        newWxUser.setStatus("未验证");
                        wxUserRepository.save(newWxUser);
                        txtmsg.setContent("非常感谢您的关注，本公众号将助您更快捷的查询复合火焰探测器的报警信息!\n\n\n请输入 '" + temp[1] + "' 的绑定密码:");
                    }
                }
                System.out.println("==============这是关注事件！");
                return MessageUtil.textMessageToXml(txtmsg);
            case MessageUtil.EVENT_TYPE_SCAN:
                if (wxUser != null) {
                    if (map.get("EventKey").equals(sysUser.getUsername())) {
                        if (wxUser.getStatus().equals("已验证")) {
                            txtmsg.setContent("您绑定了 '" + sysUser.getUsername() + "' 的设备，可直接查看相关报警信息!");
                        } else if (wxUser.getStatus().equals("未验证")) {
                            txtmsg.setContent("您还未绑定'" + sysUser.getUsername() + "'，请输入绑定密码:");
                        }
                    } else {
                        wxUser.setStatus("未验证");
                        System.out.println(map.get("EventKey"));
                        wxUser.setUserId(sysUserRepository.findByUserName(map.get("EventKey")).getId());
                        wxUserRepository.save(wxUser);
                        txtmsg.setContent("您已与'" + sysUser.getUsername() + "'解绑，即将绑定'" + map.get("EventKey") + "',请输入绑定密码:");
                    }
                } else {
                    WxUser newWxUser = new WxUser();
                    newWxUser.setUserId(sysUserRepository.findByUserName(map.get("EventKey")).getId());
                    newWxUser.setWxUser(fromUserName);
                    newWxUser.setStatus("未验证");
                    wxUserRepository.save(newWxUser);
                    txtmsg.setContent("请输入 '" + map.get("EventKey") + "' 的绑定密码:");
                }
                System.out.println("==============这是扫描二维码事件！");
                return MessageUtil.textMessageToXml(txtmsg);
            case MessageUtil.EVENT_TYPE_CLICK:
                NewsMessage newmsg = new NewsMessage();
                newmsg.setToUserName(fromUserName);
                newmsg.setFromUserName(toUserName);
                newmsg.setCreateTime(new Date().getTime());
                newmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                if ("one".equals(map.get("EventKey"))) {
                    if (wxUser != null && wxUser.getStatus().equals("已验证")) {
                        List<Alarm> alarmList = alarmRepository.findByGroup(sysUserRepository.findById(wxUser.getUserId()).get().getUsername());
                        Alarm lastAlarm = alarmList.get(alarmList.size() - 1);
                        String string = lastAlarm.getGroup() + "组 / " + lastAlarm.getName() + "\n" + format.format(lastAlarm.getTime()) + " / ";
                        if (lastAlarm.getType().equals("4")) {
                            string += "异物入侵";
                        } else if (lastAlarm.getType().equals("2")) {
                            string += "火焰报警";
                        } else if (lastAlarm.getType().equals("3")) {
                            string += "遮挡报警";
                        }
                        List<Article> list = new ArrayList<Article>();
                        Article article = new Article();
                        article.setTitle("最近一次");
                        article.setDescription(string);
                        article.setUrl(GlobalConstant.PUBLIC_URL + "/wxAlarmDetail?id=" + lastAlarm.getId());
                        article.setPicUrl(GlobalConstant.SERVER_URL + lastAlarm.getImg());
                        list.add(article);
                        newmsg.setArticleCount(list.size());
                        newmsg.setArticles(list);
                    } else {
                        txtmsg.setContent("您还没有权限查看该内容，请绑定后再查看吧！");
                        return MessageUtil.textMessageToXml(txtmsg);
                    }
                } else if ("eight".equals(map.get("EventKey"))) {
                    if (wxUser != null && wxUser.getStatus().equals("已验证")) {
                        List<Alarm> alarmList = alarmRepository.findByGroup(sysUserRepository.findById(wxUser.getUserId()).get().getUsername());
                        List<Article> list = new ArrayList<Article>();
                        if(alarmList.size()<8){
                            txtmsg.setContent("您还没有8次报警信息，请一条一条查看吧！");
                            return MessageUtil.textMessageToXml(txtmsg);
                        }else {
                            for (int i = alarmList.size() - 8; i < alarmList.size(); i++) {
                                Alarm alarm = alarmList.get(i);
                                String string = alarm.getGroup() + "组 / " + alarm.getName() + "\n" + format.format(alarm.getTime()) + " / ";
                                if (alarm.getType().equals("4")) {
                                    string += "异物入侵";
                                } else if (alarm.getType().equals("2")) {
                                    string += "火焰报警";
                                } else if (alarm.getType().equals("3")) {
                                    string += "遮挡报警";
                                }
                                Article article = new Article();
                                article.setTitle(string);
                                article.setUrl(GlobalConstant.PUBLIC_URL + "/wxAlarmDetail?id=" + alarm.getId());
                                article.setPicUrl(GlobalConstant.SERVER_URL + alarm.getImg());
                                list.add(article);
                            }
                            newmsg.setArticleCount(list.size());
                            newmsg.setArticles(list);
                        }
                    } else {
                        txtmsg.setContent("您还没有权限查看该内容，请绑定后在查看吧！");
                        return MessageUtil.textMessageToXml(txtmsg);
                    }
                } else if ("week".equals(map.get("EventKey"))) {
                    if (wxUser != null && wxUser.getStatus().equals("已验证")) {
                        List<Article> list = new ArrayList<Article>();
                        Article article = new Article();
                        article.setTitle("最近7天的报警信息");
                        article.setDescription("点击进入详情列表");
                        article.setUrl(GlobalConstant.PUBLIC_URL + "/wxAlarmList?time=week&group=" + sysUser.getUsername());
                        article.setPicUrl(GlobalConstant.PUBLIC_URL + "/img/logo2.png");
                        list.add(article);
                        newmsg.setArticleCount(list.size());
                        newmsg.setArticles(list);
                    } else {
                        txtmsg.setContent("您还没有权限查看该内容，请绑定后再查看吧！");
                        return MessageUtil.textMessageToXml(txtmsg);
                    }
                } else if ("month".equals(map.get("EventKey"))) {
                    if (wxUser != null && wxUser.getStatus().equals("已验证")) {
                        List<Article> list = new ArrayList<Article>();
                        Article article = new Article();
                        article.setTitle("最近30天的报警信息");
                        article.setDescription("点击进入详情列表");
                        article.setUrl(GlobalConstant.PUBLIC_URL + "/wxAlarmList?time=month&group=" + sysUser.getUsername());
                        article.setPicUrl(GlobalConstant.PUBLIC_URL + "/img/logo2.png");
                        list.add(article);
                        newmsg.setArticleCount(list.size());
                        newmsg.setArticles(list);
                    } else {
                        txtmsg.setContent("您还没有权限查看该内容，请绑定后再查看吧！");
                        return MessageUtil.textMessageToXml(txtmsg);
                    }
                } else if ("all".equals(map.get("EventKey"))) {
                    if (wxUser != null && wxUser.getStatus().equals("已验证")) {
                        List<Article> list = new ArrayList<Article>();
                        Article article = new Article();
                        article.setTitle("全部报警信息");
                        article.setDescription("点击进入详情列表");
                        article.setUrl(GlobalConstant.PUBLIC_URL + "/wxAlarmList?time=all&group=" + sysUser.getUsername());
                        article.setPicUrl(GlobalConstant.PUBLIC_URL + "/img/logo2.png");
                        list.add(article);
                        newmsg.setArticleCount(list.size());
                        newmsg.setArticles(list);
                    } else {
                        txtmsg.setContent("您还没有权限查看该内容，请绑定后再查看吧！");
                        return MessageUtil.textMessageToXml(txtmsg);
                    }
                }
                System.out.println("==============这是自定义菜单点击事件！");
                return MessageUtil.newsMessageToXml(newmsg);
            case MessageUtil.EVENT_TYPE_VIEW:
                System.out.println("==============这是自定义菜单View事件！");
                break;
            case MessageUtil.EVENT_TYPE_UNSUBSCRIBE:
                System.out.println("==============这是取消关注事件！");
                break;
            case MessageUtil.EVENT_TYPE_LOCATION:
                System.out.println("==============这是位置上报事件！");
                break;
            default:
        }
        return null;
    }
}
