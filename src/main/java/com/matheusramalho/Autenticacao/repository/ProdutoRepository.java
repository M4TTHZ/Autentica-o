package com.matheusramalho.Autenticacao.repository;

import com.matheusramalho.Autenticacao.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
