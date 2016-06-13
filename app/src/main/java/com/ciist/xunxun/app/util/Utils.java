package com.ciist.xunxun.app.util;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xieke on 2016/5/6 0006.
 */
public  class Utils {

    public static String PHONE_NUM = "PHONE_NUM";  //电话号码
    public static String CODE = "CODE";  //验证码


    /**
     * 判断EditText输入的信息是否有null值
     * @param editTexts   //一个页面所有EditText内容的数组
     * @return
     */
    public static boolean editIsNull(Context context,String[]editTexts){
        boolean is = true;
        for (int i = 0;i<editTexts.length;i++){
            String tag = editTexts[i];
            if (tag.length()<1 || tag.equals("")) {
                Toast.makeText(context,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                is = false;
                break;
            }
        }
        return is;
    }


    /**
     * 验证是否为手机号码
     * @param phoneNum
     * @return
     */
    public static boolean isPhoneNum(Context context,String phoneNum){

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNum);
        if (!m.matches()){
            Toast.makeText(context,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 是否是8位电话号码
     * @param phonenumber
     * @return
     */
    public static boolean isTelNum(String phonenumber) {
        String phone = "0\\d{2,3}-\\d{7,8}";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }




}
