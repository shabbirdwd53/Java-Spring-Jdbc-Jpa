package com.dailycodebuffer.example.repository;

import com.dailycodebuffer.example.entity.OrderEntity;
import com.dailycodebuffer.example.model.CustomerDetailsView;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJPARepository extends JpaRepository<OrderEntity,Integer> {

    List<OrderEntity> findByItemName(String itemName);

    List<OrderEntity> findByItemNameLikeOrderByCustomerName(String itemName);


    @Query("SELECT o from OrderEntity o WHERE o.customerName Like %:customerName%")
    List<OrderEntity> searchByCustomerNameLike(@Param("customerName") String customerName, Sort sort);

    List<CustomerDetailsView> findAllByItemName(String itemName);
}
