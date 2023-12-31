package com.pro06.config;
import com.pro06.controller.ErrorController;
import com.pro06.repository.NoticeRepository;
import com.pro06.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // stateless한 rest api를 개발할 것이므로 csrf 공격에 대한 옵션은 꺼둔다.
               .csrf(AbstractHttpConfigurer::disable)
               .cors(AbstractHttpConfigurer::disable)
                // 특정 URL에 대한 권한 설정.
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests
                            .requestMatchers(new AntPathRequestMatcher("/course/**"), new AntPathRequestMatcher("/error/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/Ebook/**"), new AntPathRequestMatcher("/Mbook/**"),
                                    new AntPathRequestMatcher("/Hbook/**"), new AntPathRequestMatcher("/Tbook/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/user/**"), new AntPathRequestMatcher("/board/**"),
                                    new AntPathRequestMatcher("/mycourse/**"), new AntPathRequestMatcher("/video/**"),
                                    new AntPathRequestMatcher("/myvideo/**")).authenticated() // 인증된, 로그인 한 사람만 접근 가능
                            .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ADMIN") // admin만 접근 가능
                            .requestMatchers(new AntPathRequestMatcher("/css/**"), new AntPathRequestMatcher("/js/**"),
                                    new AntPathRequestMatcher("/upload/**"), new AntPathRequestMatcher("/cleditor/**"),
                                    new AntPathRequestMatcher("/scss/**"), new AntPathRequestMatcher("/vendors/**"),
                                    new AntPathRequestMatcher("/ckeditor/**"), new AntPathRequestMatcher("/webfonts/**"),
                                    new AntPathRequestMatcher("/resource/**"), new AntPathRequestMatcher("/assets/**"),
                                    new AntPathRequestMatcher("/shop/**"))
                            .permitAll() // 모두 접근 가능
                            .anyRequest().permitAll();
                })
                // 로그인
                .formLogin((formLogin) -> {
                    formLogin
                            .loginPage("/login")
                            .loginProcessingUrl("/loginPro")
                            .failureUrl("/login?error=true")
                            .defaultSuccessUrl("/status")
                            .usernameParameter("id")
                            .passwordParameter("pw");
                })
                // 로그아웃
                .logout((logout) -> {
                    logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .invalidateHttpSession(true)
                            .logoutSuccessUrl("/");
                })
                // 인증 관련 예외, 사용하면 에러페이지 발생해서 주석처리
                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                })
                // 중복 로그인 방지
                .sessionManagement((sessionManagement) -> {
                    sessionManagement.sessionFixation().changeSessionId()
                            .maximumSessions(1)
                            .maxSessionsPreventsLogin(false)
                            .sessionRegistry(sessionRegistry());
                })
                .build();
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}