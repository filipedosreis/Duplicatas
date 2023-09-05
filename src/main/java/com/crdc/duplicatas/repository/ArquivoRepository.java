package com.crdc.duplicatas.repository;

import com.crdc.duplicatas.model.ArquivoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoRepository extends JpaRepository<ArquivoModel, Long> {
}
