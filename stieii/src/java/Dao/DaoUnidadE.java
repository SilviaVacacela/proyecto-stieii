/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import HibernateUtil.HibernateUtil;
import Interface.InterfaceUnidadE;
import Pojo.Unidadensenianza;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public class DaoUnidadE implements InterfaceUnidadE {

    @Override
    public boolean registrar(Session session, Unidadensenianza unidadensenianza) throws Exception {
        session.save(unidadensenianza);
        return true;
    }

    @Override
    public List<Unidadensenianza> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Unidadensenianza.class);
        List<Unidadensenianza> listaUnidadE = (List<Unidadensenianza>) criteria.list();
        session.close();
        return listaUnidadE;
    }

    @Override
    public List<Unidadensenianza> verTodo(Session session) {
        String hql = "from Unidadensenianza where estado=true";
        Query query = session.createQuery(hql);
        List<Unidadensenianza> listaUnidades = (List<Unidadensenianza>) query.list();
        for (Unidadensenianza lista : listaUnidades) {
            Hibernate.initialize(lista.getAdministrador());
            Hibernate.initialize(lista.getAdministrador().getUsuario());
        }
        return listaUnidades;
    }

    @Override
    public Unidadensenianza verPorNombreUnidad(Session session, String nombreUnidad) throws Exception {
        String hql = "from Unidadensenianza where nombreUnidad=:nombreUnidad";
        Query query = session.createQuery(hql);
        query.setParameter("nombreUnidad", nombreUnidad);
        Unidadensenianza unidad = (Unidadensenianza) query.uniqueResult();
        return unidad;
    }

    public List<Unidadensenianza> obtenerNombreUnidadRepetidos(Session session, String nameUnidad) throws Exception {
        String hql = "from Unidadensenianza where nombreUnidad=:nombreUnidad and estado=:estado";
        Query query = session.createQuery(hql);
        query.setParameter("nombreUnidad", nameUnidad);
        query.setParameter("estado", true);
        List<Unidadensenianza> listaUnidadesAct = (List<Unidadensenianza>) query.list();
        for (Unidadensenianza lista : listaUnidadesAct) {
            Hibernate.initialize(lista.getAdministrador());
        }
        return listaUnidadesAct;
    }

    @Override
    public boolean actualizar(Session session, Unidadensenianza unidadensenianza) throws Exception {
        session.update(unidadensenianza);
        return true;
    }

    @Override
    public Unidadensenianza verPorCodigoUnidad(Session session, int id) throws Exception {
        String hql = "from Unidadensenianza where id=:id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        Unidadensenianza unidadE = (Unidadensenianza) query.uniqueResult();
        Hibernate.initialize(unidadE.getAdministrador());
        Hibernate.initialize(unidadE.getAdministrador().getUsuario());
        return unidadE;
    }

     public boolean eliminar(Session session, Unidadensenianza unidadensenianza) throws Exception {
        session.delete(unidadensenianza);
        return true;
    }

}
