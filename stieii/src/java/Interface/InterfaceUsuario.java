/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interface;

import Pojo.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceUsuario {
    public boolean registrar(Session session, Usuario usuario) throws Exception;
    public List<Usuario> verTodo(Session session) throws Exception;
    public Usuario verPorCodigoUsuario(Session session,int idUsuario) throws Exception;
    public Usuario verPorUsername(Session session, String username) throws Exception;
    public boolean actualizar(Session session,Usuario usuario) throws Exception;
}
