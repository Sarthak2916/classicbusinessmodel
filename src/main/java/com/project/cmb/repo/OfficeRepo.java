package com.project.cmb.repo;

import com.project.cmb.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "office")
public interface OfficeRepo extends JpaRepository<Office, String> {

    List<Office> findByCountryIn(@Param("countries") List<String> countries);
}