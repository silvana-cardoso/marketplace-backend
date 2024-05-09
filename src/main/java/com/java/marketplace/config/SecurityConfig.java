package com.java.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.java.marketplace.services.UserService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
/*
 * THE FOLLOWING ANNOTATIONS WILL BE USED IN THEIR RESPECTIVE CLASSES
 *
 * @EnableResourceServer
 *
 * @EnableAuthorizationServer
 *
 */
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserService userService;
	
	@Override
//	AuthenticationManagerBuilder: responsável por construir o gerenciamento dos usuários e senhas cadastrados na aplicação.
//	utilizar dados em memória para username e password, ao invés do username
//	padrão e a senha gerada pelo spring
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("user@gmail.com").password("123").roles("User");
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Bean
//	faz a autenticação e o gerenciamento de usuários, e utiliza o
//	método acima
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Bean
//	realiza a codificação dos dados utilizados
//	NoOpPasswordEncoder mantém os dados originais
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
//	utilizado para configurar questões relativas ao http, como por exemplo,
//	autorizar url, habilitar o cors, fazer controle de seção, etc
	public void configure(HttpSecurity http) throws Exception {
//		TO ENABLE THE CORS METHOD
		http.csrf().disable().cors()
//		TO THE APPLICATION DOES NOT SAVE SECTION.
//		THE SECTION WILL BE SAVED VIA THE ACCESS TOKEN (TOKEN EXPIRES)
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		CSRF COMES ENABLED BY DEFAULT AND SERVES TO SAFEGUARD THE WEB APPLICATION,
//		BUT AS WE ARE NOT USING THE WEB APPLICATION, IT IS NOT NECESSARY;
//		.httpBasic();
//		.and().csrf().disable();
	}
}
