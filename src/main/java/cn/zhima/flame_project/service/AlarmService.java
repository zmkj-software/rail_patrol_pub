package cn.zhima.flame_project.service;

import cn.zhima.flame_project.entity.Alarm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * @author 冫Soul丶
 */
public interface AlarmService {
    /**
     * 根据Id查询出相应的设备
     * @param id 编号
     * @return Alarm
     */
    Alarm findById(Integer id);

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
     * 增加报警信息
     *
     * @param ip IP地址
     * @param type 类型
     * @param group 组/公司名
     * @param pushUrl 推流路径
     * @param name 名称
     */
    Alarm saveAlarm(String ip, String type, String group, String pushUrl, String name, String time, MultipartFile[] multipartFiles);

    /**
     * 查询近7天的数据
     * @param group 组名
     * @return List<Alarm>
     */
    List<Alarm> findByWeekAndGroup(String group);

    /**
     * 查询近30天的数据
     * @param group 组名
     * @return List<Alarm>
     */
    List<Alarm> findByMonthAndGroup(String group);

    /**
     * 发送客服信息到微信
     * @param alarm
     */
    void sendMessage(Alarm alarm);


    /**
     * 同组下不同客户端发来的同一信息的查询条件
     * @param ip
     * @param type
     * @param group
     * @return
     */
    List<Alarm> findByTimeAndGroupAndIpAndType(Long ffTime,Long fTime,Long nowTime ,Long eTime,Long eeTime,String group,String ip,String type);
}
