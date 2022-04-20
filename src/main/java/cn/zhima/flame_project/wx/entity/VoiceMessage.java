package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 音频
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoiceMessage extends BaseMessage {
    /**
     * 媒体ID
     */
    private String MediaId;
    /**
     * 语音格式
     */
    private String Format;
}
