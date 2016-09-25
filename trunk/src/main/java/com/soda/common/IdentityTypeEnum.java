package com.soda.common;

import java.io.Serializable;

/**
 * Created by kcao on 2016/9/23.
 */
public enum IdentityTypeEnum implements Serializable {
    IMEI,CARDID;

    public static IdentityTypeEnum convert(String val){
        if(IMEI.toString().equals(val)){
            return IMEI;
        }
        if(CARDID.toString().equals(val)){
            return CARDID;
        }
        return null;
    }
}
