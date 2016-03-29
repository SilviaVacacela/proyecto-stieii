/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Interface.InterfaceFichasPregunta;
import Pojo.Fichaspregunta;
import Pojo.Pregunta;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public class DaoFichaPregunta implements InterfaceFichasPregunta {

    @Override
    public boolean registrarFichaPregunta(Session session, Fichaspregunta fichaPregunt) throws Exception {
        session.save(fichaPregunt);
        return true;
    }

    @Override
    public List<Fichaspregunta> verPreguntaEntrenamiento(Session session, int idPregunta) throws Exception {
        String hql = "from Fichaspregunta where pregunta=:pregunta"; //revisar si existe un error
        Query query = session.createQuery(hql);
        query.setInteger("pregunta", idPregunta);
        List<Fichaspregunta> listaFichasPreguntas = (List<Fichaspregunta>) query.list();
        for (Fichaspregunta lista : listaFichasPreguntas) {
            Hibernate.initialize(lista.getPreguntaentrenar());
        }
        return listaFichasPreguntas;
    }
    
}
