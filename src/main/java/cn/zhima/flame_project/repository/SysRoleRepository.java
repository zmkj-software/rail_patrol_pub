package cn.zhima.flame_project.repository;

import cn.zhima.flame_project.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 冫Soul丶
 */
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole,Long>{
}
