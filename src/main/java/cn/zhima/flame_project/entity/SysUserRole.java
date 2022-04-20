package cn.zhima.flame_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "sys_user_role")
public class SysUserRole implements Serializable {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309424L;

    /**
     * 用户角色编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户编号
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色编号
     */
    @Column(name = "role_id")
    private Long roleId;
}
