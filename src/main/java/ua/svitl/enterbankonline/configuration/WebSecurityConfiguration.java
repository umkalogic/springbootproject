package ua.svitl.enterbankonline.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.svitl.enterbankonline.service.CustomUserDetailsService;


@Configuration
@ComponentScan(basePackages = {"ua.svitl.enterbankonline"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
               .authorizeRequests()
                   .antMatchers("/").permitAll()
                   .antMatchers("/login/**").permitAll()
                   .antMatchers("/registration/**").permitAll()
                   .antMatchers("/admin/**").hasAuthority("ADMIN")
                   .antMatchers("/user/**").hasAuthority("USER")
                   .antMatchers("/home/**").permitAll()
                   .antMatchers("/error/**").permitAll()
                   .anyRequest()
                   .authenticated()
                   .and().csrf().disable()
                .formLogin()
                   .loginPage("/login")
                   .loginPage("/")
                   .failureUrl("/login?error=true")
                   .defaultSuccessUrl("/default")
                   .usernameParameter("user_name")
                   .passwordParameter("password")
                   .and()
                .logout()
                   .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                   .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                        "/bootstrap/**", "/fonts/**");
    }

}
