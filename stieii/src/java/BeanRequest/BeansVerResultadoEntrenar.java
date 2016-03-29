/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import BeanSession.BeanSEntrenar;
import Clases.RedNeuronal.RedNeuronal;
import Dao.DaoEntrenar;
import Dao.DaoFicha;
import Dao.DaoFichaPregunta;
import Dao.DaoPreguntaEntrenar;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Ficha;
import Pojo.Fichaspregunta;
import Pojo.Preguntaentrenar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author USUARIO
 */
@ManagedBean
@ViewScoped
public class BeansVerResultadoEntrenar {

    private Session session;
    private Transaction transaction;

    //llamar al bean de sesion 
    @ManagedProperty("#{beanSEntrenar}")
    private BeanSEntrenar beanSEntrena;

    private Entrenamiento entrenamiento;

    private String resultado;
    private String imagenResult;
    private String imgREDneuronal;
    private String nivelEntrenamiento;

    public BeansVerResultadoEntrenar() {

    }

    public void generarResultado() throws Exception {
        entrenamiento = obtnerEntrenamiento();
        RedNeuronal redN = new RedNeuronal();
        redN.redNeuronal(entrenamiento.getPuntaje(), entrenamiento.getTiempo(), entrenamiento.getError(), entrenamiento.getNivel());
        resultado = redN.getResultado();
        imagenResult = redN.getImgResultado();
        imgREDneuronal = redN.getImgREDneuronal();
        nivelEntrenamiento = redN.getNivelSigEntrena();
        // almacenar el resulado de la red neuronal
        actualizarEntrenamientoRedN(entrenamiento, resultado, nivelEntrenamiento);
        //metodo para actualizar las estado de las fichas segun la red neuronal.
        actualizarFichasSegunRedN(entrenamiento.getIdEntrena(), resultado);

    }

    public Entrenamiento obtnerEntrenamiento() {
        this.session = null;
        this.transaction = null;
        Entrenamiento entrenamiento = new Entrenamiento();

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para obtener un entrenamiento por su id
            DaoEntrenar daoEntrenar = new DaoEntrenar();//verPorCodigoEntrenamiento
            entrenamiento = daoEntrenar.verPorCodigoEntrenamiento(session, this.beanSEntrena.getIdEntrenamiento());
            this.transaction.commit();

            System.out.println("Correcto: Al Obtner el Entrenamiento con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner el Entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return entrenamiento;
    }

    public void mostrarMsj() {
        RequestContext.getCurrentInstance().update("frmResultado:panelResultado");
        RequestContext.getCurrentInstance().execute("PF('dialogResultado').show()");
    }

    public String actualizarPagina() {
        this.beanSEntrena.finalizar();
        return "verResultadosT";
    }

    //metodo para actualizar el resultado de la red neural en  el entrenamiento
    public void actualizarEntrenamientoRedN(Entrenamiento entrenamiento, String resultadRedN, String nivelEntrenamiento) {
        this.session = null;
        this.transaction = null;

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            DaoEntrenar daoEntrenar = new DaoEntrenar();
            entrenamiento.setResultadoredn(resultadRedN);
            entrenamiento.setNivel(nivelEntrenamiento);
            daoEntrenar.actualizar(session, entrenamiento);
            this.transaction.commit();

            System.out.println("Correcto: La Actualizacion del entrnamiento se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL actualizar el Entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //metodo para actualizar las estado de las fichas segun la red neuronal.
    public void actualizarFichasSegunRedN(int idEntrenamiento, String resultadoRedN) {
        String estadoAprendizaje = "";
        if (resultadoRedN.equals("Excelente ha aprendido")) {
            estadoAprendizaje = "Exelente ha aprendido";
        } else {
            estadoAprendizaje = "En proceso de aprendizaje";
        }
        listPreguntaEntrenar = obtenrListaPreguntaEntrenar(idEntrenamiento);
        for (int i = 0; i < listPreguntaEntrenar.size(); i++) {

            listFichasEn = obtenerListFichasPregunt(listPreguntaEntrenar.get(i).getIdInt());
            for (int j = 0; j < listFichasEn.size(); j++) {
                actualizarFicha(listFichasEn.get(j).getFicha().getIdFicha(), estadoAprendizaje);
            }
        }

    }

    //metodo para obtner la lista de preguntas de entrenamiento
    List<Preguntaentrenar> listPreguntaEntrenar;

    public List<Preguntaentrenar> obtenrListaPreguntaEntrenar(int idEntrenamiento) {
        this.session = null;
        this.transaction = null;
        List<String> listasIdPreg;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener una pregunta entrenar
            DaoPreguntaEntrenar daoPreguntaEntrenar = new DaoPreguntaEntrenar();
            listPreguntaEntrenar = daoPreguntaEntrenar.verListPreguntaEntrenarPorIdEntrena(session, idEntrenamiento);
            this.transaction.commit();

            System.out.println("Correcto: Al Obtner la listas de preguntaEntrenar TODOS LOS TEST se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner lista de preguntaEntrenar:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return listPreguntaEntrenar;
    }

    //metodo para obtner la lista de fichasPreguntas segun un Enrenamiento
    List<Fichaspregunta> listFichasEn;

    public List<Fichaspregunta> obtenerListFichasPregunt(int idPreguntaEnt) {
        this.session = null;
        this.transaction = null;
        List<String> listasIdPreg;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener una pregunta entrenar
            DaoFichaPregunta daoFichaPregunta = new DaoFichaPregunta();
            listFichasEn = daoFichaPregunta.verPreguntaEntrenamiento(session, idPreguntaEnt);
            this.transaction.commit();

            System.out.println("Correcto: Al Obtner la listas de preguntaEntrenar TODOS LOS TEST se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner lista de preguntaEntrenar:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return listFichasEn;
    }

    public void actualizarFicha(int idFicha, String resultadoA) {
        this.session = null;
        this.transaction = null;
        Ficha ficha = new Ficha();
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            DaoFicha daoFicha = new DaoFicha();
            ficha = daoFicha.verPorCodigoFicha(session, idFicha);

            ficha.setEstadoAprendizaje(resultadoA);
            daoFicha.actualizar(session, ficha);
            this.transaction.commit();

            System.out.println("Correcto: La Actualizacion de ficha  se ha realizado con exito");
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL Obtner el Entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //.................setter y getter.....................
    public Entrenamiento getEntrenamiento() {
        return entrenamiento;
    }

    public String getImagenResult() {
        return imagenResult;
    }

    public String getResultado() {
        return resultado;
    }

    public BeanSEntrenar getBeanSEntrena() {
        return beanSEntrena;
    }

    public void setBeanSEntrena(BeanSEntrenar beanSEntrena) {
        this.beanSEntrena = beanSEntrena;
    }

    public String getImgREDneuronal() {
        return imgREDneuronal;
    }

    public void setImgREDneuronal(String imgREDneuronal) {
        this.imgREDneuronal = imgREDneuronal;
    }

}
