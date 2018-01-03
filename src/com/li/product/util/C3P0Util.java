package com.li.product.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class C3P0Util {
	
	//自己模拟法的连接池没人认识，datasource的接口都认识啊
	//datasource里面没有release 装饰设计模式，搞个myconnection装饰connection,重写close，在datasource里getconnection返回myconnection
	//connection只是java里面的接口，具体实现是com.mysql.jdbc提供的connection
	//写个适配器，子类继承他很干净啊，MyConnectionWraper
	//c3p0下载后里面的index.html有使用说明文档，web项目lib就放在webroot的lib下
	//DataSource连close方法都没有，更没有setDriverClass等方法了，所以要private static ComboPooledDataSource，
	//不能private static DataSource
	//datasource导包不要导错
	private static DataSource ds = null;
	
	
    //在静态代码块中创建数据库连接池
    static{
        try{
            //通过代码创建C3P0数据库连接池
            /*ds = new ComboPooledDataSource();
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/jdbcstudy");
            ds.setUser("root");
            ds.setPassword("XDP");
            ds.setInitialPoolSize(10);
            ds.setMinPoolSize(5);
            ds.setMaxPoolSize(20);*/
            
            //通过读取C3P0的xml配置文件创建数据源，C3P0的xml配置文件c3p0-config.xml必须放在src目录下
            ds = new ComboPooledDataSource();//使用C3P0的默认配置来创建数据源
            //ds = new ComboPooledDataSource("MySQL");//使用C3P0的命名配置来创建数据源
            
        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
    * @Method: getConnection
    * @Description: 从数据源中获取数据库连接
    * @Anthor:li
    * @return Connection
    * @throws SQLException
    */ 
    public static Connection getConnection() throws SQLException{
        //从数据源中获取数据库连接
        return ds.getConnection();
    }
    
    /**
    * @Method: release
    * @Description: 释放资源，
    * 释放的资源包括Connection数据库连接对象，负责执行SQL命令的Statement对象，存储查询结果的ResultSet对象
    * @Anthor:li
    *
    * @param conn
    * @param st
    * @param rs
    */ 
    public static void release(Connection conn,Statement st,ResultSet rs){
        if(rs!=null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st!=null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(conn!=null){
            try{
                //将Connection连接对象还给数据库连接池
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	public static DataSource getDataSource() {
		return ds;
	}
	
}
