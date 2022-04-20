package cn.zhima.flame_project.repository;

import cn.zhima.flame_project.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 冫Soul丶
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>{
    /**
     * 根据用户名查询用户
     * @param username
     * @return SysUser
     */
    SysUser findByUserName(String username);
}
