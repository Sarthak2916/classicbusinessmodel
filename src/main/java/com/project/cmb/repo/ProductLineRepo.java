package com.project.cmb.repo;

import com.project.cmb.entity.ProductLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLineRepo extends JpaRepository<ProductLine, String> {}