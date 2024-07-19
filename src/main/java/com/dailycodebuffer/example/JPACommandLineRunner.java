package com.dailycodebuffer.example;

import com.dailycodebuffer.example.entity.OrderEntity;
import com.dailycodebuffer.example.repository.OrderJPARepository;
import com.dailycodebuffer.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
@Profile("jpa")
public class JPACommandLineRunner implements CommandLineRunner {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderJPARepository jpaRepository;

    @Override
    public void run(String... args) throws Exception {
        //repository.getAllOrders();
        //repository.insertAndGetOrder();
        System.out.println("Deleting all the data");
        jpaRepository.deleteAll();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(13);
        orderEntity.setCustomerName("JPA");
        orderEntity.setItemName("JPA Item");
        orderEntity.setAddress("JPA Address");

        System.out.println("Inserting new Record with JPA");
        jpaRepository.save(orderEntity);

        Optional<OrderEntity> byId = jpaRepository.findById(13);
        System.out.println("byId.get() = " + byId.get());

        OrderEntity entity = byId.get();
        entity.setCustomerName("New Customer Name");
        jpaRepository.save(entity);

        System.out.println("Deleting the Data!!");

        jpaRepository.deleteById(13);


        List<OrderEntity> ls = new ArrayList<>();
        IntStream.range(20,30)
                .forEach(e -> {
                    OrderEntity order = new OrderEntity();
                    order.setOrderId(e);
                    order.setItemName("JPA_IEM"+e);
                    order.setCustomerName("JPA_CUSTOMER"+e);
                    order.setAddress("JPA_ADDRESS"+e);
                    ls.add(order);
                });

        System.out.println("Inserting data in Batch");
        jpaRepository.saveAll(ls);

        System.out.println("Fetching all the Records");
        List<OrderEntity> orderEntities
                = jpaRepository.findAll();
        System.out.println("orderEntities = " + orderEntities);


        System.out.println("Fetching Orders based on Item name");
        List<OrderEntity> itemList =
                jpaRepository.findByItemNameLikeOrderByCustomerName("JPA_IEM20");
        System.out.println("itemList = " + itemList);


        System.out.println("Using JPQL Query");
        List<OrderEntity> orderEntities1
                = jpaRepository.searchByCustomerNameLike("JPA_CUSTOMER",
                Sort.by("itemName"));

        System.out.println("orderEntities1 = " + orderEntities1);


        System.out.println("Fetch Data using Projection only Customer Details");

        System.out.println("Fetching All Items in Projection: " + jpaRepository.findAllByItemName("JPA_IEM20"));


        executePaging();
    }

    private void executePaging() {

        System.out.println("Fetching data in Paging in JPA");

        Page<OrderEntity> all =
                jpaRepository.findAll(Pageable.ofSize(2));

        System.out.println("all.getNumberOfElements() = " + all.getNumberOfElements());
        System.out.println("all.getTotalPages() = " + all.getTotalPages());
        System.out.println("all.getTotalElements() = " + all.getTotalElements());

        System.out.println("Fetching Next Sets of Records");
        all = jpaRepository.findAll(all.getPageable().next());

        System.out.println("all.getNumberOfElements() = " + all.getNumberOfElements());
        System.out.println("all.getTotalPages() = " + all.getTotalPages());
        System.out.println("all.getTotalElements() = " + all.getTotalElements());

    }
}
