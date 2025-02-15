package cn.bugstack.mybatis.test;

import cn.bugstack.mybatis.binding.MapperProxyFactory;
import cn.bugstack.mybatis.test.dao.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小傅哥，微信：fustack
 * @description 单元测试
 * @date 2022/3/26
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_MapperProxyFactory() {
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);

        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("cn.bugstack.mybatis.test.dao.IUserDao.queryUserName", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户姓名");
        sqlSession.put("cn.bugstack.mybatis.test.dao.IUserDao.queryUserAge", "模拟执行 Mapper.xml 中 SQL 语句的操作：查询用户年龄");
        // 生成一个代理对象
        IUserDao userDao = factory.newInstance(sqlSession);

        String res = userDao.queryUserName("10001");
        logger.info("测试结果：{}", res);
    }

    @Test
    public void test_proxy_class() {
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class}, (proxy, method, args) -> "你被代理了！");
        String result = userDao.queryUserName("10001");
        System.out.println("测试结果：" + result);
    }

    @Test
    public void test_jdbc() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://101.43.40.204:3306/my_test?useUnicode=true";
        String userName = "root";
        String passWord = "1234";


        Connection connection = DriverManager.getConnection(url, userName, passWord);
        Statement statement = connection.createStatement();

        String sql = "select id, username, age from t_user";

        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            System.out.println("id= " + resultSet.getObject("id") + "");
            System.out.println("username= " + resultSet.getObject("username") + "");
            System.out.println("age= " + resultSet.getObject("age") + "");
        }

        resultSet.close();
        statement.close();
        connection.close();

    }

}