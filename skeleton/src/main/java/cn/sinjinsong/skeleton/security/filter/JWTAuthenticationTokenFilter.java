package cn.sinjinsong.skeleton.security.filter;

import cn.sinjinsong.skeleton.properties.AuthenticationProperties;
import cn.sinjinsong.skeleton.security.domain.TokenCheckResult;
import cn.sinjinsong.skeleton.security.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        System.out.println("经过JWTAuthenticationTokenFilter");
        //拿到token
        String token = request.getHeader(AuthenticationProperties.AUTH_HEADER);
        //验证token，如果无效，结果返回exception；如果有效，结果返回username
        TokenCheckResult result = tokenManager.checkToken(token);
        if (!result.isValid()) {
            System.out.println("Token无效");
            request.setAttribute(AuthenticationProperties.EXCEPTION_ATTR_NAME, result.getException());
        } else {
            System.out.println("checking authentication " + result);
            UserDetails userDetails = userDetailsService.loadUserByUsername(result.getUsername());

            //如果未登录
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                        request));
                System.out.println("authenticated user " + result + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
