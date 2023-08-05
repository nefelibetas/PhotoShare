package com.fish.photoshare.utils;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: LL
 * @Description: java 正则校验数据是否符合规范
 * @Date: Create in 16:15 2020/10/19
 */
public class FormatUtils {
    //密码长度为8到20位,必须包含字母和数字，字母区分大小写
    private static String regEx1 = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{8,20})$";
    //密码中必须包含字母、数字、特称字符，至少8个字符，最多16个字符
    private static String regEx2 = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(.{8,16})$";

    /**
     * 密码长度为8到20位,必须包含字母和数字，字母区分大小写
     * @param password
     * @return
     */
    public static boolean checkPassword(String password){
        Pattern Password_Pattern = Pattern.compile(regEx1);
        Matcher matcher = Password_Pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 密码中必须包含字母、数字、特称字符，至少8个字符，最多16个字符
     * @param password
     * @return
     */
    public static boolean password(String password){
        Pattern Password_Pattern = Pattern.compile(regEx2);
        Matcher matcher = Password_Pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}

