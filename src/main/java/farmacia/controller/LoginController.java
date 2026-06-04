package farmacia.controller;

import farmacia.model.Administrador;
import farmacia.model.Funcionario;
import farmacia.model.Usuario;
import farmacia.service.AutenticacaoService;
import farmacia.view.LoginView;
import farmacia.view.MenuAdminView;
import farmacia.view.MenuFuncionarioView;

public class LoginController {

    private final AutenticacaoService autenticacaoService;
    private final MedicamentoController medicamentoController;
    private final CategoriaController categoriaController;
    private final FornecedorController fornecedorController;
    private final ClienteController clienteController;
    private final VendaController vendaController;
    private final UsuarioController usuarioController;

    public LoginController(AutenticacaoService autenticacaoService,
                           MedicamentoController medicamentoController,
                           CategoriaController categoriaController,
                           FornecedorController fornecedorController,
                           ClienteController clienteController,
                           VendaController vendaController,
                           UsuarioController usuarioController) {
        this.autenticacaoService = autenticacaoService;
        this.medicamentoController = medicamentoController;
        this.categoriaController = categoriaController;
        this.fornecedorController = fornecedorController;
        this.clienteController = clienteController;
        this.vendaController = vendaController;
        this.usuarioController = usuarioController;
    }

    public void iniciar() {
        LoginView loginView = new LoginView();

        while (true) {
            String[] credenciais = loginView.solicitarCredenciais();
            String login = credenciais[0];
            String senha = credenciais[1];

            try {
                Usuario usuario = autenticacaoService.autenticar(login, senha);
                loginView.exibirSucesso(usuario.getNome());

                if (usuario instanceof Administrador) {
                    MenuAdminView menuAdmin = new MenuAdminView(
                            (Administrador) usuario,
                            medicamentoController,
                            categoriaController,
                            fornecedorController,
                            clienteController,
                            vendaController,
                            usuarioController
                    );
                    menuAdmin.exibir();
                } else if (usuario instanceof Funcionario) {
                    MenuFuncionarioView menuFuncionario = new MenuFuncionarioView(
                            usuario,
                            vendaController
                    );
                    menuFuncionario.exibir();
                }

            } catch (IllegalArgumentException e) {
                loginView.exibirErro(e.getMessage());
            }

            if (!loginView.confirmarNovoLogin()) {
                break;
            }
        }
    }
}
