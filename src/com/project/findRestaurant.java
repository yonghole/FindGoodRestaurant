package com.project;

import javax.swing.*;
import javax.swing.border.Border;
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
            while (select != 6){
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

                    List<List<String>> list = new ArrayList<>();

                    while(rs.next()){
                        List<String> tmp = new ArrayList<>();
                        String name = rs.getString("name");
                        String address = rs.getString("add");
                        String type = rs.getString("type");
                        String telnum = rs.getString("telnum");
                        String cmenu = rs.getString("cmenu");
                        String cid = Integer.toString(rs.getInt("cid"));

//                        record[0] = Integer.toString(++i);
                        tmp.add(Integer.toString(++i));
//                        record[1] = name;
                        tmp.add(name);
//                        record[2] = address;
                        tmp.add(address);
                        tmp.add(type);
                        tmp.add(telnum);
                        tmp.add(cmenu);
                        tmp.add(cid);
//                        tableModel.addRow(record);
                        list.add(tmp);

                    }
                    Object record[][] = new Object[list.size()][3];
                    for(int j=0;j<list.size();j++){
                        record[j][0] = list.get(j).get(0);
                        record[j][1] = list.get(j).get(1);
                        record[j][2] = list.get(j).get(2);
                    }
                    final JFrame frame = new JFrame(select_menu1 + "에서 위생 불량 적발 업체를 제외한 결과입니다.");
                    frame.setLayout(new BorderLayout());
//                    frame.setLocation(200,400);
//                    frame.setSize(1499,1000);
//                    Object record[] = new Object[3];
                    String header[] = {"번호", "업소명", "주소"};
                    DefaultTableModel tableModel = new DefaultTableModel(record,header);
                    JTable table = new JTable(tableModel);
                    JScrollPane sp = new JScrollPane(table);

//                    sp.setLocation(0,0);
//                    sp.setSize(950,200);
//                    frame.setVisible(true);

                    frame.setSize(new Dimension(1000,1000));
                    frame.add(sp,BorderLayout.NORTH);
//                    frame.setLocationRelativeTo(null);
//                    frame.setUndecorated(true);
                    frame.setVisible(true);
//                    frame.repaint();

                    int select_store = 0;
                    while(select_store >=-1){
                        System.out.println("업체를 선택해주세요. 표 기준의 번호를 선택해주세요. 메인 화면으로 돌아가시려면 -1을 입력해주세요.\n");
                        select_store = Integer.parseInt(sc.nextLine())-1;
                        if(select_store<=-1 || select_store >=list.size()){
                            System.out.println("메인 화면으로 돌아갑니다.\n");
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
//                                    JFrame frameReview = new JFrame(select_menu1 + "의 리뷰 정보입니다.");
//                                    frameReview.setLocation(200,400);
//                                    frameReview.setSize(1000,500);


                                    List<List<String>> listReview = new ArrayList<>();
                                    rs = st.executeQuery(sql);
                                    while(rs.next()){
                                        List<String> tmp = new ArrayList<>();
                                        String writer = rs.getString("writer");
                                        String review = rs.getString("review");
                                        String rating = Integer.toString(rs.getInt("rating"));

//                                        recordReview[0] = writer;
//                                        recordReview[1] = review;
//                                        recordReview[2] = rating;
                                        tmp.add(writer);
                                        tmp.add(review);
                                        tmp.add(rating);
                                        listReview.add(tmp);


//                                        tableModelReview.addRow(recordReview);
                                    }

                                    Object recordReview[][] = new Object[listReview.size()][3];
                                    for(int j=0;j<listReview.size();j++){
                                        recordReview[j][0] = listReview.get(j).get(0);
                                        recordReview[j][1] = listReview.get(j).get(1);
                                        recordReview[j][2] = listReview.get(j).get(2);
                                    }

//                                    Object recordReview[] = new Object[3];
                                    String headerReview[] = {"글쓴이", "리뷰 내용", "별점 (5점 만점)"};
                                    DefaultTableModel tableModelReview = new DefaultTableModel(recordReview,headerReview);
                                    JTable tableReview = new JTable(tableModelReview);
                                    JScrollPane spReview = new JScrollPane(tableReview);
//                                    JPanel pane = new JPanel();
//                                    pane.setSize(1000,25);
//                                    pane.setLocation(0,450);

                                    JLabel label = new JLabel(cur.get(1)+"의 리뷰입니다.",SwingConstants.CENTER);
                                    label.setLocation(0,575);
                                    label.setSize(1000,25);
                                    frame.add(label,BorderLayout.CENTER);
//                                    label.setLabelFor(spReview);


//                                    pane.add(label);
//                                    frame.add(pane,BorderLayout.CENTER);
//                                    sp.add(tableReview);
                                    spReview.setLocation(0,600);
                                    spReview.setSize(1000,150);
//                                    frameReview.setVisible(true);
                                    frame.add(spReview,BorderLayout.SOUTH);





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
                    String sql = "select distinct * \n"
                            + "from Violated C\n"
                            + "where areaname='"+select_menu2+"';";
                    rs = st.executeQuery(sql);
                    int i = 0;
                    JFrame frame = new JFrame(select_menu2 + "에서 위생 불량 적발 업체를 검색한 결과입니다.");
                    frame.setLocation(100,50);
                    frame.setSize(1000,1000);
                    Object record[] = new Object[4];
                    String header[] = {"번호", "업소명", "주소","업종"};
                    DefaultTableModel tableModel = new DefaultTableModel(header,0);
                    JTable table = new JTable(tableModel);
                    JScrollPane sp = new JScrollPane(table);
//                    sp.setLocation(50,50);
                    frame.add(sp,BorderLayout.NORTH);
//                    sp.setSize(800,350);
                    frame.setVisible(true);

                    List<List<String>> list = new ArrayList<>();

                    while(rs.next()){
                        List<String> tmp = new ArrayList<>();
                        String name = rs.getString("name");
                        String address = rs.getString("add");
                        String vcontent = rs.getString("vcontent");
                        String vdate = rs.getString("vdate");
                        String type = rs.getString("type");
                        String disposal = rs.getString("disposal");
                        String vid = Integer.toString(rs.getInt("vid"));

                        record[0] = Integer.toString(++i);
                        tmp.add(Integer.toString(i));
                        record[1] = name;
                        tmp.add(name);
                        record[2] = address;
                        record[3] = type;
                        tmp.add(address);
                        tmp.add(vcontent);
                        tmp.add(vdate);
                        tmp.add(disposal);
                        tmp.add(vid);
                        tmp.add(type);
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
                            while(select_detail != 3){
                                System.out.println("1.위반 내용 보기 2. 업체 리뷰 보기 3. 돌아가기\n");
                                select_detail = Integer.parseInt(sc.nextLine());
                                List<String> cur = list.get(select_store);
                                if(select_detail == 1){

                                    System.out.println("선택하신 업체 " + cur.get(1) + "의 위반 사실은 다음과 같습니다.\n");
                                    System.out.println("|업체명 : " + cur.get(1) + " |적발 일시 :" + cur.get(4) + " |위반 사실 : " + cur.get(3) + " |처분 결과 : " + cur.get(5)+"\n");
                                }
                                else if(select_detail == 2){
                                    sql = "select writer, review, rating from review where vid =" + cur.get(6) + " order by rating;";
//                                    JFrame frameReview = new JFrame(select_menu2 + "의 리뷰 정보입니다.");
//                                    frameReview.setLocation(200,400);
//                                    frameReview.setSize(1000,500);


                                    Object recordReview[] = new Object[3];
                                    String headerReview[] = {"글쓴이", "리뷰 내용", "별점 (5점 만점)"};
                                    DefaultTableModel tableModelReview = new DefaultTableModel(headerReview,0);
                                    JTable tableReview = new JTable(tableModelReview);
                                    JScrollPane spReview = new JScrollPane(tableReview);
//                                    frameReview.setVisible(true);
//                                    JLabel label = new JLabel(cur.get(1)+"의 리뷰입니다.");
//                                    label.setLabelFor(spReview);
//                                    label.setLocation(450,520);
//                                    frame.add(label);
                                    spReview.setLocation(0,600);
                                    spReview.setSize(1000,150);

                                    frame.add(spReview,BorderLayout.SOUTH);
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

                }else if(select == 3) {
                    int select_menu3 = -1;
                    String menu = "";
                    while (select_menu3 < 0) {
                        System.out.println("메뉴를 선택해주세요. 번호로 입력해주세요.\n");
                        System.out.println("1. 분식 2. 한식 3. 중국식 4. 일식 5. 경양식 6. 뷔페식 7. 정종/대포집(선술집) 8.식육취급 9. 복어취급\n");
                        select_menu3 = Integer.parseInt(sc.nextLine());

                        if (select_menu3 == 1) {
                            menu = "분식";
                        } else if (select_menu3 == 2) {
                            menu = "한식";
                        } else if (select_menu3 == 3) {
                            menu = "중국식";
                        } else if (select_menu3 == 4) {
                            menu = "일식";
                        } else if (select_menu3 == 5) {
                            menu = "경양식";
                        } else if (select_menu3 == 6) {
                            menu = "뷔페식";
                        } else if (select_menu3 == 7) {
                            menu = "정종/대포집(선술집)";
                        } else if (select_menu3 == 8) {
                            menu = "식육취급";
                        } else if (select_menu3 == 9) {
                            menu = "복어취급";
                        } else {
                            System.out.println("잘못된 선택입니다. 다시 선택해주세요.\n");
                            select_menu3 = 0;
                        }
                    }

                    String sql = "select distinct name, add, type, telnum, cmenu, cid\n"
                            + "	from Certificated C\n"
                            + "	where type='" + menu + "' and not name = any(select C1.name from Certificated C1, Violated V1\n"
                            + "						  where C1.name=V1.name and C1.areaname=V1.areaname);\n";
                    rs = st.executeQuery(sql);
                    int i = 0;
                    JFrame frame = new JFrame(menu + "을 취급하는 인증 업체입니다.");
                    frame.setLocation(200, 400);
                    frame.setSize(1000, 1000);
                    Object record[] = new Object[4];
                    String header[] = {"번호", "업소명", "주소", "업종"};
                    DefaultTableModel tableModel = new DefaultTableModel(header, 0);
                    JTable table = new JTable(tableModel);
                    JScrollPane sp = new JScrollPane(table);
                    frame.add(sp, BorderLayout.NORTH);
                    frame.setVisible(true);

                    List<List<String>> list = new ArrayList<>();

                    while (rs.next()) {
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
                        tableModel.addRow(record);
                        list.add(tmp);
                    }
                    int select_store = 0;
                    while (select_store >= -1) {
                        System.out.println("업체를 선택해주세요. 표 기준의 번호를 선택해주세요. 메인 화면으로 돌아가시려면 -1을 입력해주세요.\n");
                        select_store = Integer.parseInt(sc.nextLine()) - 1;
                        if (select_store <= -1 || select_store >= list.size()) {
                            System.out.println("메인 화면으로 돌아갑니다.\n");
//                            continue;
                        }
                        else {
                            int select_detail = 0;
                            while (select_detail != 4) {
                                System.out.println("1.업체 연락처 보기 2. 업체 대표 메뉴 보기 3. 업체 리뷰 보기 4. 돌아가기\n");
                                select_detail = Integer.parseInt(sc.nextLine());
                                List<String> cur = list.get(select_store);
                                if (select_detail == 1) {

                                    System.out.println("선택하신 업체 " + cur.get(1) + "의 연락처는 " + cur.get(4) + " 입니다.\n");
                                } else if (select_detail == 2) {
                                    System.out.println("선택하신 업체 " + cur.get(1) + "의 대표 메뉴는 " + cur.get(5) + " 입니다.\n");
                                } else if (select_detail == 3) {
                                    sql = "select writer, review, rating from review where cid =" + cur.get(6) + " order by rating;";
//                                    JFrame frameReview = new JFrame(cur.get(1) + "의 리뷰 정보입니다.");
//                                    frameReview.setLocation(200, 400);
//                                    frameReview.setSize(1000, 500);


                                    Object recordReview[] = new Object[3];
                                    String headerReview[] = {"글쓴이", "리뷰 내용", "별점 (5점 만점)"};
                                    DefaultTableModel tableModelReview = new DefaultTableModel(headerReview, 0);
                                    JTable tableReview = new JTable(tableModelReview);
                                    JScrollPane spReview = new JScrollPane(tableReview);
//                                    frameReview.setVisible(true);
//                                    frameReview.add(spReview, "Center");
                                    spReview.setLocation(0,600);
                                    spReview.setSize(1000,150);
                                    frame.add(spReview,BorderLayout.SOUTH);

                                    rs = st.executeQuery(sql);
                                    while (rs.next()) {
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
                else if(select == 4){
                    System.out.println("업소명을 입력해주세요.");
                    String store_name = sc.nextLine();

                    String sql = "SELECT * from Certificated where name = '" + store_name + "' and name not in (select distinct name from Violated where name = '"+store_name + "');";


                    rs = st.executeQuery(sql);
                    List<List<String>> list = new ArrayList<>();
                    int i = 0;
                    while(rs.next()){
                        List<String> tmp = new ArrayList<>();
                        String name = rs.getString("name");
                        String address = rs.getString("add");
                        String type = rs.getString("type");
                        String telnum = rs.getString("telnum");
                        String cmenu = rs.getString("cmenu");
                        String cid = Integer.toString(rs.getInt("cid"));

//                        record[0] = Integer.toString(++i);
//                        tmp.add(Integer.toString(i));
//                        record[1] = name;
                        tmp.add(name);
//                        record[2] = address;
                        tmp.add(address);
                        tmp.add(type);
                        tmp.add(telnum);
                        tmp.add(cmenu);
                        tmp.add(cid);
//                        tableModel.addRow(record);
                        System.out.format("선택하신 업체 %s는(은) 위반 사항이 없는 안전 업체입니다.\n\n",name);
                        System.out.format("%s %s %s %s\n\n", name,address, type, telnum);
//                        list.add(tmp);
                         i++;
                    }
                    if(i == 0){
                        System.out.format("입력하신 업체 %s에 대한 정보를 찾을 수 없습니다. 그런 식당이 존재하지 않거나 인증을 받지 못한 업소입니다.\n\n",store_name);
                    }
//                    for(int i=0;i<list.size();i++){
//                        System.out.format("%s %s %s %s\n", list.get(i).get(0), list.get(i).get(1), list.get(i).get(2), list.get(i).get(3));
//                    }
                }
                else if(select == 5){
                    String select_menu1;
                    System.out.println("지역구를 선택해주세요.\n");
                    System.out.println("1.관악구  2.종로구  3.강서구  4.구로구  5.성동구  6.용산구  7.양천구  8.서초구  9.마포구\n 10.중구" +
                            "  11.동대문구  12.강북구  13.동작구  14.강동구  15.광진구  16.송파구  17.은평구  18.중랑구  19.서대문구  20.도봉구  21.노원구 22.강남구\n");
                    select_menu1 = sc.nextLine();

                    String sql = "select distinct name, add, type, telnum, cmenu, cid\n"
                            + "	from Certificated C\n"
                            + "	where areaname='"+select_menu1+"' and not name = any(select C1.name from Certificated C1, Violated V1\n"
                            + "						  where C1.name=V1.name and C1.areaname=V1.areaname) and cmenu like '%비건%';\n";
                    rs = st.executeQuery(sql);

                    int i = 0;
                    final JFrame frame = new JFrame(select_menu1 + "에서 인증 비건 식당을 검색 결과입니다.");
                    frame.setLocation(200,400);
                    frame.setSize(1000,1000);
                    Object record[] = new Object[3];
                    String header[] = {"번호", "업소명", "주소"};
                    DefaultTableModel tableModel = new DefaultTableModel(header,0);
                    JTable table = new JTable(tableModel);
                    JScrollPane sp = new JScrollPane(table);

                    frame.add(sp,BorderLayout.NORTH);
                    frame.setVisible(true);
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
                        tableModel.addRow(record);
                        list.add(tmp);

                    }
                    frame.repaint();
                    int select_store = 0;
                    while(select_store >=-1){
                        System.out.println("업체를 선택해주세요. 표 기준의 번호를 선택해주세요. 메인 화면으로 돌아가시려면 -1을 입력해주세요.\n");
                        select_store = Integer.parseInt(sc.nextLine())-1;
                        if(select_store<=-1 || select_store >=list.size()){
                            System.out.println("메인 화면으로 돌아갑니다.\n");
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
//                                    final JFrame frameReview = new JFrame(select_menu1 + "의 리뷰 정보입니다.");
//                                    frameReview.setLocation(200,400);
//                                    frameReview.setSize(1000,500);


                                    Object recordReview[] = new Object[3];
                                    String headerReview[] = {"글쓴이", "리뷰 내용", "별점 (5점 만점)"};
                                    DefaultTableModel tableModelReview = new DefaultTableModel(headerReview,0);
                                    JTable tableReview = new JTable(tableModelReview);
                                    JScrollPane spReview = new JScrollPane(tableReview);
//                                    frame.setVisible(true);
                                    spReview.setLocation(0,600);
                                    spReview.setSize(1000,150);
                                    frame.add(spReview,BorderLayout.SOUTH);
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
//                                        frameReview.repaint();
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    System.out.println("종료합니다. ");
                    System.exit(0);
                }
            }



        }catch(SQLException ex){
            throw ex;
        }

    }

}
