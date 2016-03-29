/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Concepto;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceConcepto {
    
    public boolean registrar(Session session, Concepto concepto) throws Exception;
    public List<Concepto> verTodo();
    public List<Concepto> verTodo(Session session) throws Exception;
    public Concepto verPorCodigoConcepto(Session session, int idConcepto) throws Exception;
    public boolean actualizar(Session session, Concepto concepto) throws Exception;
    public boolean eliminar(Session session, Concepto concepto)throws Exception;
    public List<Concepto> verPorTema(Session session, int tema) throws Exception;
    
}
