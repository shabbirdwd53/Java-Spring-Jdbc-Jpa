package com.dailycodebuffer.example.repository;

import com.dailycodebuffer.example.entity.OrderEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class OrderRepository {

    @Autowired private EntityManager entityManager;


    public void getAllOrders() {
        List<OrderEntity> orderEntities= 
        entityManager.createQuery("from OrderEntity", OrderEntity.class)
                .getResultList();
        System.out.println("orderEntities = " + orderEntities);
    }

    public void insertAndGetOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(11);
        orderEntity.setCustomerName("JPA");
        orderEntity.setItemName("JPA Item");
        orderEntity.setAddress("JPA Address");

        entityManager.persist(orderEntity);

        OrderEntity insertedOrderEntity =
                entityManager.find(OrderEntity.class,11);

        System.out.println("insertedOrderEntity = " + insertedOrderEntity);
    }
}
