/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Ficha;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public interface InterfaceFicha {

    public boolean registrar(Session session, Ficha ficha) throws Exception;

    public List<Ficha> obtenerFichaRepetidos(Session session, String nameFicha, int idtema, boolean estado) throws Exception;

    public Ficha verFichaPorAtributos(Session session, String nameFicha, String descripcion, int idtema) throws Exception;

    public List<Ficha> verListfichasActivasPorTema(Session session, int idTema) throws Exception;

    public boolean actualizar(Session session, Ficha ficha) throws Exception;
    
    public List<Ficha> verTodo(Session session) throws Exception;

    public Ficha verPorCodigoFicha(Session session, int idFicha) throws Exception;

    public List<Ficha> verListfichasPorTema(Session session, int idTema) throws Exception;

}
