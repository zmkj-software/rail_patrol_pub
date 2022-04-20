package cn.zhima.flame_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 冫Soul丶
 */
@Data
@Entity(name = "sys_res")
public class SysRes implements Serializable {
    /**
     * 序列化编号
     */
    private static final long serialVersionUID = 1556269309423L;

    /**
     * 资源编号(主键)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 资源路径
     */
    @Column(name = "url")
    private String url;

    /**
     * 资源名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 资源描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 父级资源
     */
    @Column(name = "pid")
    private Long pid;
}
