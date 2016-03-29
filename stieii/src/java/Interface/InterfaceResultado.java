/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Resultado;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceResultado {
    
    public boolean registrar(Session session, Resultado resultado) throws Exception;
    public List<Resultado> verTodo();
    public List<Resultado> verTodo(Session session) throws Exception;
    public Resultado verPorCodigoResultado(Session session, int idResultado) throws Exception;
    public boolean actualizar(Session session, Resultado resultado) throws Exception;
    public boolean eliminar(Session session, Resultado Resultado)throws Exception;
    public List<Resultado> verPorEstudiante(Session session, int estudiante) throws Exception;
    public List<Resultado> verPorConcepto(Session session, int concepto) throws Exception;
    
}
