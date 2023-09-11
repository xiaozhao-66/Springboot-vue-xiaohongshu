package com.yanhuo.common.utils;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils {


    public static boolean isHttpUrl(String urls) {

        //设置正则表达式
        String regex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        return mat.matches();//判断是否匹配
    }

}
