package com.jiajie.subjecttable.subject.controller;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import java.sql.*;
import java.util.*;

public class createSubjectTable {
    private static Connection conn = null;

    public createSubjectTable() {
        super();
    }

    public Connection getConn() {
        return conn;
    }

    public static String connect(String ip, String port, String username, String password, String dbName) throws Exception, IllegalAccessException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url="jdbc:mysql://" + ip + ":" + port + "/" + dbName;
        try {
            conn = DriverManager.getConnection(url, username, password);
            return "Connection succeeded";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public void disconnect() throws SQLException{
        if (conn != null) {
            conn.close();
        }
    }

    public ArrayList<String> showTables() throws Exception{

        ArrayList<String> tableName = new ArrayList<String>();
        if(conn != null && !conn.isClosed()){
//			Statement stmt = conn.createStatement();
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, null, new String[] { "TABLE" });
            while (rs.next()) {
                String name = rs.getString(3);
                tableName.add(name);
            }
            rs.close();
            return tableName;
        }
        else {
            return null;
        }

    }


    public Map<String,String> showColumnType(String tableName, String columns) throws Exception{

        Map<String,String> columnType = new LinkedHashMap<String,String>();
        if(conn != null && !conn.isClosed()){
            String sql = "select " + columns + " from " + tableName + " limit 1 ";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                for (int i = 1; i < columnCount + 1; i++) {
                    columnType.put(meta.getColumnName(i), meta.getColumnTypeName(i));
                }
                rs.close();
                ps.closeOnCompletion();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return columnType;
        }
        else {
            return null;
        }

    }

    public static ArrayList<Map<String,String>> sqlSelect(String sql, int maxRows) throws Exception{
        ArrayList<Map<String,String>> rows = new ArrayList<Map<String,String>>();
        if(conn != null && !conn.isClosed()){
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                if(maxRows != 0) {
                    ps.setMaxRows(maxRows);
                }
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                rs.last();
                int rowCount = rs.getRow();
                rs.first();
                for(int i = 0; i < rowCount; i++) {
                    Map<String,String> columns = new LinkedHashMap<String,String>();
                    for (int j = 1; j < columnCount + 1; j++) {
                        columns.put(meta.getColumnName(j), rs.getString(j));
                    }
                    rows.add(columns);
                    rs.next();
                }
                rs.close();
                ps.closeOnCompletion();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return rows;
        }
        else {
            return null;
        }
    }

    public String sqlUpdate(String sql) throws Exception{
        if(conn != null && !conn.isClosed()){
            try {
                Statement ps = conn.createStatement();
                ps.executeUpdate(sql);
                ps.closeOnCompletion();
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return "success";
        }
        else {
            return "Connection's state is not available.";
        }
    }

    public List<String> getMeta(String sql) throws Exception{

        List<String> columns = new ArrayList<>();
        if(conn != null && !conn.isClosed()){
            sql += " limit 1 ";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();

                int columnCount = meta.getColumnCount();

                for (int i = 1; i < columnCount + 1; i++) {
					/*
				    String columnLabel = meta.getColumnLabel(i);
					if (columnLabel == null || columnLabel.equals("")) {
						columns.add(meta.getColumnName(i));
					}
					else {
						columns.add(columnLabel);
					}
                    */
                    columns.add(meta.getColumnName(i));
                }
                rs.close();
                ps.closeOnCompletion();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return columns;
        }
        else {
            return null;
        }

    }

    /**
     * Below：2018-10-04 16:14 added
     */
    /**
     * 获得某表的建表语句
     * @param tableName
     * @return
     * @throws Exception
     */
    public String getCommentByTableName(String tableName) throws Exception {
        if(conn != null && !conn.isClosed()){
            Map<String, String> map = new HashMap();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName);
            String comment = "";
            String createDDl = "";
            if (rs != null && rs.next()) {
                createDDl = rs.getString(2);
                System.out.println("dsd:"+createDDl);
                comment = parse(createDDl);
                System.out.println("dsa"+comment);
            }
            rs.close();
            stmt.close();
            return comment;
        }
        else {
            return null;
        }
    }

    /**
     * 获得某表中所有字段的注释
     * @param tableName
     * @return
     * @throws Exception
     */
    public Map<String, String> getColumnCommentByTableName(String tableName) throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        if(conn != null && !conn.isClosed()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                map.put(rs.getString("Field"), rs.getString("Comment"));
            }
            rs.close();
            stmt.close();
            return map;
        }
        else {
            return null;
        }
    }

    /**
     * 返回注释信息
     * @param all
     * @return
     */
    public static String parse(String all) {
        String comment = null;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }

    /**
     * 2019/4/18 20:00
     * 获取表中的字段对应的类型
     * @param tableName
     * @return
     * @throws Exception
     */
    public static Map<String, String> getTypeByTableName(String tableName) throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        if(conn != null && !conn.isClosed()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("show COLUMNS from " + tableName);
            String tq = "";
            while (rs.next()) {
                tq = rs.getString("Field");
                map.put(tq, rs.getString("Type"));
            }
            rs.close();
            stmt.close();
            return map;
        }
        else {
            return null;
        }
    }
    /**
     * 2019/4/18 20:30
     * 创建主题表所在的数据库
     * @param dbName
     * @return
     * @throws Exception
     */
    public static String createDB(String dbName) throws Exception{
        if(conn != null && !conn.isClosed()){
            Statement stmt = conn.createStatement();
            stmt.executeQuery("CREATE DATABASE " + dbName);
            String flag = "success";
            stmt.close();
            return flag;
        }
        else {
            return null;
        }
    }
    /**
     * 2019/4/18 20:30
     * 创建主题表
     * @param tableName,Item
     * @return
     * @throws Exception
     */
    public static String createDB(String tableName, Map Item) throws Exception{
        if(conn != null && !conn.isClosed()){
            Statement stmt = conn.createStatement();
            stmt.executeQuery("create table "+tableName+" (username varchar(50) not null primary key,"
                    + "password varchar(20) not null ); ");
            String flag = "success";
            stmt.close();
            return flag;
        }
        else {
            return null;
        }
    }

    public static void main(String args[]) throws Exception {
        Map<String, String> map = new LinkedHashMap<>();
        String a = connect("127.0.0.1","3306","root","root","test");
        ArrayList<Map<String,String>> data = new ArrayList<Map<String,String>>();
        map = getTypeByTableName("Person");
        for(String key : map.keySet()){
            String value = map.get(key);
            System.out.println(key+"  "+value);
        }
        data = sqlSelect("SELECT * FROM Person;",10);
        System.out.println(data);


    }
}//end main
