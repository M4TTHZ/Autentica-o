package com.matheusramalho.Autenticacao.service;


import com.matheusramalho.Autenticacao.dto.ClienteRequestDTO;
import com.matheusramalho.Autenticacao.dto.ClienteResponseDTO;
import com.matheusramalho.Autenticacao.exception.RecursoNaoEncontradoException;
import com.matheusramalho.Autenticacao.exception.UsuarioDuplicadoException;
import com.matheusramalho.Autenticacao.model.Acao;
import com.matheusramalho.Autenticacao.model.Cliente;
import com.matheusramalho.Autenticacao.model.Usuario;
import com.matheusramalho.Autenticacao.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PermissaoService permissaoService;

    public ClienteService(ClienteRepository clienteRepository, PermissaoService permissaoService){
        this.clienteRepository = clienteRepository;
        this.permissaoService = permissaoService;
    }

    public ClienteResponseDTO criar(ClienteRequestDTO dto, Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.CRIAR_CLIENTE);

        if (clienteRepository.existsByCpf(dto.getCpf())){
            throw new UsuarioDuplicadoException("Ja existe um cliente com o CPF informado");
        }

        Cliente cliente = new Cliente();
        cliente.getNome();
        cliente.getCpf();
        cliente.getCep();

        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    public List<ClienteResponseDTO> listar(Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.VER_CLIENTE);

        return clienteRepository.findAll().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id, Usuario usuarioLogado){
        permissaoService.verificarPermissao(usuarioLogado, Acao.VER_CLIENTE);

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente nao encontrado."));

        return ClienteResponseDTO.fromEntity(cliente);
    }
}
