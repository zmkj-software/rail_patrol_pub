package cn.zhima.flame_project.tools;

import java.lang.annotation.*;

/**
 * @author 冫Soul丶
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SaveAlarm {
    boolean value() default false;
}
