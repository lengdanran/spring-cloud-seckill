package com.danran.common.util;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Classname OrderIDUtil
 * @Description TODO
 * @Date 2021/6/7 14:35
 * @Created by ASUS
 */
@Resource
public class OrderIDUtil {

    @Transactional
    public String getOrderID() {
//        int hashCodeV = UUID.randomUUID().toString().hashCode();
//        if (hashCodeV < 0) {//有可能是负数
//            hashCodeV = -hashCodeV;
//        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String prefix = format.format(new Date());
        return prefix + UUID.randomUUID().toString().replace("-", "");
    }

    //得到32位的uuid
    public String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();

    }

    private static String getRandomChinese() {
        String str = "";
        int hightPos; //
        int lowPos;
        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();
        try {
            str = new String(b, "GBK");
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getRandomBookName() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            stringBuilder.append(getRandomChinese());
        }
        return stringBuilder.toString();
    }

    private static void createTestData() throws IOException {
        BufferedReader bookReader = new BufferedReader(new FileReader("F:\\EEworkspace\\miaosha\\src\\RandomBookData.csv"));
        BufferedReader userReader = new BufferedReader(new FileReader("src/RandomUserData.csv"));

        List<String> bookList = new ArrayList<>();
        List<String> userList = new ArrayList<>();
        String tmp;

        while ((tmp = bookReader.readLine()) != null) {
            bookList.add(tmp.split(",")[0]);
        }
        System.out.println(bookList.size());
        while ((tmp = userReader.readLine()) != null) {
            userList.add(tmp.split(",")[0]);
        }
        System.out.println(userList.size());

        File file;
        OutputStream os;
        OutputStreamWriter osw;
        file = new File("src/RandomTestData.csv" );
        os = new FileOutputStream(file);
        osw = new OutputStreamWriter(os);
        List<String> data = new ArrayList<>();
        for (String user_str : userList) {
            for (String book_str : bookList) {
                data.add(user_str + "," + book_str + "\n");
            }
        }
        System.out.println(data.get(0));
        Collections.shuffle(data);
        System.out.println(data.get(0));
        for (String datum : data) {
            osw.write(datum);
        }
        osw.close();






    }

    public static void main(String[] args) throws IOException {
        createTestData();
////        System.out.println(new OrderIDUtil().getOrderID());
//        OrderIDUtil orderIDUtil = new OrderIDUtil();
//        Random random = new Random();
////        System.out.println(new OrderIDUtil().getUUID32().toUpperCase());
//        File file;
//        OutputStream os;
//        OutputStreamWriter osw;
//        file = new File("src/RandomUserData.csv" );
//        os = new FileOutputStream(file);
//        osw = new OutputStreamWriter(os);
////        for (int i = 0; i < 100; i++) {
////            osw.write(orderIDUtil.getUUID32().toUpperCase() + "," + getRandomBookName() +"," +(random.nextInt(100) + 100) + "," + "0,1\n");
////        }
//        for (int i = 0; i < 10000; i++) {
//            osw.write((i + 2) +"," + getRandomBookName() + "\n");
//        }
//        osw.flush();
//        osw.close();
    }
}
