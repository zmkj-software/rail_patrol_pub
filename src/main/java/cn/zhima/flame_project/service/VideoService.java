package cn.zhima.flame_project.service;

import cn.zhima.flame_project.entity.Video;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 冫Soul丶
 */
public interface VideoService {
    void saveVideo(String beginTime, String endTime, MultipartFile[] multipartFiles);
}
