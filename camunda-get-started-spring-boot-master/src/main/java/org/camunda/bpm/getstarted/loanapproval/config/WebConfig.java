package org.camunda.bpm.getstarted.loanapproval.config;

import org.camunda.bpm.getstarted.loanapproval.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebConfig {

    @Value("*")
    private String origins;

    @Value("false")
    private boolean sessionSecured;

    @Autowired
    private Utility utility;

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
                SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
                sessionCookieConfig.setHttpOnly(true);
                sessionCookieConfig.setSecure(sessionSecured);
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource())
//		.and()
//		.authorizeRequests()
//		.antMatchers(HttpMethod.POST,
//				"/public/CheckTranLimitPro",
//				"/public/CheckTranLimitByTranCode"
//		).access("hasIpAddress('10.86.144.0/24')")
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers()
                .xssProtection()
                .and()
                .contentSecurityPolicy("script-src 'self'")
                .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
                .and()
                .defaultsDisabled()
                .contentTypeOptions();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(UtilityHelper.ImmutableList(origins));
        configuration.setAllowedOriginPatterns(utility.ImmutableList(origins));
        configuration.setAllowedMethods(utility.ImmutableList("POST"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(utility.ImmutableList("Authorization", "Cache-Control", "Content-Type", "X-Token", "X-Real-IP", "X-Forwarded-For", "Referer"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
