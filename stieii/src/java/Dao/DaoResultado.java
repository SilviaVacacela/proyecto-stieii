/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Resultado;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoResultado implements Interface.InterfaceResultado {

    @Override
    public boolean registrar(Session session, Resultado resultado) throws Exception {
        session.save(resultado);
        return true;
    }

    public boolean registrarVarios(Session session, List<Resultado> resultados) throws Exception {
        for (Resultado r : resultados) {
            session.save(r);
            session.flush();
            session.clear();
        }
        return true;
    }

    public boolean actualizarVarios(Session session, List<Resultado> resultados) throws Exception {
        for (Resultado r : resultados) {
            session.update(r);
            session.flush();
            session.clear();
        }
        return true;
    }

    @Override
    public List<Resultado> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Resultado.class);
        List<Resultado> listaResultados = (List<Resultado>) criteria.list();
        session.close();
        return listaResultados;
    }

    @Override
    public List<Resultado> verTodo(Session session) throws Exception {
        String hql = "from Resultado";
        Query query = session.createQuery(hql);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        for (Resultado lista : listaResultados) {
            Hibernate.initialize(lista.getEstudiante());
            Hibernate.initialize(lista.getConcepto());
        }
        return listaResultados;
    }

    @Override
    public Resultado verPorCodigoResultado(Session session, int idResultado) throws Exception {
        String hql = "from Resultado where idResultado=:idResultado";
        Query query = session.createQuery(hql);
        query.setParameter("idResultado", idResultado);
        Resultado resultado = (Resultado) query.uniqueResult();
        Hibernate.initialize(resultado.getEstudiante());
        Hibernate.initialize(resultado.getConcepto());
        return resultado;

    }

    @Override
    public boolean actualizar(Session session, Resultado resultado) throws Exception {
        session.update(resultado);
        return true;

    }

    @Override
    public boolean eliminar(Session session, Resultado resultado) throws Exception {
        session.delete(resultado);
        return true;
    }

    @Override
    public List<Resultado> verPorEstudiante(Session session, int estudiante) throws Exception {
        String hql = "from Resultado where estudiante=:estudiante";
        Query query = session.createQuery(hql);
        query.setInteger("estudiante", estudiante);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        if (listaResultados != null) {
            for (Resultado lista : listaResultados) {
                Hibernate.initialize(lista.getEstudiante());
                Hibernate.initialize(lista.getConcepto());
            }
        }
        return listaResultados;
    }

    public Double concAprendidoPorEst(Session session, int estudiante, int concepto) throws Exception {
        String hql = "from Resultado where estudiante=:estudiante and concepto=:concepto";
        Query query = session.createQuery(hql);
        query.setInteger("estudiante", estudiante);
        query.setInteger("concepto", concepto);
        Resultado resultado = (Resultado) query.uniqueResult();
        Hibernate.initialize(resultado.getEstudiante());
        Hibernate.initialize(resultado.getConcepto());        
            return resultado.getValor();
    }

    @Override
    public List<Resultado> verPorConcepto(Session session, int concepto) throws Exception {
        String hql = "from Resultado where concepto=:concepto";
        Query query = session.createQuery(hql);
        query.setInteger("concepto", concepto);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        if (listaResultados != null) {
            for (Resultado lista : listaResultados) {
                Hibernate.initialize(lista.getEstudiante());
                Hibernate.initialize(lista.getConcepto());
            }
        }
        return listaResultados;
    }

    public List<Resultado> verPorEstudianteTema(Session session, int tema, int estudiante) throws Exception {
        String hql1 = "select resultad from Resultado as resultad join resultad.concepto as conceptos where resultad.estudiante=:estudiante and conceptos.tema=:tema";
        Query query = session.createQuery(hql1);
        query.setInteger("estudiante", estudiante);
        query.setInteger("tema", tema);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        for (Resultado lista : listaResultados) {
            Hibernate.initialize(lista.getEstudiante());
            Hibernate.initialize(lista.getConcepto());
        }
        return listaResultados;
    }

    public List<Resultado> verPorEstudianteUnidad(Session session, int unidadensenianza, int estudiante) throws Exception {
        String hql1 = "select resultad from Resultado as resultad join resultad.concepto as conceptos where resultad.estudiante=:estudiante and conceptos.tema.unidadensenianza=:unidadensenianza";
        Query query = session.createQuery(hql1);
        query.setInteger("estudiante", estudiante);
        query.setInteger("unidadensenianza", unidadensenianza);
        List<Resultado> listaResultados = (List<Resultado>) query.list();
        for (Resultado lista : listaResultados) {
            Hibernate.initialize(lista.getEstudiante());
            Hibernate.initialize(lista.getConcepto());
        }
        return listaResultados;
    }
    
    public Resultado verPorEstudianteConcepto(Session session, int estudiante, int concepto) throws Exception {
        String hql = "from Resultado where estudiante=:estudiante and concepto=:concepto";
        Query query = session.createQuery(hql);
        query.setInteger("estudiante", estudiante);
        query.setInteger("concepto", concepto);
        Resultado resultado = (Resultado) query.uniqueResult();
        Hibernate.initialize(resultado.getEstudiante());
        Hibernate.initialize(resultado.getConcepto());

        return resultado;
    }

}
