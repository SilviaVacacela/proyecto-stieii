/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojo.Item;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoItem implements Interface.InterfaceItems {

    @Override
    public boolean registrar(Session session, Item item) throws Exception {
        session.save(item);
        return true;
    }
    
    public boolean registrarVarios(Session session, List<Item> items) throws Exception {
        for (Item r : items) {
            session.save(r);
            session.flush();
            session.clear();
        }
        return true;
    }
    
    @Override
    public List<Item> verTodo(Session session) throws Exception {
        String hql = "from Item";
        Query query = session.createQuery(hql);
        List<Item> listaItems = (List<Item>) query.list();
        for (Item lista : listaItems) {
            Hibernate.initialize(lista.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaItems;
    }

    @Override
    public List<Item> verPorPregunta(Session session, int pregunta) throws Exception {
        String hql = "from Item where pregunta=:pregunta";
        Query query = session.createQuery(hql);
        query.setInteger("pregunta", pregunta);
        List<Item> listaItems = (List<Item>) query.list();
        for (Item lista : listaItems) {
            Hibernate.initialize(lista.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        return listaItems;
    }

    @Override
    public Item verPorPREGNombreItem(Session session, String nombreItem, int preguntaItem) throws Exception {
        String hql = "from Item where nombreItem=:nombreItem and preguntaItem=:preguntaItem";
        Query query = session.createQuery(hql);
        query.setParameter("nombreItem", nombreItem);
        query.setInteger("preguntaItem", preguntaItem);
        Item item = (Item) query.uniqueResult();
        Hibernate.initialize(item.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        
        return item;
    }

    @Override
    public Item verPorCodigoItem(Session session, int idItem) throws Exception {
        String hql = "from Item where idItem=:idItem";
        Query query = session.createQuery(hql);
        query.setParameter("idItem", idItem);
        Item pregunta = (Item) query.uniqueResult();
        Hibernate.initialize(pregunta.getPregunta());
//        Hibernate.initialize(pregunta.getTest().getTema());
        return pregunta;
    }

    @Override
    public boolean actualizar(Session session, Item item) throws Exception {
        session.update(item);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Item item) throws Exception {
        session.delete(item);
        return true;
    }
    
    public int verNumItemsPorPregunta(Session session, int pregunta) throws Exception {
        String hql = "from Item where pregunta=:pregunta";
        Query query = session.createQuery(hql);
        query.setInteger("pregunta", pregunta);
        List<Item> listaItems = (List<Item>) query.list();
        for (Item lista : listaItems) {
            Hibernate.initialize(lista.getPregunta());
//            Hibernate.initialize(lista.getTest().getTema());
        }
        int numItems;
        if(!listaItems.isEmpty()){
            numItems = listaItems.size();
        }else{
            numItems=0;
        }
        return numItems;
    }
    
}
