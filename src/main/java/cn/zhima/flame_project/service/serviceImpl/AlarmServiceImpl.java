package cn.zhima.flame_project.service.serviceImpl;

import cn.zhima.flame_project.entity.Alarm;
import cn.zhima.flame_project.entity.WxUser;
import cn.zhima.flame_project.repository.AlarmRepository;
import cn.zhima.flame_project.repository.SysUserRepository;
import cn.zhima.flame_project.repository.WxUserRepository;
import cn.zhima.flame_project.service.AlarmService;
import cn.zhima.flame_project.wx.service.MessageService;
import cn.zhima.flame_project.wx.tools.GlobalConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 冫Soul丶
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    final
    SysUserRepository sysUserRepository;
    final
    WxUserRepository wxUserRepository;
    final
    AlarmRepository alarmRepository;

    public AlarmServiceImpl(AlarmRepository alarmRepository, WxUserRepository wxUserRepository, SysUserRepository sysUserRepository) {
        this.alarmRepository = alarmRepository;
        this.wxUserRepository = wxUserRepository;
        this.sysUserRepository = sysUserRepository;
    }

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取上传路径
     */
    @Value("${web.image-upload-path}")
    private String path;

    @Override
    public Alarm findById(Integer id) {
        return alarmRepository.findById(id).get();
    }

    @Override
    public List<Alarm> findByGroup(String group) {
        return alarmRepository.findByGroup(group);
    }

    @Override
    public List<Alarm> findByGroupAndPushUrl(String group, String pushUrl) {
        return alarmRepository.findByGroupAndPushUrl(group,pushUrl);
    }

    @Override
    public Alarm saveAlarm(String ip, String type, String group, String pushUrl, String name,String time, MultipartFile[] multipartFiles) {
        for (MultipartFile  multipartFile: multipartFiles){
            if (multipartFile.isEmpty()) {
                System.out.println("上传的文件为空");
            }
            String uploadName = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + uploadName.substring(uploadName.lastIndexOf("."));
            try {
                File folder = new File(path);
                if (!folder.exists() && !folder.isDirectory()) {
                    folder.mkdirs();
                    System.out.println("创建文件夹");
                } else {
                    System.out.println("文件夹已存在");
                }
                FileCopyUtils.copy(multipartFile.getBytes(), new File(path + fileName));
                Alarm alarm = new Alarm();
                alarm.setTime(new Timestamp(Long.parseLong(time)*1000));
                alarm.setIp(ip);
                alarm.setType(type);
                alarm.setGroup(group);
                alarm.setPushUrl(pushUrl);
                alarm.setName(name);
                alarm.setImg("/flame_project/alarmImage/"+fileName);
                alarmRepository.save(alarm);
                return  alarm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Alarm> findByWeekAndGroup(String group) {
        return alarmRepository.findByWeekAndGroup(group);
    }

    @Override
    public List<Alarm> findByMonthAndGroup(String group) {
        return alarmRepository.findByMonthAndGroup(group);
    }

    @Override
    public void sendMessage(Alarm alarm) {
        List<WxUser> wxUserList = wxUserRepository.findByUserId(sysUserRepository.findByUserName(alarm.getGroup()).getId());
        for (WxUser w:wxUserList) {
            if (w.getStatus().equals("已验证")){
                String string = alarm.getGroup()+"组 / "+alarm.getName()+"\n"+format.format(alarm.getTime())+" / ";
                if (alarm.getType().equals("4")){
                    string += "异物入侵";
                }else if (alarm.getType().equals("2")){
                    string += "火焰报警";
                }
                else if (alarm.getType().equals("3")){
                    string += "遮挡报警";
                }
                MessageService.sendMessage("警告：有新的报警信息",string,GlobalConstant.PUBLIC_URL+"/wxAlarmDetail?id="+alarm.getId(),GlobalConstant.SERVER_URL+alarm.getImg(),w.getWxUser());
            }
        }
    }

    @Override
    public List<Alarm> findByTimeAndGroupAndIpAndType(Long ffTime,Long fTime,Long nowTime ,Long eTime,Long eeTime,String group,String ip,String type) {
        return alarmRepository.findByTimeAndGroupAndIpAndType(ffTime,fTime,nowTime ,eTime,eeTime,group,ip,type);
    }
}
