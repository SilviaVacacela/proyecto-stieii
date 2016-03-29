/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import HibernateUtil.HibernateUtil;
import Pojo.Rol;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoRol implements Interface.InterfaceRol{

    @Override
    public boolean registrar(Session session, Rol rol) throws Exception {
        session.save(rol);
        return true;
    }

    @Override
    public List<Rol> verTodo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Criteria criteria=session.createCriteria(Rol.class);
//        String hql="from Rol";
//        Query query=session.createQuery(hql);
        List<Rol> listaRol=(List<Rol>) criteria.list();
        session.close();
        return listaRol;
    }

    
    @Override
    public boolean actualizar(Session session, Rol rol) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rol verPorTipoRol(Session session, String tipo) throws Exception {
        String hql="from Rol where tipo=:tipo";
        Query query=session.createQuery(hql);
        query.setParameter("tipo", tipo);
        Rol rol=(Rol) query.uniqueResult();
        return rol;
        
    }
    
}
