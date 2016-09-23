package com.soda.dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface Subway {
      Map getlongitudeandlatitude(String address) throws IOException;
}
