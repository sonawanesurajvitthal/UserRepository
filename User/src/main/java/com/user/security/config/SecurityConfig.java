package com.user.security.config;

//import com.user.constants.ApiConstants;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
public class SecurityConfig {

//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeHttpRequests ->
//                        authorizeHttpRequests
//                                .requestMatchers(ApiConstants.USER_CONTROLLER + ApiConstants.USER_LIST)
//                                .permitAll()
//                                .anyRequest()
//                                .authenticated())
//                .formLogin(withDefaults());
//
//        return httpSecurity.build();
//    }
}
