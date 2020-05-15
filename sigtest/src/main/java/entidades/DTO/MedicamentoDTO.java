/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.DTO;

import entidades.Medicamento;
import entidades.MedicamentoMision;

/**
 *
 * @author Chaly
 */
public class MedicamentoDTO {

    private int idMisonMedicamento;
    private String codMedicamento;
    private String nombre;
    private String tipo;
    private String estado;
    private int dosificación;
    private int posologia;
    private short recordar;
    private String observaciones;
    private String ultimaToma;

    public MedicamentoDTO() {
    }

    public MedicamentoDTO(Medicamento med) {
        this.codMedicamento = med.getCodMedicamento();
        this.nombre = med.getNombre();
        this.tipo = med.getTipo();
        this.estado = med.getEstado();
        for (MedicamentoMision medicamentoMision : med.getMedicamentoMisionList()) {
            this.dosificación = medicamentoMision.getDosificación();
            this.posologia = medicamentoMision.getPosologia();
            this.recordar = medicamentoMision.getRecordar();
            this.observaciones = medicamentoMision.getObservaciones();
            this.idMisonMedicamento = medicamentoMision.getIdMedicamentoMision();
            String hora = medicamentoMision.getUltimaToma().getHours()>9?medicamentoMision.getUltimaToma().getHours()+"":"0"+medicamentoMision.getUltimaToma().getHours();
            String minuto = medicamentoMision.getUltimaToma().getMinutes()>9?medicamentoMision.getUltimaToma().getMinutes()+"":"0"+medicamentoMision.getUltimaToma().getMinutes();
            String segundo = medicamentoMision.getUltimaToma().getSeconds()>9?medicamentoMision.getUltimaToma().getSeconds()+"":"0"+medicamentoMision.getUltimaToma().getSeconds();
            this.ultimaToma = hora + ":" + minuto + ":" +segundo;
            break;//muchos a muchos mal hecho, relamente es una relacion 1-1
        }
    }

    public int getIdMisonMedicamento() {
        return idMisonMedicamento;
    }

    public void setIdMisonMedicamento(int idMisonMedicamento) {
        this.idMisonMedicamento = idMisonMedicamento;
    }

    
    public int getDosificación() {
        return dosificación;
    }

    public int getPosologia() {
        return posologia;
    }

    public short getRecordar() {
        return recordar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getUltimaToma() {
        return ultimaToma;
    }

    
    
    public String getCodMedicamento() {
        return codMedicamento;
    }

    public void setCodMedicamento(String codMedicamento) {
        this.codMedicamento = codMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDosificación(int dosificación) {
        this.dosificación = dosificación;
    }

    public void setPosologia(int posologia) {
        this.posologia = posologia;
    }

    public void setRecordar(short recordar) {
        this.recordar = recordar;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setUltimaToma(String ultimaToma) {
        this.ultimaToma = ultimaToma;
    }

}
