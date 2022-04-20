package cn.zhima.flame_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "sys_role_res")
public class SysRoleRes implements Serializable {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309425L;

    /**
     * 角色资源编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色编号
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 资源编号
     */
    @Column(name = "res_id")
    private Long resId;
}
