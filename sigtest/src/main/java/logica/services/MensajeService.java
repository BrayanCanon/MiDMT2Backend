/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controladores.MensajeJpaController;
import entidades.Mensaje;
import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class MensajeService {

    MensajeJpaController mensajelist;
    Gson gson = new Gson();

    public MensajeService() {
        mensajelist = new MensajeJpaController(Persistence.createEntityManagerFactory("DT2PU"));

    }

    public List mensajeLista() {
        List<Mensaje> salida = new ArrayList();
        List<Mensaje> lista = mensajelist.findMensajeEntities();
        for (int ab = 0; ab < lista.size(); ab++) {
            if (lista.get(ab).getEstado().equals("a")) {
                salida.add(lista.get(ab));
            }
        }
        return salida;
    }

    public void mensajeAdd(Mensaje a) throws Exception {
        mensajelist.create(a);

    }

    public void mensajeEdit(Mensaje a) throws Exception {
        mensajelist.edit(a);

    }

    public void mensajeEditB2(Mensaje a) throws Exception {
        a.setEstado("b");
        mensajelist.edit(a);

    }

    public void mensajeEdit(String jsonString) throws Exception {

        Mensaje mensaje = gson.fromJson(jsonString, Mensaje.class);
        mensajeEdit(mensaje);

    }

    public void mensajeDelete(String jsonString) throws Exception {
        JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
        int result = jobj.get("idMensaje").getAsInt();
        Mensaje x = mensajelistaId(result);
        x.setEstado("b");
        mensajeEdit(x);

    }

    public Mensaje mensajelistaId(int id) {
        Mensaje lo = mensajelist.findMensaje(id);
        if (lo.getEstado().equals("a")) {
            return lo;
        }
        return null;
    }

    public List<Mensaje> BuscarMensajeUsu(Usuario nom) {

        return mensajelist.BuscarMensajeUsu(nom);
    }

    public List<Mensaje> sqlbuscMensaje(Mensaje nom) {

        return mensajelist.sqlbuscMensaje(nom);
    }

    public List<Mensaje> BuscarMensajedest(String cod) {

        return mensajelist.BuscarMensajedest(cod);
    }

    public ArrayList consultachat(String us1, String us2) {
        return mensajelist.consultachat(us1, us2);

    }
    public String cantidadNotificaciones(String codigoDestinatario){
        return mensajelist.cantidadNotificaciones(codigoDestinatario);
    }
}
