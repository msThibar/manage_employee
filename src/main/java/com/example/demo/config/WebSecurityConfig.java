package com.example.demo.config;

import com.example.demo.security.jwt.JwtEntryPoint;
import com.example.demo.security.jwt.JwtTokenFilter;
import com.example.demo.security.userprincal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    JwtEntryPoint jwtEntryPoint;
    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManager();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        /*auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder().encode("user1"))
                .authorities("ROLE_USER");*/

        // Các User trong bộ nhớ (MEMORY).

        auth.inMemoryAuthentication().withUser("user").password("$2a$12$2HjMz60cmuwDR4MZ8VEtTOlgArw8NFvFIJOJZMU8Fp43vgFmlN4o6").roles("EMPLOYEE");
        auth.inMemoryAuthentication().withUser("Admin").password("$2a$12$EB380oqnyP3NEj9Vu9YoMOHzH1Me5LvA5YbfU6OpMu4MHmcm6vADe").roles("EMPLOYEE, ADMIN");

        // Các User trong Database
        auth.userDetailsService(userDetailService);
    }
    @Override
    public void configure(HttpSecurity http)throws Exception{
        http.cors().and().csrf().disable();
        //truy cập khi chưa login
        http.authorizeRequests().antMatchers("/", "/login", "/api/login", "/checkin", "/index", "/test/**", "/test/**", "/j_spring_security_check", "/mail/**", "/loginWithGoogle").permitAll();
        //quyền admin và employee
        http.authorizeRequests().antMatchers("/User/**", "/api/User/**").hasAnyAuthority("admin", "employee");
        //quyền admin
        http.authorizeRequests().antMatchers("/Admin/**", "/api/Admin/**").hasAuthority("admin");
        //chuyển hướng khi không đủ quyền
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error");
        //custom login
        http.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutUrl("/logout");

        http.authorizeRequests().anyRequest().authenticated();
        http.authorizeRequests().and().exceptionHandling().authenticationEntryPoint(jwtEntryPoint).and().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
