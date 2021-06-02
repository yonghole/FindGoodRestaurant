package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/*
Table Review 생성
ReviewID int - primary key(ReviewID)
CID int - foreign key(CID) references Certificated(CID))
Writer varchar(30)
Review varchar(200)
Rating int
 */

public class getReview {

    public static void getReviewData(){
        Connection con = null;
        Statement st = null;

        String url = "jdbc:postgresql://localhost/";
        String user = "postgres";
        String password = "spirit0113";
        String csvFile = "리뷰.csv";

        try{

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            String lineText = null;

            st.executeUpdate("drop table if exists Review cascade");

            st.executeUpdate("create table Review(ReviewID int, CID int, Writer varchar(30), Review varchar(200), Rating int, primary key(ReviewID), foreign key(CID) references Certificated(CID));");

            String sql = "insert into Review(ReviewID, CID, Writer, Review, Rating) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);

            lineReader.readLine();

            int count = 1;

            while((lineText = lineReader.readLine()) != null && count <= 500){
                if(lineText == null){
                    continue;
                }
                String[] data = lineText.split(",");
                int rid = Integer.parseInt(data[0]);
                int cid = Integer.parseInt(data[4]);
                int rate = Integer.parseInt(data[3]);
                String writer = data[1];
                String Review = data[2];

                statement.setInt(1, rid);
                statement.setInt(2, cid);
                statement.setString(3, writer);
                statement.setString(4, Review);
                statement.setInt(5, rate);

                statement.addBatch();
                count += 1;
            }
            //csv 파일을 읽고 Table에 저장.

            lineReader.close();

            statement.executeBatch();

            con.close();

        }catch(IOException ex){
            System.err.println(ex);
        }catch(SQLException ex){
            ex.printStackTrace();
        }


    }

}
