package cn.itcast.bos.service.impl.system;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    //注入dao
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> findByUser(User user) {
        if (user.getUsername().equals("123")) {
            return permissionRepository.findAll();
        } else {
            return permissionRepository.findByUser(user.getId());
        }

    }

    //权限数据展示
    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    //权限数据添加
    @Override
    public void save(Permission permission) {
        permissionRepository.save(permission);
    }
}
