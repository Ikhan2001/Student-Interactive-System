package com.cleducate.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {

    public static boolean isValidTimeZone(String timeZone){
        try{
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone));
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
