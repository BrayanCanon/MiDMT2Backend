/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.RutinaPacienteJpaController;
import controladores.VerificacioPacienteJpaController;
import entidades.DTO.VerificacionRutinaDTO;
import entidades.VerificacionRutinaPaciente;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author carlos.escobar
 */
public class VerificacionPacienteService {

    private VerificacioPacienteJpaController verificacionJpaController;
    private RutinaPacienteJpaController rutinaPacienteJpaController;

    public VerificacionPacienteService() {
        verificacionJpaController = new VerificacioPacienteJpaController(Persistence.createEntityManagerFactory("DT2PU"));
        rutinaPacienteJpaController = new RutinaPacienteJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar -29-04-2019</p>
     *
     * @param codPaciente
     * @param idRutina
     * @return
     */
    public List<VerificacionRutinaDTO> consultaMisionPaciente(String codPaciente, Integer idRutina) {
        List<VerificacionRutinaDTO> listadoDTO = new ArrayList<>();
        List<VerificacionRutinaPaciente> listado = new ArrayList(verificacionJpaController.consultaRutinaPaciente(codPaciente, idRutina));
        for (VerificacionRutinaPaciente verificacionRutinaPaciente : listado) {
            listadoDTO.add(new VerificacionRutinaDTO(verificacionRutinaPaciente));
        }
        return listadoDTO;
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar -29-04-2019</p>
     *
     * @param codPaciente
     * @param idRutina
     * @return
     */
    public List<String> consultaMisionPacienteValores(String codPaciente, Integer idRutina) {
        List<String> listadoDTO = new ArrayList<>();
        List<VerificacionRutinaPaciente> listado = new ArrayList(verificacionJpaController.consultaRutinaPaciente(codPaciente, idRutina));
        for (VerificacionRutinaPaciente verificacionRutinaPaciente : listado) {
            listadoDTO.add(verificacionRutinaPaciente.getValor());
        }
        return listadoDTO;
    }

    /**
     * TO-DO
     * <p>
     * Carlos D. Escobar -04-05-2019</p>
     *
     * @param idRutina
     * @param codPaciente
     * @param valor
     * @return
     */
    public String insertarVerificacionPaciente(Integer idRutina, String codPaciente, String valor) {
        VerificacionRutinaPaciente veri = new VerificacionRutinaPaciente();
        List<VerificacionRutinaPaciente> listado = new ArrayList(verificacionJpaController.consultaRutinaPaciente(codPaciente, idRutina));
        boolean yaFueTomado = false;
        LocalDate fechaConsulta = LocalDate.from(Instant.ofEpochMilli(listado.get(listado.size()-1).getFecha().getTime()).atZone(ZoneId.systemDefault()));
        LocalDate fechaActual = LocalDate.now();
        
        if (fechaActual.equals(fechaConsulta)) {
            yaFueTomado = true;
        }
        if (yaFueTomado) {
            return "El registro ya fue reportado";
        } else {
            veri.setIdRutinaPaciente(rutinaPacienteJpaController.consultarRutinaPacientePorId(idRutina, codPaciente));
            veri.setValor(valor);
            veri.setFecha(Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            verificacionJpaController.create(veri);
            return "Insertado";

        }

    }

}
