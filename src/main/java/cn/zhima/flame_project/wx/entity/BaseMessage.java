package cn.zhima.flame_project.wx.entity;

import lombok.Data;

/**
 * 基本属性
 * @author 冫Soul丶
 */
@Data
public class BaseMessage {
    /**
     * 用户微信号（一个OpenID）
     */
    private String ToUserName;
    /**
     * 开发者微信号
     */
    private String FromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private long CreateTime;
    /**
     * 消息类型
     * text/image/location/link/video/shortVideo
     */
    private String MsgType;
    /**
     * 消息id
     */
    private long MsgId;
}
