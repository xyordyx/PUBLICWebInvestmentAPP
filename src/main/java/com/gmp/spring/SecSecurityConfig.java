package com.gmp.spring;

import com.gmp.security.CustomRememberMeServices;
import com.gmp.security.google2fa.CustomAuthenticationProvider;
import com.gmp.security.google2fa.CustomWebAuthenticationDetailsSource;
import com.gmp.security.location.DifferentLocationChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@ComponentScan(basePackages = {"com.gmp.security"})
// @ImportResource({ "classpath:webSecurityConfig.xml" })
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private LogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    private DifferentLocationChecker differentLocationChecker;

    public SecSecurityConfig() {
        super();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/h2/**").permitAll() // to enable access to H2 db's console
                .antMatchers("/login*","/login*", "/logout*","/expiredAccount*",
                        "/publicIndex","/badUser*","/registrationConfirm*", "/emailError*", "/resources/**").permitAll()
                .antMatchers("/user/updatePassword*","/userUpdate*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")

                .antMatchers("/invalidSession*").anonymous()
                .antMatchers("/index","/landing").hasAuthority("USER_PRIVILEGE")
                .antMatchers("/adminHome").hasAuthority("ADMIN_PRIVILEGE")
                .antMatchers("/facturedo","/factuWaitForInvoice", "/invoiceStatus.json","/stoptransactions",
                        "historicalData.json","messageStatus.json").hasAuthority("FACTUREDO_PRIVILEGE")
                .antMatchers("/investmentAPP","/waitForInvoice", "/invoiceStatus.json","/stoptransactions",
                        "historicalData.json","messageStatus.json").hasAuthority("investmentAPP_PRIVILEGE")
                .anyRequest().hasAuthority("READ_PRIVILEGE")
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
            .permitAll()
                .and()
            .sessionManagement()
                .invalidSessionUrl("/login?param=invalidSession")
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .sessionFixation().none()
            .and()
            .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/login?param=logOut")
                .deleteCookies("JSESSIONID")
                .permitAll()
             .and()
                .rememberMe().rememberMeServices(rememberMeServices()).key("theKey")
             .and()
                .headers().frameOptions().disable(); // this is needed to access the H2 db's console


    // @formatter:on
    }

    // beans

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        authProvider.setPostAuthenticationChecks(differentLocationChecker);
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new CustomRememberMeServices("theKey", userDetailsService, new InMemoryTokenRepositoryImpl());
    }


}