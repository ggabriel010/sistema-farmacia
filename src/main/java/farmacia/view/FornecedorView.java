package farmacia.view;

import farmacia.controller.FornecedorController;
import farmacia.model.Administrador;
import farmacia.model.Fornecedor;
import java.util.List;

public class FornecedorView {

    private final Administrador admin;
    private final FornecedorController fornecedorController;

    public FornecedorView(Administrador admin, FornecedorController fornecedorController) {
        this.admin = admin;
        this.fornecedorController = fornecedorController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("FORNECEDORES");
            System.out.println("  1. Listar todos");
            System.out.println("  2. Cadastrar novo");
            System.out.println("  3. Editar");
            System.out.println("  4. Remover");
            System.out.println("  0. Voltar");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 4);

            switch (opcao) {
                case 1 -> listarTodos();
                case 2 -> cadastrar();
                case 3 -> editar();
                case 4 -> remover();
                case 0 -> { return; }
            }
        }
    }

    private void listarTodos() {
        Tela.cabecalho("TODOS OS FORNECEDORES");
        List<Fornecedor> lista = fornecedorController.listarTodos();
        if (lista.isEmpty()) {
            Tela.aviso("Nenhum fornecedor cadastrado.");
        } else {
            System.out.printf("%n  %-36s %-22s %-18s %-15s%n", "ID", "Nome", "CNPJ", "Telefone");
            Tela.separador();
            for (Fornecedor f : lista) {
                System.out.printf("  %-36s %-22s %-18s %-15s%n",
                        f.getId(), truncar(f.getNome(), 21), f.getCnpj(), f.getTelefone());
            }
        }
        Tela.pausar();
    }

    private void cadastrar() {
        Tela.cabecalho("CADASTRAR FORNECEDOR");
        String nome      = Tela.lerLinhaObrigatoria("Nome");
        String cnpj      = Tela.lerLinhaObrigatoria("CNPJ");
        String telefone  = Tela.lerLinha("Telefone");
        String email     = Tela.lerLinha("E-mail");

        try {
            fornecedorController.cadastrar(admin, nome, cnpj, telefone, email);
            Tela.sucesso("Fornecedor cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void editar() {
        Tela.cabecalho("EDITAR FORNECEDOR");
        listarTodos();
        String id = Tela.lerLinhaObrigatoria("ID do fornecedor a editar");

        Fornecedor forn = fornecedorController.buscarPorId(id);
        if (forn == null) {
            Tela.erro("Fornecedor não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Deixe em branco para manter o valor atual.");
        String nome     = Tela.lerLinha("Nome [" + forn.getNome() + "]");
        String cnpj     = Tela.lerLinha("CNPJ [" + forn.getCnpj() + "]");
        String telefone = Tela.lerLinha("Telefone [" + forn.getTelefone() + "]");
        String email    = Tela.lerLinha("E-mail [" + forn.getEmail() + "]");

        try {
            fornecedorController.editar(admin, id, nome, cnpj, telefone, email);
            Tela.sucesso("Fornecedor atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void remover() {
        Tela.cabecalho("REMOVER FORNECEDOR");
        listarTodos();
        String id = Tela.lerLinhaObrigatoria("ID do fornecedor a remover");

        Fornecedor forn = fornecedorController.buscarPorId(id);
        if (forn == null) {
            Tela.erro("Fornecedor não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Fornecedor: " + forn.getNome());
        if (!Tela.confirmar("Confirma a remoção?")) {
            Tela.aviso("Operação cancelada.");
            Tela.pausar();
            return;
        }

        try {
            fornecedorController.remover(admin, id);
            Tela.sucesso("Fornecedor removido com sucesso.");
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
