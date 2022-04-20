package cn.zhima.flame_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "alarm")
public class Alarm implements Serializable {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309426L;

    /**
     * 编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 报警时间
     */
    @Column(name = "time")
    private Date time;

    /**
     * 设备ip
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 报警类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 报警图片
     */
    @Column(name = "img")
    private String img;

    /**
     * 所属分组(对应了用户名)
     */
    @Column(name = "`group`")
    private String group;

    /**
     * 推流路径
     */
    @Column(name = "push_url")
    private String pushUrl;

    /**
     * 设备名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 备注，预留字段
     */
    @Column(name = "remark")
    private String remark;

    @Transient
    private String tempTime;

    @Transient
    private String tempTime2;
}
