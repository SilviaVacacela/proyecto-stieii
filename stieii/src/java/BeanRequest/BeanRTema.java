/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BeanRequest;


import Dao.DaoTema;
import Dao.DaoUnidadE;
import HibernateUtil.HibernateUtil;
import Pojo.Tema;
import Pojo.Unidadensenianza;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author KathyR
 */
@ManagedBean
@ViewScoped
public class BeanRTema {

    private Tema tema;
    private Unidadensenianza unidadensenianza;
    private List<Unidadensenianza> listaUnidadensenianza;
    private List<Tema> listaTemas;
    private List<Tema> listaTemaFiltrada;
    private String nombreImagen;
    private UploadedFile imagen;
    private Session session;
    private Transaction transaction;
    private UploadedFile csvFile;
    private String nombreArchivo;

    //constructor
    public BeanRTema() {
        this.tema = new Tema();
    }
    //    metodos para crear,actualizar y ver temas

    public void registrar(Unidadensenianza unidadE) {
        this.session = null;
        this.transaction = null;
        if (imagen.getSize() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar una imagen"));
            return;
        } else {
            try {

                Dao.DaoTema daoTema = new DaoTema();
                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
//                Tema t = daoTema.verPorTemaname(session, tema.getNombre());
//                if (t != null) {
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El nombre de Tema ya se encuentra registrado"));
//                    return;
//                }
                List<Tema> lista = daoTema.obtenerNombreTemasRepetidos(session, tema.getNombre(), unidadE.getId());
                if (!lista.isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El nombre de Tema ya se encuentra registrado"));
                    return;
                }

                boolean estadoImagen = false;
                //llamar al metodo para guardar la imagen
                try {
                    estadoImagen = actualizarImg();
                } catch (IOException ex) {
                    System.out.println("ERROR DE LECTURA ESCRITURA IMAGEN: Contacte con el administrador" + ex.getMessage());
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
                }
                if (estadoImagen) {
                    this.tema.setUnidadensenianza(unidadE);
                    this.tema.setImgTema(nombreImagen);
                    this.tema.setEstado(true);
                    daoTema.registrar(this.session, this.tema);
                    this.transaction.commit();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado con éxito"));

                    this.tema = new Tema();
                }

//            // Crear nodo Tema en la red bayesiana
//            CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
//            redBayesiana.crearTema(unidadE.getNombreUnidad(), tema.getNombre());
//            CrearBayesDynamic rbDynamic = new CrearBayesDynamic();
//            rbDynamic.crearRedTema(tema.getNombre());
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

    }

    public List<Tema> getTemasPorUnidad(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            List<Tema> t = daoTema.verPorUnidad(session, codigo);
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

    public void abrirDialogoCrearTema() {
        try {
            this.tema = new Tema();
            RequestContext.getCurrentInstance().update("frmCrearTema:panelCrearTema");
            RequestContext.getCurrentInstance().execute("PF('dialogCrearTema').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR TEMA CREAR:", "Contacte con el administrador" + ex.getMessage()));
        }
    }

    public void cargarTemaEditar(int codigo) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            this.tema = daoTema.verPorCodigoTema(session, codigo);

            RequestContext.getCurrentInstance().update("frmEditarTema:panelEditarTema");
            RequestContext.getCurrentInstance().execute("PF('dialogEditarTema').show()");
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR TEMA EDITAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void actualizar(Unidadensenianza unidadE) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();

            //ver nombre de temas repetidos
            Dao.DaoTema daoTema = new DaoTema();
            List<Tema> lista = daoTema.obtenerNombreTemasRepetidos(session, tema.getNombre(), unidadE.getId());
            this.transaction.commit();
            this.session.close();

            boolean estado = false;
            if (lista.isEmpty()) { //no hay ninguna temas con ese [nombre temas]

                estado = true;
            } else {
                //se realiza un for comprar si existe el tema(con el mismo nombre) para eliminarla de la lista
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getIdTema() == this.tema.getIdTema()) {
                        lista.remove(i);
                    }
                }
                //si la lista es vacia, quiere decir que no hay Nombre de temas repetidas
                if (lista.isEmpty()) {
                    estado = true;
                }
            }

            if (!estado) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El nombre del tema ya se encuentra registrada"));
                return;
            }
            //llamar al metodo para guardar la imagen
            boolean estadoImg = false;
            try {
                if (imagen.getSize() > 0) {
                    estadoImg = actualizarImg();
                }
            } catch (IOException ex) {
                System.out.println("ERROR DE LECTURA ESCRITURA IMAGEN: Contacte con el administrador" + ex.getMessage());
//                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
            }
            this.tema.setUnidadensenianza(unidadE);
            if (estadoImg) {
                this.tema.setImgTema(nombreImagen);
            }

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            daoTema.actualizar(this.session, this.tema);//actualiza la tema
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));

        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "Contacte con el administrador" + ex.getMessage()));
        } finally {
            if (this.session != null) {
                if (session.isOpen()) {
                    this.session.close();
                }
            }
//            this.tema = new Tema();
        }
    }
    //    public void actualizar() {
    //        this.session = null;
    //        this.transaction = null;
    //        try {
    //            
    //            
    //            if (imagen != null) {
    //                if (imagen.getSize() > 0) {
    //                    actualizarImg();
    //                }
    //            }
    //        } catch (IOException ex) {
    //            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR DE LECTURA ESCRITURA:", "Contacte con el administrador" + ex.getMessage()));
    //        }
    //        try {
    //            this.session = HibernateUtil.getSessionFactory().openSession();
    //            this.transaction = session.beginTransaction();
    //            
    //            DaoTema daoTema = new DaoTema();
    //            Tema temaAnterior = daoTema.verPorCodigoTema(session, tema.getIdTema());
    //            String nombreUnidad = temaAnterior.getUnidadensenianza().getNombreUnidad();
    //            String nombre = temaAnterior.getNombre();
    //            this.transaction.commit();
    //            this.session.close();
    //            temaAnterior = null;
    //
    //            // Crear nodo Tema en la red bayesiana
    //            if (!nombre.equals(tema.getNombre())) {
    //                this.session = HibernateUtil.getSessionFactory().openSession();
    //                this.transaction = session.beginTransaction();
    //                if (imagen.getSize() > 0) {
    //                    this.tema.setImgTema(nombreImagen);
    //                }
    //                daoTema.actualizar(this.session, this.tema);
    //                CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
    //                redBayesiana.editarTema(nombreUnidad, nombre, tema.getNombre());
    //                CrearBayesDynamic rbDynamic = new CrearBayesDynamic();
    //                rbDynamic.editarTema(nombre, tema.getNombre());
    //                this.transaction.commit();
    //            } else {
    //                this.session = HibernateUtil.getSessionFactory().openSession();
    //                this.transaction = session.beginTransaction();
    //                if (imagen.getSize() > 0) {
    //                    this.tema.setImgTema(nombreImagen);
    //                }
    //                daoTema.actualizar(this.session, this.tema);
    //                this.transaction.commit();
    //
    //            }
    //
    //            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
    //        } catch (Exception ex) {
    //            if (this.transaction != null) {
    //                this.transaction.rollback();
    //            }
    //            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR ACTUALIZAR:", "Contacte con el administrador" + ex.getMessage()));
    //        } finally {
    //            if (this.session != null) {
    //                if (session.isOpen()) {
    //                    this.session.close();
    //                }
    //            }
    //            this.tema = new Tema();
    //        }
    //    }

    public void eliminar() {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoTema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
//            Tema temaAnterior = daoTema.verPorCodigoTema(session, tema.getIdTema());
//            String nombreUnidad = temaAnterior.getUnidadensenianza().getNombreUnidad();
//
//            this.transaction.commit();
//            this.session.close();
//            temaAnterior = null;
//
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            daoTema.eliminar(this.session, this.tema);
//            // Eliminar nodo Tema en la red bayesiana
//            CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
//            redBayesiana.eliminarTema(nombreUnidad, tema.getNombre());
//            CrearBayesDynamic rbDynamic = new CrearBayesDynamic();
//            rbDynamic.eliminarTema(tema.getNombre());
            this.tema.setEstado(false);
            daoTema.actualizar(this.session, this.tema);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Tema eliminado correctamente."));
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL ELIMINAR:", "Contacte con el administrador, " + ex));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
//            this.tema = new Tema();
        }
    }

//    public void abrirDialogoVerConceptos(int codigo) {
//        this.session = null;
//        this.transaction = null;
//        try {
//            DaoTema daoTema = new DaoTema();
//            this.session = HibernateUtil.getSessionFactory().openSession();
//            this.transaction = session.beginTransaction();
//            this.tema = daoTema.verPorCodigoTema(session, codigo);
//            RequestContext.getCurrentInstance().update("frmVerConceptos:panelVerConceptos");
//            RequestContext.getCurrentInstance().execute("PF('dialogVerConceptos').show()");
//            this.transaction.commit();
////            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios se realizaron con éxito."));
//        } catch (Exception ex) {
//            if (this.transaction != null) {
//                this.transaction.rollback();
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR CARGAR CONCEPTO:", "Contacte con el administrador" + ex.getMessage()));
//        } finally {
//            if (this.session != null) {
//                this.session.close();
//            }
//        }
//    }
    public Tema consultarTemaPorNombre(String tema) {
        try {
            DaoTema daotema = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema t = daotema.verPorTemaname(session, tema);
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

    public List<Tema> getAllTema() {
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

    //    setter and getter de atributos
    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Unidadensenianza getUnidadensenianza() {
        return unidadensenianza;
    }

    public void setUnidadensenianza(Unidadensenianza unidadensenianza) {
        this.unidadensenianza = unidadensenianza;
    }

    public List<Tema> getListaTemas() {
        return listaTemas;
    }

    public void setListaTemas(List<Tema> listaTemas) {
        this.listaTemas = listaTemas;
    }

    //Recupera la lista de unidades de enseñanza de la BD
    public List<Unidadensenianza> getListaUnidadensenianza() {
        DaoUnidadE daoUnidad = new DaoUnidadE();
        List<Unidadensenianza> unidades = daoUnidad.verTodo();
        this.listaUnidadensenianza = unidades;
        return listaUnidadensenianza;
    }

    public void setListaUnidadensenianza(List<Unidadensenianza> listaUnidadensenianza) {
        this.listaUnidadensenianza = listaUnidadensenianza;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        this.nombreImagen = file.getFileName();
    }

    public boolean actualizarImg() throws IOException {
        boolean estado = true;
        InputStream inputS = null;
        OutputStream outputS = null;
        try {
            if (imagen.getSize() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar una imagen"));
                return false;
            }
            //controlar que el documento solo se imagen
            if (!(imagen.getContentType().equals("image/jpeg") || imagen.getContentType().equals("image/png"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar un archivo tipo imagen [.jpg /.png]"));
                return false;
            } else {
                inputS = this.imagen.getInputstream();
                ServletContext servletContex = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String imgTemas = (String) servletContex.getRealPath("/resources/imagen/imgTemas") + "/" + imagen.getFileName();

                int contador = 1;
                File f = new File(imgTemas);
                String nombre;
                String extension = ".png";
                int pos = imgTemas.lastIndexOf(".");
                if (pos == -1) {
                    nombre = imgTemas;
                } else {
                    nombre = imgTemas.substring(0, pos);
                    extension = imgTemas.substring(pos);
                }

                boolean bandera = true;
                do {
                    if (f.exists()) {
                        bandera = false;
                        imgTemas = nombre + "_" + contador + extension;
                        contador++;
                        f = new File(imgTemas);
                    } else {
                        bandera = true;
                    }
                } while (bandera == false);

                outputS = new FileOutputStream(f);

                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = inputS.read(bytes)) != -1) {
                    outputS.write(bytes, 0, read);
                }
                this.nombreImagen = f.getName();
                System.out.println("Correcto: Imagen de Tema actualizada correctamente.");
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Imagen de Tema actualizada correctamente."));
                return estado;

            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "ERROR AL GUARDAR IMAGEN:", "Contacte con el administrador, " + ex));
        } finally {
            if (inputS != null) {
                inputS.close();
            }
            if (outputS != null) {
                outputS.close();
            }
        }
        return estado;
    }

    public boolean deshabilitarBotonCrearPregunta() {
        if (this.tema.getNombre() != null) {
            return false;
        }
        return true;
    }

    public boolean deshabilitarBotonEliminarTema() {
        if (this.tema.getNombre() != null) {
            return false;
        }
        return true;
    }

//    public void limpiarFormulario() {
//        this.tema = new Tema();
//    }
    public List<Tema> getListaTemaFiltrada() {
        return listaTemaFiltrada;
    }

    public void setListaTemaFiltrada(List<Tema> listaTemaFiltrada) {
        this.listaTemaFiltrada = listaTemaFiltrada;
    }

    public Tema consultarTestPorCodigo(int idTest) {
        this.session = null;
        this.transaction = null;
        try {
            DaoTema daoUnidad = new DaoTema();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = session.beginTransaction();
            Tema t = daoUnidad.verPorCodigoTema(session, idTest);
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

    public void importarTemas() {
        ICsvMapReader mapReader = null;
        this.session = null;
        this.transaction = null;
        Dao.DaoTema daoTema = new DaoTema();
        DaoUnidadE daoUnidad = new DaoUnidadE();
        try {
            actualizarCSV();
            if (this.nombreArchivo == null) {
                return;
            }
            mapReader = new CsvMapReader(new FileReader(this.nombreArchivo), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            Map<String, Object> userMap = null;
            Tema t = null;
            while ((userMap = mapReader.read(header, processors)) != null) {
                // INSTRUCCIONES CREACIÓN

                this.session = HibernateUtil.getSessionFactory().openSession();
                this.transaction = session.beginTransaction();
                t = daoTema.verPorTemaname(session, (String) userMap.get("nombre"));
                if (t != null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al importar.", "Tema duplicado: " + t.getNombre()));
                    return;
                } else {
                    t = new Tema();
                }
                t.setNombre((String) userMap.get("nombre"));
                t.setVocabulario((String) userMap.get("vocabulario"));
                t.setObjetivo((String) userMap.get("objetivo"));
                t.setDominio((String) userMap.get("dominio"));
                t.setImgTema((String) userMap.get("imgTema"));
                t.setEstado((boolean) userMap.get("estado"));
                t.setUnidadensenianza(daoUnidad.verPorNombreUnidad(session, (String) userMap.get("unidadensenianza")));
                daoTema.registrar(this.session, t);

//                // Crear nodo Tema en la red bayesiana
//                CrearBayesNetwork1 redBayesiana = new CrearBayesNetwork1();
//                redBayesiana.crearTema(t.getUnidadensenianza().getNombreUnidad(), t.getNombre());
//                CrearBayesDynamic rbDynamic = new CrearBayesDynamic();
//                rbDynamic.crearRedTema(t.getNombre());
                this.transaction.commit();
                t = new Tema();
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
            new NotNull(), // unidadensenianza
            new NotNull(), // nombre
            new NotNull(), // vocabulario
            new NotNull(), // objetivo
            new NotNull(), // dominio
            new NotNull(), // imgTema
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

    public void actualizarCSV() throws IOException {
        InputStream inputS = null;
        OutputStream outputS = null;
        try {
            if (this.csvFile.getSize() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "Debe seleccionar un archivo csv"));
                return;
            }
            inputS = this.csvFile.getInputstream();
            ServletContext servletContex = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String imgTemas = (String) servletContex.getRealPath("/resources") + "/" + this.csvFile.getFileName();
            String nombre = "";
            String extension = "";
            int pos = imgTemas.lastIndexOf(".");
            if (pos == -1) {
                nombre = imgTemas;
            } else {
                nombre = imgTemas.substring(0, pos);
                extension = imgTemas.substring(pos);
            }
            if (!extension.equals(".csv")) {
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

}
