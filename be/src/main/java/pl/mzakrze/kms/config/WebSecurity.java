package pl.mzakrze.kms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.mzakrze.kms.config.auth.EmailPasswordAuthenticationFilter;
import pl.mzakrze.kms.config.auth.JwtAuthenticationFilter;
import pl.mzakrze.kms.config.auth.MyAuthenticationProvider;
import pl.mzakrze.kms.config.auth.UserRoles;
import pl.mzakrze.kms.user.UserProfileRepository;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyAuthenticationProvider myAuthenticationProvider;

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll() // index.html
                .antMatchers(HttpMethod.GET, "/bundle.js").permitAll()
                .antMatchers(HttpMethod.GET, "/static/**").permitAll() // FIXME tylko po ssl'u (także / i /bundle.js)
                .antMatchers(HttpMethod.GET, "/api/public").permitAll()
                .antMatchers(HttpMethod.GET, "/api/admin").hasRole(UserRoles.ADMIN)
                .antMatchers(HttpMethod.GET, "/api/user/current").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                .anyRequest().hasRole(UserRoles.USER)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new EmailPasswordAuthenticationFilter(userProfileRepository, bCryptPasswordEncoder), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }


}
