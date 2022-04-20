package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoMessage extends BaseMessage {
    /**
     * 视频标题
     */
    private String Title;
    /**
     * 媒体文件ID
     */
    private String MediaId;
    /**
     * 视频描述
     */
    private String Description;
    /**
     * 视频消息缩略图的媒体ID
     */
    private String ThumbMediaId;
}
