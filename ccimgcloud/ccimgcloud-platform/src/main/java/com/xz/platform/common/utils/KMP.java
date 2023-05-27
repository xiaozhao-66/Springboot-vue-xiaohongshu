package com.xz.platform.common.utils;

public class KMP {
    public static void main(String[] args) {
        String str1 = "当年皇帝回复帕杰佛排队交费哦亲";
        String str2 = "1111费";
        System.out.println(kmpSearch(str1, str2, kmpNext(str2)));

    }
    //kmp搜索算法

    /**
     * @param str1 源字符串
     * @param str2 子串
     * @param next 部分匹配表
     * @return 如果匹配到了返回第一个匹配的位置, 没有返回-1
     */
    public static int kmpSearch(String str1, String str2, int[] next) {
        //遍历
        for (int i = 0, j = 0; i < str1.length(); i++) {
            //需要处理不相等的时候
            while (j > 0 && str1.charAt(i) != str2.charAt(j)) {
                j = next[j - 1];
            }
            if (str1.charAt(i) == str2.charAt(j)) {
                j++;
            }
            if (j == str2.length()) {
                return i - j + 1;
            }


        }
        return -1;


    }

    //获取到一个字符串的部分匹配值
    public static int[] kmpNext(String str) {
        int[] next = new int[str.length()];
        next[0] = 0;  //当字符串的长度为1时
        for (int i = 1, j = 0; i < str.length(); i++) {
            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = next[j - 1];
            }
            if (str.charAt(i) == str.charAt(j)) {
                j++;
            }
            next[i] = j;

        }

        return next;

    }
}
