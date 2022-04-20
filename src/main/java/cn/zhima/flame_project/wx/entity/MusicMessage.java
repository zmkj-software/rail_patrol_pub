package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 音乐
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MusicMessage extends BaseMessage {
    /**
     * 音乐标题
     */
    private String Title;
    /**
     * 音乐描述
     */
    private String Description;
    /**
     * 音乐链接
     */
    private String MusicUrl;
    /**
     * 高品质音乐链接，Wi-Fi环境优先使用该链接播放音乐
     */
    private String HQMusicUrl;
    /**
     * 缩略图的媒体ID，通过上传多媒体文件得到的ID
     */
    private String ThumbMediaId;
}
