package com.li.product.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class C3P0Util {
	
	//�Լ�ģ�ⷨ�����ӳ�û����ʶ��datasource�Ľӿڶ���ʶ��
	//datasource����û��release װ�����ģʽ�����myconnectionװ��connection,��дclose����datasource��getconnection����myconnection
	//connectionֻ��java����Ľӿڣ�����ʵ����com.mysql.jdbc�ṩ��connection
	//д��������������̳����ܸɾ�����MyConnectionWraper
	//c3p0���غ������index.html��ʹ��˵���ĵ���web��Ŀlib�ͷ���webroot��lib��
	//DataSource��close������û�У���û��setDriverClass�ȷ����ˣ�����Ҫprivate static ComboPooledDataSource��
	//����private static DataSource
	//datasource������Ҫ����
	private static DataSource ds = null;
	
	
    //�ھ�̬������д������ݿ����ӳ�
    static{
        try{
            //ͨ�����봴��C3P0���ݿ����ӳ�
            /*ds = new ComboPooledDataSource();
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/jdbcstudy");
            ds.setUser("root");
            ds.setPassword("XDP");
            ds.setInitialPoolSize(10);
            ds.setMinPoolSize(5);
            ds.setMaxPoolSize(20);*/
            
            //ͨ����ȡC3P0��xml�����ļ���������Դ��C3P0��xml�����ļ�c3p0-config.xml�������srcĿ¼��
            ds = new ComboPooledDataSource();//ʹ��C3P0��Ĭ����������������Դ
            //ds = new ComboPooledDataSource("MySQL");//ʹ��C3P0��������������������Դ
            
        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
    * @Method: getConnection
    * @Description: ������Դ�л�ȡ���ݿ�����
    * @Anthor:li
    * @return Connection
    * @throws SQLException
    */ 
    public static Connection getConnection() throws SQLException{
        //������Դ�л�ȡ���ݿ�����
        return ds.getConnection();
    }
    
    /**
    * @Method: release
    * @Description: �ͷ���Դ��
    * �ͷŵ���Դ����Connection���ݿ����Ӷ��󣬸���ִ��SQL�����Statement���󣬴洢��ѯ�����ResultSet����
    * @Anthor:li
    *
    * @param conn
    * @param st
    * @param rs
    */ 
    public static void release(Connection conn,Statement st,ResultSet rs){
        if(rs!=null){
            try{
                //�رմ洢��ѯ�����ResultSet����
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st!=null){
            try{
                //�رո���ִ��SQL�����Statement����
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(conn!=null){
            try{
                //��Connection���Ӷ��󻹸����ݿ����ӳ�
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
