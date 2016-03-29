/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Pojo.Estudiante;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public class DaoEstudiante implements Interface.InterfaceEstudiante {

    @Override
    public boolean registrar(Session session, Estudiante est) throws Exception {
        session.save(est);
        return true;
    }

    @Override
    public List<Estudiante> verTodo(Session session) throws Exception {
        String hql = "from Estudiante";
        Query query = session.createQuery(hql);
        List<Estudiante> listaEst = (List<Estudiante>) query.list();
        for (int i = 0; i < listaEst.size(); i++) {
            Hibernate.initialize(listaEst.get(i).getUsuario());
            Hibernate.initialize(listaEst.get(i).getUnidadensenianza());
        }
        return listaEst;
    }
    
    public List<Estudiante> verTodoEstudianteActivos(Session session) throws Exception {
        String hql = "from Estudiante";
        Query query = session.createQuery(hql);
        List<Estudiante> listaEst = (List<Estudiante>) query.list();
        for (int i = 0; i < listaEst.size(); i++) {
            Hibernate.initialize(listaEst.get(i).getUsuario());
            Hibernate.initialize(listaEst.get(i).getUnidadensenianza());
        }
        return listaEst;
    }

    @Override
    public Estudiante verPorCodigoEstudiante(Session session, int idEstud) throws Exception {
        String hql = "from Estudiante where idEst=:idEst";
        Query query = session.createQuery(hql);
        query.setParameter("idEst", idEstud);
        Estudiante estudiante = (Estudiante) query.uniqueResult();
        Hibernate.initialize(estudiante.getUsuario());
        return estudiante;
    }

    @Override
    public Estudiante verPorCodigoUsuario(Session session, int idUsuario) throws Exception {

        String hql = "from Estudiante where usuarioEst=:usuarioEst";
        Query query = session.createQuery(hql);
        query.setParameter("usuarioEst", idUsuario);
        Estudiante estudiante = (Estudiante) query.uniqueResult();
//        Hibernate.initialize(tema.getUnidadensenianza());
        return estudiante;
    }

    @Override
    public boolean actualizar(Session session, Estudiante est) throws Exception {
        session.update(est);
        return true;
    }

    public List<Estudiante> verPorUnidadEnsenianza(Session session, String nombreUnidad) throws Exception {
        String hql = "select est from Estudiante as est join est.unidadensenianza as unidades where unidades.nombreUnidad=:nombreUnidad";
        Query query = session.createQuery(hql);
        query.setParameter("nombreUnidad", nombreUnidad);
        List<Estudiante> listaEst = (List<Estudiante>) query.list();
        for (int i = 0; i < listaEst.size(); i++) {
            Hibernate.initialize(listaEst.get(i).getUsuario());
            Hibernate.initialize(listaEst.get(i).getUnidadensenianza());
        }

        return listaEst;
    }

    public List<Estudiante> verEstudiantePorIdUnidad(Session session, int idUnidad) throws Exception {
        String hql = " from Estudiante where estUnidEnsen=:estUnidEnsen";
        Query query = session.createQuery(hql);
        query.setParameter("estUnidEnsen", idUnidad);
        List<Estudiante> listaEst = (List<Estudiante>) query.list();
        for (int i = 0; i < listaEst.size(); i++) {
            Hibernate.initialize(listaEst.get(i).getUsuario());
            Hibernate.initialize(listaEst.get(i).getUnidadensenianza());
        }

        return listaEst;
    }

    public Estudiante verPorUsername(Session session, String username) throws Exception {
        String hql1 = "select est from Estudiante as est join est.usuario as usuarios where usuarios.username=:username";
        Query query = session.createQuery(hql1);
        query.setParameter("username", username);
        Estudiante estudiante = (Estudiante) query.uniqueResult();
        return estudiante;
    }

}
