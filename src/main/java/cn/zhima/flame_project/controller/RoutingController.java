package cn.zhima.flame_project.controller;

import cn.zhima.flame_project.entity.Alarm;
import cn.zhima.flame_project.repository.AlarmRepository;
import cn.zhima.flame_project.service.AlarmService;
import cn.zhima.flame_project.service.SysUserService;
import cn.zhima.flame_project.service.VideoService;
import cn.zhima.flame_project.tools.SaveAlarm;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 冫Soul丶
 */
@Controller
public class RoutingController {
    static Alarm newAlarm;

    final
    SysUserService sysUserService;
    final
    AlarmService alarmService;
    final
    AlarmRepository alarmRepository;
    final
    VideoService videoService;

    public RoutingController(SysUserService sysUserService, AlarmService alarmService, AlarmRepository alarmRepository, VideoService videoService) {
        this.sysUserService = sysUserService;
        this.alarmService = alarmService;
        this.alarmRepository = alarmRepository;
        this.videoService = videoService;
    }

    private HashMap<String, List<String>> map = new HashMap<>();

    @RequestMapping("/")
    public String root(Model model) {
        return "login";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @RequestMapping("/401")
    public String accessDenied() {
        return "401";
    }

    @RequestMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping("/loginExit")
    public String loginExit(Model model) {
        model.addAttribute("loginExit", true);
        return "login";
    }

    @RequestMapping("/loginExpired")
    public String loginExpired(Model model) {
        model.addAttribute("loginExpired", true);
        return "login";
    }

    @RequestMapping("/allHome")
    public String allHome(Model model) {
        HashMap<String, List<Alarm>> integerListHashMap = getAll();
        HashMap<String, List<Alarm>> integerListHashMap1 = getAllUnprocessed();
        model.addAttribute("integerListHashMap", integerListHashMap);
        model.addAttribute("integerListHashMap1", integerListHashMap1);
        return "allHome";
    }

    @RequestMapping("/someHome")
    public String someHome(Model model) {
        HashMap<String, List<Alarm>> integerListHashMap = getSome();
        HashMap<String, List<Alarm>> integerListHashMap1 = getSomeUnprocessed();
        model.addAttribute("integerListHashMap", integerListHashMap);
        model.addAttribute("integerListHashMap1", integerListHashMap1);
        return "someHome";
    }

    @RequestMapping("/allStatistics")
    public String allStatistics(Model model) {
        HashMap<String, List<Alarm>> integerListHashMap = getAll();
        HashMap<String, List<Alarm>> integerListHashMap1 = getAllUnprocessed();
        model.addAttribute("integerListHashMap", integerListHashMap);
        model.addAttribute("integerListHashMap1", integerListHashMap1);
        return "allStatistics";
    }

    @RequestMapping("/someStatistics")
    public String someStatistics(Model model) {
        HashMap<String, List<Alarm>> integerListHashMap = getSome();
        HashMap<String, List<Alarm>> integerListHashMap1 = getSomeUnprocessed();
        model.addAttribute("integerListHashMap", integerListHashMap);
        model.addAttribute("integerListHashMap1", integerListHashMap1);
        return "someStatistics";
    }

    @ResponseBody
    @RequestMapping("/allAlarm")
    public HashMap<String, List<Alarm>>  allAlarm(){
        return getAllUnprocessed();
    }

    @ResponseBody
    @RequestMapping("/someAlarm")
    public HashMap<String, List<Alarm>>  someAlarm(){
        return getSomeUnprocessed();
    }

    @ResponseBody
    @RequestMapping("/pushUrlAndGroupAlarm")
    public HashMap<String, List<Alarm>>  pushUrlAndGroupAlarm(String group, String pushUrl){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, List<Alarm>> integerListHashMap = new HashMap<>();
        List<Alarm> alarmList = alarmService.findByGroupAndPushUrl(group,pushUrl);
        for (Alarm alarm : alarmList) {
            alarm.setTempTime(format.format(alarm.getTime()));
            alarm.setTempTime2(format2.format(alarm.getTime()));
        }
        integerListHashMap.put(group,alarmList);
        return integerListHashMap;
    }

    @ResponseBody
    @RequestMapping("/groupAlarm")
    public HashMap<String, List<Alarm>>  groupAlarm(String group){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, List<Alarm>> integerListHashMap = new HashMap<>();
        List<Alarm> alarmList = alarmService.findByGroup(group);
        for (Alarm alarm : alarmList) {
            alarm.setTempTime(format.format(alarm.getTime()));
            alarm.setTempTime2(format2.format(alarm.getTime()));
        }
        integerListHashMap.put(group,alarmList);
        return integerListHashMap;
    }

    /**
     * 获取客户端获取报警数据并插入数据库
     */
    @SaveAlarm(true)
    @ResponseBody
    @RequestMapping("/saveAlarm")
    public void saveAlarm(String ip,String type,String group, String name,String time,String pushUrl,@RequestParam("image") MultipartFile[] multipartFiles){
        newAlarm = null;
        Long ffTime,fTime,nowTime ,eTime,eeTime;
        nowTime = Long.parseLong(time);
        ffTime = nowTime-2;fTime = nowTime-1;eTime=nowTime+1;eeTime=nowTime+2;
        List<Alarm> alarms = alarmService.findByTimeAndGroupAndIpAndType(ffTime,fTime,nowTime ,eTime,eeTime,group,ip,type);
        if(alarms.size()==0){
            String suffix = pinyinUtil(group)+ip;
            newAlarm = alarmService.saveAlarm(ip,type,group,"http://"+pushUrl+"/live?app=live&stream="+suffix,name,time,multipartFiles);
        }
    }

    /**
     * 获取客户端获取回滚视频并插入数据库
     */
    @SaveAlarm(true)
    @ResponseBody
    @RequestMapping("/saveVideo")
    public void saveAlarm(String beginTime, String endTime, @RequestParam("video") MultipartFile[] multipartFiles){
        videoService.saveVideo(beginTime,endTime,multipartFiles);
    }

    /**
     * 获取上述插入的数据立马发给微信公众号
     */
    public void sendMessage(){
        if (newAlarm!=null){
            alarmService.sendMessage(newAlarm);
        }
    }

    /**
     * 获取客户端那边的设备状态值
     * @param group
     * @param ip
     * @param online
     */
    @ResponseBody
    @RequestMapping("/getStatus")
    public void getStatus(String group,String ip,String online,String pushUrl){
        String suffix = pinyinUtil(group)+ip;
        String pushUrl1 = "http://"+pushUrl+"/live?app=live&stream="+suffix;
        List<String> list = new ArrayList<>();
        list.add(group);
        list.add(pushUrl1);
        list.add(online);
        map.put(group+ip,list);
    }

    /**
     * 返回设备状态
     * @return map
     */
    @ResponseBody
    @RequestMapping("/returnStatus")
    public HashMap<String, List<String>> useStatus(){
        return map;
    }

    /**
     * 微信获取报警信息列表
     * @return jsonObject
     */
    @RequestMapping("/wxAlarmList")
    public ModelAndView wxAlarmList(String time, String group){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ModelAndView modelAndView = new ModelAndView();
        List<Alarm> alarmList = new ArrayList<>();
        if(group.equals("")||group==null){
            alarmList = null;
        }else {
            if (time.equals("week")){
                alarmList = alarmService.findByWeekAndGroup(group);
            }else if (time.equals("month")){
                alarmList = alarmService.findByMonthAndGroup(group);
            }else if (time.equals("all")){
                alarmList = alarmService.findByGroup(group);
            }
            for (Alarm a:alarmList) {
                a.setTempTime2(format.format(a.getTime()));
                if (a.getType().equals("2")){
                    a.setType("火焰报警");
                }else if (a.getType().equals("3")){
                    a.setType("遮挡报警");
                }else if (a.getType().equals("4")){
                    a.setType("异物入侵");
                }
            }
        }
        modelAndView.setViewName("wxAlarmList");
        modelAndView.addObject("alarmList", alarmList);
        return modelAndView;
    }

    /**
     * 微信获取报警详情
     * @return jsonObject
     */
    @RequestMapping("/wxAlarmDetail")
    public ModelAndView wxAlarmDetail(String id){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ModelAndView modelAndView = new ModelAndView();
        Alarm alarm = alarmService.findById(Integer.parseInt(id));
        if (alarm.getType().equals("2")){
            alarm.setType("火焰报警");
        }else if (alarm.getType().equals("3")){
            alarm.setType("遮挡报警");
        }else if (alarm.getType().equals("4")){
            alarm.setType("异物入侵");
        }
        alarm.setTempTime2(format.format(alarm.getTime()));
        modelAndView.setViewName("wxAlarmDetail");
        modelAndView.addObject("alarmDetail", alarm);
        return modelAndView;
    }

    /**
     * 获取所有设备信息,数据去重
     *
     * @return integerListHashMap
     */
    private HashMap<String, List<Alarm>> getAll() {
        List<Alarm> alarmList = alarmRepository.findAll();
        // 根据group分组，把组名记在groupNameList里
        List<String> groupNameList = generateGroupNameList(alarmList);
        //分组，得到最终的map<组名：设备List>
        return classificationDeduplication(groupNameList,alarmList);
    }


    /**
     * 获取所有设备信息,数据未去重
     *
     * @return integerListHashMap
     */
    private HashMap<String, List<Alarm>> getAllUnprocessed() {
        List<Alarm> alarmList = alarmRepository.findAll();
        // 根据group分组，把组名记在groupNameList里
        List<String> groupNameList = generateGroupNameList(alarmList);
        //分组，得到最终的map<组名：设备List>
        return classification(groupNameList,alarmList);
    }

    /**
     * 获取当前用户的设备信息,数据去重
     *
     * @return integerListHashMap
     */
    private HashMap<String, List<Alarm>> getSome() {
        HashMap<String, List<Alarm>> integerListHashMap = new HashMap<>();
        String groupName = sysUserService.getLoginUser().getUsername();
        List<Alarm> alarmList = alarmService.findByGroup(groupName);
        integerListHashMap.put(groupName, deduplication(alarmList));
        return integerListHashMap;
    }

    /**
     * 获取当前用户的设备信息,数据未去重
     *
     * @return integerListHashMap
     */
    private HashMap<String, List<Alarm>> getSomeUnprocessed() {
        HashMap<String, List<Alarm>> integerListHashMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String groupName = sysUserService.getLoginUser().getUsername();
        List<Alarm> alarmList = alarmService.findByGroup(groupName);
        for (Alarm alarm : alarmList) {
            alarm.setTempTime(format.format(alarm.getTime()));
            alarm.setTempTime2(format2.format(alarm.getTime()));
        }
        integerListHashMap.put(groupName, alarmList);
        return integerListHashMap;
    }

    /**
     * 生成组名列表
     * @return temp
     */
    private List<String> generateGroupNameList(List<Alarm> alarmList){
        List<String> groupNameList = new ArrayList<>();
        for (Alarm alarm : alarmList) {
            if (groupNameList.size() == 0) {
                groupNameList.add(alarm.getGroup());
            }
            int count = 0;
            for (int i = 0; i < groupNameList.size(); i++) {
                if (!groupNameList.get(i).equals(alarm.getGroup())) {
                    count++;
                }
                if (count == groupNameList.size()) {
                    groupNameList.add(alarm.getGroup());
                }
            }
        }
        return groupNameList;
    }

    /**
     * 不去重分组
     * @return temp
     */
    private HashMap<String, List<Alarm>> classification(List<String> groupNameList,List<Alarm> alarmList){
        HashMap<String, List<Alarm>> integerListHashMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String s : groupNameList) {
            List<Alarm> childAlarmList = new ArrayList<>();
            for (Alarm alarm : alarmList) {
                if (s.equals(alarm.getGroup())) {
                    alarm.setTempTime(format.format(alarm.getTime()));
                    alarm.setTempTime2(format2.format(alarm.getTime()));
                    childAlarmList.add(alarm);
                }
            }
            integerListHashMap.put(s, childAlarmList);
        }
        return integerListHashMap;
    }

    /**
     * 去重分组
     * @return temp
     */
    private HashMap<String, List<Alarm>> classificationDeduplication(List<String> groupNameList,List<Alarm> alarmList){
        HashMap<String, List<Alarm>> integerListHashMap = new HashMap<>();
        for (String s : groupNameList) {
            List<Alarm> childAlarmList = new ArrayList<>();
            for (Alarm alarm : alarmList) {
                if (s.equals(alarm.getGroup())) {
                    childAlarmList.add(alarm);
                }
            }
            integerListHashMap.put(s, deduplication(childAlarmList));
        }
        return integerListHashMap;
    }

    /**
     * List去重
     * @return alarmList
     */
    private List<Alarm> deduplication(List<Alarm> alarmList){
        for (int i = 0; i < alarmList.size() - 1; i++) {
            for (int j = alarmList.size() - 1; j > i; j--) {
                if (alarmList.get(j).getPushUrl().equals(alarmList.get(i).getPushUrl())) {
                    alarmList.remove(j);
                }
            }
        }
        return alarmList;
    }

    /**
     * 汉字转拼音
     * @param ChineseLanguage
     * @return
     */
    String pinyinUtil(String ChineseLanguage){
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); // 输出拼音全部小写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
        try {
            for (int i=0; i<cl_chars.length; i++){
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")){// 如果字符是中文,则将中文转为汉语拼音
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {// 如果字符不是中文,则不转换
                    hanyupinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
        }
        return hanyupinyin;
    }
}
