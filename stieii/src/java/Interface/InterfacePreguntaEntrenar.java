/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Preguntaentrenar;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public interface InterfacePreguntaEntrenar {

    public boolean registrarPreguntaEnt(Session session, Preguntaentrenar preguntaEntrena) throws Exception;

    public Preguntaentrenar verPreguntaEntrenamiento(Session session, int idEntrenar, Timestamp fecha) throws Exception;

    public Preguntaentrenar verPorCodigoPreguntaEntrenar(Session session, int idPreguntaE) throws Exception;

    public List<Preguntaentrenar> verListPreguntaEntrenarPorIdEntrena(Session session, int idEntrenar) throws Exception;

    public boolean actualizar(Session session, Preguntaentrenar preguntaEntrena) throws Exception;

}
