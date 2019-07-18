
package com.ruoyi.app.shiroJwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName: JWTToken
 * @Description: 
 * @Author fzq
 * @DateTime 2019年7月3日 下午4:49:43
 */

public class JWTToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	// 秘钥
	private String token;
	
	public JWTToken(String token) {
		this.token = token;
	}
	@Override
	public Object getPrincipal() {
		return token;
	}

	
	@Override
	public Object getCredentials() {
		return token;
	}

}
