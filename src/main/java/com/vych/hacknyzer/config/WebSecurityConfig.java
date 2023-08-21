package com.vych.hacknyzer.config;

import com.vych.hacknyzer.dto.domain.users.UserData;
import com.vych.hacknyzer.dto.repo.users.UserDataRepo;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserDataRepo userDataRepo;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager usersDetailsService(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String adminName = System.getenv("adminName");

        try {
            users.loadUserByUsername(adminName);
        } catch (UsernameNotFoundException e) {
            String adminPsw = System.getenv("adminPassword");
            UserDetails admin = User.builder()
                    .username(adminName)
                    .password(bCryptPasswordEncoder.encode(adminPsw))
                    .roles("USER", "ADMIN")
                    .build();

            UserData userData = new UserData()
                    .setUsername(adminName)
                    .setFirstName(adminName)
                    .setLastName(adminName)
                    .setAvatar(null)
                    .setSkills("");

            userDataRepo.save(userData);
            users.createUser(admin);
        }

        return users;
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                                .requestMatchers("/", "/login", "/signup", "/logout", "/error").permitAll()
                                .requestMatchers(HttpMethod.POST, "/proceed-signup").permitAll()
                                .requestMatchers("/admin", "/admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/dashboard", "/dashboard/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
//                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin().defaultSuccessUrl("/dashboard");
        return http.build();
    }
}
