package cn.zhima.flame_project.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "sys_role")
public class SysRole implements Serializable, GrantedAuthority {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269343822L;

    /**
     * 角色编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名
     */
    @Column(name = "name")
    private String name;

    /**
     * GrantedAuthority接口实现
     *
     * @return name
     */
    @Override
    public String getAuthority() {
        return name;
    }
}
