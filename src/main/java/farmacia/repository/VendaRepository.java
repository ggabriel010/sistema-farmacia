package farmacia.repository;

import farmacia.model.*;
import farmacia.util.JsonUtil;
import java.util.*;


public class VendaRepository extends AbstractRepository<Venda> {

    private final UsuarioRepository usuarioRepo;
    private final ClienteRepository clienteRepo;
    private final MedicamentoRepository medicamentoRepo;

    public VendaRepository(UsuarioRepository usuarioRepo,
                            ClienteRepository clienteRepo,
                            MedicamentoRepository medicamentoRepo) {
        super("vendas.json");
        this.usuarioRepo = usuarioRepo;
        this.clienteRepo = clienteRepo;
        this.medicamentoRepo = medicamentoRepo;
    }


    @Override
    protected Map<String, Object> toMap(Venda v) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", v.getId());
        map.put("dataHora", v.getDataHora());  
        map.put("funcionarioId", v.getFuncionario() != null ? v.getFuncionario().getId() : null);
        map.put("clienteId", v.getCliente() != null ? v.getCliente().getId() : null);
        map.put("itens", serializarItens(v.getItens()));
        return map;
    }

    private List<Map<String, Object>> serializarItens(List<ItemVenda> itens) {
        List<Map<String, Object>> lista = new ArrayList<>();
        if (itens == null) return lista;
        for (ItemVenda item : itens) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", item.getId());
            m.put("medicamentoId", item.getMedicamento() != null ? item.getMedicamento().getId() : null);
            m.put("quantidade", item.getQuantidade());
            m.put("precoUnitario", item.getPrecoUnitario());
            lista.add(m);
        }
        return lista;
    }

    @Override
    protected Venda fromMap(Map<String, Object> map) {
        Venda v = new Venda();
        v.setId(JsonUtil.getString(map, "id"));
        v.setDataHora(JsonUtil.getString(map, "dataHora"));

        String funcId = JsonUtil.getString(map, "funcionarioId");
        if (funcId != null) {
            Usuario u = usuarioRepo.buscarPorId(funcId);
            if (u instanceof Funcionario) v.setFuncionario((Funcionario) u);
        }

        String clienteId = JsonUtil.getString(map, "clienteId");
        if (clienteId != null) {
            v.setCliente(clienteRepo.buscarPorId(clienteId));
        }

        List<Map<String, Object>> itensMap = JsonUtil.getObjectList(map, "itens");
        for (Map<String, Object> itemMap : itensMap) {
            ItemVenda item = desserializarItem(itemMap);
            if (item != null) v.adicionarItem(item);
        }

        return v;
    }

    private ItemVenda desserializarItem(Map<String, Object> map) {
        String medId = JsonUtil.getString(map, "medicamentoId");
        if (medId == null) return null;
        Medicamento med = medicamentoRepo.buscarPorId(medId);
        if (med == null) return null;

        ItemVenda item = new ItemVenda();
        item.setId(JsonUtil.getString(map, "id"));
        item.setMedicamento(med);
        item.setQuantidade(JsonUtil.getInt(map, "quantidade"));
        item.setPrecoUnitario(JsonUtil.getDouble(map, "precoUnitario"));
        return item;
    }

    @Override
    protected String getId(Venda objeto) {
        return objeto.getId();
    }

    public List<Venda> buscarPorFuncionario(String funcionarioId) {
        List<Venda> resultado = new ArrayList<>();
        for (Venda v : listarTodos()) {
            if (v.getFuncionario() != null && funcionarioId.equals(v.getFuncionario().getId())) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Venda> buscarPorCliente(String clienteId) {
        List<Venda> resultado = new ArrayList<>();
        for (Venda v : listarTodos()) {
            if (v.getCliente() != null && clienteId.equals(v.getCliente().getId())) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}