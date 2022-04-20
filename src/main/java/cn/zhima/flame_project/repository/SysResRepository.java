package cn.zhima.flame_project.repository;

import cn.zhima.flame_project.entity.SysRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 冫Soul丶
 */
@Repository
public interface SysResRepository extends JpaRepository<SysRes,Long> {
}
