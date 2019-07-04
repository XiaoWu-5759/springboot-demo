package com.xiaowu.xwsecurity.browser;


import com.xiaowu.xwsecurity.browser.support.SimpleResponse;
import com.xiaowu.xwsecurity.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
public class BrowserSecurityController {
    // 从请求缓存中拿
    private RequestCache requestCache = new HttpSessionRequestCache();

    // 跳转 security的一个工具
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 当需要身份认证时，跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/authentication/require", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)  // 返回的状态码 return
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SavedRequest savedRequest = requestCache.getRequest(request,response);

        if (savedRequest != null){
            // 请求跳转的url
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是："+targetUrl);
            if (StringUtils.endsWithIgnoreCase(targetUrl,".html")){
                // 做跳转
                // 不是永远只传到 标准的登录页
                redirectStrategy.sendRedirect(request,response,securityProperties.getBrowser().getLoginPage());
//                跳转走了，不继续执行
             }
        }

        return new SimpleResponse("访问的服务需要身份验证");
    }
}
