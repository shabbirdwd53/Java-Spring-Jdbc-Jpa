package com.dailycodebuffer.example;

import com.dailycodebuffer.example.model.OrderDetails;
import com.dailycodebuffer.example.model.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
@Profile("jdbc")
public class JDBCCommandLineRunner implements CommandLineRunner {

    @Autowired JdbcTemplate template;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("IN JDBCCommandLineRunner");
        template.execute("truncate table order_details");
        insertHardCodeData();
        insertDataUsingPreparedStatement(2,"Jeans", "Nikhil", "Bengaluru");
        insertData(3,"Shirt", "Nikhil", "Bengaluru");
        deleteData(3);

        insertBatchData(5);

        selectOrder();

        selectSingleOrder(2);

        selectSingleColumn();
    }

    private void selectSingleColumn() {
        String query = """
                    SELECT ITEM_NAME FROM ORDER_DETAILS
                    """;
        List<String> itemList=
                template.queryForList(query, String.class);

        System.out.println("itemList = " + itemList);
    }

    private void selectSingleOrder(int id) {
        try {
            String query = """
                    SELECT * FROM ORDER_DETAILS WHERE ORDER_ID=:ORDER_ID
                    """;

            MapSqlParameterSource mapSqlParameterSource
                    = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("ORDER_ID",id);

            OrderDetails orderDetails =
                    namedParameterJdbcTemplate.queryForObject(query,mapSqlParameterSource,new OrderMapper());

            System.out.println("orderDetails = " + orderDetails);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No data found for id = " + id);
        }
    }

    private void selectOrder() {
        List<OrderDetails> orderDetails
                = template.query("SELECT * FROM ORDER_DETAILS",new OrderMapper());
        System.out.println("Inside selectOrder orderDetails = " + orderDetails);
    }

    private void insertBatchData(int count) {
        String query = """
                INSERT INTO ORDER_DETAILS values(:order_id,:item_name,
                :customer_name,:address)
                """;
        Map[] params = new HashMap[count];
        IntStream.range(0,count)
                .forEach(i -> {
                    Map param = new HashMap();
                    param.put("order_id", i+4);
                    param.put("item_name", "itemName"+ i);
                    param.put("customer_name", "customerName"+i);
                    param.put("address", "address"+i);
                    params[i] = param;
                });

        int[] rowCounts =
                namedParameterJdbcTemplate.batchUpdate(query,params);

        System.out.println("Inside insertBatchData rowCounts = " + rowCounts.length);
    }

    private void deleteData(int id) {
        String query = """
                DELETE FROM ORDER_DETAILS WHERE ORDER_ID=:ORDER_ID
                """;
        Map params = new HashMap();
        params.put("ORDER_ID",id);
        int rowCount =
                namedParameterJdbcTemplate.update(query,params);
        System.out.println("Inside deleteData rowCount = " + rowCount);
    }

    private void insertData(int id,
                            String itemName,
                            String customerName,
                            String address) {

        String query = """
                INSERT INTO ORDER_DETAILS values(:order_id,:item_name,
                :customer_name,:address)
                """;

        Map params = new HashMap();
        params.put("order_id", id);
        params.put("item_name", itemName);
        params.put("customer_name", customerName);
        params.put("address", address);

        int rowCount =
                namedParameterJdbcTemplate.update(query,params);
        System.out.println("Inside insertData rowCount = " + rowCount);
    }

    private void insertDataUsingPreparedStatement(int id,
                                                  String itemName,
                                                  String customerName,
                                                  String address) {

        String query = """
                INSERT INTO ORDER_DETAILS values(?,?,?,?)
                """;

        int rowCount =
                template.update(query, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setInt(1,id);
                        ps.setString(2,itemName);
                        ps.setString(3,customerName);
                        ps.setString(4,address);
                    }
                });

        System.out.println("rowCount = " + rowCount);
    }

    private void insertHardCodeData() {
        System.out.println("Inside insertHardCodeData Method Begins");
        template.execute("insert into order_details values(1,'Mobile', 'Shabbir', 'Bengaluru')");
        System.out.println("Inside insertHardCodeData Method Ends");
    }
}
