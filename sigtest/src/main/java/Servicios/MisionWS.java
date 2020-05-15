/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import entidades.DTO.NivelDTO;
import com.google.gson.Gson;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import logica.services.NivelService;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author Chaly
 */
@WebService(serviceName = "MisionWS")
public class MisionWS {

    /**
     * TO-DO
     * @param codPaciente
     * @return
     */
    @WebMethod(operationName = "ConsultaMisonPaciente")
    public String prueba(@WebParam(name = "codPaciente") String codPaciente) {
        NivelService nivelService = new NivelService();
        Type listType = new TypeToken<List<NivelDTO>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(nivelService.consultarListadoMisionesPorPaciente(codPaciente), listType);
        return json;
    }

}
