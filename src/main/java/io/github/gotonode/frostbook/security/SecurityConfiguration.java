package io.github.gotonode.frostbook.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
@Order(0)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Qualifier("customUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        System.out.println("Configured security");

        // TODO: Disable this for production!
        httpSecurity.csrf().disable();

        httpSecurity.headers().frameOptions().sameOrigin();

        httpSecurity.authorizeRequests()
                .antMatchers("/accounts", "/accounts/**").permitAll()
                .antMatchers("/h2-console", "/h2-console/**").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/css", "/css/**").permitAll()
                .antMatchers(HttpMethod.GET, "/img", "/img/**").permitAll()
                .antMatchers(HttpMethod.GET, "/js", "/js/**").permitAll()
                .antMatchers("/debug", "/debug/**").permitAll()
                .antMatchers(HttpMethod.GET, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/search").permitAll()
                .antMatchers(HttpMethod.POST, "/search").permitAll()
                .antMatchers(HttpMethod.GET, "/id/**").permitAll()
                .antMatchers(HttpMethod.GET, "/about").permitAll()
                .antMatchers(HttpMethod.GET, "/help").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginProcessingUrl("/login").loginPage("/login").usernameParameter("handle").passwordParameter("password").permitAll().and() //loginProcessingUrl("/login").defaultSuccessUrl("/id").failureUrl("/login?error").permitAll().and()
                .logout().logoutSuccessUrl("/").permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
