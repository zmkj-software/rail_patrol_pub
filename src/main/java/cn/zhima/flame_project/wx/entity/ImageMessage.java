package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageMessage extends BaseMessage {
    /**
     * 图片
     */
    private Image Image;
}
