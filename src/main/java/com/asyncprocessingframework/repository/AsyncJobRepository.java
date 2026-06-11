package com.asyncprocessingframework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asyncprocessingframework.entity.AsyncJob;

@Repository
public interface AsyncJobRepository extends JpaRepository<AsyncJob, Long>{

}
