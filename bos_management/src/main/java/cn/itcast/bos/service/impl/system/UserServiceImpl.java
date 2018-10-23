package cn.itcast.bos.service.impl.system;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    //注入dao
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    //后台用户登录
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //用户后台数据展示
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //后台用户添加
    @Override
    public void save(User user, String[] roleIds) {
        userRepository.save(user);
        //用户关联角色
        if (roleIds != null) {
            for (String roleId : roleIds) {
                Role role = roleRepository.findOne(Integer.parseInt(roleId));
                user.getRoles().add(role);
            }
        }
    }
}
