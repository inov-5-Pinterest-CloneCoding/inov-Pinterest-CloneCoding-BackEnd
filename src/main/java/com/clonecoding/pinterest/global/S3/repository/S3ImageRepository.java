package com.clonecoding.pinterest.global.S3.repository;

import com.clonecoding.pinterest.global.S3.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3ImageRepository extends JpaRepository<Image, Long> {

}
