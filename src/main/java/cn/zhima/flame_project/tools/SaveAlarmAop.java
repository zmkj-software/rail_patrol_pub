package cn.zhima.flame_project.tools;

import cn.zhima.flame_project.controller.RoutingController;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 冫Soul丶
 */
@Aspect
@Component
public class SaveAlarmAop {

    final
    RoutingController routingController;

    public SaveAlarmAop(RoutingController routingController) {
        this.routingController = routingController;
    }

    @Pointcut("@annotation(cn.zhima.flame_project.tools.SaveAlarm)")
    public void pointcut(){}


    @After("pointcut()")
    public void sendMessage(){
        routingController.sendMessage();
    }
}
