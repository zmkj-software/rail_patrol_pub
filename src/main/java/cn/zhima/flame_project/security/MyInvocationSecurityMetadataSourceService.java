package cn.zhima.flame_project.security;

import cn.zhima.flame_project.entity.SysRoleRes;
import cn.zhima.flame_project.repository.SysResRepository;
import cn.zhima.flame_project.repository.SysRoleRepository;
import cn.zhima.flame_project.repository.SysRoleResRepository;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 权限过滤器实现动态的权限验证
 * 它的主要责任就是当访问一个url时，返回这个url所需要的访问权限
 *
 * @author 冫Soul丶
 */
@Component
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    final
    SysResRepository sysResRepository;
    final
    SysRoleResRepository sysRoleResRepository;
    final
    SysRoleRepository sysRoleRepository;

    public MyInvocationSecurityMetadataSourceService(SysResRepository sysResRepository, SysRoleResRepository sysRoleResRepository, SysRoleRepository sysRoleRepository) {
        this.sysResRepository = sysResRepository;
        this.sysRoleResRepository = sysRoleResRepository;
        this.sysRoleRepository = sysRoleRepository;
    }

    private static HashMap<String, Collection<ConfigAttribute>> map = null;

    /**
     * 初始化资源对应的所有角色
     */
    public void loadResourceDefine() {
        map = new HashMap<>(16);
        //角色权限中间表所有数据
        List<SysRoleRes> sysRoleResList = sysRoleResRepository.findAll();
        //某个资源可以被哪些角色访问
        for (SysRoleRes sysRoleRes : sysRoleResList) {
            String url = sysResRepository.findById(sysRoleRes.getResId()).get().getUrl();
            String roleName = sysRoleRepository.findById(sysRoleRes.getRoleId()).get().getName();
            ConfigAttribute role = new SecurityConfig(roleName);
            if (map.containsKey(url)) {
                map.get(url).add(role);
            } else {
                List<ConfigAttribute> list = new ArrayList<>();
                list.add(role);
                map.put(url, list);
            }
        }
    }

    /**
     * 当接收到一个http请求时, filterSecurityInterceptor会调用的方法.
     * 参数object是一个包含url信息的HttpServletRequest实例.
     * 这个方法要返回请求该url所需要的所有权限集合
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //object中包含用户请求的request信息
        HttpServletRequest request = ((FilterInvocation) o).getHttpRequest();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            String url = it.next();
            if (new AntPathRequestMatcher(url).matches(request)) {
                return map.get(url);
            }
        }
        return null;
    }

    /**
     * 此处方法如果做了实现，返回了定义的权限资源列表，
     * Spring Security会在启动时校验每个ConfigAttribute是否配置正确，
     * 如果不需要校验，这里实现方法，方法体直接返回null即可。
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        loadResourceDefine();
        return null;
    }

    /**
     * 方法返回类对象是否支持校验，
     * web项目一般使用FilterInvocation来判断，或者直接返回true
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
