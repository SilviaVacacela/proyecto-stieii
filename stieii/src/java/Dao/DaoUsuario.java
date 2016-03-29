/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import Pojo.Usuario;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoUsuario implements Interface.InterfaceUsuario{

    @Override
    public boolean registrar(Session session, Usuario usuario) throws Exception {
        session.save(usuario);
        return true;
    }

    @Override
    public List<Usuario> verTodo(Session session) throws Exception {
        String hql="from Usuario";
        Query query=session.createQuery(hql);
        List<Usuario> listaUsuario=(List<Usuario>) query.list();
        for(Usuario lista: listaUsuario){
            Hibernate.initialize(lista.getRol());
        }
        return listaUsuario;
    }

    @Override
    public Usuario verPorCodigoUsuario(Session session, int idUsuario) throws Exception {
        String hql="from Usuario where idUsuario=:idUsuario";
        Query query=session.createQuery(hql);
        query.setParameter("idUsuario", idUsuario);
        Usuario usuario=(Usuario) query.uniqueResult();
        Hibernate.initialize(usuario.getRol());
        return usuario;
    }

    @Override
    public boolean actualizar(Session session, Usuario usuario) throws Exception {
        session.update(usuario);
        Hibernate.initialize(usuario.getRol());
        return true;
    }

    @Override
    public Usuario verPorUsername(Session session, String username) throws Exception {
        String hql="from Usuario where username=:username";
        Query query=session.createQuery(hql);
        query.setParameter("username", username);
        Usuario usuario=(Usuario) query.uniqueResult();
//        Hibernate.initialize(usuario.getRol()); // para inicializar el objeto
        return usuario;
    }
    
    public Usuario verUsuarioLogeado(Session session, String username) throws Exception {
        String hql="from Usuario where username=:username";
        Query query=session.createQuery(hql);
        query.setParameter("username", username);
        Usuario usuario=(Usuario) query.uniqueResult();
        Hibernate.initialize(usuario.getRol()); // para inicializar el objeto
        return usuario;
    }
    
}
