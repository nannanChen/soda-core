package com.soda.common;

import com.soda.vo.PointDetail;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by kcao on 2016/3/11.
 */
public class HbaseUtil implements Serializable {

    protected static Logger log = Logger.getLogger(HbaseUtil.class);


    private static Configuration hadoopConf = new Configuration();
    private static HBaseAdmin admin;

    private static HTablePool pool;

    static {
        hadoopConf.set("hbase.zookeeper.quorum", ConstantsUtil.ZOOKEEPER_ADDRESS);
        hadoopConf.set("hbase.rootdir", ConstantsUtil.HDFS_ADDRESS + "/hbase");
        try {
            admin = new HBaseAdmin(hadoopConf);
            pool=new HTablePool(hadoopConf, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HBaseAdmin getHBaseAdmin(){
        return admin;
    }

    public static HTablePool getHTablePool(){
        return pool;
    }

    public static Configuration getConfiguration(){
        return hadoopConf;
    }

    @SuppressWarnings("all")
    public static void main(String[] args) throws IOException {

        HTable table = new HTable(hadoopConf, "Bttray_WhiteList");
        for(int i=0;i<20;i++){
            Put put = new Put(Bytes.toBytes("OP1KPQWJOPEQWOENMNFVNWOVNWOEGIWJIOE"+i));
            put.add(Bytes.toBytes("basic"), Bytes.toBytes("file_name"), Bytes.toBytes(i+".txt"));
            put.add(Bytes.toBytes("basic"), Bytes.toBytes("file_path"), Bytes.toBytes("c://"+i+".txt"));
            table.put(put);
        }

    }

}
