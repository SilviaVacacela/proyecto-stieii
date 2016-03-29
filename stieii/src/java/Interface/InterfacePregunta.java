/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.PregConc;
import Pojo.Pregunta;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfacePregunta {
    
    public boolean registrar(Session session, Pregunta pregunta) throws Exception;
    public List<Pregunta> verTodo(Session session) throws Exception;
    public List<PregConc> verPorTest(Session session,int test) throws Exception;
    public List<Pregunta> verPorConcepto(Session session,int concepto) throws Exception;
    public Pregunta verPorCodigoPregunta(Session session, int idPregunta) throws Exception;
    public boolean actualizar(Session session, Pregunta pregunta) throws Exception;
    public boolean eliminar(Session session,Pregunta pregunta) throws Exception;
}
