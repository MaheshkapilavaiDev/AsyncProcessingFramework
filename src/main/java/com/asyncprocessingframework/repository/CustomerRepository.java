package com.asyncprocessingframework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asyncprocessingframework.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
