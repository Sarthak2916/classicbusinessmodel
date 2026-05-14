package com.project.cmb.repo;

import com.project.cmb.entity.Office;
import com.project.cmb.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, String> {

    Optional<Office> findByOfficeCode(String officeCode);

    List<Office> findByOfficeCodeIn(List<String> officeCodes);

    Optional<Office> findByCity(String city);

    @Query("SELECT o FROM Office o WHERE o.city IN :cities")
    List<Office> findByCityIn(@Param("cities") List<String> cities);

    boolean existsByOfficeCode(String officeCode);

    @Query("SELECT e FROM Employee e WHERE e.officeCode = :officeCode")
    List<Employee> findEmployeesByOfficeCode(@Param("officeCode") String officeCode);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.officeCode = :officeCode")
    long countEmployeesByOfficeCode(@Param("officeCode") String officeCode);

    // QUICK PHONE UPDATE
    @Transactional
    @Modifying
    @Query("UPDATE Office o SET o.phone = :phone WHERE o.officeCode = :officeCode")
    int updatePhone(
            @Param("officeCode") String officeCode,
            @Param("phone")      String phone
    );

    // FULL OFFICE UPDATE
    @Transactional
    @Modifying
    @Query("""
            UPDATE Office o SET
                o.city         = :city,
                o.country      = :country,
                o.territory    = :territory,
                o.phone        = :phone,
                o.addressLine1 = :addressLine1,
                o.addressLine2 = :addressLine2,
                o.state        = :state,
                o.postalCode   = :postalCode
            WHERE o.officeCode = :officeCode
            """)
    int updateOfficeDetails(
            @Param("officeCode")    String officeCode,
            @Param("city")         String city,
            @Param("country")      String country,
            @Param("territory")    String territory,
            @Param("phone")        String phone,
            @Param("addressLine1") String addressLine1,
            @Param("addressLine2") String addressLine2,
            @Param("state")        String state,
            @Param("postalCode")   String postalCode
    );
}