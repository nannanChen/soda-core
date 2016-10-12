package com.soda.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kcao on 2016/10/12.
 */
public class XYData implements Serializable {

    public static Map<String,Double> xyDataMap=new HashMap<String,Double>();

    static {
        xyDataMap.put("20160301",10.42113418);
        xyDataMap.put("20160302",10.53557433);
        xyDataMap.put("20160303",12.99635322);
        xyDataMap.put("20160304",13.68899294);
        xyDataMap.put("20160305",8.576316819);
        xyDataMap.put("20160306",8.075607216);
        xyDataMap.put("20160307",13.06573184);
        xyDataMap.put("20160308",12.66255211);
        xyDataMap.put("20160309",10.20774007);
        xyDataMap.put("20160310",10.70355287);
        xyDataMap.put("20160311",13.84432963);
        xyDataMap.put("20160312",10.8920161);
        xyDataMap.put("20160313",7.566077597);
        xyDataMap.put("20160314",10.26024431);
        xyDataMap.put("20160315",13.07745981);
        xyDataMap.put("20160316",13.08497271);
        xyDataMap.put("20160317",10.32696446);
        xyDataMap.put("20160318",11.04145269);
        xyDataMap.put("20160319",10.75913341);
        xyDataMap.put("20160320",10.39072249);
        xyDataMap.put("20160321",10.53315003);
        xyDataMap.put("20160322",10.69299785);
        xyDataMap.put("20160323",12.86384471);
        xyDataMap.put("20160324",12.99421147);
        xyDataMap.put("20160325",11.27279865);
        xyDataMap.put("20160326",9.4936576);
        xyDataMap.put("20160327",10.56075846);
        xyDataMap.put("20160328",13.15830963);
        xyDataMap.put("20160329",8.63233555);
        xyDataMap.put("20160330",9.569480407);
        xyDataMap.put("20160331",13.04954706);
    }


    public static void main(String[] args) throws Exception {
        for(Map.Entry<String,Double> entry:xyDataMap.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue().intValue());
        }
    }

}
