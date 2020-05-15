/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.services;

import controladores.VerificacionJpaController;
import entidades.DTO.VerficacionApoyoSocialDTO;
import entidades.MisionPaciente;
import entidades.Verificacion;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author znico
 */
public class VerificacionService {

    VerificacionJpaController verificacionList;

    public VerificacionService() {
        verificacionList = new VerificacionJpaController(Persistence.createEntityManagerFactory("DT2PU"));
    }

    public List verificacionlista() {
        return verificacionList.findVerificacionEntities();

    }

    public void verificacionAdd(Verificacion a) throws Exception {
        verificacionList.create(a);

    }

    public void verificacionEdit(Verificacion a) throws Exception {
        verificacionList.edit(a);

    }

    public void verificacionDelete(int a) throws Exception {
        verificacionList.destroy(a);

    }

    public List<Verificacion> sqlbuscVerificacionPorMP(MisionPaciente us) {

        return verificacionList.sqlbuscVerificacionPorMP(us);
    }

    public List<Verificacion> sqlbuscVerificacionPorMPN(MisionPaciente us, int num) {

        return verificacionList.sqlbuscVerificacionPorMPN(us, num);
    }

    /**
     * TO DO
     * <p>
     * Carlos D. Escobar - 12/05/2019
     * </p>
     *
     * @param codApoyoSocial
     * @return
     */
    public List<VerficacionApoyoSocialDTO> consultarVerificacionesApoyoSocial(String codApoyoSocial) {
        VerficacionApoyoSocialDTO verificacion = new VerficacionApoyoSocialDTO();
        return verificacion.modeloADTO(verificacionList.consultarVerificacionesApoyoSocial(codApoyoSocial));
    }

    /**
     * TO DO
     * <p>
     * Carlos D. Escobar - 12/05/2019
     * </p>
     *
     * @param a
     */
    public void verificacionConfirmacionAPoyoSocial(Verificacion a) throws Exception {
        Verificacion veri = verificacionList.findVerificacion(a.getIdVerificacion());
        veri.setVerifApoyoSocial(a.getVerifApoyoSocial());
        verificacionList.edit(veri);

    }
}
