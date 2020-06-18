package com.backendspring.marcosbitencourt;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.backendspring.marcosbitencourt.domain.Categoria;
import com.backendspring.marcosbitencourt.domain.Cidade;
import com.backendspring.marcosbitencourt.domain.Cliente;
import com.backendspring.marcosbitencourt.domain.Endereco;
import com.backendspring.marcosbitencourt.domain.Estado;
import com.backendspring.marcosbitencourt.domain.ItemPedido;
import com.backendspring.marcosbitencourt.domain.Pagamento;
import com.backendspring.marcosbitencourt.domain.PagamentoComBoleto;
import com.backendspring.marcosbitencourt.domain.PagamentoComCartao;
import com.backendspring.marcosbitencourt.domain.Pedido;
import com.backendspring.marcosbitencourt.domain.Produto;
import com.backendspring.marcosbitencourt.domain.enums.EstadoPagamento;
import com.backendspring.marcosbitencourt.domain.enums.TipoCliente;
import com.backendspring.marcosbitencourt.repositories.CategoriaRepository;
import com.backendspring.marcosbitencourt.repositories.CidadeRepository;
import com.backendspring.marcosbitencourt.repositories.ClienteRepository;
import com.backendspring.marcosbitencourt.repositories.EnderecoRepository;
import com.backendspring.marcosbitencourt.repositories.EstadoRepository;
import com.backendspring.marcosbitencourt.repositories.ItemPedidoRepository;
import com.backendspring.marcosbitencourt.repositories.PagamentoRepository;
import com.backendspring.marcosbitencourt.repositories.PedidoRepository;
import com.backendspring.marcosbitencourt.repositories.ProdutoRepository;

@SpringBootApplication
public class BackendSpringApplication implements CommandLineRunner {
	
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Cama mesa e banho");
		Categoria cat4 = new Categoria(null, "Eletrônicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");

		
		Produto p1 = new Produto(null,"Computador", 2000.00);
		Produto p2 = new Produto(null,"Impressora", 800.00);
		Produto p3 = new Produto(null,"Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia",est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com","36378912377", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("41814215","899887744"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "06322448", cli1, c1);
		Endereco e2 = new Endereco(null, "Av. Mattos", "105", "Sala 800", "Centro", "06122448", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2)); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
	Pedido ped1 = new Pedido(null, sdf.parse("11/06/2020 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("11/06/2020 19:35"), cli1, e2);
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("11/06/2020 00:00"),null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		ItemPedido ip1= new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2= new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3= new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p1.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
		
		
		
		
		
	
		
		
	}

}
