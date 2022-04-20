package cn.zhima.flame_project.wx.entity;

import lombok.Data;

/**
 * 临时二维码
 * @author 冫Soul丶
 */
@Data
public class TemporaryCode<T> {
    private int expire_seconds;
    private String action_name;
    private ActionInfo<T> action_info;

}
