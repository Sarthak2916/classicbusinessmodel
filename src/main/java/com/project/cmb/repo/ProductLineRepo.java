package com.project.cmb.repo;

import com.project.cmb.entity.ProductLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "productlines")
public interface ProductLineRepo extends JpaRepository<ProductLine, String> {}
