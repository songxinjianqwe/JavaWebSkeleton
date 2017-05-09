package cn.sinjinsong.skeleton.security.config;

import cn.sinjinsong.skeleton.security.endpoint.JWTAuthenticationEntryPoint;
import cn.sinjinsong.skeleton.security.filter.JWTAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RESTWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JWTAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JWTAuthenticationTokenFilter();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 添加JWT filter
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //添加JWTFilter
                .authorizeRequests()
                //允许访问静态资源
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js").permitAll()
                //允许访问swagger
                .antMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-resources/configuration/ui",
                        "/swagge‌​r-ui.html",
                        "/swagger-resources/configuration/security").permitAll()
                //获取图片验证码
                .antMatchers(HttpMethod.GET, "/captchas").permitAll()
                //检查用户名是否重复
                .antMatchers(HttpMethod.GET, "/users/*/duplication").permitAll()
                //注册
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                //获取头像
                .antMatchers(HttpMethod.GET, "/users/*/avatar").permitAll()
                //用户激活
                .antMatchers(HttpMethod.GET, "/users/*/activation").permitAll()
                //用户申请忘记密码
                .antMatchers(HttpMethod.GET, "/users/*/password/reset_validation").permitAll()
                //用户忘记密码后重置密码
                .antMatchers(HttpMethod.PUT, "/users/*/password").permitAll()
                //获取token
                .antMatchers(HttpMethod.POST, "/tokens").permitAll().and()
                //除上面外的所有请求全部需要鉴权认证
                .authorizeRequests().anyRequest().authenticated().and()
                //Filter要放到是否认证的配置之后
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        httpSecurity
                .headers().cacheControl();
    }
}
