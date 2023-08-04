package com.poho.stuup.api.interceptor;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CheckResult;
import com.poho.common.custom.ResponseMsg;
import com.poho.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 在请求处理之前进行调用（Controller方法调用之前
 *
 * @Author: wupeng
 * @Description:
 * @Date: Created in 20:21 2019-06-09
 * @Modified By:
 */
@Component
public class MicrovanInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || "".equals(token)) {
            this.setErrorResponse(response, ResponseMsg.noLogin());
            return false;
        }
        // 验证JWT的签名，返回CheckResult对象
        CheckResult checkResult = JwtUtil.parseJwt(token);
        if (checkResult.isSuccess()) {
            Claims claims = checkResult.getClaims();
            //剩余过期时间(分钟)
            long duration = DateUtil.between(new Date(), claims.getExpiration(), DateUnit.MINUTE, false);
            if (duration > 0 && duration <= CommonConstants.DURATION_MINUTE) { //如果还有xx分钟自动刷新token
                this.setErrorResponse(response, ResponseMsg.refreshToken(claims.getId()));
                return false;
            }
            request.setAttribute(CommonConstants.CLAIMS_USER, claims);
            return true;
        } else {
            this.setErrorResponse(response, ResponseMsg.loginExpire());
            return false;
        }
    }

    /**
     * @param response
     * @param message
     * @throws IOException
     */
    protected void setErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
