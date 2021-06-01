package com.project;

import java.sql.*;
import java.io.*;

/*
Table Certificated 생성
CID int
Add varchar(100)
Name varchar(20)
Type varchar(10)
AreaName varchar(10)
CName varchar(20)
TelNum varchar(20)
CMenu varchar(100)
 */

public class getGoodRestaurant {

    public static void getGoodRestaurantData(){

        Connection con = null;
        Statement st = null;

        String url = "jdbc:postgresql://localhost/";
        String user = "postgres";
        String password = "spirit0113";
        String csvFile = "서울시 지정 인증업소 현황.csv";

        try {

            // JDBC를 이용해 PostgreSQL 서버 및 데이터베이스 연결
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            String lineText = null;
            //line reader 생성

            st.executeUpdate("create table if not exists Certificated( CID int, Add varchar(100), Name varchar(100), Type varchar(50), AreaName varchar(10), CName varchar(20), TelNum varchar(20), CMenu varchar(100), primary key (CID));");

            String sql = "insert into Certificated(CID, Add, Name, Type, AreaName, CName, TelNum, CMenu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);

            lineReader.readLine();

            int count = 1;

            while((lineText = lineReader.readLine()) != null && count <= 500){
                if(lineText == null){
                    continue;
                }
                String[] data = lineText.split(",");
                int cid = Integer.parseInt(data[0].substring(1,data[0].length()-1));
                String add = data[21].substring(1, data[21].length()-1);
                String name = data[2].substring(1, data[2].length()-1);
                String type = data[8].substring(1, data[8].length()-1);
                String telNum = data[18].substring(1, data[18].length()-1);
                String cName = data[8].substring(1, data[8].length()-1);
                String cMenu = data[25].substring(1, data[25].length()-1);
                String areaName = data[4].substring(1, data[4].length()-1);
                if(add.equals("") || name.equals("") || type.equals("") || telNum.equals("") || cMenu.equals("") || areaName.equals("")) continue;

                if(cMenu.length() >= 100) continue;

                statement.setInt(1, cid);
                statement.setString(2, add);
                statement.setString(3, name);
                statement.setString(4, type);
                statement.setString(5, areaName);
                statement.setString(6, cName);
                statement.setString(7, telNum);
                statement.setString(8, cMenu);

                statement.addBatch();
                count += 1;
            }
            //csv 파일을 읽고 Table에 저장.

            lineReader.close();

            statement.executeBatch();

            con.close();

        } catch(IOException ex){
            System.err.println(ex);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }


}
