/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interface;

import Pojo.Rol;
import Pojo.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceRol {
    public boolean registrar(Session session, Rol rol) throws Exception;
    public List<Rol> verTodo();
    public Rol verPorTipoRol(Session session,String tipo) throws Exception;
    public boolean actualizar(Session session,Rol rol) throws Exception;
}
