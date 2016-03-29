/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Tema;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author silvy
 * nos ayuda para definir los comportamientos de cada pojo(tablas de la bd)
 */
public interface InterfaceTema {
    //creamos un interface por cada tabla de bd(por cada pojo)
    //Utilizamos en clase Dao/DaoTema
    
    //Creamos los metodos 
    public boolean registrar(Session session, Tema tema) throws Exception;
    public List<Tema> verTodo(Session session) throws Exception;
    public  Tema verPorCodigoTema(Session session,int idTema) throws Exception;
    public Tema verPorTemaname(Session session, String nombreTema) throws Exception;
    public List<Tema> obtenerNombreTemasRepetidos(Session session, String nameTema, int idUnidad) throws Exception;
    public boolean actualizar(Session session,Tema tema) throws Exception;
}
