package cn.itcast.bos.service.impl.system;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Page<Menu> findAll(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    @Override
    public List<Menu> findListMenu() {
        return menuRepository.findAll();
    }

    //添加菜单
    @Override
    public void save(Menu menu) {
        //防止用户未选父菜单，关联一个空对象
        if (menu.getParentMenu() != null && menu.getParentMenu().getId() == 0) {
            menu.setParentMenu(null);
        }
        menuRepository.save(menu);
    }

    //根据单前登录用户显示不同的菜单
    @Override
    public List<Menu> findByUser(User user) {
        if (user.getUsername().equals("123")) {
            return menuRepository.findAll();
        } else {
            return menuRepository.findByUser(user.getId());
        }
    }
}
