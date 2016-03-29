/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Pregunta;
import Pojo.Tema;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRConsultarTest {

    private Tema tema = new Tema();
    private List<Tema> listaTemas;
    private List<Tema> listaTemaFiltrada;
    private List<Pregunta> listaPreguntas;
    private Session session;
    private Transaction transaction;
    private MenuModel model;

    public BeanRConsultarTest() {
        model = new DefaultMenuModel();

    }

    public Tema consultarTestPorCodigo(int idTema) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema t = daoTema.verPorCodigoTema(session, idTema);
            this.tema=t;
            transaction.commit();
            return t;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Tema> getAllTest() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaTemas = daoTema.verTodo(session);
            this.transaction.commit();
            return this.listaTemas;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public List<Tema> getListaTemas() {
        return listaTemas;
    }

    public void setListaTemas(List<Tema> listaTemas) {
        this.listaTemas = listaTemas;
    }

    public List<Tema> getListaTemaFiltrada() {
        return listaTemaFiltrada;
    }

    public void setListaTemaFiltrada(List<Tema> listaTemaFiltrada) {
        this.listaTemaFiltrada = listaTemaFiltrada;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public List<Tema> getItemsMenu() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Tema> temas = daoTema.verPorUnidad(session, 2);
            this.listaTemas = temas;
            this.transaction.commit();
            return this.listaTemas;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CREAR MENU DINÁMICO:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

}
