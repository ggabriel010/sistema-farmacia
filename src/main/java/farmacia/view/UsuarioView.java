package farmacia.view;

import farmacia.controller.UsuarioController;
import farmacia.model.Administrador;
import farmacia.model.Usuario;

import java.util.List;

/**
 * Telas de gerenciamento de usuários (somente Administrador).
 * Administradores são cadastrados diretamente no banco (usuarios.json).
 * Pela interface, o Administrador pode cadastrar, editar e remover Funcionários.
 */
public class UsuarioView {

    private final Administrador admin;
    private final UsuarioController usuarioController;

    public UsuarioView(Administrador admin, UsuarioController usuarioController) {
        this.admin = admin;
        this.usuarioController = usuarioController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("USUÁRIOS");
            System.out.println("  1. Listar todos");
            System.out.println("  2. Cadastrar Funcionário");
            System.out.println("  3. Editar usuário");
            System.out.println("  4. Remover usuário");
            System.out.println("  0. Voltar");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 4);

            switch (opcao) {
                case 1 -> listarTodos();
                case 2 -> cadastrarFuncionario();
                case 3 -> editar();
                case 4 -> remover();
                case 0 -> { return; }
            }
        }
    }

    private void listarTodos() {
        Tela.cabecalho("TODOS OS USUÁRIOS");
        List<Usuario> lista = usuarioController.listarTodos();
        if (lista.isEmpty()) {
            Tela.aviso("Nenhum usuário cadastrado.");
        } else {
            System.out.printf("%n  %-36s %-22s %-15s %-15s%n", "ID", "Nome", "Login", "Perfil");
            Tela.separador();
            for (Usuario u : lista) {
                System.out.printf("  %-36s %-22s %-15s %-15s%n",
                        u.getId(), truncar(u.getNome(), 21), u.getLogin(), u.getPerfil());
            }
        }
        Tela.pausar();
    }

    private void cadastrarFuncionario() {
        Tela.cabecalho("CADASTRAR FUNCIONÁRIO");
        Tela.info("Administradores são cadastrados diretamente no banco de dados.");
        System.out.println();

        String nome  = Tela.lerLinhaObrigatoria("Nome completo");
        String login = Tela.lerLinhaObrigatoria("Login");
        String senha = Tela.lerLinhaObrigatoria("Senha");

        try {
            usuarioController.cadastrarFuncionario(admin, nome, login, senha);
            Tela.sucesso("Funcionário '" + nome + "' cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void editar() {
        Tela.cabecalho("EDITAR USUÁRIO");
        listarTodos();
        String id = Tela.lerLinhaObrigatoria("ID do usuário a editar");

        Usuario usuario = usuarioController.buscarPorId(id);
        if (usuario == null) {
            Tela.erro("Usuário não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Usuário: " + usuario.getNome() + " (" + usuario.getPerfil() + ")");
        System.out.println("  Deixe em branco para manter o valor atual.");

        String nome  = Tela.lerLinha("Nome [" + usuario.getNome() + "]");
        String login = Tela.lerLinha("Login [" + usuario.getLogin() + "]");
        String senha = Tela.lerLinha("Nova senha (deixe em branco para não alterar)");

        try {
            usuarioController.editar(admin, id, nome, login, senha);
            Tela.sucesso("Usuário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void remover() {
        Tela.cabecalho("REMOVER USUÁRIO");
        listarTodos();
        String id = Tela.lerLinhaObrigatoria("ID do usuário a remover");

        Usuario usuario = usuarioController.buscarPorId(id);
        if (usuario == null) {
            Tela.erro("Usuário não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Usuário: " + usuario.getNome() + " (" + usuario.getPerfil() + ")");
        if (!Tela.confirmar("Confirma a remoção?")) {
            Tela.aviso("Operação cancelada.");
            Tela.pausar();
            return;
        }

        try {
            usuarioController.remover(admin, id);
            Tela.sucesso("Usuário removido com sucesso.");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
