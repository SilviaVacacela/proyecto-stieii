/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Dao.DaoRol;

import HibernateUtil.HibernateUtil;
import Pojo.Rol;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KathyR
 */
@ManagedBean
@RequestScoped
public class BeanRRol {

    /**
     * Creates a new instance of BeanRUsuario
     */
    private Rol rol;
    private List<Rol> listaRoles;
    private Session session;
    private Transaction transaction;

    public BeanRRol() {
        
    }

    public void registrar() {
        this.session = null;
        this.transaction = null;

        try {
            Dao.DaoRol daoRol = new DaoRol();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            // COMPROBACIÓN
            daoRol.registrar(this.session, this.rol);

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

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
    
    // Recuperar un determinado Rol
    public Rol consultarRolPorTipo(String tipo){
        try{
        DaoRol daoRol=new DaoRol();
            this.session=HibernateUtil.getSessionFactory().openSession();
            this.transaction=session.beginTransaction();
            Rol r=daoRol.verPorTipoRol(session, tipo);
            transaction.commit();
            return r;
            
        }catch (Exception ex) {
            if(this.transaction!=null){
                this.transaction.rollback();
            }
            return null;
        }finally{
            if(this.session!=null){
                this.session.close();
            }
        }
    }
    
   
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Rol> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    
    

}
