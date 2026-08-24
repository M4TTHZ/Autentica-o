package com.matheusramalho.Autenticacao.service;


import com.matheusramalho.Autenticacao.exception.AcessoNegadoException;
import com.matheusramalho.Autenticacao.model.Acao;
import com.matheusramalho.Autenticacao.model.Papel;
import com.matheusramalho.Autenticacao.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;


/**
 * RBAC (B2): define o que cada Papel pode fazer. Antes de executar
 * qualquer acao sensivel, o controller/service correspondente chama
 * verificarPermissao(usuarioLogado, Acao.X) -- se o papel nao tiver essa
 * acao na lista, a chamada e interrompida com AcessoNegadoException (403).
 */

@Service
public class PermissaoService {

    private static final Map<Papel, Set<Acao>> PERMISSOES = new EnumMap<>(Papel.class);

    static {
        PERMISSOES.put(Papel.VENDEDOR, EnumSet.of(
           Acao.CRIAR_CLIENTE,
           Acao.VER_CLIENTE,
           Acao.VER_PRODUTOS,
           Acao.LANCA_VENDA,
           Acao.VER_PEDIDO,
           Acao.ATUALIZAR_STATUS_PEDIDO
        ));

        PERMISSOES.put(Papel.FINANCEIRO, EnumSet.of(
                Acao.CRIAR_CLIENTE,
                Acao.VER_CLIENTE,
                Acao.VER_PRODUTOS,
                Acao.VER_PEDIDO,
                Acao.VER_PAGAMENTO
        ));

        // ADMIN acumula tudo que existe no enum, sem precisar listar de novo
        PERMISSOES.put(Papel.ADMIN, EnumSet.allOf(Acao.class));
    }

    public boolean temPermissao(Papel papel, Acao acao){
        return PERMISSOES.getOrDefault(papel, Set.of()).contains(acao);
    }

    /**
     * Usado direto nos controllers: se o usuario nao tiver a acao, ja
     * interrompe a requisicao lancando 403 -- o controller nao precisa
     * escrever if/else de permissao, so chamar isso no comeco do metodo.
     */
    public void verificarPermissao(Usuario usuario, Acao acao){
        if (!temPermissao(usuario.getPapel(), acao)){
            throw new AcessoNegadoException(
                    "Seu papel ("+ usuario.getPapel() +") nao tem permissao para: "+ acao
            );
        }
    }
}
