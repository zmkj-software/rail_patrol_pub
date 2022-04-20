package cn.zhima.flame_project.wx.entity;

import lombok.Data;

/**
 * 菜单点击对象
 *
 * @author 冫Soul丶
 */
@Data
public class ClickButton {
    private String type;
    private String name;
    private String key;
}
