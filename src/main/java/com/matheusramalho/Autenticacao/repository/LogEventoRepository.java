package com.matheusramalho.Autenticacao.repository;

import com.matheusramalho.Autenticacao.model.LogEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEventoRepository extends JpaRepository<LogEvento, Long> {
}
