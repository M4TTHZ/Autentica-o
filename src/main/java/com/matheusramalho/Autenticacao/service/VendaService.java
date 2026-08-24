package com.matheusramalho.Autenticacao.service;

import com.matheusramalho.Autenticacao.dto.VendaRequestDTO;
import com.matheusramalho.Autenticacao.dto.VendaResponseDTO;
import com.matheusramalho.Autenticacao.exception.RecursoNaoEncontradoException;
import com.matheusramalho.Autenticacao.model.*;
import com.matheusramalho.Autenticacao.repository.ClienteRepository;
import com.matheusramalho.Autenticacao.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoService produtoService;
    private final PermissaoService permissaoService;

    public VendaService(VendaRepository vendaRepository,
                        ClienteRepository clienteRepository,
                        ProdutoService produtoService,
                        PermissaoService permissaoService){
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
        this.permissaoService = permissaoService;
    }

    public VendaResponseDTO lancar(VendaRequestDTO dto, Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.LANCA_VENDA);

        Produto produto = produtoService.buscarEntidadePorId(dto.getProdutoId());
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado"));

        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setCliente(cliente);
        venda.setVendedor(usuarioLogado);
        venda.setQuantidade(dto.getQuantidade());
        venda.setStatus(StatusVenda.PENDENTE);

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    public List<VendaResponseDTO> listarPedidos(Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.VER_PEDIDO);

        return vendaRepository.findAll().stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    public VendaResponseDTO atualizarStatus(Long id, StatusVenda novoStatus, Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.ATUALIZAR_STATUS_PEDIDO);

        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido nao encontrado"));

        venda.setStatus(novoStatus);
        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    public List<Venda> listarEntidades(Usuario usuarioLogado) {
        permissaoService.verificarPermissao(usuarioLogado, Acao.VER_PAGAMENTO);
        return vendaRepository.findAll();
    }
}
