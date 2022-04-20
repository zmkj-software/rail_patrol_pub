package cn.zhima.flame_project.repository;

import cn.zhima.flame_project.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 冫Soul丶
 */
@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long>{
    /**
     * 根据用户编号查找中间表，得到当前用户对应的所有的角色
     * @param userId
     * @return List<SysUserRole>
     */
    List<SysUserRole> findByUserId(Long userId);
}
