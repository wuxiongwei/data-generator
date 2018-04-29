package org.apdplat.data.generator.mysql2es;

import org.apdplat.data.generator.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysc on 20/04/2018.
 */
public class Area extends Output{
    private static final Logger LOGGER = LoggerFactory.getLogger(Area.class);

    private static final String INDEX = "area";
    private static final String TYPE = "area";

    private void generateCommand() {
        String sql = "select id, city, city_full, province, province_full, area.longitude, area.latitude from area";
        generateCommand("area", sql, INDEX, TYPE, INDEX+".sh");
    }

    @Override
    protected Map<String, Object> getRow(ResultSet rs){
        Map<String, Object> data = new HashMap<>();
        try {
            int id = rs.getInt("id");
            String city = rs.getString("city");
            String city_full = rs.getString("city_full");
            String province = rs.getString("province");
            String province_full = rs.getString("province_full");
            Float longitude = rs.getFloat("longitude");
            Float latitude = rs.getFloat("latitude");

            data.put("id", id);
            data.put("city", city);
            data.put("city_full", city_full);
            data.put("province", province);
            data.put("province_full", province_full);
            Map<String, Float> location = new HashMap<>();
            location.put("lon", longitude);
            location.put("lat", latitude);
            data.put("geo_location", location);
        }catch (Exception e){
            LOGGER.error("获取数据异常", e);
        }
        return data;
    }

    @Override
    public void run(){
        long start = System.currentTimeMillis();
        generateCommand();
        LOGGER.info("生成区域JSON文档耗时: {}", TimeUtils.getTimeDes(System.currentTimeMillis()-start));
    }

    public static void main(String[] args) throws Exception{
        new Area().run();
    }
}
