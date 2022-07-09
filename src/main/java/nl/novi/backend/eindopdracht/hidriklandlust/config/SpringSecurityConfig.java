package nl.novi.backend.eindopdracht.hidriklandlust.config;

import nl.novi.backend.eindopdracht.hidriklandlust.filter.JwtRequestFilter;
import nl.novi.backend.eindopdracht.hidriklandlust.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .formLogin().disable()
                //UserController endpoints
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**/authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**/authorities").hasRole("ADMIN")
                //AuthenticationController endpoints
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers(HttpMethod.GET, "/authenticated").authenticated()
                //ProjectController endpoints
                .antMatchers(HttpMethod.GET, "/projects").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/projects").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/projects/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/projects/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/projects/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/projects/**/assignments").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/projects/**/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/projects/**/accounts/**").hasRole("ADMIN")
                //AssignmentController endpoints
                .antMatchers(HttpMethod.GET, "/assignments").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/assignments").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/assignments/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/assignments/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/assignments/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/assignments/**/finishedWork").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/assignments/**/components/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/assignments/**/components/**").hasAnyRole("USER", "ADMIN")
                //ComponentController endpoints
                .antMatchers(HttpMethod.GET, "/components").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/components").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/components/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/components/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/components/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/components/**/file").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/components/**/file").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/components/**/file").hasAnyRole("USER", "ADMIN")
                //AccountController endpoints
                .antMatchers(HttpMethod.GET, "/accounts").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/accounts/**").hasRole("ADMIN")
                .and()
                .authorizeRequests()
                .anyRequest().denyAll()
                .and()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}