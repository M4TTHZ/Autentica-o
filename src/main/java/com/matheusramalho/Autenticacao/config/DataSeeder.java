package com.matheusramalho.Autenticacao.config;

import com.matheusramalho.Autenticacao.model.*;
import com.matheusramalho.Autenticacao.repository.ClienteRepository;
import com.matheusramalho.Autenticacao.repository.ProdutoRepository;
import com.matheusramalho.Autenticacao.repository.UsuarioRepository;
import com.matheusramalho.Autenticacao.repository.VendaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Popula o banco (em memoria, recriado a cada start) com dados de
 * exemplo. Roda uma vez, logo depois do contexto do Spring subir.
 *
 * A conta ADMIN aqui e o unico "bootstrap" do sistema agora que a regra
 * de "primeiro usuario vira ADMIN" foi removida do CadastroService --
 * sem esse seeder, ninguem conseguiria criar a primeira conta, porque
 * criar VENDEDOR/FINANCEIRO/ADMIN sempre exige um ADMIN ja logado.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository,
            VendaRepository vendaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // guarda de seguranca: nao duplica os dados se o seeder rodar
        // mais de uma vez por algum motivo
        if (usuarioRepository.count() > 0) {
            return;
        }

        Usuario admin = criarUsuario("Matheus Ramalho", "Batata12345", Papel.ADMIN);
        Usuario vendedor1 = criarUsuario("joao.vendas", "Vendedor123", Papel.VENDEDOR);
        criarUsuario("ana.financeiro", "Financeiro123", Papel.FINANCEIRO);

        // B4: conta com senha fraca de proposito -- passa na politica minima
        // (8+ caracteres, letra e numero, nao esta na lista de proibidas),
        // mas segue um padrao previsivel (palavra+ano). Alvo do ataque do B5.
        criarUsuario("carlos.vendas", "vendedor2024", Papel.VENDEDOR);

        Cliente cliente1 = criarCliente("Marcos Silva", "111.222.333-44", "78455-000");
        Cliente cliente2 = criarCliente("Beatriz Souza", "555.666.777-88", "78460-000");

        Produto produto1 = criarProduto("Mouse sem fio", "89.90", "42.00");
        Produto produto2 = criarProduto("Teclado mecanico", "249.90", "130.00");

        criarVenda(produto1, cliente1, vendedor1, 2, StatusVenda.ENTREGUE);
        criarVenda(produto2, cliente2, vendedor1, 1, StatusVenda.PENDENTE);

        System.out.println("=== Dados iniciais carregados ===");
        System.out.println("ADMIN de teste -> usuario: Matheus Ramalho | senha: Batata12345");
        System.out.println("Conta fraca (B4/B5) -> usuario: carlos.vendas | senha: vendedor2024");
    }

    private Usuario criarUsuario(String username, String senha, Papel papel) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordHash(passwordEncoder.encode(senha));
        usuario.setPapel(papel);
        return usuarioRepository.save(usuario);
    }

    private Cliente criarCliente(String nome, String cpf, String cep) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setCep(cep);
        return clienteRepository.save(cliente);
    }

    private Produto criarProduto(String nome, String precoVenda, String custo) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPrecoVenda(new BigDecimal(precoVenda));
        produto.setCusto(new BigDecimal(custo));
        return produtoRepository.save(produto);
    }

    private void criarVenda(Produto produto, Cliente cliente, Usuario vendedor, int quantidade, StatusVenda status) {
        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setCliente(cliente);
        venda.setVendedor(vendedor);
        venda.setQuantidade(quantidade);
        venda.setStatus(status);
        vendaRepository.save(venda);
    }
}
