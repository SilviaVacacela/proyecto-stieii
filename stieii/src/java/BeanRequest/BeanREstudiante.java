/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.Encrypt;
import Dao.DaoEstudiante;
import Dao.DaoUnidadE;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Estudiante;
import Pojo.Unidadensenianza;
import Pojo.Usuario;
import java.util.ArrayList;
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
 * @author USUARIO
 */
@ManagedBean
@ViewScoped
public class BeanREstudiante {

    private Session session;
    private Transaction transaction;

    //Para obtner estudiantes respecto a esa unidad
    private Unidadensenianza unidadE;
    private List<Unidadensenianza> listaUnidades; //inicalizado en método(ObtnerListaUnidades)

    //obtener estudiante de la unidad selecionada
    private Estudiante estudianteSeleccionado;
    private List<Estudiante> listaEstudiantes;
    private List<Estudiante> listaEstudiantesPorUnidad;

    private List<Estudiante> listaEstudiantesFiltrada;

    public BeanREstudiante() {
        estudianteSeleccionado = new Estudiante();
    }

    //obtniene la lista de estudiantes con respecto a la unidad elegida en XHTML
    public List<Estudiante> getListaALLEstudiantes1() {
        //metodo para obtener todos los estudiantes
//        ListaALLEstudiantes();

        System.out.println("el tamaño de la lista de todos los estudiantes es :" + listaEstudiantesPorUnidad.size());
        int tamano = listaEstudiantesPorUnidad.size();
        Usuario u = new Usuario();
        for (int i = 0; i < tamano; i++) {
            System.out.println("......................... " + i);
////            System.out.println(listaEstudiantesPorUnidad.get(i).getUsuario().getNombre());
//            u=obtnerUsuario(listaEstudiantes.get(i).getIdEst());
//            if ((u.isEstado() == false)) {
//                listaEstudiantesPorUnidad.remove(i);
//            }
////            if (
////                    
////                    (listaEstudiantes.get(i).getUsuario().isEstado() == false) & (listaEstudiantes.get(i).getUsuario().getRol().getTipo().equals("Estudiante"))) {
////                System.out.println(" eliminamos........" + listaEstudiantes.get(i).getUsuario().getNombre());
////                listaEstudiantesPorUnidad.remove(listaEstudiantesPorUnidad.get(i));
////            }

        }
        System.out.println("el tamaño de la lista de todos los estudiantes es fin :" + listaEstudiantesPorUnidad.size());
        return listaEstudiantesPorUnidad;
    }

    public List<Estudiante> getListaALLEstudiantes() {
        ArrayList listaNumero = new ArrayList();
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoEstudiante daoEstudiante = new DaoEstudiante();
            this.listaEstudiantesPorUnidad = daoEstudiante.verTodo(session);
//            listaEstudiantes=listaEstudiantesPorUnidad;
            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener lista de estudiantes por unidad:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
        System.out.println(".............. numero de elementos" + listaEstudiantesPorUnidad.size());
        int tam = listaEstudiantesPorUnidad.size();
        for (int i = 0; i < tam; i++) {
            System.out.println("......." + listaEstudiantesPorUnidad.get(i).getUsuario().getIdUsuario());
            if ((listaEstudiantesPorUnidad.get(i).getUsuario().isEstado() == true) && (listaEstudiantesPorUnidad.get(i).getUsuario().getRol().getTipo().equals("Estudiante"))) {
                listaNumero.add(listaEstudiantesPorUnidad.get(i));
            }

        }
        listaEstudiantes = listaNumero;
        System.out.println(".............. numero de elementos" + listaEstudiantes);

        return listaEstudiantes;
    }

    public Usuario obtnerUsuario(int codigo) {
        Usuario usuario = new Usuario();
        this.session = null;
        this.transaction = null;
        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            usuario = daoUsuario.verPorCodigoUsuario(session, codigo);

            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            System.out.println("error al obtner al usuario Contacte con el administrador" + ex.getMessage());
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        return usuario;
    }

    //Metodo para obtner la lista de Unidades
    public List<Unidadensenianza> obtnerListaUnidades() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            DaoUnidadE daoUnidades = new DaoUnidadE();
            //obtiene la lista de unidades del dao

            this.listaUnidades = daoUnidades.verTodo(session);
            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR al obtener lista de estudiantes por unidad:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
        }
        return listaUnidades;
    }

    public void actualizar() {
        this.session = null;
        this.transaction = null;
        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Dao.DaoUsuario daoUsuario = new DaoUsuario();

            //obtiene al estudiante seleccionado para modificar la informacion del usuario
            Usuario usuario = daoUsuario.verPorUsername(session, estudianteSeleccionado.getUsuario().getUsername());
            usuario.setNombre(estudianteSeleccionado.getUsuario().getNombre());
            usuario.setApellido(estudianteSeleccionado.getUsuario().getApellido());
            usuario.setGenero(estudianteSeleccionado.getUsuario().isGenero());
            usuario.setFechaNacimiento(estudianteSeleccionado.getUsuario().getFechaNacimiento());
            daoUsuario.actualizar(this.session, usuario);

            DaoEstudiante daoEstudiante = new DaoEstudiante();
            daoEstudiante.actualizar(this.session, this.estudianteSeleccionado);
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

    public boolean deshabilitarBotonModif() {
        if (this.estudianteSeleccionado.getIdEst() != 0) {
            return true;
        }
        return false;
    }
//    public boolean deshabilitarBotonCrear() {
//        if (this.usuario.getIdUsuario() != 0) {
//            return true;
//        }
//        return false;
//    }

    public void cargarUsuarioDesactivar(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Usuario usuario = daoUsuario.verPorCodigoUsuario(session, codigo);
            System.out.println(usuario.toString());
            if (usuario.isEstado()) {
                usuario.setEstado(false);
            }

            daoUsuario.actualizar(this.session, usuario);

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El proceso de eliminación  ha terminado con éxito"));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DESACTIVAR USUARIO:", "Contacte con el administrador, " + ex));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
        limpiarFormulario();

    }

    public void limpiarFormulario() {

        this.estudianteSeleccionado.setIdEst(0);
        this.estudianteSeleccionado.getUsuario().setUsername("");
        this.estudianteSeleccionado.getUsuario().setNombre("");
        this.estudianteSeleccionado.getUsuario().setApellido("");
        this.estudianteSeleccionado.getUsuario().setGenero(true);
        this.estudianteSeleccionado.getUsuario().setFechaNacimiento(null);

    }
//....................................................................

    public Estudiante getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public List<Estudiante> getListaEstudiantes() {
        return listaEstudiantes;
    }

    public void setListaEstudiantes(List<Estudiante> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public List<Estudiante> getListaEstudiantesFiltrada() {
        return listaEstudiantesFiltrada;
    }

    public void setListaEstudiantesFiltrada(List<Estudiante> listaEstudiantesFiltrada) {
        this.listaEstudiantesFiltrada = listaEstudiantesFiltrada;
    }

    public Unidadensenianza getUnidadE() {
        return unidadE;
    }

    public void setUnidadE(Unidadensenianza unidadE) {
        this.unidadE = unidadE;
    }

    public List<Unidadensenianza> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<Unidadensenianza> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

}
