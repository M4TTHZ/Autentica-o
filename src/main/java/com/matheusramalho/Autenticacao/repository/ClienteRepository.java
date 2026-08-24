package com.matheusramalho.Autenticacao.repository;

import com.matheusramalho.Autenticacao.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);
}
