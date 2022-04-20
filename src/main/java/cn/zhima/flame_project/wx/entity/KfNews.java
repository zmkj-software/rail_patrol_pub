package cn.zhima.flame_project.wx.entity;

import lombok.Data;

/**
 * @author 冫Soul丶
 */
@Data
public class KfNews {
    private KfArticleList news;
    private String touser;
    private String msgtype;
}
