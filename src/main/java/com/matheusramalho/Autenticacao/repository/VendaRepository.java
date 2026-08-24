package com.matheusramalho.Autenticacao.repository;

import com.matheusramalho.Autenticacao.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
