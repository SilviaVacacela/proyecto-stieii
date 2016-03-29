/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import Pojo.Administrador;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoAdministrador implements Interface.InterfaceAdministrador{

    @Override
    public boolean registrar(Session session, Administrador admin) throws Exception {
        session.save(admin);
        return true;    }

    @Override
    public List<Administrador> verTodo(Session session) throws Exception {
        String hql="from Administrador";
        Query query=session.createQuery(hql);
        List<Administrador> listaAdministrador=(List<Administrador>) query.list();
        for(Administrador lista: listaAdministrador){
            Hibernate.initialize(lista.getUsuario());
        }
        return listaAdministrador;
    }

    @Override
    public Administrador verPorCodigoAdministrador(Session session, int idAdmin) throws Exception {
        String hql="from Administrador where idAdmin=:idAdmin";
        Query query=session.createQuery(hql);
        query.setParameter("idAdmin", idAdmin);
        Administrador admin=(Administrador) query.uniqueResult();
        Hibernate.initialize(admin.getUsuario());
        return admin;
    }

    @Override
    public Administrador verPorCodigoUsuario(Session session, int idUsuario) throws Exception {
        
        String hql="from Administrador where usuarioAdmin=:usuarioAdmin";
        Query query=session.createQuery(hql);
        query.setParameter("usuarioAdmin", idUsuario);
        Administrador admin=(Administrador) query.uniqueResult();
        Hibernate.initialize(admin.getUsuario());
        return admin;
        
    }

    @Override
    public boolean actualizar(Session session, Administrador admin) throws Exception {
        session.update(admin);
        return true;
    }
    
}
