/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interface;

import Pojo.Administrador;
import Pojo.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceAdministrador {
    public boolean registrar(Session session, Administrador admin) throws Exception;
    public List<Administrador> verTodo(Session session) throws Exception;
    public Administrador verPorCodigoAdministrador(Session session,int idAdmin) throws Exception;
    public Administrador verPorCodigoUsuario(Session session, int idUsuario) throws Exception;
    public boolean actualizar(Session session,Administrador admin) throws Exception;
}
