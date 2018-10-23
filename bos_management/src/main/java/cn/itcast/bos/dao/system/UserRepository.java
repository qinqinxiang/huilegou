package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    //后台用户登录
    User findByUsername(String username);
}
