/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entidades.DTO.VerficacionApoyoSocialDTO;
import entidades.DTO.VerificacionRutinaDTO;
import entidades.Verificacion;
import java.lang.reflect.Type;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import logica.services.VerificacionPacienteService;
import logica.services.VerificacionService;

/**
 * TO-DO
 *
 * @author Charly
 */
@WebService(serviceName = "VerificacionRutinaWS")
public class VerificacionPacienteWS {

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar - 29/04/2019</p>
     *
     * @param codigoPaciente
     * @param idRutina
     * @return
     */
    @WebMethod(operationName = "consultarVerificacionRutinaPaciente")
    public String consultarVerificacionRutinaPaciente(@WebParam(name = "codPaciente") String codigoPaciente, @WebParam(name = "idRutina") Integer idRutina) {
        VerificacionPacienteService verificacionPacienteService = new VerificacionPacienteService();
        Type listType = new TypeToken<List<VerificacionRutinaDTO>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(verificacionPacienteService.consultaMisionPaciente(codigoPaciente, idRutina), listType);
        return json;
    }

    /**
     * <p>
     * Carlos D. Escobar - 04/05/2019</p>
     *
     * @param codigoPaciente
     * @param idRutina
     * @return
     */
    @WebMethod(operationName = "consultarVerificacionRutinaPacienteValores")
    public String consultarVerificacionRutinaPacienteValores(@WebParam(name = "codPaciente") String codigoPaciente, @WebParam(name = "idRutina") Integer idRutina) {
        VerificacionPacienteService verificacionPacienteService = new VerificacionPacienteService();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(verificacionPacienteService.consultaMisionPacienteValores(codigoPaciente, idRutina), listType);
        return json;
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar - 03/05/2019</p>
     *
     * @param codigoPaciente
     * @param idRutina
     * @param valor
     * @return
     */
    @WebMethod(operationName = "crearVerificacionPaciente")
    public String crearVerificacionPaciente(@WebParam(name = "codPaciente") String codigoPaciente, @WebParam(name = "idRutina") Integer idRutina, @WebParam(name = "valor") String valor) {
        VerificacionPacienteService verificacionPacienteService = new VerificacionPacienteService();

        String mensaje = verificacionPacienteService.insertarVerificacionPaciente(idRutina, codigoPaciente, valor);
        return mensaje;
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar - 03/05/2019
     * </p>
     *
     * @param codigoApoyoSocial
     * @return
     */
    @WebMethod(operationName = "consultarVerificacionesApoyoSocial")
    public String consultarVerificacionesApoyoSocial(@WebParam(name = "codigoApoyoSocial") String codigoApoyoSocial) {
        VerificacionService verificacionService = new VerificacionService();
        Type listType = new TypeToken<List<VerficacionApoyoSocialDTO>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(verificacionService.consultarVerificacionesApoyoSocial(codigoApoyoSocial), listType);
        return json;
    }

    @WebMethod(operationName = "actualizarVerificacionApoyoSocial")
    public String actualizarVerificacionApoyoSocial(@WebParam(name = "idVerificacion") int idVerificacion, @WebParam(name = "verificado") String verificado) {
        try {
            VerificacionService verificacionService = new VerificacionService();
            Verificacion v = new Verificacion(idVerificacion);
            v.setVerifApoyoSocial(verificado.equals("1")); 
            verificacionService.verificacionConfirmacionAPoyoSocial(v);
            return "Actualizado";
        } catch (Exception ex) {
            return "Ha ocurrido un error";
        }
    }

}
