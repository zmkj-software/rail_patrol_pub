package cn.zhima.flame_project.wx.entity;

import lombok.Data;

/**
 * 永久二维码
 * @param <T>
 */
@Data
public class PermanentCode<T> {
    private String action_name;
    private ActionInfo<T> action_info;
}
