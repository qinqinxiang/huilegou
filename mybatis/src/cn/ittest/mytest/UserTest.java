package cn.ittest.mytest;

import cn.ittest.domain.Orders;
import cn.ittest.domain.Query;
import cn.ittest.domain.User;
import cn.ittest.mapper.OrdersMapper;
import cn.ittest.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UserTest {
    private SqlSessionFactory sqlSessionFactory = null;

    @Before
    public void init() throws IOException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        InputStream inputStream = Resources.getResourceAsStream("SqlMapperConfig.xml");
        sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
    }

    @Test
    public void method() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Query query = new Query();
        User user = new User();
        user.setId(1);
        query.setUser(user);
        User result = userMapper.getQueryByUser(query);
        System.out.println(result);
        sqlSession.close();
    }

    @Test
    public void method2() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        Integer count = userMapper.getQueryCount();
        System.out.println(count);
        sqlSession.close();
    }

    @Test
    public void method3() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
        Orders orders = ordersMapper.findOrdersById(3);
        System.out.println(orders);
        sqlSession.close();
    }

    @Test
    public void method4() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
        List<Orders> list = ordersMapper.findOrderAll();
        for (Orders orders : list) {
            System.out.println(orders);
        }
        sqlSession.close();
    }

    @Test
    public void method5() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
        List<Orders> list = ordersMapper.findAllOrders();
        for (Orders orders : list) {
            System.out.println(orders);
        }
        sqlSession.close();
    }
}
