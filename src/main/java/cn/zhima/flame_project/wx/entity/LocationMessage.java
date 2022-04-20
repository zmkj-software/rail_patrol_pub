package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 地理位置
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LocationMessage extends BaseMessage {
    /**
     * 地理位置纬度
     */
    private String Location_X;
    /**
     * 地理位置经度
     */
    private String Location_Y;
    /**
     * 地图缩放大小
     */
    private String Scale;
    /**
     * 地理位置信息
     */
    private String Label;
}
