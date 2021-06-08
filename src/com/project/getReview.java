package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import static java.sql.Types.NULL;

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

        String url = "jdbc:postgresql://localhost:5432/";
        String user = "postgres";
        String password = "1234";
        String csvFile = "리뷰.csv";

        try{

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFile));
            String lineText = null;

            st.executeUpdate("drop table if exists Review cascade");

            st.executeUpdate("create table Review(ReviewID int, CID int, Writer varchar(30), Review varchar(200), Rating int, vID int, primary key(ReviewID), foreign key(CID) references Certificated(CID), foreign key(vID) references Violated(vID));");

            String sql = "insert into Review(ReviewID, CID, Writer, Review, Rating, vID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);

            lineReader.readLine();

            int count = 1;

            while((lineText = lineReader.readLine()) != null){
                if(lineText == null){
                    continue;
                }
                String[] data = lineText.split(",");
                if(data.length < 6) {
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
                    statement.setNull(6, Types.INTEGER);

                }else{
                    int rid = Integer.parseInt(data[0]);
                    int vid = Integer.parseInt(data[5]);
                    int rate = Integer.parseInt(data[3]);
                    String writer = data[1];
                    String Review = data[2];

                    statement.setInt(1, rid);
                    statement.setNull(2, Types.INTEGER);
                    statement.setString(3, writer);
                    statement.setString(4, Review);
                    statement.setInt(5, rate);
                    statement.setInt(6, vid);
                }


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
