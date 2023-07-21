package com.clonecoding.pinterest.pin.repository;

import com.clonecoding.pinterest.pin.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {
}
