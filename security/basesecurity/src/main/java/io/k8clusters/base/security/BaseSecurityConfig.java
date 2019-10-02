package io.k8clusters.base.security;

import io.k8clusters.base.filters.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@EnableWebSecurity // Enable security config. This annotation denotes config for spring security.
@EnableAutoConfiguration
public class BaseSecurityConfig {

    private static void enableCorsCsrf(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .csrf().disable()
                .cors();
    }


    @Configuration
    @Order(10)
    public static class ActuatorBasicSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin")
                    .password(passwordEncoder().encode("admin123")).authorities("ADMIN").roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/actuator/**").authorizeRequests().anyRequest().authenticated().and()
                    .httpBasic();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Configuration
    @Order(20)
    public class SwaggerActuatorBasicSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/swagger**").authorizeRequests().anyRequest()
                    .hasRole("ADMIN").and().httpBasic();
        }
    }



    @Configuration
    @Order(40)
    public class BootAdminBasicSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/boot-admin**").authorizeRequests().anyRequest()
                    .hasRole("ADMIN").and().httpBasic();
        }
    }



    @Configuration
    @Order(50)
    public class ApiJwtSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            BaseSecurityConfig.enableCorsCsrf(http);
            http
                    // make sure we use stateless session; session won't be used to store user's
                    // state.
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .antMatcher("/api/**")
                    // handle an authorized attempts
                    .exceptionHandling()
                    .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
                    // Add a filter to validate the tokens with every request
                    .addFilterAfter(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    // authorization requests config
                    .authorizeRequests()
                    .antMatchers("/api/**")
                    .authenticated();
        }
    }


    /**
     * Only works for endpoints with no authentication.
     * If any endpoint being Authenticated, needs to be handled again in that Bean
     */
    @Configuration
    @Order(90)
    public static class CorsSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            BaseSecurityConfig.enableCorsCsrf(http);
        }
    }

    @Configuration
    @Order(100)
    public static class NoAuthSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers("/actuator/hystrix.stream");
            web.ignoring().antMatchers("/turbine.stream");
            web.ignoring().antMatchers("/actuator/health");
            web.ignoring().antMatchers("/HealthCheck");
            web.ignoring().antMatchers("/admin");
            web.ignoring().antMatchers("/health");
        }
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PATCH", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList(
                "accept",
                "Origin",
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Access-Control-Allow-Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
