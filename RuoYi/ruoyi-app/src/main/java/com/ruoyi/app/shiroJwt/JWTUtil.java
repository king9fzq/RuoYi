package com.ruoyi.app.shiroJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ruoyi.app.util.NullUtil;

import java.util.Date;

/**
 * @ClassName: JWTUtil
 * @Description: 
 * @Author fzq
 * @DateTime 2019年7月3日 下午4:32:56
 */

public class JWTUtil {
	//单位秒 过期时间24小时
	private static final long EXPRIE_TIME = 24*60*60*1000;

	/**
	 * 校验
	 * @param token
	 * @param username
	 * @param secret
	 * @return
	 */
	public static boolean verify(String token, String username, String secret) {
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			JWTVerifier verifier = JWT.require(algorithm)
					.withClaim("username", username)
					.build();
			verifier.verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * @Title: getUsername
	 * @Description: 获取token中的信息无需secret解密也能获得
	 * @Author fzq
	 * @DateTime 2019年7月3日 下午4:42:39
	 * @param token
	 * @return
	 */
	public static String getUsername(String token) {
		if(NullUtil.isNull(token))return null;
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("username").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	public static String sign(String username, String secret){
		Date date = new Date(System.currentTimeMillis()+EXPRIE_TIME);
		Algorithm algorithm = Algorithm.HMAC512(secret);
		// 附带username信息
		return JWT.create()
				.withClaim("username", username)
				.withExpiresAt(date)
				.sign(algorithm);
	}
}
