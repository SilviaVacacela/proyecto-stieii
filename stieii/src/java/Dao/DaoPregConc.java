/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojo.Concepto;
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
public class DaoPregConc implements Interface.InterfacePregConc {

    @Override
    public boolean registrar(Session session, PregConc pregConc) throws Exception {
        session.save(pregConc);
        return true;
    }
    
    public boolean registrarVarios(Session session, List<PregConc> resultados) throws Exception {
        for (PregConc r : resultados) {
            session.save(r);
            session.flush();
            session.clear();
        }
        return true;
    }
    
        @Override
    public boolean actualizar(Session session, PregConc pregConc) throws Exception {
        session.update(pregConc);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Pregunta pregunta, Concepto concepto) throws Exception {
        String hql = "from PregConc p where p.pregunta=:pregunta AND p.concepto=:concepto ";
        Query query = session.createQuery(hql);
        query.setParameter("pregunta", pregunta);
        query.setParameter("concepto", concepto);
        PregConc pregConc = (PregConc) query.uniqueResult();
        session.delete(pregConc);
        return true;
    }
    
    @Override
    public PregConc verPorCodigo(Session session, int idPregConc) throws Exception {
        String hql = "from PregConc where idPregConc=:idPregConc";
        Query query = session.createQuery(hql);
        query.setParameter("idPregConc", idPregConc);
        PregConc pregConc = (PregConc) query.uniqueResult();
//        Hibernate.initialize(concepto.getTema().getUnidadensenianza());
        return pregConc;
    }

    @Override
    public List<PregConc> verTodo(Session session) throws Exception {
        String hql = "from PregConc";
        Query query = session.createQuery(hql);
        List<PregConc> listaPreguntas = (List<PregConc>) query.list();
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
    public List<PregConc> verPorConcepto(Session session, int concepto) throws Exception {
        String hql = "from PregConc where concepto=:concepto";
        Query query = session.createQuery(hql);
        query.setInteger("concepto", concepto);
        List<PregConc> listaPreguntas = (List<PregConc>) query.list();
        
        return listaPreguntas;
    }

    
    @Override
    public List<PregConc> verPorPregunta(Session session, int idPregunta) throws Exception {
        String hql1 = "select conc from PregConc as conc join conc.pregunta as preguntas where preguntas.idPregunta =:idPregunta";
        Query query = session.createQuery(hql1);
        query.setInteger("idPregunta", idPregunta);
        List<PregConc> listaPreguntas = (List<PregConc>) query.list();
        
        return listaPreguntas;
    }
    
    

}
