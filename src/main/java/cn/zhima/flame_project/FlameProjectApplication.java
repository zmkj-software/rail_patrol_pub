package cn.zhima.flame_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 冫Soul丶
 */
@SpringBootApplication
@EnableScheduling
public class FlameProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlameProjectApplication.class, args);
    }

}
