package cn.zhima.flame_project.repository;

import cn.zhima.flame_project.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 冫Soul丶
 */
@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Integer> {

    /**
     * 根据group查询出相应的设备
     * @param group
     * @return List<Alarm>
     */
    List<Alarm> findByGroup(String group);

    /**
     * 根据group和推流url查询出相应的报警信息
     * @param group, pushUrl
     * @return List<Alarm>
     */
    List<Alarm> findByGroupAndPushUrl(String group,String pushUrl);

    /**
     * 查询近7天的数据
     * @param group 组名
     * @return List<Alarm>
     */
    @Query(value = "SELECT * FROM alarm WHERE DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= time AND `group`= :group",nativeQuery=true)
    List<Alarm> findByWeekAndGroup(@Param("group")String group);

    /**
     * 查询近30天的数据
     * @param group 组名
     * @return List<Alarm>
     */
    @Query(value = "SELECT * FROM alarm WHERE DATE_SUB(CURDATE(), INTERVAL 29 DAY) <= time AND `group`= :group",nativeQuery=true)
    List<Alarm> findByMonthAndGroup(@Param("group")String group);

    /**
     * 同组下不同客户端发来的同一信息的查询条件
     * @param ip
     * @param type
     * @param group
     * @return
     */
    @Query(value = "select * from alarm " +
            "Where (date_format(from_unixtime(:ffTime),'%Y-%m-%d %T') = time " +
            "OR date_format(from_unixtime(:fTime),'%Y-%m-%d %T') = time " +
            "OR date_format(from_unixtime(:nowTime),'%Y-%m-%d %T') = time " +
            "OR date_format(from_unixtime(:eTime),'%Y-%m-%d %T') = time " +
            "OR date_format(from_unixtime(:eeTime),'%Y-%m-%d %T') = time) " +
            "AND `group`=:group AND ip=:ip AND type=:type",nativeQuery=true)
    List<Alarm> findByTimeAndGroupAndIpAndType(@Param("ffTime")Long ffTime,@Param("fTime")Long fTime,@Param("nowTime")Long nowTime ,@Param("eTime")Long eTime,@Param("eeTime")Long eeTime,@Param("group")String group,@Param("ip")String ip,@Param("type")String type);
}
