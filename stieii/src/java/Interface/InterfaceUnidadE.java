/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Unidadensenianza;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public interface InterfaceUnidadE {
    //creamos un interface por cada tabla de bd(por cada pojo)
    //Utilizamos en clase Dao/DaoTema
    
    //Creamos los metodos 
    public boolean registrar(Session session, Unidadensenianza unidadensenianza) throws Exception;
    public List<Unidadensenianza> verTodo();
    public List<Unidadensenianza> verTodo(Session session) throws Exception;
    public Unidadensenianza verPorNombreUnidad(Session session, String nombreUnidad) throws Exception;
    public Unidadensenianza verPorCodigoUnidad(Session session, int id) throws Exception;
    public boolean actualizar(Session session,Unidadensenianza unidadensenianza) throws Exception;
    
    
}
