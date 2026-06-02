package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Funcionario;
import farmacia.model.Usuario;
import farmacia.repository.UsuarioRepository;
import java.util.List;
import java.util.UUID;

public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    public Usuario buscarPorId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID não pode ser vazio.");
        return usuarioRepository.buscarPorId(id);
    }

    public void cadastrarFuncionario(Administrador solicitante, String nome, String login, String senha) {
        validarCampo(nome, "Nome");
        validarCampo(login, "Login");
        validarCampo(senha, "Senha");

        if (usuarioRepository.buscarPorLogin(login) != null) {
            throw new IllegalArgumentException("Já existe um usuário com o login '" + login + "'.");
        }

        Funcionario func = new Funcionario();
        func.setId(UUID.randomUUID().toString());
        func.setNome(nome.trim());
        func.setLogin(login.trim());
        func.setSenha(senha.trim());

        usuarioRepository.salvar(func);
    }

    public void editar(Administrador solicitante, String id, String nome, String login, String senha) {
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");

        if (nome != null && !nome.isBlank()) usuario.setNome(nome.trim());

        if (login != null && !login.isBlank()) {
            Usuario outro = usuarioRepository.buscarPorLogin(login.trim());
            if (outro != null && !outro.getId().equals(id)) {
                throw new IllegalArgumentException("Login '" + login + "' já está em uso.");
            }
            usuario.setLogin(login.trim());
        }

        if (senha != null && !senha.isBlank()) usuario.setSenha(senha.trim());

        usuarioRepository.atualizar(usuario);
    }

    public void remover(Administrador solicitante, String id) {
        if (solicitante.getId().equals(id)) {
            throw new IllegalArgumentException("Você não pode remover sua própria conta.");
        }
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        usuarioRepository.remover(id);
    }

    private void validarCampo(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }
}
