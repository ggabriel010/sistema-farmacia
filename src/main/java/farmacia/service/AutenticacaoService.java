package farmacia.service;

import farmacia.model.Usuario;
import farmacia.repository.UsuarioRepository;


public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login não pode ser vazio.");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }

        Usuario usuario = usuarioRepository.buscarPorLogin(login);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado: " + login);
        }
        if (!usuario.getSenha().equals(senha)) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return usuario;
    }


    public boolean loginJaExiste(String login) {
        return usuarioRepository.buscarPorLogin(login) != null;
    }
}