package com.project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.Scanner;


public class findRestaurant {

    public static void main(String[] args) throws SQLException {


        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;


        String url = "jdbc:postgresql://localhost:5432/";
        String user = "postgres";
        String password = "1234";


        try{
            Scanner sc = new Scanner(System.in);

//            conn = DriverManager.getConnection(url, user, password);
//            st = con.createStatement();



            try {
                conn = DriverManager.getConnection(url, user, password);
                st = conn.createStatement();
                rs = st.executeQuery("SELECT VERSION()");

                if (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            getBadRestaurant.getBadRestaurantData();
            getGoodRestaurant.getGoodRestaurantData();
            getReview.getReviewData();

            System.out.println("안심 식당 찾기 서비스를 이용해주셔서 감사합니다.\n");
            Integer select = 0;
            while (select != 5) {
                System.out.println("메뉴를 선택해주세요.\n");
                System.out.println("1.우리 지역 업소 찾기  2. 우리 지역 위생 불량 업소 찾기  3. 음식 종류로 찾기  4.업소명 검색하기  5.비건 식당 찾기 6.종료\n");
                select = Integer.parseInt(sc.nextLine());
                if(select == 1){
                    String select_menu1;
                    System.out.println("지역구를 선택해주세요.\n");
                    System.out.println("1.관악구  2.종로구  3.강서구  4.구로구  5.성동구  6.용산구  7.양천구  8.서초구  9.마포구\n 10.중구" +
                            "  11.동대문구  12.강북구  13.동작구  14.강동구  15.광진구  16.송파구  17.은평구  18.중랑구  19.서대문구  20.도봉구  21.노원구 22.강남구\n");
                    select_menu1 = sc.nextLine();

                    String sql = "select distinct name, add, type, telnum, cmenu, cid\n"
                            + "	from Certificated C\n"
                            + "	where areaname='"+select_menu1+"' and not name = any(select C1.name from Certificated C1, Violated V1\n"
                            + "						  where C1.name=V1.name and C1.areaname=V1.areaname);\n";
                    rs = st.executeQuery(sql);
                    int i = 0;
                    JFrame frame = new JFrame(select_menu1 + "에서 위생 불량 적발 업체를 제외한 결과입니다.");
                    frame.setLocation(200,400);
                    frame.setSize(1000,500);
                    Object record[] = new Object[3];
                    String header[] = {"번호", "업소명", "주소"};
                    DefaultTableModel tableModel = new DefaultTableModel(header,0);
                    JTable table = new JTable(tableModel);
                    JScrollPane sp = new JScrollPane(table);
                    frame.setVisible(true);
                    frame.add(sp,"Center");
                    List<List<String>> list = new ArrayList<>();

                    while(rs.next()){
                        List<String> tmp = new ArrayList<>();
                        String name = rs.getString("name");
                        String address = rs.getString("add");
                        String type = rs.getString("type");
                        String telnum = rs.getString("telnum");
                        String cmenu = rs.getString("cmenu");
                        String cid = Integer.toString(rs.getInt("cid"));

                        record[0] = Integer.toString(++i);
                        tmp.add(Integer.toString(i));
                        record[1] = name;
                        tmp.add(name);
                        record[2] = address;
                        tmp.add(address);
                        tmp.add(type);
                        tmp.add(telnum);
                        tmp.add(cmenu);
                        tmp.add(cid);
//                        record[3] = type;
//                        record[4] = telnum;
                      tableModel.addRow(record);
                      list.add(tmp);
                    }
                    int select_store = 0;
                    while(select_store >=-1){
                        System.out.println("업체를 선택해주세요. 표 기준의 번호를 선택해주세요. 메인 화면으로 돌아가시려면 -1을 입력해주세요.\n");
                        select_store = Integer.parseInt(sc.nextLine())-1;
                        if(select_store<=-1 || select_store >=list.size()){
                            System.out.println("메인 화면으로 돌아갑니다.\n");
//                            continue;
                        }
                        else{
                            int select_detail = 0;
                            while(select_detail != 4){
                                System.out.println("1.업체 연락처 보기 2. 업체 대표 메뉴 보기 3. 업체 리뷰 보기 4. 돌아가기\n");
                                select_detail = Integer.parseInt(sc.nextLine());
                                List<String> cur = list.get(select_store);
                                if(select_detail == 1){

                                    System.out.println("선택하신 업체 " + cur.get(1) + "의 연락처는 " + cur.get(4)+" 입니다.\n");
                                }
                                else if(select_detail == 2){
                                    System.out.println("선택하신 업체 " + cur.get(1) + "의 대표 메뉴는 " + cur.get(5)+" 입니다.\n");
                                }
                                else if(select_detail == 3){
                                    sql = "select writer, review, rating from review where cid =" + cur.get(6) + " order by rating;";
                                    JFrame frameReview = new JFrame(select_menu1 + "의 리뷰 정보입니다.");
                                    frameReview.setLocation(200,400);
                                    frameReview.setSize(1000,500);


                                    Object recordReview[] = new Object[3];
                                    String headerReview[] = {"글쓴이", "리뷰 내용", "별점 (5점 만점)"};
                                    DefaultTableModel tableModelReview = new DefaultTableModel(headerReview,0);
                                    JTable tableReview = new JTable(tableModelReview);
                                    JScrollPane spReview = new JScrollPane(tableReview);
                                    frameReview.setVisible(true);
                                    frameReview.add(spReview,"Center");
                                    rs = st.executeQuery(sql);
                                    while(rs.next()){
                                        List<String> tmp = new ArrayList<>();
                                        String writer = rs.getString("writer");
                                        String review = rs.getString("review");
                                        String rating = Integer.toString(rs.getInt("rating"));

                                        recordReview[0] = writer;
                                        recordReview[1] = review;
                                        recordReview[2] = rating;
                                        tableModelReview.addRow(recordReview);
                                    }
                                }
                            }
                        }
                    }


                }
                else if(select == 2){

                    String select_menu2;
                    System.out.println("지역구를 선택해주세요.\n");
                    System.out.println("1.관악구  2.종로구  3.강서구  4.구로구  5.성동구  6.용산구  7.양천구  8.서초구  9.마포구\n 10.중구" +
                            "  11.동대문구  12.강북구  13.동작구  14.강동구  15.광진구  16.송파구  17.은평구  18.중랑구  19.서대문구  20.도봉구  21.노원구 22.강남구 23.금천구 24.성북구 25.영등포구\n");
                    select_menu2 = sc.nextLine();
                    String sql = "select distinct name, type, vdate, disposal, add\n"
                            + "from Violated C\n"
                            + "where areaname='"+select_menu2+"';";
                    rs = st.executeQuery(sql);
                    

                }else if(select == 3){

                }
                else if(select == 4){

                }
                else return;
            }


        }catch(SQLException ex){
            throw ex;
        }

    }

}
