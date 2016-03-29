/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoPregunta;
import Dao.DaoTema;
import HibernateUtil.HibernateUtil;
import Pojo.Concepto;
import Pojo.PregConc;
import Pojo.Pregunta;
import Pojo.Tema;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRPregConc {

    private Pregunta pregunta;
    private Concepto concepto;
    private PregConc pregconc;
    private Tema tema;
    private List<Pregunta> listaPreguntas;
    private List<Concepto> listaConceptos;
    private List<PregConc> listaPregConc;
    private Session session;
    private Transaction transaction;

    //constructor
    public BeanRPregConc() {
        this.pregunta = new Pregunta();
    }

    public void registrar(Tema tema) {
        this.session = null;
        this.transaction = null;
        try {

            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.pregunta.setEstado(true);
            daoPregunta.registrar(this.session, this.pregunta);
            // INGRESAR REGISTROS PREG_CONCEPTO
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));
            this.pregunta = new Pregunta();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void actualizar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoPregunta.actualizar(this.session, this.pregunta);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void eliminar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoPregunta.eliminar(this.session, this.pregunta);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Pregunta eliminada correctamente."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL ELIMINAR:", "Contacte con el administrador, " + ex));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    
    public List<PregConc> getPreguntasPorTest(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoPregunta daoPregunta = new DaoPregunta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            listaPregConc = daoPregunta.verPorTest(session, codigo);
            transaction.commit();
            return listaPregConc;
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

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public PregConc getPregconc() {
        return pregconc;
    }

    public void setPregconc(PregConc pregconc) {
        this.pregconc = pregconc;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public List<Pregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<Pregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public List<Concepto> getListaConceptos() {
        return listaConceptos;
    }

    public void setListaConceptos(List<Concepto> listaConceptos) {
        this.listaConceptos = listaConceptos;
    }

    public List<PregConc> getListaPregConc() {
        return listaPregConc;
    }

    public void setListaPregConc(List<PregConc> listaPregConc) {
        this.listaPregConc = listaPregConc;
    }
    
    
    
    


}
