package farmacia.repository;

import farmacia.model.Medicamento;
import farmacia.util.JsonUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MedicamentoRepository extends AbstractRepository<Medicamento> {

    private final CategoriaRepository categoriaRepo;
    private final FornecedorRepository fornecedorRepo;

    public MedicamentoRepository(CategoriaRepository categoriaRepo,
                                  FornecedorRepository fornecedorRepo) {
        super("medicamentos.json");
        this.categoriaRepo = categoriaRepo;
        this.fornecedorRepo = fornecedorRepo;
    }

    @Override
    protected Map<String, Object> toMap(Medicamento m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", m.getId());
        map.put("nome", m.getNome());
        map.put("descricao", m.getDescricao());
        map.put("principioAtivo", m.getPrincipioAtivo());
        map.put("preco", m.getPreco());
        map.put("quantidadeEstoque", m.getQuantidadeEstoque());
        map.put("quantidadeMinima", m.getQuantidadeMinima());
        map.put("dataValidade", m.getDataValidade());
        map.put("ativo", m.isAtivo());
        map.put("categoriaId", m.getCategoria() != null ? m.getCategoria().getId() : null);
        map.put("fornecedorId", m.getFornecedor() != null ? m.getFornecedor().getId() : null);
        return map;
    }

    @Override
    protected Medicamento fromMap(Map<String, Object> map) {
        Medicamento m = new Medicamento();
        m.setId(JsonUtil.getString(map, "id"));
        m.setNome(JsonUtil.getString(map, "nome"));
        m.setDescricao(JsonUtil.getString(map, "descricao"));
        m.setPrincipioAtivo(JsonUtil.getString(map, "principioAtivo"));
        m.setPreco(JsonUtil.getDouble(map, "preco"));
        m.setQuantidadeEstoque(JsonUtil.getInt(map, "quantidadeEstoque"));
        m.setQuantidadeMinima(JsonUtil.getInt(map, "quantidadeMinima"));
        m.setDataValidade(JsonUtil.getString(map, "dataValidade"));
        m.setAtivo(JsonUtil.getBoolean(map, "ativo"));

        String categoriaId = JsonUtil.getString(map, "categoriaId");
        if (categoriaId != null) {
            m.setCategoria(categoriaRepo.buscarPorId(categoriaId));
        }

        String fornecedorId = JsonUtil.getString(map, "fornecedorId");
        if (fornecedorId != null) {
            m.setFornecedor(fornecedorRepo.buscarPorId(fornecedorId));
        }

        return m;
    }

    @Override
    protected String getId(Medicamento objeto) {
        return objeto.getId();
    }

    public List<Medicamento> listarAtivos() {
        return listarTodos().stream()
                .filter(Medicamento::isAtivo)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Medicamento> buscarPorNome(String nome) {
        String nomeMin = nome.toLowerCase();
        return listarTodos().stream()
                .filter(m -> m.getNome().toLowerCase().contains(nomeMin))
                .collect(java.util.stream.Collectors.toList());
    }
}
