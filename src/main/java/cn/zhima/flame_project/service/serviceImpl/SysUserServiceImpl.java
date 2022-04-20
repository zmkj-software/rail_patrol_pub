package cn.zhima.flame_project.service.serviceImpl;

import cn.zhima.flame_project.entity.SysUser;
import cn.zhima.flame_project.service.SysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author 冫Soul丶
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Override
    public SysUser getLoginUser() {
        try {
            // security自带的获取登录用户函数
            return (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ignored) {
            // 忽略异常处理
        }
        // 如果没有登录用户，返回空
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getLoginUserRole() {
        try {
            // security自带的获取登录用户权限的函数
            return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        } catch (Exception ignored) {
            // 忽略异常处理
        }
        // 如果没有登录用户，返回空
        return null;
    }
}
