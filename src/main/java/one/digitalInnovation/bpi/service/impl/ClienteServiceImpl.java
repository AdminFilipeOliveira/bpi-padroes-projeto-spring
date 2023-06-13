package one.digitalInnovation.bpi.service.impl;

import one.digitalInnovation.bpi.model.Cliente;
import one.digitalInnovation.bpi.model.ClienteRepository;
import one.digitalInnovation.bpi.model.Endereco;
import one.digitalInnovation.bpi.model.EnderecoRepository;
import one.digitalInnovation.bpi.service.ClienteService;
import one.digitalInnovation.bpi.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	


	@Override
	public Iterable<Cliente> buscarTodos() {

		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {

		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {

		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {

		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
	
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {

			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);

		clienteRepository.save(cliente);
	}

}
