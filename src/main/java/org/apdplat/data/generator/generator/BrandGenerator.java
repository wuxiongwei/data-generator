package org.apdplat.data.generator.generator;

import org.apdplat.data.generator.mysql.MySQLUtils;
import org.apdplat.data.generator.utils.MultiResourcesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by ysc on 18/04/2018.
 */
public class BrandGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrandGenerator.class);

    public static void clear(){
        MySQLUtils.clean("brand");
    }

    public static int generate(int batchSize){
        Connection con = MySQLUtils.getConnection();
        if(con == null){
            return 0;
        }
        int brandCount = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "insert into brand (id, name) values(?, ?);";
        try {
            con.setAutoCommit(false);
            pst = con.prepareStatement(sql);
            for(String name : MultiResourcesUtils.load("brand.txt")){
                brandCount++;
                pst.setInt(1, brandCount);
                pst.setString(2, name);
                pst.addBatch();
                if(brandCount % batchSize == 0) {
                    pst.executeBatch();
                }
            }
            pst.executeBatch();
            con.commit();
            LOGGER.info("保存到数据库成功");
        } catch (Exception e) {
            LOGGER.error("保存到数据库失败", e);
        } finally {
            MySQLUtils.close(con, pst, rs);
        }
        return brandCount;
    }

    public static void main(String[] args) {
        clear();
        System.out.println(generate(1000));
    }
}
