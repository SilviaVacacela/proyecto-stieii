/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojo.PregConc;
import Pojo.Pregunta;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoPregunta implements Interface.InterfacePregunta {

    @Override
    public boolean registrar(Session session, Pregunta pregunta) throws Exception {
        session.save(pregunta);
        
        return true;
    }

    @Override
    public List<Pregunta> verTodo(Session session) throws Exception {
        String hql = "from Pregunta";
        Query query = session.createQuery(hql);
        List<Pregunta> listaPreguntas = (List<Pregunta>) query.list();

        return listaPreguntas;
    }

    @Override
    public List<PregConc> verPorTest(Session session, int tema) throws Exception {
        
        String hql1 = "select preg from PregConc as preg join preg.concepto as conceptos where conceptos.tema =:tema";
        Query query = session.createQuery(hql1);
        query.setInteger("tema", tema);
        List<PregConc> listaPreguntas = (List<PregConc>) query.list();
        for(PregConc lista: listaPreguntas){
            Hibernate.initialize(lista.getPregunta());
            Hibernate.initialize(lista.getConcepto());
        }
        
        return listaPreguntas;
    }

    @Override
    public List<Pregunta> verPorConcepto(Session session, int idConcepto) throws Exception {
        String hql = "select preg.pregunta from PregConc as preg join preg.concepto as conceptos where conceptos.idConcepto =:idConcepto";
        Query query = session.createQuery(hql);
        query.setInteger("idConcepto", idConcepto);
        List<Pregunta> listaPreguntas = (List<Pregunta>) query.list();
        
        return listaPreguntas;
    }

    @Override
    public Pregunta verPorCodigoPregunta(Session session, int idPregunta) throws Exception {
        String hql = "from Pregunta where idPregunta=:idPregunta";
        Query query = session.createQuery(hql);
        query.setParameter("idPregunta", idPregunta);
        Pregunta pregunta = (Pregunta) query.uniqueResult();
        Hibernate.initialize(pregunta.getItems());
        return pregunta;
    }
    
    public int verUltimoRegistro(Session session) throws Exception {
        String hql = "SELECT max(idPregunta) from Pregunta";
        Query query = session.createQuery(hql);
        int preg= (int) query.uniqueResult();
        return preg;
    }
    
    @Override
    public boolean actualizar(Session session, Pregunta pregunta) throws Exception {
        session.update(pregunta);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Pregunta pregunta) throws Exception {
        session.delete(pregunta);
        return true;
    }

}
