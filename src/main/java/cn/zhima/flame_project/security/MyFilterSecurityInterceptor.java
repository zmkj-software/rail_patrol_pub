package cn.zhima.flame_project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


/**
 *
 * 自定义拦截器(使用自定义的 MyAccessDecisionManager 和 MyInvocationSecurityMetadataSource)
 * @author 冫Soul丶
 */
@Component
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    private final FilterInvocationSecurityMetadataSource myInvocationSecurityMetadataSource;

    public MyFilterSecurityInterceptor(FilterInvocationSecurityMetadataSource myInvocationSecurityMetadataSource) {
        this.myInvocationSecurityMetadataSource = myInvocationSecurityMetadataSource;
    }

    /**
     * 调用自定义的决策器
     */
    @Autowired
    public void setMyAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager) {
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    /**
     * 调用自定义过滤器
     * @return this.myInvocationSecurityMetadataSource
     */
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.myInvocationSecurityMetadataSource;
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }
}