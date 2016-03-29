/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Concepto;
import Pojo.PregConc;
import Pojo.Pregunta;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfacePregConc {
    
    public boolean registrar(Session session, PregConc pregConc) throws Exception;
    public boolean actualizar(Session session, PregConc pregConc) throws Exception;
    public boolean eliminar(Session session,Pregunta pregunta, Concepto concepto) throws Exception;
    public List<PregConc> verTodo(Session session) throws Exception;
    public PregConc verPorCodigo(Session session, int idPregConc) throws Exception;
    public List<PregConc> verPorTest(Session session, int test) throws Exception;
    public List<PregConc> verPorConcepto(Session session, int concepto) throws Exception;
    public List<PregConc> verPorPregunta(Session session, int pregunta) throws Exception;
}
