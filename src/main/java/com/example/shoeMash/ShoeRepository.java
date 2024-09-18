package com.example.shoeMash;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    boolean existsByName(String name);
}
