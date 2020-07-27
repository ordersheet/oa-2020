package com.byoa.system.config;

public class StringUtils {

    public static Long[] getLongArray(String s){
        String[] split = s.split(",");
            Long[] longs = new Long[split.length];
            for (int i = 0; i < split.length; i++) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(split[i])) {
                    Long l = Long.valueOf(split[i]);
                    longs[i] = l;}
            }
            return longs;
    }
}
