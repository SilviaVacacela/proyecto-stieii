/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interface;

import Pojo.Administrador;
import Pojo.Estudiante;
import Pojo.Usuario;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceEstudiante {
    public boolean registrar(Session session, Estudiante est) throws Exception;
    public List<Estudiante> verTodo(Session session) throws Exception;
    public Estudiante verPorCodigoEstudiante(Session session,int idEst) throws Exception;
    public Estudiante verPorCodigoUsuario(Session session, int idUsuario) throws Exception;
    public boolean actualizar(Session session,Estudiante est) throws Exception;
}
