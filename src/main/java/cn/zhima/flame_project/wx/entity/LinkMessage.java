package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 链接
 * @author 冫Soul丶
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class LinkMessage extends BaseMessage {
    /**
     * 消息标题
     */
    private String Title;
    /**
     * 消息描述
     */
    private String Description;
    /**
     * 消息链接
     */
    private String Url;
}
