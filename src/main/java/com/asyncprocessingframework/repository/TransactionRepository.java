package com.asyncprocessingframework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asyncprocessingframework.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
