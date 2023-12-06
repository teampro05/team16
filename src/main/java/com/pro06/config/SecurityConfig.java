package com.pro06.config;
import com.pro06.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // stateless한 rest api를 개발할 것이므로 csrf 공격에 대한 옵션은 꺼둔다.
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                // 특정 URL에 대한 권한 설정.
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests
                            .requestMatchers("/video/**").permitAll()
                            .requestMatchers("/user/**").authenticated() // 인증된, 로그인 한 사람만 접근 가능
                            .requestMatchers("/admin/**").hasAuthority("ADMIN") // admin만 접근 가능
                            .requestMatchers("/css/**", "/js/**", "/upload/**", "/cleditor/**", "/scss/**",
                                    "/vendors/**", "/ckeditor/**", "/webfonts/**", "/resource/**", "/assets/**")
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
                            .passwordParameter("pw")
                            ;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}