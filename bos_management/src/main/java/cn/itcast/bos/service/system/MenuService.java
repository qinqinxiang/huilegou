package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuService {
    /*List<Menu> findListMenu();*/

    Page<Menu> findAll(Pageable pageable);

    List<Menu> findListMenu();

    void save(Menu model);

    List<Menu> findByUser(User user);
}
