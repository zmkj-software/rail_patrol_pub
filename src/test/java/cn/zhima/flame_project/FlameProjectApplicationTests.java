package cn.zhima.flame_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootTest
class FlameProjectApplicationTests {

    @Test
    void timeUtil() throws ParseException {
        Date date = new Date();
        String time2 = "2020-07-30 14:57:30";
        String timestamp1 = "1596092250123";
        String timestamp2 = "1596092250";

        // 13位和10位 转 Date
        System.out.println(new Timestamp(Long.parseLong(timestamp1)));
        System.out.println(new Timestamp(Long.parseLong(timestamp2)*1000));

        //Date 转 13位和10位;
        System.out.println(date.getTime());
        System.out.println((int) ((date.getTime())/1000));

        //String(yyyy-MM-dd HH:mm:ss) 转 Date
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time2));

        //Date 转 String(yyyy-MM-dd HH/mm/ss)
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH/mm/ss").format(date));

        //String(yyyy-MM-dd HH:mm:ss) 转 10位
        System.out.println((int) ((Timestamp.valueOf(time2).getTime())/1000));

        //10位 转 String(yyyy-MM-dd HH:mm:ss)
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(Long.parseLong(timestamp2)*1000)));
    }

    @Test
    void md5Util(){
        String psw = "shidaoxinxi";
        System.out.println(DigestUtils.md5DigestAsHex(psw.getBytes()));
    }
}
