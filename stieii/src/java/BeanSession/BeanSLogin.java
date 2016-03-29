/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanSession;

import Clases.Encrypt;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author KathyR
 */
@ManagedBean
@SessionScoped
public class BeanSLogin {

    private Session session;
    private Transaction transaction;

    private String usernameLogin;
    private String passwordLogin;

    /**
     * Creates a new instance of BeanSLogin
     */
    public BeanSLogin() {
        HttpSession miSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        miSession.setMaxInactiveInterval(5000);
    }

    public String login() {
        this.session = null;
        this.transaction = null;

        try {
            DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Usuario usuarioLogeado = daoUsuario.verPorUsername(session, usernameLogin);

            if (usuarioLogeado != null) {
                if (usuarioLogeado.getPassword().equals(Encrypt.sha512(passwordLogin))) {
                    HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    httpSession.setAttribute("usernameLogin", this.usernameLogin);
                    //USUARIO ACTIVO
                    if (usuarioLogeado.isEstado() == true) {
                        if (usuarioLogeado.getRol().getTipo().equals("Estudiante")) {
                            return "/estudiante/estudianteS/inicioAprendizaje?faces-redirect=true";

                        }
                        return "/admin/buscar?faces-redirect=true";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Contacte con el administrador: Usuario desactivado"));
                        return "/index";
                    }
                }
            }
            this.transaction.commit();
            this.passwordLogin = null;
            this.usernameLogin = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Usuario o contraseña incorrecta"));
            return "/index";

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

    public String logout() {
        this.usernameLogin = null;
        this.passwordLogin = null;
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.invalidate();

        return "/index?faces-redirect=true";
    }

    public String getUsernameLogin() {
        return usernameLogin;
    }

    public void setUsernameLogin(String usernameLogin) {
        this.usernameLogin = usernameLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin;
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }

}
