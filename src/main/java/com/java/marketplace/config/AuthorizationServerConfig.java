package com.java.marketplace.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.java.marketplace.services.UserService;

//Nesta classe será configurado o acesso do client, e o acesso dos endpoints através do
//token (em memória)
//Além disso, como o acesso aos
//endpoints será realizado através de uma única aplicação, é possível salvar esses dados
//também em memória, definindo um client-id e um client-secret fixos.

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
		
	@Autowired
	private UserService userService;
	
	@Value("${security.jwt.signing-key}")
	private String signingKey;
	
	/*
	 * JWT TOKEN CONVERSOR 
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(signingKey);
//		tokenConverter.setAccessTokenConverter(customAccessTokenConverter);
		return tokenConverter;
	}

	/*
	 * IN MEMORY TOKEN
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
	*/
	
	/*
	 * JWT TOKEN
	*/
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/*
	 * ENDPOINTS CONFIGURATION
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
		endpoints.tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter())
				.authenticationManager(authenticationManager)
				.userDetailsService(userService);
	}
	/* 
	 * CLIENTS CONFIGURATION
	 */ 
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("rampUp").secret("123").scopes("read", "write").authorizedGrantTypes("password")
				.accessTokenValiditySeconds(3600);
	}
}
