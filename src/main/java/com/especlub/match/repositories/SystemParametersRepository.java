package com.especlub.match.repositories;

import com.especlub.match.models.SystemParameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemParametersRepository extends JpaRepository<SystemParameters, Long> {
    @Query("SELECT s FROM SystemParameters s WHERE s.mnemonic = :mnemonic AND s.recordStatus = true")
    SystemParameters findByMnemonicAndRecordStatusTrue(@Param("mnemonic") String mnemonic);
}
