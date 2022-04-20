package cn.zhima.flame_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "video")
public class Video implements Serializable {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309427L;

    /**
     * 编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 视频起始时间
     */
    @Column(name = "begin_time")
    private Date beginTime;

    /**
     * 视频终止时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 视频路径
     */
    @Column(name = "video_url")
    private String videoUrl;

    /**
     * 视频大小
     */
    @Column(name = "video_size")
    private Long videoSize;

    /**
     * 状态
     */
    @Column(name = "status")
    private Boolean status;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
