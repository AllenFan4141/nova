package com.kdgcsoft.web.config.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.config.NovaProperties;
import com.kdgcsoft.web.config.jwt.cache.JwtTokenCache;
import com.kdgcsoft.web.config.jwt.cache.LocalJwtTokenCache;
import com.kdgcsoft.web.config.jwt.cache.RedisJwtTokenCache;
import com.kdgcsoft.web.config.security.detailservice.NormalUserDetailService;
import com.kdgcsoft.web.config.security.detailservice.RootUserDetailService;
import com.kdgcsoft.web.config.security.filter.JwtAuthenticationTokenFilter;
import com.kdgcsoft.web.config.security.handler.JwtAuthenticationHandler;
import com.kdgcsoft.web.config.security.handler.FormAuthenticationHandler;
import com.kdgcsoft.web.config.security.scanner.AnonAccess;
import com.kdgcsoft.web.config.security.scanner.AnonAccessScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fyin
 * @date 2022年07月27日 16:10
 * 采用spring-security 5.7以后的新写法,之前继承 WebSecurityConfigurerAdapter的方式已经弃用
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(AnonAccessScanner.class)
@Slf4j
public class SecurityConfiguration {
    @Resource
    NovaProperties novaProperties;

    @Autowired
    AnonAccessScanner anonAccessScanner;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /**
     * JWT认证处理类
     */
    @Autowired
    private JwtAuthenticationHandler jwtAuthenticationHandler;

    @Autowired
    private FormAuthenticationHandler formAuthenticationHandler;

    /**
     * 系统默认的白名单
     **/
    public static String[] DEF_WHITE_LIST = {"/static/**", "/webjars/**", "/anon/**","/doc.html", "/swagger-resources/**", "/v2/api-docs"};

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        /**静态资源放行放到了HttpSecurity中,新版springboot建议放在HttpSecurity中**/
        return (web) -> web.httpFirewall(new DefaultHttpFirewall());
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Application will run at fontbackend:" + novaProperties.isFontBackend());

        /**禁用csrf防御  springSecurity默认会开启,开启后会要求所有的post请求都需要带上csrf token, 因为不使用session**/
        http.csrf().disable();
        /**允许跨域**/
        http.cors();
        /**去除x-frame-options header 允许iframe嵌入**/
        http.headers().frameOptions().disable();

        /**注解 {@link AnonAccess} 标记的controller方法允许匿名访问的url**/
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        anonAccessScanner.getAnonymousUrls().forEach(url -> registry.antMatchers(url).permitAll());

        List<String> whiteList = new ArrayList<>();
        whiteList.add(novaProperties.getLoginPageUrl());
        whiteList.add(novaProperties.getLoginUrl());
        whiteList.add(novaProperties.getLogoutUrl());
        CollUtil.addAll(whiteList, DEF_WHITE_LIST);
        CollUtil.addAll(whiteList, StrUtil.split(novaProperties.getWhiteList(), StrUtil.COMMA, true, true));
        /**默认白名单里面的内容允许访问**/
        http.authorizeRequests()
                .antMatchers(ArrayUtil.toArray(whiteList, String.class))
                .permitAll()
                /**剩余的都需要认证后才能访问**/
                .anyRequest()
                .authenticated();
        /**前后端分离项目启用不同的安全策略**/
        if (novaProperties.isFontBackend()) {
            /**前后端分离模式取消默认表单登录方式**/
            http.formLogin().disable();
            /**前后端分离模式,无需session**/
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


            /**添加JWT filter,一般情况下是配置在UsernamePasswordAuthenticationFilter之前,
             * 但是放在这里可以保证退出的请求也会被JWT拦截器拦截到,从而将JWT字符串注入到认证器中,保证能够发布logoutSuccessEvent事件**/
            http.securityContext(httpSecuritySecurityContextConfigurer -> httpSecuritySecurityContextConfigurer.requireExplicitSave(true));
            http.addFilterAfter(jwtAuthenticationTokenFilter, SecurityContextHolderFilter.class);

            /**认证失败处理类**/
            http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationHandler);
            /**添加Logout filter,用来处理退出登录的返回数据**/


            /**前后端分离方式的时候 因为没有使用到formLogin 所以spring不会自动帮我们建立login地址的映射,
             * 所以我们需要在自己的controller中定义接收登录请求的地址(并放行) 并在代码中手动的进行用户认证
             * 同样,因为我们使用http.logout配置了logout逻辑,spring会自动为我们创建logout的地址映射,所以在自己的controller中定义的logout的请求地址并不会被调用**/
            http.logout().logoutUrl(novaProperties.getLogoutUrl()).logoutSuccessHandler(jwtAuthenticationHandler);
        } else {
            /**非前后端分离方式的时候 因为使用到formLogin 所以spring会自动帮我们建立login地址的映射,不需要在controller中去自定登录逻辑,spring会自动帮我们进行认证
             * 并且在自己的controller里定义的login的请求地址也不会被调用
             * 同样,因为我们使用http.logout配置了logout逻辑,spring会自动为我们创建logout的地址映射,所以在自己的controller中定义的logout的请求地址并不会被调用**/
            http.formLogin().loginPage(novaProperties.getLoginPageUrl())
                    .loginProcessingUrl(novaProperties.getLoginUrl())
                    /*登陆成功的默认跳转地址,false为从哪里来跳到哪里去,true为无论从哪里来 都跳到index
                     * 配置了successHandler 则会失效,统一在successHandler里处理
                     * 参照{@link org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler}
                     **/
                    .defaultSuccessUrl("/", true)
                    .successHandler(formAuthenticationHandler)
                    .failureHandler(formAuthenticationHandler);
            /*
             * 加入此设置可以使SpringSecurity使用SessionRegistry来管理在线用户
             */
            http.sessionManagement()
                    .maximumSessions(this.novaProperties.getMaxSession())
                    .expiredUrl(novaProperties.getLoginPageUrl());
            http.logout()
                    .logoutUrl(novaProperties.getLogoutUrl())
                    .logoutSuccessHandler(formAuthenticationHandler)
                    .logoutSuccessUrl(novaProperties.getLoginPageUrl())
                    .invalidateHttpSession(true);
        }
        return http.build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RootUserDetailService rootUserDetailService() {
        return new RootUserDetailService();
    }

    @Bean
    public NormalUserDetailService normalUserDetailService() {
        return new NormalUserDetailService();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        /**
         * 添加两种认证策略,一种是root用户的 一种是普通用户的
         */
        DaoAuthenticationProvider rootProvider = new DaoAuthenticationProvider();
        rootProvider.setUserDetailsService(rootUserDetailService());
        rootProvider.setPasswordEncoder(bCryptPasswordEncoder());

        DaoAuthenticationProvider normalProvider = new DaoAuthenticationProvider();
        normalProvider.setUserDetailsService(normalUserDetailService());
        normalProvider.setPasswordEncoder(bCryptPasswordEncoder());

        ProviderManager providerManager = new ProviderManager(rootProvider, normalProvider);
        //设置认证管理器使用事件发布器来进行认证相关事件的发布,默认是一个空的事件发布器并不会真的发布事件
        providerManager.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));
        return providerManager;
    }

    @Bean
    public JwtTokenCache jwtTokenCache() {
        //如果使用redis缓存则创建redis缓存bin
        if (StrUtil.equals(WebConst.REDIS_JWT_CACHE, novaProperties.getJwtCacheType())) {
            return new RedisJwtTokenCache();
        } else {
            return new LocalJwtTokenCache();
        }
    }
}
