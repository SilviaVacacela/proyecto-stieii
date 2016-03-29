/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;

import Clases.Encrypt;
import Dao.DaoAdministrador;
import Dao.DaoConcepto;
import Dao.DaoEstudiante;
import Dao.DaoResultado;
import Dao.DaoRol;
import Dao.DaoUnidadE;
import Dao.DaoUsuario;
import HibernateUtil.HibernateUtil;
import Pojo.Administrador;
import Pojo.Concepto;
import Pojo.Estudiante;
import Pojo.Resultado;
import Pojo.Rol;
import Pojo.Unidadensenianza;
import Pojo.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import weka.core.logging.OutputLogger;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRUsuario {

    /**
     * Creates a new instance of BeanRUsuario
     */
    private Usuario usuario;
    private Rol rol;
    private List<Usuario> listaUsuarios;
    private List<Usuario> listaUsuarioFiltrado;
    private List<Rol> listaRoles;
    private String txtPasswordRepita;
    private String txtPassword;
    private Session session;
    private Transaction transaction;
    private boolean establecerPass;
    private UploadedFile csvFile;
    private String nombreArchivo;

    public BeanRUsuario() {
        this.usuario = new Usuario();
        this.usuario.setEstado(true);
        this.usuario.setGenero(true);
        this.establecerPass = false;
        this.usuario.setPassword("");
        this.txtPasswordRepita = "";
    }

    //Ingresa un nuevo Usuario a la BD
    public void registrar() {
        this.session = null;
        this.transaction = null;
        try {
            if (!this.txtPassword.equals(this.txtPasswordRepita)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Las contraseñas no coinciden"));
                return;
            }
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Usuario u = daoUsuario.verPorUsername(session, usuario.getUsername());
            if (u != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El nombre de usuario ya se encuentra registrado"));
                return;
            }
            this.usuario.setPassword(Encrypt.sha512(this.txtPassword));
            daoUsuario.registrar(this.session, this.usuario);
            if (this.usuario.getRol().getTipo().equals("Administrador")) {
                Administrador admin = new Administrador();
                admin.setUsuario(daoUsuario.verPorUsername(session, usuario.getUsername()));
                DaoAdministrador daoAdmin = new DaoAdministrador();
                daoAdmin.registrar(session, admin);
            } else {
                if (this.usuario.getRol().getTipo().equals("Estudiante")) {
                    Estudiante est = new Estudiante();
                    est.setUsuario(daoUsuario.verPorUsername(session, usuario.getUsername()));
                    DaoUnidadE daoUnidad = new DaoUnidadE();
                    Unidadensenianza unidadE = daoUnidad.verPorNombreUnidad(session, "Basic Unit (Top Tip 2)"); // Nombre de la unidad estándar
                    est.setUnidadensenianza(unidadE);
                    DaoEstudiante daoEst = new DaoEstudiante();
                    daoEst.registrar(session, est);
//                    // Agregar registro resultados
//                    Estudiante estRegistrado = daoEst.verPorCodigoUsuario(session, daoUsuario.verPorUsername(session, usuario.getUsername()).getIdUsuario());
//                    DaoConcepto daoConcepto = new DaoConcepto();
//                    List<Concepto> lstConceptos = daoConcepto.verTodo(session); // posteriormente cambiar a verPorUnidadEnsenianza
//                    List<Resultado> lstResultados = new ArrayList<>();
//                    Resultado r = new Resultado();
//                    for (int i = 0; i < lstConceptos.size(); i++) {
//                        r.setEstudiante(estRegistrado);
//                        r.setConcepto(lstConceptos.get(i));
//                        r.setValor(0.05);
//                        lstResultados.add(r);
//                        r = new Resultado();
//                    }
//                    DaoResultado daoResultado = new DaoResultado();
//                    daoResultado.registrarVarios(session, lstResultados);
                }
            }

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

            this.usuario = new Usuario();
            this.usuario.setEstado(true);
            this.usuario.setGenero(true);
            this.establecerPass = true;
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            if (this.txtPassword == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENCIÓN:", "Se necesita establecer una contraseña"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR REGISTRO:", "Contacte con el administrador" + ex.getMessage()));
            }
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
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            if (this.establecerPass) {
                if (!this.txtPassword.equals(this.txtPasswordRepita)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Las contraseñas no coinciden"));
                    return;
                }
                this.usuario.setPassword(Encrypt.sha512(this.txtPassword));
            }
            daoUsuario.actualizar(this.session, this.usuario);
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

    //Recupera toda la lista de usuarios de la BD
    public List<Usuario> getAllUsuario() {
        this.session = null;
        this.transaction = null;
        try {
            DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.listaUsuarios = daoUsuario.verTodo(this.session);
            this.transaction.commit();
            return this.listaUsuarios;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR RECUPERAR USUARIOS:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    //Recuperar el usuario que ha iniciado sesión a través del BeanSLogin
    public Usuario getPorUsername() {
        this.session = null;
        this.transaction = null;

        try {
            DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            HttpSession sesionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            this.usuario = daoUsuario.verUsuarioLogeado(session, sesionUsuario.getAttribute("usernameLogin").toString());
            this.transaction.commit();
            return this.usuario;

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR OBTENER POR USERNAME:", "Contacte con el administrador" + ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void cargarUsuarioEditar(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.usuario = daoUsuario.verPorCodigoUsuario(session, codigo);

            //Para cargar los datos en el panelEditarUsuario del formulario frmEditarUsuario
            RequestContext.getCurrentInstance().update("frmEditarUsuario:panelEditarUsuario");

            //Para mostrar el diálogo que contiene los datos del usuario con el widgetVar: dialogEditarUsuario
            RequestContext.getCurrentInstance().execute("PF('dialogEditarUsuario').show()");
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR USUARIO EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void cargarUsuarioDesactivar(int codigo) {
        this.usuario = new Usuario();
        this.usuario.setEstado(true);
        this.session = null;
        this.transaction = null;
        try {
            Dao.DaoUsuario daoUsuario = new DaoUsuario();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.usuario = daoUsuario.verPorCodigoUsuario(session, codigo);
            System.out.println(this.usuario.toString());
            if (this.usuario.isEstado()) {
                this.usuario.setEstado(false);
            } else {
                this.usuario.setEstado(true);
            }

            daoUsuario.actualizar(this.session, this.usuario);

            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Estado modificado con éxito."));
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

    }

    public void importarUsuarios() {
        ICsvMapReader mapReader = null;
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        this.session = null;
        this.transaction = null;
        Dao.DaoUsuario daoUsuario = new DaoUsuario();
        DaoAdministrador daoAdmin = new DaoAdministrador();
        DaoUnidadE daoUnidad = new DaoUnidadE();
        DaoEstudiante daoEst = new DaoEstudiante();
        DaoConcepto daoConcepto = new DaoConcepto();
        DaoResultado daoResultado = new DaoResultado();
        try {
            actualizarCSV();
            if(this.nombreArchivo==null){
                return;
            }
            mapReader = new CsvMapReader(new FileReader(this.nombreArchivo), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            Map<String, Object> userMap = null;
            Usuario user = new Usuario();
            Administrador admin = null;
            Estudiante est = null;
            Resultado r = null;
            List<Concepto> lstConceptos; // posteriormente cambiar a verPorUnidadEnsenianza
            List<Resultado> lstResultados;
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Unidadensenianza unidadE = daoUnidad.verPorNombreUnidad(session, "Unidad Básica"); // Nombre de la unidad estándar
            this.transaction.commit();
            this.session.close();
            while ((userMap = mapReader.read(header, processors)) != null) {
                // INSTRUCCIONES CREACIÓN
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
                user = daoUsuario.verPorUsername(session, (String)userMap.get("username"));
                if (user != null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al importar.", "Username duplicado: " + user.getUsername()));
                    return;
                }else{
                    user = new Usuario();
                }
                user.setNombre((String)userMap.get("nombre").toString());
                user.setApellido((String)userMap.get("apellido"));
                user.setFechaNacimiento(formatoDelTexto.parse((String) userMap.get("fechaNacimiento")));
                user.setGenero((boolean) userMap.get("genero"));
                user.setUsername((String) userMap.get("username"));
                user.setPassword(Encrypt.sha512((String) userMap.get("password")));
                user.setEstado((boolean) userMap.get("estado"));
                user.setRol(new DaoRol().verPorTipoRol(session, (String)userMap.get("rol")));
                daoUsuario.registrar(this.session, user);
                if (((String) userMap.get("rol")).equals("Administrador")) {
                    admin = new Administrador();
                    admin.setUsuario(daoUsuario.verPorUsername(session, user.getUsername()));
                    daoAdmin.registrar(session, admin);
                } else {
                    est = new Estudiante();
                    est.setUsuario(daoUsuario.verPorUsername(session, user.getUsername()));
                    est.setUnidadensenianza(unidadE);
                    daoEst.registrar(session, est);
                    // Agregar registro resultados
                    Estudiante estRegistrado = daoEst.verPorCodigoUsuario(session, daoUsuario.verPorUsername(session, user.getUsername()).getIdUsuario());
                    lstConceptos = daoConcepto.verTodo(session); // posteriormente cambiar a verPorUnidadEnsenianza
                    lstResultados = new ArrayList<>();
                    for (int i = 0; i < lstConceptos.size(); i++) {
                        r = new Resultado();
                        r.setEstudiante(estRegistrado);
                        r.setConcepto(lstConceptos.get(i));
                        r.setValor(0.05);
                        lstResultados.add(r);
                    }
                    daoResultado.registrarVarios(session, lstResultados);
                }
                this.transaction.commit();
                user = new Usuario();
            }
            mapReader.close();
            File archivoCsvTemp = new File(this.nombreArchivo);
            archivoCsvTemp.delete();
            this.nombreArchivo = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Datos importados correctamente."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                if (transaction.isInitiator()) {
                    this.transaction.rollback();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El archivo no corresponde a la plantilla."));
        } finally {
            if (this.session != null) {
                if (this.session.isOpen()) {
                    this.session.close();
                }
            }
        }
    }

    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[]{
            new NotNull(), // rol
            new NotNull(), // nombre
            new NotNull(), // apellido
            new NotNull(), // fechaNacimiento
            new NotNull(new ParseBool()), // genero
            new NotNull(), // username
            new NotNull(), // password
            new NotNull(new ParseBool()), // estado
        };

        return processors;
    }

    public UploadedFile getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(UploadedFile csvFile) {
        this.csvFile = csvFile;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        this.nombreArchivo = file.getFileName();
    }

    public void actualizarCSV() throws IOException {
        InputStream inputS = null;
        OutputStream outputS = null;
        try {
            if (this.csvFile.getSize() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar una imagen"));
                return;
            }
            inputS = this.csvFile.getInputstream();
            ServletContext servletContex = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String imgTemas = (String) servletContex.getRealPath("/resources") + "/" + this.csvFile.getFileName();
            String nombre="";
            String extension="";
            int pos = imgTemas.lastIndexOf(".");
            if (pos == -1) {
                nombre = imgTemas;
            } else {
                nombre = imgTemas.substring(0, pos);
                extension = imgTemas.substring(pos);
            }
            if(!extension.equals(".csv")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL CARGAR ARCHIVO:", "La extensión debe ser csv."));
                return;
            }
            outputS = new FileOutputStream(imgTemas);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputS.read(bytes)) != -1) {
                outputS.write(bytes, 0, read);
            }
            this.nombreArchivo = imgTemas;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Datos importados correctamente."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL CARGAR ARCHIVO:", "Contacte con el administrador, " + ex));
        } finally {
            if (inputS != null) {
                inputS.close();
            }
            if (outputS != null) {
                outputS.close();
            }
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public String getTxtPasswordRepita() {
        return txtPasswordRepita;
    }

    public void setTxtPasswordRepita(String txtPasswordRepita) {
        this.txtPasswordRepita = txtPasswordRepita;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    //Recupera la lista de Roles de la BD
    public List<Rol> getListaRoles() {
        DaoRol daoRol = new DaoRol();
        List<Rol> roles = daoRol.verTodo();
        listaRoles = roles;
        return listaRoles;
    }

    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Usuario> getListaUsuarioFiltrado() {
        return listaUsuarioFiltrado;
    }

    public void setListaUsuarioFiltrado(List<Usuario> listaUsuarioFiltrado) {
        this.listaUsuarioFiltrado = listaUsuarioFiltrado;
    }

    public boolean isEstablecerPass() {
        return establecerPass;
    }

    public void setEstablecerPass(boolean establecerPass) {
        this.establecerPass = establecerPass;
    }

    public void limpiarFormulario() {
        this.usuario = new Usuario();
        this.usuario.setEstado(true);
        this.usuario.setGenero(true);
        this.txtPassword = "";
        this.txtPasswordRepita = "";
        this.establecerPass = false;
    }

    public boolean deshabilitarBotonCrear() {
        if (this.usuario.getIdUsuario() != 0) {
            return true;
        }
        return false;
    }

    public boolean deshabilitarBotonEstado() {
        if (this.usuario.getIdUsuario() != 0) {
            HttpSession sesionUsuario = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            if (this.usuario.getUsername().equals(sesionUsuario.getAttribute("usernameLogin").toString())) {
                return true;
            }
            return false;
        }
        return true;
    }

    public void cambiarDatosPass() {
        this.establecerPass = false;
    }

    public String getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(String txtPassword) {
        this.txtPassword = txtPassword;
    }
}
