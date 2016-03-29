/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import HibernateUtil.HibernateUtil;
import Interface.InterfaceTema;
import Pojo.Tema;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author silvy
 */
public class DaoTema implements InterfaceTema {
    //creamos un DAO por cada tabla de bd(por cada pojo)
    //Implemetamos la interfaz creada en Interface/InterfaceTema

    //Metodos Abstractos de la implemtacion del InterfaceTema
    @Override
    public boolean registrar(Session session, Tema tema) throws Exception {
        //session es de hibernate para establecer la conexion con la bd
        session.save(tema);
        return true;
    }

    @Override
    public List<Tema> verTodo(Session session) throws Exception {
        String hql = "from Tema";
        Query query = session.createQuery(hql);
        List<Tema> listaTemas = (List<Tema>) query.list();
        for (Tema lista : listaTemas) {
            Hibernate.initialize(lista.getUnidadensenianza());
        }
        return listaTemas;
    }

    public List<Tema> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Tema.class);
        List<Tema> listaTemas = (List<Tema>) criteria.list();
        session.close();
        return listaTemas;
    }

    @Override
    public boolean actualizar(Session session, Tema tema) throws Exception {
        session.update(tema);
        return true;
    }

    public boolean eliminar(Session session, Tema tema) throws Exception {
        session.delete(tema);
        return true;
    }

    @Override
    public Tema verPorCodigoTema(Session session, int idTema) throws Exception {
        String hql = "from Tema where idTema=:idTema";
        Query query = session.createQuery(hql);
        query.setParameter("idTema", idTema);
        Tema tema = (Tema) query.uniqueResult();
//        Hibernate.initialize(tema.getUnidadensenianza());
        return tema;
    }

    @Override
    public Tema verPorTemaname(Session session, String nombre) throws Exception {
        String hql = "from Tema where nombre=:nombre";
        Query query = session.createQuery(hql);
        query.setParameter("nombre", nombre);
        Tema tema = (Tema) query.uniqueResult();
        return tema;
    }

    @Override
    public List<Tema> obtenerNombreTemasRepetidos(Session session, String nameTema, int idUnidad) throws Exception {
        String hql = "from Tema where nombre=:nombre and estado=:estado and temUnidEnsen=:temUnidEnsen";
        Query query = session.createQuery(hql);
        query.setParameter("nombre", nameTema);
        query.setParameter("estado", true);
        query.setInteger("temUnidEnsen", idUnidad);
        List<Tema> listaTemasAct = (List<Tema>) query.list();
        for (Tema lista : listaTemasAct) {
            Hibernate.initialize(lista.getUnidadensenianza());
        }
        return listaTemasAct;
    }
    public List<Tema> verPorUnidad(Session session, int idUnidad) throws Exception {
        String hql = "from Tema where temUnidEnsen=:temUnidEnsen and estado=:estado ";
        Query query = session.createQuery(hql);
        query.setParameter("estado", true);
        query.setInteger("temUnidEnsen", idUnidad);
        //        query.setParameter("unidadensenianza", idUnidad);
        List<Tema> tema = (List<Tema>) query.list();
        if(tema.isEmpty())
            System.out.println("La lista está vacía");
        else{
            for(Tema lista: tema){
                System.out.println("Tema: "+lista.getNombre());
            }
        }

        return tema;
    }
    
}
