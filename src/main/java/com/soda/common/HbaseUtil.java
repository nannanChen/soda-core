package com.soda.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
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

    public static Result getPrecursorResult(String precursor){
        if("".equals(precursor)||"null".equals(precursor)||precursor==null){
            return null;
        }
        HTable table = null;
        try {
            table = new HTable(hadoopConf, "point_detail");
            while (true){
                Get get = new Get(precursor.getBytes()); // 根据主键查询
                Result result = table.get(get);
                precursor = Bytes.toString(result.getValue("basic".getBytes(),"precursor".getBytes()));
                if("".equals(precursor)||"null".equals(precursor)||precursor==null){
                    return result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        HTable table = new HTable(hadoopConf, "point_detail");
//        Scan scan = new Scan();

//        Filter prefixFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator("201603019".getBytes()));
//        Filter prefixFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(".*2016030117"));
//        scan.setFilter(prefixFilter);
//
//        ResultScanner scanner = table.getScanner(scan);
//        for (Result res : scanner) {
//            System.out.println(res);
//        }
//        scanner.close();

    }

}
