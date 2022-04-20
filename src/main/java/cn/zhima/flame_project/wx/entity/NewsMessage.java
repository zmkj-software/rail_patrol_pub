package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 图文
 * @author 冫Soul丶
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsMessage extends BaseMessage {
    /**
     * 图文消息个数，限制为10条以内
     */
    private int ArticleCount;
    /**
     * 多条图文消息信息，默认第一个item为大图
     */
    private List<Article> Articles;
}
