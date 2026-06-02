package farmacia;

import farmacia.controller.*;
import farmacia.repository.*;
import farmacia.service.*;
import farmacia.view.AlertaView;

/**
 * Ponto de entrada do Sistema de Gerenciamento de Farmácia.
 */
public class Main {

    public static void main(String[] args) {

        // ── Repositories ──────────────────────────────────────────────────
        CategoriaRepository   categoriaRepo   = new CategoriaRepository();
        FornecedorRepository  fornecedorRepo  = new FornecedorRepository();
        ClienteRepository     clienteRepo     = new ClienteRepository();
        UsuarioRepository     usuarioRepo     = new UsuarioRepository();
        MedicamentoRepository medicamentoRepo =
                new MedicamentoRepository(categoriaRepo, fornecedorRepo);
        VendaRepository vendaRepo =
                new VendaRepository(usuarioRepo, clienteRepo, medicamentoRepo);

        // ── Services ──────────────────────────────────────────────────────
        AutenticacaoService autenticacaoService = new AutenticacaoService(usuarioRepo);
        EstoqueService      estoqueService      = new EstoqueService(medicamentoRepo);
        VendaService        vendaService        = new VendaService(
                vendaRepo, medicamentoRepo, clienteRepo, estoqueService);

        // ── Controllers ───────────────────────────────────────────────────
        UsuarioController    usuarioController    = new UsuarioController(usuarioRepo);
        MedicamentoController medicamentoController =
                new MedicamentoController(medicamentoRepo, categoriaRepo, fornecedorRepo);
        CategoriaController  categoriaController  = new CategoriaController(categoriaRepo);
        FornecedorController fornecedorController = new FornecedorController(fornecedorRepo);
        ClienteController    clienteController    = new ClienteController(clienteRepo);
        VendaController      vendaController      = new VendaController(vendaService, medicamentoRepo);

        LoginController loginController = new LoginController(
                autenticacaoService,
                medicamentoController,
                categoriaController,
                fornecedorController,
                clienteController,
                vendaController,
                usuarioController
        );

        // ── Alertas ao iniciar ────────────────────────────────────────────
        new AlertaView(estoqueService).exibirAlertas();

        // ── Login ─────────────────────────────────────────────────────────
        loginController.iniciar();

        System.out.println("\n  Sistema encerrado. Até logo!\n");
    }
}
