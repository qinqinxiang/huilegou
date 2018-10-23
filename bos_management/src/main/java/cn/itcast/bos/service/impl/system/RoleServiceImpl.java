package cn.itcast.bos.service.impl.system;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    //注如dao
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Role> findByUser(User user) {
        //根据不同的用户赋予不同角色
        if (user.getUsername().equals("123")) {
            return roleRepository.findAll();
        } else {
            return roleRepository.findByUser(user.getId());
        }
    }

    //角色数据列表展示
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    //角色添加
    @Override
    public void save(Role role, String menuIds, String[] permissionIds) {
        //保存角色
        roleRepository.save(role);
        //角色关联菜单
        if (StringUtils.isNoneBlank(menuIds)) {
            String[] menuIdsStr = menuIds.split(",");
            for (String menuId : menuIdsStr) {
                Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
                role.getMenus().add(menu);
            }
        }
        //角色关联权限
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
                role.getPermissions().add(permission);
            }
        }
    }
}
