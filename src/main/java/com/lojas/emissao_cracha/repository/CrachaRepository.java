package com.lojas.emissao_cracha.repository;

import com.lojas.emissao_cracha.domain.Cracha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrachaRepository extends JpaRepository<Cracha, Long> {

}
