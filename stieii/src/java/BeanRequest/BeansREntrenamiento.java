/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import BeanSession.BeanSEntrenar;
import Dao.DaoEntrenar;
import Dao.DaoEstudiante;
import Dao.DaoFicha;
import Dao.DaoPreguntaEntrenar;
import Dao.DaoTema;
import Dao.DaoUnidadE;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Entrenamiento;
import Pojo.Estudiante;
import Pojo.Ficha;
import Pojo.Preguntaentrenar;
import Pojo.Tema;
import Pojo.Unidadensenianza;
import Pojo.Usuario;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author silvy
 */
@ManagedBean
@ViewScoped
public class BeansREntrenamiento {

    private Session session;
    private Transaction transaction;
    private Entrenamiento entrenar;
    java.util.Date fecha = new Date();
    DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
    DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    String fechaRegistro = formatoFecha.format(fecha) + " " + formatoHora.format(fecha);
    int h = fecha.getHours();
    int m = fecha.getMinutes();
    int s = fecha.getSeconds();
    int tiempo = (h * 3600) + (m * 60) + s;

    java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());

    //llamar al bean de ssesion para establecer datos.
    @ManagedProperty("#{beanSEntrenar}")
    private BeanSEntrenar beanSEntrena;

    //atributo para crear pregunta de entrenamiento
    private Preguntaentrenar preguntaEnt;

    public BeansREntrenamiento() {
        this.entrenar = new Entrenamiento();
        this.entrenar.setError(0);
        this.entrenar.setTiempo(tiempo);
        this.entrenar.setPuntaje(0);
        this.entrenar.setFecha(sqlDate);
        this.entrenar.setResultadoredn("sin resultado");
        this.entrenar.setNivel("facil");
    }

    //proceso para obtner temas que tengan mayor a 6 fichas
    public List<Tema> temasConFichas(String username) {
        List<Tema> listaTemas = getTemasPorUnidad(username);
//        System.out.println("listaTemas-------------------------------"+listaTemas.size());
        int id;
        int sizeListFichas;
        for (int i = 0; i < listaTemas.size(); i++) {
            id = listaTemas.get(i).getIdTema(); //obtener el id del Tema
            sizeListFichas = sizeListafichaPorTema(id); //tamaño de lista de fichas segun un tema
            System.out.println("el id tema " + id + " tiene una lista de fichas con un tamaño " + sizeListFichas);
            if (sizeListFichas < 6) {
                listaTemas.remove(i);
            }
        }
        return listaTemas;

    }

    public List<Tema> getTemasPorUnidad(String username) {
        int idUsuario = idUsuario(username);
        List<Tema> temasUs=getTemasPorUnidadES(idUsuario);
        return temasUs;
    }

    //metodo para obtneter el id del UserNAME si no existe devuelve 0
    public int idUsuario(String username) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //obtener un usuario por su username, para obtener un estudiante y fijar en el entrenamiento
            DaoUsuario daoUsuario = new DaoUsuario();
            Usuario usuarioEstudiante = daoUsuario.verPorUsername(session, username); //obtuvimos el usuario segun su username
//            
            System.out.println("...........................+ " + usuarioEstudiante.getIdUsuario());

            transaction.commit();
            return usuarioEstudiante.getIdUsuario();

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            return 0;
        }
    }

    //metodo para obtneter Todos los tema  de una determinada UNIDAD con respecto al idUnidad
    public List<Tema> getTemasPorUnidadES(int idUsuario) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //obtener un ESTUDIANYE por su IDusuario, 
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            Estudiante estudiante = daoEstudiante.verPorCodigoUsuario(session, idUsuario);//obtuvimos el estudiante  segun el id del usuario
            int idUnidadEstudiante = estudiante.getUnidadensenianza().getId();
            System.out.println("...........................+ " + idUnidadEstudiante);

            //consultar si la unidad del estudiante 
            DaoUnidadE daoUnidadE = new DaoUnidadE();
            Unidadensenianza unidadE = daoUnidadE.verPorCodigoUnidad(session, idUnidadEstudiante);
            System.out.println(".....dgrhg.....................sdgvz...." + idUnidadEstudiante);
            System.out.println(".....dgrhg.....................sdgvz...." + unidadE.getNombreUnidad());

            //si el tema esta eliminado logicamente entonces por defecto idUnidadEstudiante=0
            if (unidadE.isEstado() == false) {
                idUnidadEstudiante = 0; //no se podra ver los temas de esta unidad porq la unidad 0 no existe
            }
            DaoTema daoTema = new DaoTema();
            List<Tema> listaTemas = daoTema.verPorUnidad(session, idUnidadEstudiante);

            transaction.commit();
            return listaTemas;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            return null;
        }
    }
////    //metodo para obtneter Todos los tema  de una determinada UNIDAD con respecto al idUnidad
////    public List<Tema> getTemasPorUnidad(String username) {
////        this.session = null;
////        this.transaction = null;
////        try {
////            this.session = HibernateUtil.getSessionFactory().openSession();
////            this.transaction = session.beginTransaction();
////            //obtener un usuario por su username, para obtener un estudiante y fijar en el entrenamiento
////            DaoUsuario daoUsuario = new DaoUsuario();
////            Usuario usuarioEstudiante = daoUsuario.verPorUsername(session, username); //obtuvimos el usuario segun su username
//////            this.transaction.commit();
////            this.session.close();
////            
////            
////            this.session = HibernateUtil.getSessionFactory().openSession();
////            this.transaction = session.beginTransaction();
////            DaoEstudiante daoEstudiante = new DaoEstudiante();
////            System.out.println("...........................+ "+usuarioEstudiante.getIdUsuario());
////            Estudiante estudiante = daoEstudiante.verPorCodigoUsuario(session, usuarioEstudiante.getIdUsuario());//obtuvimos el estudiante  segun el id del usuario
////            int idUnidadEstudiante = estudiante.getUnidadensenianza().getId();
////            System.out.println("...........................+ "+idUnidadEstudiante);
////
////            //consultar si la unidad del estudiante 
////            DaoUnidadE daoUnidadE = new DaoUnidadE();
////            Unidadensenianza unidadE = daoUnidadE.verPorCodigoUnidad(session, idUnidadEstudiante);
////            System.out.println(".....dgrhg.....................sdgvz...." + idUnidadEstudiante);
////            System.out.println(".....dgrhg.....................sdgvz...." + unidadE.getNombreUnidad());
////
////            //si el tema esta eliminado logicamente entonces por defecto idUnidadEstudiante=0
////            if (unidadE.isEstado() == false) {
////                idUnidadEstudiante = 0; //no se podra ver los temas de esta unidad porq la unidad 0 no existe
////            }
////            DaoTema daoTema = new DaoTema();
////            List<Tema> listaTemas = daoTema.verPorUnidad(session, idUnidadEstudiante);
////
////            transaction.commit();
////            return listaTemas;
////
////        } catch (Exception ex) {
////            if (this.transaction != null) {
////                this.transaction.rollback();
////            }
////            return null;
////        }
////    }

    public String iniciarEntrenamiento(String usernameLogin, int idtema) {
        String direcionar = "inicioAprendizaje";
        boolean estado = false;
        int idEntrena = 0;
        int tamñoListficha = sizeListafichaPorTema(idtema);

        //si existe lista de fichas mayor a 5 entonces crea el entrenamiento y las preguntas
        if (tamñoListficha >= 6) {
            //creamos el entrenamiento devuelve true si se crea correctamente,o false si no se crea.
            estado = crearEntrenamiento(usernameLogin, idtema);
            // si el entrenamiento se ha realizado con exito(estado=true) entonces  consultamos su idEntrenar
            idEntrena = obtenerIdEntrenamiento(estado);

            //si tiene un idEntrena diferente a 0 entonces crea una pregunta
            if (idEntrena != 0) {
                boolean stadoPrenguntEnt = crearPreguntaEntrenaTEST1(idEntrena);
                // si se crea la 1° preguntaEntrenamiento entonces ,obtner el dereccionamiento del test[1,2,ó 3]
                if (stadoPrenguntEnt) {
                    direcionar = direccionarTest(usernameLogin, idtema);
                }
                return direcionar;
            }
        } else {
            //mostrar mensaje para indicar que no existe fichas, para el entrenamiento
            RequestContext.getCurrentInstance().update("frmPresentarMensaje:panelPresentarMensaje");
            RequestContext.getCurrentInstance().execute("PF('dialogPresentarMensaje').show()");
        }
        //se debe mostraraun mensaje diciendo que no hay suficientes fichas del tema para realizar el entrenamiento.
        return direcionar;
    }

    public int sizeListafichaPorTema(int idtema) {
        List<Ficha> listFichas;
        int sizeListaFichaPregunta = 0;
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener lista de fichas por le Tema segun el entrenamiento
            DaoFicha daoficha = new DaoFicha();
            listFichas = daoficha.verListfichasPorTema(session, idtema);
            this.transaction.commit();

            //fijar el numero de fijas que se obtiene de la listFichas
            sizeListaFichaPregunta = listFichas.size();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL OBTENER LISTA DE FICHAS:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return sizeListaFichaPregunta;
    }

    public boolean crearEntrenamiento(String usernameLogin, int idtema) {
        boolean stateE = false;
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener un tema por su id y fijar en el entrenamiento.
            DaoTema daoTema = new DaoTema();
            Tema tema = daoTema.verPorCodigoTema(session, idtema);
            this.entrenar.setTema(tema);

            //obtener un usuario por su username, para obtener un estudiante y fijar en el entrenamiento
            DaoUsuario daoUsuario = new DaoUsuario();
            Usuario usuarioEstudiante = daoUsuario.verPorUsername(session, usernameLogin); //obtuvimos el usuario segun su username
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            Estudiante estudiante = daoEstudiante.verPorCodigoUsuario(session, usuarioEstudiante.getIdUsuario());//obtuvimos el estudiante  segun el id del usuario
            this.entrenar.setEstudiante(estudiante);

            //para crear entrenamiento
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            stateE = daoEntrenar.registrar(this.session, this.entrenar);
            this.transaction.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA ENTRENAMIENTO:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return stateE;
    }

    //obtner id entrenamimiento y fijar un atributo en -> beanSEntrena(beansSession )
    public int obtenerIdEntrenamiento(boolean state) {
        int idE = 0;
        if (state) {
            this.session = null;
            this.transaction = null;

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtner un entrenamiento(session,idest, idtema,tiempo, fecha) para obtner la identrena
            DaoEntrenar daoEntrena = new DaoEntrenar();
            Entrenamiento e = new Entrenamiento();
            try {
                e = daoEntrena.verEntrenamiento(session, entrenar.getEstudiante().getIdEst(), entrenar.getTema().getIdTema(), entrenar.getTiempo(), sqlDate);
            } catch (Exception ex) {
                System.out.println("problemas al consultar el entremaniento creado");
                Logger.getLogger(BeansREntrenamiento.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.transaction.commit();

            //fijamos en el beanSessin  del entrenamiento el atributo del idEntrena
//          this.beanSEntrena.setIdEntrenamiento(this.entrenar.getIdEntrena());
            this.beanSEntrena.iniciarEntrenamiento(e.getIdEntrena());
            idE = e.getIdEntrena();

            if (this.session != null) {
                this.session.close(); //cerrar sesion
            }
        }
        return idE;

    }

    //crea una pregunta de entrenamiento (para test1)si existe un entrenamiento.
    public boolean crearPreguntaEntrenaTEST1(int idEntrenar) {
        this.preguntaEnt = new Preguntaentrenar();

        boolean estado = false;
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            //obtener el entrenamiento mediante el id para fijar a las preguntas.
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            Entrenamiento entrene = daoEntrenar.verPorCodigoEntrenamiento(session, idEntrenar);
            this.preguntaEnt.setEntrenamiento(entrene);
            this.preguntaEnt.setIncorrecto(0);
            this.preguntaEnt.setValor(0);
            this.preguntaEnt.setFecha(sqlDate);

            DaoPreguntaEntrenar daoPregunta = new DaoPreguntaEntrenar();
            estado = daoPregunta.registrarPreguntaEnt(this.session, this.preguntaEnt);
            System.out.println("ingreso de la pregunta............................." + estado);
            //si la pregunta se creo correctamente lo fijamos el atributo idPrenguntaEnt en -> beanSEntrena(beansSession )
            if (estado) {
                obtenerIdPreguntaEntTEST1(session, estado, idEntrenar, sqlDate);
            }

            this.transaction.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA  PREGUNTAS DE entrenamiento:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return estado;
    }

    public void obtenerIdPreguntaEntTEST1(Session session, boolean state, int idEntrenar, Timestamp Fecha) {
        if (state) {
            //obtner un pregunta(session,idestrenar, idtema,fecha) para obtner la idPregunta del respectivo entrenamiento
            DaoPreguntaEntrenar daoPrengunt = new DaoPreguntaEntrenar();
            Preguntaentrenar e = new Preguntaentrenar();
            try {
                e = daoPrengunt.verPreguntaEntrenamiento(session, idEntrenar, Fecha);
            } catch (Exception ex) {
                System.out.println("problemas al consultar el pregunta creada recientemente");
                Logger.getLogger(BeansREntrenamiento.class.getName()).log(Level.SEVERE, null, ex);
            }

            //fijamos en el beanSessin(beanSEntrena) el atributo del idPrenguntaEnt(id de la preguntaEntrena)
            this.beanSEntrena.setIdPrenguntaEnt(e.getIdInt());
//            //numero de entrenamientos//fijamos cero por que no hay ningun entrenamiento
//            this.beanSEntrena.setNumEntrenamiento(0);
        }

    }

    public String direccionarTest(String usernameLogin, int idtema) {
        String direccionHTML = "aprenderFichasPregunta1";
        List<Entrenamiento> entrenamientos = obtenerListaEntrenEstTema(usernameLogin, idtema);
        //si hay mayores de 2 entrenamientos entoces obtener el nivel del entrenamiento
        if (entrenamientos.size() >= 2) {
            int posicion = entrenamientos.size() - 2;
            int posicion1 = entrenamientos.size() - 1;
            String nivel = entrenamientos.get(posicion).getNivel();
            switch (nivel) {
                case "facil":
                    direccionHTML = "aprenderFichasPregunta1";
                    break;
                case "medio":
                    direccionHTML = "aprenderFichasPregunta2";
                    break;
                case "dificil":
                    direccionHTML = "aprenderFichasPregunta3";
                    break;
                default:
                    direccionHTML = "aprenderFichasPregunta1";
                    break;
            }
            //metodo para actualizar entrenamiento para fijar el nivel del entrenamiento
            actualizarNivelEntrenamiento(nivel, entrenamientos.get(posicion1).getIdEntrena());
        }
        return direccionHTML;
    }

    //Obtenemos lista de entrenamientos del estudiante(segun el usernamelogin) y un tema
    public List<Entrenamiento> obtenerListaEntrenEstTema(String usernameLogin, int idtema) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //obtener un usuario por su username, para obtener un estudiante y obtener la lista de entrenamientos.
            DaoUsuario daoUsuario = new DaoUsuario();
            Usuario usuarioEstudiante = daoUsuario.verPorUsername(session, usernameLogin); //obtuvimos el usuario segun su username
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            Estudiante estudiante = daoEstudiante.verPorCodigoUsuario(session, usuarioEstudiante.getIdUsuario());//obtuvimos el estudiante  segun el id del usuario

            //para obtener la lista de entrenamientos entrenamiento
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            List<Entrenamiento> listaEntrenamiento = daoEntrenar.listEntrenaPorIdEstTema(session, estudiante.getIdEst(), idtema);
            this.transaction.commit();

            System.out.println("correcto al obtener la lsita de entrenamiento de un usuario");
            return listaEntrenamiento;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL REGISTRA ENTRENAMIENTO:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return null;
    }

    public void actualizarNivelEntrenamiento(String nivel, int idEntrena) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //para crear entrenamiento
            DaoEntrenar daoEntrenar = new DaoEntrenar();
            Entrenamiento entrenamiento = daoEntrenar.verPorCodigoEntrenamiento(session, idEntrena);
            entrenamiento.setNivel(nivel);
            daoEntrenar.actualizar(this.session, entrenamiento);
            this.transaction.commit();
            System.out.println("...................................................................................................");
            System.out.println("se actualiza el nivel del entrenamiento correctamente....... nivel:" + nivel + " idEntrenamiento:" + idEntrena);

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            System.out.println("ERROR AL actualizar el nivel del ENTRENAMIENTO:Contacte con el administrador" + ex.getMessage());
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Entrenamiento getEntrenar() {
        return entrenar;
    }

    public void setEntrenar(Entrenamiento entrenar) {
        this.entrenar = entrenar;
    }

    public BeanSEntrenar getBeanSEntrena() {
        return beanSEntrena;
    }

    public void setBeanSEntrena(BeanSEntrenar beanSEntrena) {
        this.beanSEntrena = beanSEntrena;
    }

    public Preguntaentrenar getPreguntaEnt() {
        return preguntaEnt;
    }

    public void setPreguntaEnt(Preguntaentrenar preguntaEnt) {
        this.preguntaEnt = preguntaEnt;
    }

}
