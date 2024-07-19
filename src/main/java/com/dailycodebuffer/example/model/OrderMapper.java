package com.dailycodebuffer.example.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<OrderDetails> {
    @Override
    public OrderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(rs.getInt("order_id"));
        orderDetails.setItemName(rs.getString("item_name"));
        orderDetails.setCustomerName(rs.getString("customer_name"));
        orderDetails.setAddress(rs.getString("address"));
        return orderDetails;
    }
}
