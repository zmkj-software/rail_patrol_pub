package cn.zhima.flame_project.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "sys_user")
public class SysUser implements Serializable, UserDetails {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309421L;

    /**
     * 用户编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String userName;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 非数据库字段
     */
    @Transient
    private List<SysRole> authorities;

    /**
     * 获取角色列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 获取用户名
     */
    @Override
    public String getUsername() {
        return userName;
    }

    /**
     * 用户账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户账号是否被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户密码是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
