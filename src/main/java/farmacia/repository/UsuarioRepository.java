package farmacia.repository;

import farmacia.model.Administrador;
import farmacia.model.Funcionario;
import farmacia.model.Usuario;
import farmacia.util.JsonUtil;
import java.util.LinkedHashMap;
import java.util.Map;


public class UsuarioRepository extends AbstractRepository<Usuario> {

    public UsuarioRepository() {
        super("usuarios.json");
    }

    @Override
    protected Map<String, Object> toMap(Usuario u) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", u.getId());
        map.put("nome", u.getNome());
        map.put("login", u.getLogin());
        map.put("senha", u.getSenha());
        map.put("perfil", u.getPerfil());   
        return map;
    }

    @Override
    protected Usuario fromMap(Map<String, Object> map) {
        String perfil = JsonUtil.getString(map, "perfil");
        Usuario u;
        if ("ADMINISTRADOR".equals(perfil)) {
            u = new Administrador();
        } else {
            u = new Funcionario();
        }
        u.setId(JsonUtil.getString(map, "id"));
        u.setNome(JsonUtil.getString(map, "nome"));
        u.setLogin(JsonUtil.getString(map, "login"));
        u.setSenha(JsonUtil.getString(map, "senha"));
        return u;
    }

    @Override
    protected String getId(Usuario objeto) {
        return objeto.getId();
    }


    public Usuario buscarPorLogin(String login) {
        for (Usuario u : listarTodos()) {
            if (login.equals(u.getLogin())) return u;
        }
        return null;
    }
}