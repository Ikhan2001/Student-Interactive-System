package com.cleducate.repository;

import com.cleducate.entity.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    @Query(value = "Select * from module m where m.id =:moduleId", nativeQuery = true)
    Module getModuleById(Long moduleId);

    @Query(value = "SELECT * FROM module ", nativeQuery = true)
    Page<Module> getAllModules(Pageable pageable);
}
