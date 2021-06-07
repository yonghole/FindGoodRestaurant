package com.project;

import java.sql.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
Table Violated 생성
 VID      int - primary key(VID)
 AreaName varchar(10)
 Add     varchar(100)
 Type     varchar(10)
 Name     varchar(30)
 VDate    datetime
 VContent varchar(100)
 Disposal varchar(100)
 */

public class getBadRestaurant {



    public static void getBadRestaurantData() throws SQLException {

        Connection con = null;
        Statement st = null;

        String url = "jdbc:postgresql://localhost:5432/";
        String user = "postgres";
        String password = "1234";
        String csvFile = "서울시 위생업소 전체 행정처분내역 현황.csv";

        try {

            // JDBC를 이용해 PostgreSQL 서버 및 데이터베이스 연결
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            String lineText = null;
            //line reader 생성

            st.executeUpdate("drop table if exists Violated cascade");

            st.executeUpdate("create table Violated( VID int, AreaName varchar(10), Add varchar(100), Type varchar(50), Name varchar(50), VDate date, VContent varchar(100), Disposal varchar(100), primary key(VID));");

            String sql = "insert into Violated (VID, AreaName, Add, Type, Name, VDate, VContent, Disposal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);

            lineReader.readLine();

            int count = 1;

            DateFormat format = new SimpleDateFormat("yyyyMMdd");

            while((lineText = lineReader.readLine()) != null && count <= 500){

                String[] data = lineText.split(",");

                if(data[12].length() < 10 ){
                    continue;
                }

                String restaurantName = data[4].substring(1, data[4].length()-1);
                String type = data[3].substring(1, data[3].length()-1);
                String restaurantAdd = data[5].substring(1, data[5].length()-1);
                String rawViolationContent = data[11].substring(1, data[11].length()-1);
                String rawDisposal = data[14].substring(1, data[14].length()-1);

                String date = data[12].substring(1, 9);
                if(date.contains(" ") || Integer.parseInt(date) < 20160000) {
                    continue;
                }
                //2016년 이전의 것은 count X
                java.util.Date utilDate = format.parse(date);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                int vID = count;


                String[] areaParsing = restaurantAdd.split(" ");
                if(areaParsing.length<=2){
                    continue;
                }
                String areaName = areaParsing[1];

                statement.setInt(1, vID);
                statement.setString(2, areaName);
                statement.setString(3, restaurantAdd);
                statement.setString(4, type);
                statement.setString(5, restaurantName);
                statement.setDate(6, sqlDate);
                statement.setString(7, rawViolationContent);
                statement.setString(8, rawDisposal);

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
        catch(ParseException ex){
            System.err.println(ex);
        }
    }

}