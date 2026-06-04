package farmacia.view;

import farmacia.controller.CategoriaController;
import farmacia.controller.FornecedorController;
import farmacia.controller.MedicamentoController;
import farmacia.model.Administrador;
import farmacia.model.Categoria;
import farmacia.model.Fornecedor;
import farmacia.model.Medicamento;
import java.util.List;

public class MedicamentoView {

    private final Administrador admin;
    private final MedicamentoController medicamentoController;
    private final CategoriaController categoriaController;
    private final FornecedorController fornecedorController;

    public MedicamentoView(Administrador admin,
                           MedicamentoController medicamentoController,
                           CategoriaController categoriaController,
                           FornecedorController fornecedorController) {
        this.admin = admin;
        this.medicamentoController = medicamentoController;
        this.categoriaController = categoriaController;
        this.fornecedorController = fornecedorController;
    }

    public void exibir() {
        while (true) {
            Tela.cabecalho("MEDICAMENTOS");
            System.out.println("  1. Listar todos");
            System.out.println("  2. Buscar por nome");
            System.out.println("  3. Cadastrar novo");
            System.out.println("  4. Editar");
            System.out.println("  5. Desativar");
            System.out.println("  0. Voltar");
            Tela.separador();

            int opcao = Tela.lerOpcao(0, 5);

            switch (opcao) {
                case 1 -> listarTodos();
                case 2 -> buscarPorNome();
                case 3 -> cadastrar();
                case 4 -> editar();
                case 5 -> desativar();
                case 0 -> { return; }
            }
        }
    }

    private void listarTodos() {
        Tela.cabecalho("TODOS OS MEDICAMENTOS");
        List<Medicamento> lista = medicamentoController.listarTodos();
        if (lista.isEmpty()) {
            Tela.aviso("Nenhum medicamento cadastrado.");
        } else {
            exibirTabela(lista);
        }
        Tela.pausar();
    }

    private void buscarPorNome() {
        Tela.cabecalho("BUSCAR MEDICAMENTO POR NOME");
        String nome = Tela.lerLinhaObrigatoria("Nome (parcial)");
        try {
            List<Medicamento> lista = medicamentoController.buscarPorNome(nome);
            if (lista.isEmpty()) {
                Tela.aviso("Nenhum medicamento encontrado com o nome '" + nome + "'.");
            } else {
                exibirTabela(lista);
            }
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void cadastrar() {
        Tela.cabecalho("CADASTRAR MEDICAMENTO");

        String nome           = Tela.lerLinhaObrigatoria("Nome");
        String descricao      = Tela.lerLinhaObrigatoria("Descrição");
        String principioAtivo = Tela.lerLinhaObrigatoria("Princípio ativo");
        String preco          = Tela.lerLinhaObrigatoria("Preço (ex: 12.50)");
        String qtdEstoque     = Tela.lerLinhaObrigatoria("Quantidade em estoque");
        String qtdMinima      = Tela.lerLinhaObrigatoria("Quantidade mínima");
        String validade       = Tela.lerLinhaObrigatoria("Data de validade (yyyy-MM-dd)");

        String categoriaId  = selecionarCategoria();
        if (categoriaId == null) return;

        String fornecedorId = selecionarFornecedor();
        if (fornecedorId == null) return;

        try {
            medicamentoController.cadastrar(admin, nome, descricao, principioAtivo,
                    preco, qtdEstoque, qtdMinima, validade, categoriaId, fornecedorId);
            Tela.sucesso("Medicamento cadastrado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void editar() {
        Tela.cabecalho("EDITAR MEDICAMENTO");
        String id = Tela.lerLinhaObrigatoria("ID do medicamento");

        Medicamento med = medicamentoController.buscarPorId(id);
        if (med == null) {
            Tela.erro("Medicamento não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Dados atuais:");
        exibirDetalhe(med);
        System.out.println("\n  Deixe em branco para manter o valor atual.");

        String nome           = Tela.lerLinha("Nome [" + med.getNome() + "]");
        String descricao      = Tela.lerLinha("Descrição [" + med.getDescricao() + "]");
        String principioAtivo = Tela.lerLinha("Princípio ativo [" + med.getPrincipioAtivo() + "]");
        String preco          = Tela.lerLinha("Preço [" + med.getPreco() + "]");
        String qtdEstoque     = Tela.lerLinha("Qtd. estoque [" + med.getQuantidadeEstoque() + "]");
        String qtdMinima      = Tela.lerLinha("Qtd. mínima [" + med.getQuantidadeMinima() + "]");
        String validade       = Tela.lerLinha("Validade [" + med.getDataValidade() + "]");
        String categoriaId    = Tela.lerLinha("ID Categoria [" + (med.getCategoria() != null ? med.getCategoria().getId() : "—") + "]");
        String fornecedorId   = Tela.lerLinha("ID Fornecedor [" + (med.getFornecedor() != null ? med.getFornecedor().getId() : "—") + "]");

        try {
            medicamentoController.editar(admin, id, nome, descricao, principioAtivo,
                    preco, qtdEstoque, qtdMinima, validade, categoriaId, fornecedorId);
            Tela.sucesso("Medicamento atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private void desativar() {
        Tela.cabecalho("DESATIVAR MEDICAMENTO");
        String id = Tela.lerLinhaObrigatoria("ID do medicamento");

        Medicamento med = medicamentoController.buscarPorId(id);
        if (med == null) {
            Tela.erro("Medicamento não encontrado.");
            Tela.pausar();
            return;
        }

        System.out.println("\n  Medicamento: " + med.getNome());
        if (!Tela.confirmar("Confirma a desativação?")) {
            Tela.aviso("Operação cancelada.");
            Tela.pausar();
            return;
        }

        try {
            medicamentoController.desativar(admin, id);
            Tela.sucesso("Medicamento desativado com sucesso.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            Tela.erro(e.getMessage());
        }
        Tela.pausar();
    }

    private String selecionarCategoria() {
        List<Categoria> categorias = categoriaController.listarTodos();
        if (categorias.isEmpty()) {
            Tela.aviso("Nenhuma categoria cadastrada. Cadastre uma categoria primeiro.");
            Tela.pausar();
            return null;
        }
        System.out.println("\n  Categorias disponíveis:");
        for (Categoria c : categorias) {
            System.out.printf("    [%s] %s%n", c.getId(), c.getNome());
        }
        return Tela.lerLinhaObrigatoria("ID da Categoria");
    }

    private String selecionarFornecedor() {
        List<Fornecedor> fornecedores = fornecedorController.listarTodos();
        if (fornecedores.isEmpty()) {
            Tela.aviso("Nenhum fornecedor cadastrado. Cadastre um fornecedor primeiro.");
            Tela.pausar();
            return null;
        }
        System.out.println("\n  Fornecedores disponíveis:");
        for (Fornecedor f : fornecedores) {
            System.out.printf("    [%s] %s%n", f.getId(), f.getNome());
        }
        return Tela.lerLinhaObrigatoria("ID do Fornecedor");
    }

    private void exibirTabela(List<Medicamento> lista) {
        System.out.printf("%n  %-36s %-25s %8s %6s %-6s%n",
                "ID", "Nome", "Preço", "Estq.", "Ativo");
        Tela.separador();
        for (Medicamento m : lista) {
            System.out.printf("  %-36s %-25s R$%6.2f %6d %-6s%n",
                    m.getId(), truncar(m.getNome(), 24),
                    m.getPreco(), m.getQuantidadeEstoque(),
                    m.isAtivo() ? "Sim" : "Não");
        }
    }

    private void exibirDetalhe(Medicamento m) {
        Tela.info("Nome:           " + m.getNome());
        Tela.info("Descrição:      " + m.getDescricao());
        Tela.info("Princípio ativo:" + m.getPrincipioAtivo());
        Tela.info("Preço:          R$ " + String.format("%.2f", m.getPreco()));
        Tela.info("Estoque:        " + m.getQuantidadeEstoque() + " | Mín: " + m.getQuantidadeMinima());
        Tela.info("Validade:       " + m.getDataValidade());
        Tela.info("Categoria:      " + (m.getCategoria() != null ? m.getCategoria().getNome() : "—"));
        Tela.info("Fornecedor:     " + (m.getFornecedor() != null ? m.getFornecedor().getNome() : "—"));
        Tela.info("Ativo:          " + (m.isAtivo() ? "Sim" : "Não"));
    }

    private String truncar(String s, int max) {
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
