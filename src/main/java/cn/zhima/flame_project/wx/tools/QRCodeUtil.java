package cn.zhima.flame_project.wx.tools;

import cn.zhima.flame_project.wx.entity.*;

/**
 * @author 冫Soul丶
 */
public class QRCodeUtil {
    /**
     * 生成临时的以字符串为参数的二维码
     */
    public static TemporaryCode<?> initStrCode(String s) {
        TemporaryCode<Object> code = new TemporaryCode<>();
        ActionInfo<Object> actionInfo = new ActionInfo<>();
        SceneStr scene = new SceneStr();
        scene.setScene_str(s);
        actionInfo.setScene(scene);
        code.setAction_info(actionInfo);
        code.setAction_name("QR_STR_SCENE");
        //过期时间
        code.setExpire_seconds(604800);
        return code;
    }
    /**
     * 生成临时的以数字为参数的二维码
     */
    public static TemporaryCode<?> initIdCode(int i) {
        TemporaryCode<Object> code = new TemporaryCode<>();
        ActionInfo<Object> actionInfo = new ActionInfo<>();
        SceneId scene = new SceneId();
        scene.setScene_id(i);
        actionInfo.setScene(scene);
        code.setAction_info(actionInfo);
        code.setAction_name("QR_STR_SCENE");
        //过期时间
        code.setExpire_seconds(604800);
        return code;
    }
    /**
     * 生成永久的以数字为参数的二维码
     */
    public static PermanentCode<?> initIdCodePer(int i) {
        PermanentCode<Object> code = new PermanentCode<>();
        ActionInfo<Object> actionInfo = new ActionInfo<>();
        SceneId scene = new SceneId();
        scene.setScene_id(i);
        actionInfo.setScene(scene);
        code.setAction_info(actionInfo);
        code.setAction_name("QR_LIMIT_STR_SCENE");
        return code;
    }
    /**
     * 生成永久的以字符串为参数的二维码
     */
    public static PermanentCode<?> initStrCodePer(String s) {
        PermanentCode<Object> code = new PermanentCode<>();
        ActionInfo<Object> actionInfo = new ActionInfo<>();
        SceneStr scene = new SceneStr();
        scene.setScene_str(s);
        actionInfo.setScene(scene);
        code.setAction_info(actionInfo);
        code.setAction_name("QR_LIMIT_STR_SCENE");
        return code;
    }
}
