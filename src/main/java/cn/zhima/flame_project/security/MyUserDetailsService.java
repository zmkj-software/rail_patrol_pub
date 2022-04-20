package cn.zhima.flame_project.security;

import cn.zhima.flame_project.entity.SysRole;
import cn.zhima.flame_project.entity.SysUser;
import cn.zhima.flame_project.entity.SysUserRole;
import cn.zhima.flame_project.repository.SysRoleRepository;
import cn.zhima.flame_project.repository.SysUserRepository;
import cn.zhima.flame_project.repository.SysUserRoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详细信息（用户名，密码，所属角色列表）
 * 如果用户不存在，则抛出UsernameNotFoundException异常
 *
 * @author 冫Soul丶
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    final
    SysRoleRepository sysRoleRepository;
    final
    SysUserRepository sysUserRepository;
    final
    SysUserRoleRepository sysUserRoleRepository;

    public MyUserDetailsService(SysUserRepository sysUserRepository, SysRoleRepository sysRoleRepository, SysUserRoleRepository sysUserRoleRepository) {
        this.sysUserRepository = sysUserRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
    }

    /**
     * 返回一个详细的用户对象，包含了基本信息和所属角色列表
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserRepository.findByUserName(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("不存在该用户");
        } else {
            List<SysUserRole> sysUserRoleList = sysUserRoleRepository.findByUserId(sysUser.getId());
            List<Long> roleIdList = new ArrayList<>();
            for (SysUserRole sysUserRole : sysUserRoleList) {
                roleIdList.add(sysUserRole.getRoleId());
            }
            List<SysRole> sysRoleList = new ArrayList<>();
            for (Long roleId : roleIdList) {
                sysRoleList.add(sysRoleRepository.findById(roleId).get());
            }
            sysUser.setAuthorities(sysRoleList);
            return sysUser;
        }
    }
}
