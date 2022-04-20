package cn.zhima.flame_project.wx.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 请求token时用的实体
 *
 * @author 冫Soul丶
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class AccessTokenReq {
    private String accessTokenUrl;
    private String appId;
    private String appSecret;
}
