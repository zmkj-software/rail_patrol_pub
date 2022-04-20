package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 请求到的token封装实体
 *
 * @author 冫Soul丶
 */
@Data
@Component
public class AccessTokenResp {
    private String accessToken;
    private Integer expiresIn;
}
