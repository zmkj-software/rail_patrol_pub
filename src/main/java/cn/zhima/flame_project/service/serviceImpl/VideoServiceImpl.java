package cn.zhima.flame_project.service.serviceImpl;

import cn.zhima.flame_project.entity.Video;
import cn.zhima.flame_project.repository.VideoRepository;
import cn.zhima.flame_project.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author 冫Soul丶
 */
@Service
public class VideoServiceImpl implements VideoService {

    final
    VideoRepository videoRepository;

    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /**
     * 获取上传路径
     */
    @Value("${web.video-upload-path}")
    private String path;

    @Override
    public void saveVideo(String beginTime, String endTime, MultipartFile[] multipartFiles) {
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
                Video video = new Video();
                video.setBeginTime(new Timestamp(Long.parseLong(beginTime)*1000));
                video.setEndTime(new Timestamp(Long.parseLong(endTime)*1000));
                video.setVideoUrl("/flame_project/video/"+fileName);
                video.setVideoSize(multipartFile.getSize()/1024/1024);
                video.setStatus(true);
                videoRepository.save(video);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
