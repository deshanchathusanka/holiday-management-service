//package com.holiday.configuration;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
///**
// * @author : dchat
// * @since : 11/11/2020, Wed
// **/
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    /**
//     * The below instance variables are autowired and initialised from
//     * application.properties file.
//     *
//     */
//    @Autowired
//    private DataSource dataSource;
//
//    @Value("${spring.queries.users-query}")
//    private String usersQuery;
//
//    @Value("${spring.queries.roles-query}")
//    private String rolesQuery;
//
//    /**
//     * This method will check for the users for authentication provided with
//     * username and password.
//     *
//     * @param AuthenticationManagerBuilder
//     *
//     */
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//	auth.jdbcAuthentication()
//			.usersByUsernameQuery(usersQuery)
//			.authoritiesByUsernameQuery(rolesQuery)
//			.dataSource(dataSource)
//			.passwordEncoder(bCryptPasswordEncoder);
//    }
//
//    /**
//     * This method catches all urls in the application and checks if user is
//     * authentic and manages login,logout,error like pages and also guides users
//     * to role-specific urls.
//     */
//    protected void configure(HttpSecurity http) throws Exception{
//	http.authorizeRequests()
//		.antMatchers("/", "/login", "/registration").permitAll().anyRequest()
//		.authenticated().and().csrf().disable().formLogin()
//		.loginPage("/login").failureUrl("/login?error=true")
//		.defaultSuccessUrl("/user/home")
//		.usernameParameter("email")
//		.passwordParameter("password")
//		.and().logout()
//		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//		.logoutSuccessUrl("/").and().exceptionHandling()
//		.accessDeniedPage("/access-denied");
//    }
//
//    /**
//     * To ignore security on particular things like resources,jar files etc,this
//     * method is used.
//     */
//    public void configure(WebSecurity web) throws Exception {
//	web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/fonts/**", "/js/**", "/img/**");
//    }
//
//}
