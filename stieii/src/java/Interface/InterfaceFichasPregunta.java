/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Fichaspregunta;
import org.hibernate.Session;
import java.util.List;
/**
 *
 * @author silvy
 */
public interface InterfaceFichasPregunta {
    public boolean registrarFichaPregunta(Session session, Fichaspregunta fichaPregunt) throws Exception;
    public List<Fichaspregunta> verPreguntaEntrenamiento(Session session, int idPregunta) throws Exception;
    
}
