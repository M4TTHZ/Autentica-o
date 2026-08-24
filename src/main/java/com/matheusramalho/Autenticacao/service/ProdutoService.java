package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.dto.ProdutoRequestDTO;
import com.matheusramalho.Autenticacao.dto.ProdutoResponseDTO;
import com.matheusramalho.Autenticacao.exception.RecursoNaoEncontradoException;
import com.matheusramalho.Autenticacao.model.Acao;
import com.matheusramalho.Autenticacao.model.Produto;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PermissaoService permissaoService;

    public ProdutoService(ProdutoRepository produtoRepository,
                          PermissaoService permissaoService){
        this.produtoRepository = produtoRepository;
        this.permissaoService = permissaoService;
    }

    public ProdutoResponseDTO criar(ProdutoRequestDTO dto, Usuario usuarioLogado){
        // so ADMIN tem essa acao (nem VENDEDOR nem FINANCEIRO recebem
        // CRIAR_PRODUTO no mapa do PermissaoService)
        permissaoService.verificarPermissao(usuarioLogado, Acao.CRIAR_CLIENTE);

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setCusto(dto.getCusto());

        Produto salvo = produtoRepository.save(produto);
        // quem criou e ADMIN, entao sempre pode ver o custo do que acabou de criar
        return ProdutoResponseDTO.fromEntity(salvo, true);
    }

    public List<ProdutoResponseDTO> listar(Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.VER_PRODUTOS);

        // RBAC de campo: custo so aparece para quem tambem pode VER_PAGAMENTO
        // (FINANCEIRO/ADMIN) -- VENDEDOR ve o produto, mas nao a margem
        boolean podeVerCusto = permissaoService.temPermissao(usuarioLogado.getPapel(), Acao.VER_PAGAMENTO);

        return produtoRepository.findAll().stream()
                .map(p -> ProdutoResponseDTO.fromEntity(p, podeVerCusto))
                .toList();
    }

    public Produto buscarEntidadePorId(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto nao encontado."));
    }
}
