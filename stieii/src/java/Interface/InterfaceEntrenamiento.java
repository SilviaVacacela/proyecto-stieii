/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Entrenamiento;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public interface InterfaceEntrenamiento {

    public boolean registrar(Session session, Entrenamiento entrenar) throws Exception;
    public Entrenamiento verEntrenamiento(Session session, int estudiate, int tema, int tiempo, Timestamp fecha) throws Exception ;
    public Entrenamiento verPorCodigoEntrenamiento(Session session, int idEntrenar) throws Exception;
    public  List<Entrenamiento> listEntrenamientoPorIdEstudiante(Session session, int idEstudiante) throws Exception ;
    public boolean actualizar(Session session, Entrenamiento entrenar) throws Exception;
}
