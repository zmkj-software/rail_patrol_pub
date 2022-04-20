package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文本
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TextMessage extends BaseMessage {
    /**
     * 消息内容
     */
    private String Content;
}
