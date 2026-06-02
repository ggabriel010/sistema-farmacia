package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Funcionario;
import farmacia.model.Usuario;
import farmacia.repository.UsuarioRepository;

import java.util.List;
import java.util.UUID;

/**
 * Controla o cadastro e gerenciamento de usuários.
 * Apenas Administrador pode cadastrar, editar e remover usuários.
 * Administradores são inseridos diretamente no banco (usuarios.json).
 */
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /** Lista todos os usuários cadastrados. */
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    /** Busca usuário por id. Retorna null se não encontrado. */
    public Usuario buscarPorId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID não pode ser vazio.");
        return usuarioRepository.buscarPorId(id);
    }

    /**
     * Cadastra um novo Funcionário. Apenas Administrador pode fazer isso.
     *
     * @param solicitante adm logado
     * @param nome        nome completo
     * @param login       login de acesso
     * @param senha       senha
     */
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

    /**
     * Edita os dados de um usuário existente. Apenas Administrador.
     * Campos em branco mantêm o valor atual.
     */
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

    /**
     * Remove um usuário. Administrador não pode remover a si mesmo.
     */
    public void remover(Administrador solicitante, String id) {
        if (solicitante.getId().equals(id)) {
            throw new IllegalArgumentException("Você não pode remover sua própria conta.");
        }
        Usuario usuario = usuarioRepository.buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        usuarioRepository.remover(id);
    }

    // ---- auxiliares ----

    private void validarCampo(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }
}
