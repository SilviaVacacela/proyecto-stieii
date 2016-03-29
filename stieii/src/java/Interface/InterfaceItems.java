/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Pojo.Item;
import Pojo.Pregunta;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author KathyR
 */
public interface InterfaceItems {
    
    public boolean registrar(Session session, Item item) throws Exception;
    public List<Item> verTodo(Session session) throws Exception;
    public List<Item> verPorPregunta(Session session, int pregunta) throws Exception;
    public Item verPorPREGNombreItem(Session session, String nombreItem, int preguntaItem) throws Exception;
    public Item verPorCodigoItem(Session session, int idItem) throws Exception;
    public boolean actualizar(Session session, Item item) throws Exception;
    public boolean eliminar(Session session, Item item) throws Exception;
}
