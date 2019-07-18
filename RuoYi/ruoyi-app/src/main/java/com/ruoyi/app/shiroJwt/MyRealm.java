package com.ruoyi.app.shiroJwt;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: MyRealm
 * @Description:
 * @Author fzq
 * @DateTime 2019年7月3日 下午4:31:33
 */

@Component
public class MyRealm extends AuthorizingRealm {

	@Resource
	private ISysUserService iSysUserService;

	@Resource
	private ISysRoleService iSysRoleService;

	@Resource
	private ISysMenuService iSysMenuService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = JWTUtil.getUsername(principals.toString());
		SysUser user = iSysUserService.selectUserByLoginName(username);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addRoles(iSysRoleService.selectRoleKeys(user.getUserId()));
		simpleAuthorizationInfo.addStringPermissions(iSysMenuService.selectPermsByUserId(user.getUserId()));
		
		return simpleAuthorizationInfo;
	}

	/**
	 * 默认使用此方法进行用户正确与否验证，错误抛出异常即可
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		String token = (String) authenticationToken.getCredentials();
		// 解密获得username，用于和数据库进行对比
		String username = JWTUtil.getUsername(token);
		if (username == null) {
			throw new AuthenticationException("token 无效！");
		}

		SysUser user = iSysUserService.selectUserByLoginName(username);
		if (user == null) {
			throw new AuthenticationException("用户"+username+"不存在");
		}
		
		if (!JWTUtil.verify(token, username, user.getPassword())){
			throw new AuthenticationException("账户或者密码错误!");
		}
		return new SimpleAuthenticationInfo(token, token, "my_realm");
	}

}
