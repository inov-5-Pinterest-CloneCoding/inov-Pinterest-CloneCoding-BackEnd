package com.clonecoding.pinterest.global.S3.repository;


import com.amazonaws.services.s3.model.S3Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface S3ObjectRepository extends CrudRepository<S3Object, Long> {
}