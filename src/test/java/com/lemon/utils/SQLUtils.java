package com.lemon.utils;

import com.lemon.pojo.Member;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SQLUtils {
    public static void main(String[] args) throws Exception {
        scalarHandler();
    }

    /**
     * sql查询方法
     * @param sql
     * @return
     */
    public static Object getSingleResult(String sql){
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        //1.定义返回值
        Object result = null;
        try {
            //2.创建DBUtils sql 语句操作类。
            QueryRunner runner = new QueryRunner();
            //3.获取数据库连接
            Connection connection = JDBCUtils.getConnection();
            //4.创建ScalarHandler，针对单列的数据
            ScalarHandler handler = new ScalarHandler();
            //5.执行sql语句
            result = runner.query(connection, sql, handler);
            System.out.println(result);
            //6.关闭数据库连接
            JDBCUtils.close(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    private static void scalarHandler() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from member where id = 2073699;";
        Connection connection = JDBCUtils.getConnection();

        ScalarHandler<Long> handler = new ScalarHandler<>();
        Long result = runner.query(connection, sql, handler);
        System.out.println(result);

        JDBCUtils.close(connection);
    }

    private static void beanListHandler() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from member limit 5;";
        Connection connection = JDBCUtils.getConnection();

        BeanListHandler<Member> handler = new BeanListHandler<>(Member.class);
        List<Member> memberList = runner.query(connection, sql, handler);
        for (Member member : memberList) {
            System.out.println(member);
        }

        JDBCUtils.close(connection);
    }

    private static void beanHandler() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from member where id = 2073699;";
        Connection connection = JDBCUtils.getConnection();

        BeanHandler<Member> handler = new BeanHandler<>(Member.class);
        Member member = runner.query(connection, sql, handler);
        System.out.println(member);

        JDBCUtils.close(connection);
    }

    private static void mapHandler() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from member where id = 2073699;";
        Connection connection = JDBCUtils.getConnection();

        MapHandler handler = new MapHandler();
        Map<String, Object> query = runner.query(connection, sql, handler);
        System.out.println(query);

        JDBCUtils.close(connection);
    }

    public static void update() throws Exception {
        QueryRunner runner = new QueryRunner();
        String sql = "update member set leave_amount = 100 where id = 2073699;";
        Connection connection = JDBCUtils.getConnection();
        int count = runner.update(connection, sql);
        System.out.println(count);
        JDBCUtils.close(connection);
    }

    public static void insert() throws Exception {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into member values(null,'null','123456','123123123',1,now());";
        Connection connection = JDBCUtils.getConnection();
        int count = runner.update(connection, sql);
        System.out.println(count);
        JDBCUtils.close(connection);
    }
}
