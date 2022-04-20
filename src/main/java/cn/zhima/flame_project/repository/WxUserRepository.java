package cn.zhima.flame_project.repository;

import cn.zhima.flame_project.entity.SysUser;
import cn.zhima.flame_project.entity.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 冫Soul丶
 */
@Repository
public interface WxUserRepository extends JpaRepository<WxUser, Integer> {
    /**
     * 根据wxUser查询
     * @param wxUser 微信用户
     * @return SysUser
     */
    WxUser findByWxUser(String wxUser);

    /**
     * 根据用户Id(也就是group)找出对应的微信用户列表
     * @param userId
     * @return
     */
    List<WxUser> findByUserId(Long userId);
}
