package cn.zhima.flame_project.service;

import cn.zhima.flame_project.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author 冫Soul丶
 */
public interface SysUserService {
    /**
     * 获取当前登录的用户
     * @return user
     */
    SysUser getLoginUser();

    /**
     * 获取当前登录用户所有权限
     * @return authorities
     */
    Collection<? extends GrantedAuthority> getLoginUserRole();
}
