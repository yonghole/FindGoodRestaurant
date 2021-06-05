package com.project;

import java.sql.*;
import java.util.Scanner;


public class findRestaurant {

    public static void main(String[] args) throws SQLException {


        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        String url = "jdbc:postgresql://localhost/";
        String user = "postgres";
        String password = "spirit0113";


        try{
            Scanner scan = new Scanner(System.in);

            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();

            getBadRestaurant.getBadRestaurantData();
            getGoodRestaurant.getGoodRestaurantData();
            getReview.getReviewData();




        }catch(SQLException ex){
            throw ex;
        }

    }

}
