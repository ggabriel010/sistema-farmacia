package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Cliente;
import farmacia.repository.ClienteRepository;

import java.util.List;
import java.util.UUID;

/**
 * Controla as operações sobre clientes. Apenas Administrador pode cadastrar/editar/remover.
 */
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    public Cliente buscarPorId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID não pode ser vazio.");
        return clienteRepository.buscarPorId(id);
    }

    public Cliente buscarPorCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) throw new IllegalArgumentException("CPF não pode ser vazio.");
        return clienteRepository.buscarPorCpf(cpf);
    }

    public void cadastrar(Administrador solicitante, String nome, String cpf,
                          String telefone, String email) {
        validarCampo(nome, "Nome");
        validarCampo(cpf, "CPF");

        if (clienteRepository.buscarPorCpf(cpf.trim()) != null) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com o CPF informado.");
        }

        Cliente cli = new Cliente();
        cli.setId(UUID.randomUUID().toString());
        cli.setNome(nome.trim());
        cli.setCpf(cpf.trim());
        cli.setTelefone(telefone != null ? telefone.trim() : "");
        cli.setEmail(email != null ? email.trim() : "");

        clienteRepository.salvar(cli);
    }

    public void editar(Administrador solicitante, String id, String nome, String cpf,
                       String telefone, String email) {
        Cliente cli = clienteRepository.buscarPorId(id);
        if (cli == null) throw new IllegalArgumentException("Cliente não encontrado.");

        if (nome != null && !nome.isBlank()) cli.setNome(nome.trim());
        if (cpf != null && !cpf.isBlank()) {
            Cliente outro = clienteRepository.buscarPorCpf(cpf.trim());
            if (outro != null && !outro.getId().equals(id)) {
                throw new IllegalArgumentException("CPF já cadastrado para outro cliente.");
            }
            cli.setCpf(cpf.trim());
        }
        if (telefone != null && !telefone.isBlank()) cli.setTelefone(telefone.trim());
        if (email != null && !email.isBlank()) cli.setEmail(email.trim());

        clienteRepository.atualizar(cli);
    }

    public void remover(Administrador solicitante, String id) {
        Cliente cli = clienteRepository.buscarPorId(id);
        if (cli == null) throw new IllegalArgumentException("Cliente não encontrado.");
        clienteRepository.remover(id);
    }

    private void validarCampo(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }
}
