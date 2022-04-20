package cn.zhima.flame_project.security;

import cn.zhima.flame_project.security.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @author 冫Soul丶
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    final MyUserDetailsService myUserDetailsService;

    public SecurityConfiguration(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * 自定义用户
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //数据库用户
        auth.userDetailsService(myUserDetailsService)
                //密码加密
                .passwordEncoder(new PasswordEncoder() {
                    //对密码进行加密
                    @Override
                    public String encode(CharSequence charSequence) {
                        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                    }

                    //对密码进行判断匹配
                    @Override
                    public boolean matches(CharSequence charSequence, String s) {
                        String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                        boolean res = s.equals(encode);
                        System.out.println(s);
                        return res;
                    }
                });
    }

    /**
     * 对于security来说，只识别post级别的请求，
     * 如果在地址栏输入通行资源（单纯访问页面，get请求），将不会执行到安全逻辑
     * 例如："/loginExit"是通行资源，可get可post；
     * get时只是作为页面访问，而post是便执行内置的退出逻辑，清空了session等，再跳转到/loginExit
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录、失败，成功
        http.formLogin()
                .loginPage("/login")
                .failureUrl("/loginError")
                .successHandler(new LoginSuccessHandler())
                .permitAll()

                //禁用csrf和拒绝访问
                .and()
                .csrf()
                .disable()//禁用csrf
                .exceptionHandling()
                .accessDeniedPage("/401")

                //允许某些未登录通行资源；登录后任何请求均可以访问
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/loginError", "/loginExit","/loginExpired", "/getStatus","/saveAlarm","/saveVideo","/video/**","/alarmImage/**","/alarmImageNative/**","/wxAlarmDetail","/wxAlarmList","/verify_wx_token","/401", "/css/**", "/js/**", "/img/**").permitAll()
                .anyRequest()
                .authenticated();
        //登出
        http.logout().logoutUrl("/loginExit_post").logoutSuccessUrl("/loginExit");
        //只能同一个地方登录
        //http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/loginExpired");
    }

//    /**
//     * 退出登录清理session
//     * @return
//     */
//    @Bean
//    HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
}
