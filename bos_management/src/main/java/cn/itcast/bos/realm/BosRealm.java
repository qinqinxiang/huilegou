package cn.itcast.bos.realm;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*@Service("bosRealm")*/
public class BosRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    //注入角色接口
    @Autowired
    private RoleService roleService;
    //注入权限接口
    @Autowired
    private PermissionService permissionService;

    @Override
    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //创建SimpleAuthenticationInfo对象
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取单前登录用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //调用业务层查询单前用户角色
        List<Role> roles = roleService.findByUser(user);
        //遍历角色集合
        for (Role role : roles) {
            authorizationInfo.addRole(role.getKeyword());
        }
        //调用业务层查询单前用户权限
        List<Permission> permissions = permissionService.findByUser(user);
        for (Permission permission : permissions) {
            authorizationInfo.addStringPermission(permission.getKeyword());
        }
        return authorizationInfo;
    }

    @Override
    //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //转换token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //调用业务层查询数据
        User user = userService.findByUsername(usernamePasswordToken.getUsername());
        if (user == null) {
            //用户名不存在
            return null;
        } else {
            //用户名存在,密码错误
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        }

    }
}
