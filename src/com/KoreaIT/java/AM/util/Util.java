package com.KoreaIT.java.AM.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String getNowDateStr() {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf1.format(now);
    }
}