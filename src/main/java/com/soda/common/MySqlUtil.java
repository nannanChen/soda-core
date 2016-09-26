package com.soda.common;

import org.apache.log4j.Logger;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * Created by kcao on 2016/3/11.
 */
public class MySqlUtil implements Serializable{

    protected Logger log = Logger.getLogger(this.getClass());


    private DataSource dataSource;

    public MySqlUtil(DataSource dataSource){
        this.dataSource=dataSource;
        log.info("init MySqlUtil dataSource:"+dataSource);
    }

    public Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增加、删除、改
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public boolean updateByPreparedStatement(String sql, List<Object> params)throws SQLException{
        boolean flag = false;
        int result = -1;
        Connection connection=dataSource.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        int index = 1;
        if(params != null && !params.isEmpty()){
            for(int i=0; i<params.size(); i++){
                pstmt.setObject(index++, params.get(i));
            }
        }
        result = pstmt.executeUpdate();
        flag = result > 0 ? true : false;
        release(connection,pstmt,null);
        return flag;
    }

    public List<Map<String, Object>> findMoreResult(String sql, List<Object> params) throws SQLException{
        List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
        int index  = 1;
        Connection connection=dataSource.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty()){
            for(int i=0; i<params.size(); i++){
                pstmt.setObject(index++, params.get(i));
            }
        }
        ResultSet resultSet = pstmt.executeQuery();//返回查询结果
        ResultSetMetaData metaData = resultSet.getMetaData();
        int col_len = metaData.getColumnCount();
        while(resultSet.next()){
            Map<String, Object> map = new HashMap<String, Object>();
            for(int i=0; i<col_len; i++ ){
                String cols_name = metaData.getColumnName(i+1);
                Object cols_value = resultSet.getObject(cols_name);
                if(cols_value == null){
                    cols_value = "";
                }
                map.put(cols_name, cols_value);
            }
            list.add(map);
        }
        release(connection,pstmt,resultSet);
        return list;
    }

    /**
     * 查询单条记录
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> findSimpleResult(String sql, List<Object> params) throws SQLException{
        List<Map<String, Object>> list=this.findMoreResult(sql,params);
        Map<String, Object> map=null;
        if(list!=null&&list.size()>0){
            map=list.get(0);
        }
        return map;
    }

    /**通过反射机制查询多条记录
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> List<T> findMoreRefResult(String sql, List<Object> params, Class<T> cls )throws Exception {
        List<T> list = new ArrayList<T>();
        List<Map<String, Object>> listMap=this.findMoreResult(sql,params);
        for(Map<String, Object> map:listMap){
            //通过反射机制创建一个实例
            T resultObject = cls.newInstance();
            for(Map.Entry<String,Object> entry:map.entrySet()){
                String cols_name = entry.getKey();
                Object cols_value = entry.getValue();
                if(cols_value == null){
                    cols_value = " ";
                }
                try {
                    Field field = cls.getDeclaredField(cols_name);
                    field.setAccessible(true); //打开javabean的访问权限
                    field.set(resultObject, cols_value);
                }catch (NoSuchFieldException e){
//                   log.warn("NoSuchField :",e);
                }
            }
            list.add(resultObject);
        }
        return list;
    }

    /**通过反射机制查询单条记录
     * @param sql
     * @param params
     * @param cls
     * @return
     * @throws Exception
     */
    public <T> T findSimpleRefResult(String sql, List<Object> params, Class<T> cls )throws Exception{
        T resultObject=this.findMoreRefResult(sql,params,cls).get(0);
        return resultObject;
    }

    public <T> Set<T> findMoreRefResultToSet(String sql, List<Object> params,Class<T> cls)throws Exception  {
        Set<T> rs= new HashSet<T>();
        List<T> list=this.findMoreRefResult(sql,params,cls);
        for(T l:list){
            rs.add(l);
        }
        return  rs;
    }

    public void release(Connection connection, PreparedStatement pstmt, ResultSet resultSet) {
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pstmt!=null){
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception{
        MySqlUtil jdbc=new MySqlUtil(DataSourceUtil.dataSource);

    }
}
