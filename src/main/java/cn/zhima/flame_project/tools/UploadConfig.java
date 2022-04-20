package cn.zhima.flame_project.tools;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/alarmImage/**").addResourceLocations("file:C:/flame_project/alarmImage/");
        registry.addResourceHandler("/video/**").addResourceLocations("file:C:/flame_project/video/");
    }
}
