package cn.zhima.flame_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "wx_user")
public class WxUser implements Serializable {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309431L;

    /**
     * 用户编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 系统用户编号
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 微信用户
     */
    @Column(name = "wx_user")
    private String wxUser;

    /**
     * 登录状态
     */
    @Column(name = "status")
    private String status;
}
