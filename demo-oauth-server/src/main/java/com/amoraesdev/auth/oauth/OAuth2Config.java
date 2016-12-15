package com.amoraesdev.auth.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Some OAuth2 configurations
 * @author Alessandro Moraes alessandro(at)amoraesdev.com
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public ApprovalStore approvalStore() {
        return new InMemoryApprovalStore();
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }
    
    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices(){
    	DefaultTokenServices tokenServices = new DefaultTokenServices();
    	tokenServices.setSupportRefreshToken(true);
    	tokenServices.setTokenStore(tokenStore());
    	return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore());
        endpoints.approvalStore(approvalStore());
        endpoints.authorizationCodeServices(authorizationCodeServices());
        endpoints.authenticationManager(authenticationManager);
        
    }

    
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
        	.withClient("mobify")
        		.authorities("CLIENT","CLIENT_MASTER")
        		.secret("aaa111bbb222ccc333")
        		.scopes("openid")
        		.autoApprove(true)
        		.authorizedGrantTypes("authorization_code","password","refresh_token","client_credentials")
        	.and()
        	.withClient("monitor-checker")
        		.authorities("CLIENT")
        		.secret("aaa111bbb222")
        		.scopes("openid")
        		.autoApprove(true)
        		.autoApprove("openid")
        		.authorizedGrantTypes("authorization_code","password","refresh_token","client_credentials");
    }
    
    @Override
	public void configure(AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer)
			throws Exception {
    	authorizationServerSecurityConfigurer.tokenKeyAccess("permitAll()").checkTokenAccess(
				"isAuthenticated()");
    	
	}

    
    
    
}
