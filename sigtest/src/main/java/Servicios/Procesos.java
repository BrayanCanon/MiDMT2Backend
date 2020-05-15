package Servicios;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entidades.ApoyoSocial;
import entidades.Asignacion;
import entidades.Codusuario;
import entidades.Comentario;
import entidades.DTO.MedicamentoDTO;
import entidades.Encuesta;
import entidades.Indicador;
import entidades.Logro;
import entidades.Medicamento;
import entidades.MedicamentoMision;
import entidades.Mensaje;
import entidades.Mision;
import entidades.MisionPaciente;
import entidades.MisionPasoLogro;
import entidades.MisionTipoRecurso;
import entidades.NivelPaciente;
import entidades.Notificacion;
import entidades.Paciente;
import entidades.Paso;
import entidades.Pregunta;
import entidades.Profesional;
import entidades.PruebaDiagnostica;
import entidades.RecursoApoyo;
import entidades.Tema;
import entidades.TipoEncuesta;
import entidades.TipoRecurso;
import entidades.Usuario;
import entidades.Verificacion;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import logica.services.ApoyoSocialService;
import logica.services.AsignacionService;
import logica.services.CategoriaService;
import logica.services.ComentarioService;
import logica.services.EncuestaService;
import logica.services.IndicadorService;
import logica.services.LogroService;
import logica.services.MedicamentoMisionService;
import logica.services.MedicamentoService;
import logica.services.MensajeService;
import logica.services.MisionPacienteService;
import logica.services.MisionPasoLogroService;
import logica.services.MisionService;
import logica.services.NivelPacienteService;
import logica.services.NivelService;
import logica.services.NotificacionService;
import logica.services.PacienteService;
import logica.services.PasoService;
import logica.services.PreguntaService;
import logica.services.ProfesionalService;
import logica.services.PruebaDiagnosticaService;
import logica.services.RecursoApoyoService;
import logica.services.TemaService;
import logica.services.TipoEncuestaService;
import logica.services.TipoIndicadorService;
import logica.services.TipoPreguntaService;
import logica.services.UsuarioService;
import logica.services.CodusuarioService;
import logica.services.GformsService;
import logica.services.MisionTipoRecursoService;
import logica.services.TipoRecursoService;
import logica.services.VerificacionService;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author Juan David Velásquez Bedoya Actualizado en septiembre de 2017
 */
@WebService(serviceName = "Procesos")

public class Procesos {

    String destinoRecurso = "/opt/tomcat/webapps/DT3/Imagenes/";
    Gson gson = new Gson();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String sal;
    String fecha3 = null;
    java.util.Date date = new Date();

    public static Connection conexionBD() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection miConexion = DriverManager.getConnection("jdbc:mysql://18.218.252.83:3306/dmt2", "root", "UBosque2018");
        return miConexion;
    }

    Hashtable<Long, Integer> sesion = new Hashtable<>();

    @WebMethod(operationName = "acceso")
    public String acceso(@WebParam(name = "correo") String correo, @WebParam(name = "clave") String clave) throws CheckVerifyFault {
        ArrayList<String> tabla = new ArrayList<String>();

        try {
            String resultado = "Correo o Contraseña incorrectos";
            String id = "0";

            PruebaDiagnosticaService pds = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();
            UsuarioService us = new UsuarioService();
            TipoEncuestaService tee = new TipoEncuestaService();

            Usuario aa = us.usuariolistaId(correo);

            CodusuarioService cs = new CodusuarioService();
            Codusuario ccc = new Codusuario();
            ccc.setNomUsuario(correo);

            Codusuario cu = cs.busNombre(ccc);

            aa.setFechaIngreso(date);
            us.usuarioEdit(aa);

            //------------------------------
            // funciona
            //---------------------------------
            String ii = aa.getClave();

            if (ii.equals(clave)) {

                tabla.add(String.valueOf(aa.getCodUsuario()));
                tabla.add(aa.getIdPerfil().getNomPerfil());
                tabla.add(String.valueOf((cu.getTerminos()) ? 1 : 0));

                resultado = tabla.get(0) + ";" + tabla.get(1) + ";" + tabla.get(2) + ";0";

                //---------------------------------------
                //---------------------------Revisión de la sesión -----------------
                sesion.put(Long.parseLong(id), 1);
                //Verficación si ha respondido la encuesta de la App
                int vEncuesta = 0;
                if (tabla.get(1).contains("psicologo")) {
                    // ResultSet rs9  = st.executeQuery("select completado from avances where identificacion="+id);

                    tabla.add(String.valueOf(cu.getAvances()));
                    //-----------------------------------------------
                    //Verificacion para responder con la encuesta
                    if (tabla.get(3).equals("0")) {
                        String fecha8 = null;
                        //   ResultSet rs8  = st.executeQuery("SELECT fecha from avances where identificacion = "+id);
                        fecha8 = format.format(cu.getFechaAvance());

                        if (verificarFecha(fecha8) > 5) {
                            vEncuesta = 1;
                        }
                    }

                    //Actualización del contador de accesos o entradas
                    //  st.executeUpdate("update avances set entradas = 2 + entradas - 1 where identificacion ="+ id);
                    int contador = cu.getAvances();
                    if (cu.getAvances() < 1) {
                        cu.setAvances(contador + 1);
                        cs.codusuarioEdit(cu);

                    }

                    resultado = tabla.get(0) + ";" + tabla.get(1) + ";" + tabla.get(2) + ";" + vEncuesta;
                }

                //-------------------------Prueba 2222------------------------------------
                //------------------------------------------------------------------
                //Verficaciones del familiar
                if (tabla.get(1).contains("familiar")) {
                    // ResultSet rs10  = st.executeQuery("select completado from avances where identificacion="+id);

                    tabla.add(String.valueOf(cu.getAvances()));

                    // rs10  = st.executeQuery("select paciente from tratantes where idfamiliar1 ="+id);
                    AsignacionService as = new AsignacionService();
                    Asignacion kk = as.sqlbuscFamiliar(aa.getCodUsuario());
                    tabla.add(kk.getCodPaciente().getCodPaciente());

                    if (cu.getAvances() == 0) {
                        cu.setAvances(cu.getAvances() + 1);
                        cs.codusuarioEdit(cu);
                    }

                    resultado = tabla.get(0) + ";" + tabla.get(1) + ";" + tabla.get(2) + ";" + tabla.get(3) + ";" + tabla.get(4);
                    //Identificación, rol, aceptaterminos,avance,id del paciente familiar
                }
                //------------Prueba 333----------------
                if (tabla.get(1).contains("paciente")) {

                    // ResultSet rs1  = st.executeQuery("select completado from avances where identificacion = "+ id);
                    tabla.add(String.valueOf(cu.getAvances()));

                    //Actualización del contador de accesos o entradas
                    // st.executeUpdate("update avances set entradas = 2 + entradas - 1 where identificacion ="+ id);
                    if (cu.getAvances() < 1) {
                        cu.setAvances(cu.getAvances() + 1);
                        cs.codusuarioEdit(cu);
                    }

                    //-----------------------------------------------
                    //Verificacion de fecha para el Animo
                    // ResultSet rs2  = st.executeQuery("SELECT fecha from animo where paciente = "+id+" order by fecha DESC LIMIT 1");
                    EncuestaService es = new EncuestaService();
                    int vAnimo = 0;
                    try {
                        List<Encuesta> enn = es.busEncuestaPorTipoPac(ps.pacienteListaId(tabla.get(0)), tee.tipoencuestalistaId(7));

                        String fecha = format.format(enn.get(enn.size() - 1).getFecha()); //Fecha de la bd

                        if (verificarFecha(fecha) > 0) {
                            vAnimo = 1;
                        }

                    } catch (Exception e) {

                    }
                    //-----------------------------------------------
                    //Verificacion de fecha quincenal para el peso

                    int vPeso = 0;

                    String fecha2 = null;
                    //  ResultSet rs3  = st.executeQuery("SELECT fecha from pesoimc where paciente = "+id+" order by fecha DESC LIMIT 1");

                    List<PruebaDiagnostica> pdd = pds.busUsuarioId2(ps.pacienteListaId(tabla.get(0)), "actualizarPeso");

                    fecha2 = format.format(pdd.get(pdd.size() - 1).getFechaPrueba());

                    if (verificarFecha(fecha2) > 14) {
                        vPeso = 1;
                    }

                    //-----------------------------------------------
                    //Verificación a 3 meses
                    int trimestral = 0;

                    // ResultSet rs4  = st.executeQuery("SELECT fecha from hba1c where paciente = "+id+" order by fecha DESC LIMIT 1");
                    List<PruebaDiagnostica> pdd2 = pds.busUsuarioId2(ps.pacienteListaId(tabla.get(0)), "actualizarHba1c");
                    fecha3 = format.format(pdd2.get(pdd.size() - 1).getFechaPrueba());

                    if (verificarFecha(fecha3) > 89) {
                        trimestral = 1;
                    }

                    resultado = tabla.get(0) + ";" + tabla.get(1) + ";" + tabla.get(2) + ";" + tabla.get(3) + ";" + vAnimo + ";" + vPeso + ";" + trimestral;

                }

            }
            return resultado;
        } catch (Exception e) {
            return "Correo o Contraseña incorrectos";
        }

    }

    public int verificarFecha(String fechaBD) {
        int respuesta = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //formateador
        Calendar ca = Calendar.getInstance();
        Calendar actual = Calendar.getInstance();
        Date d = new Date();
        try {
            d = sdf.parse(fechaBD);//Se asigna la fecha de la bd a la variable d
            ca.setTime(d);// Se asigna la fecha de la bd a la variable ca
            int a1, a2, f1, f2;
            a1 = ca.get(Calendar.DAY_OF_MONTH); //De la bd
            a2 = actual.get(Calendar.DAY_OF_MONTH);
            f1 = Integer.parseInt(fechaBD.substring(5, 7));//De la bd
            f2 = actual.get(Calendar.MONTH) + 1;
            respuesta = (a2 - a1) + (f2 - f1) * 30;
        } catch (ParseException ex) {
            //Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    @WebMethod(operationName = "cerrarSesion")
    public String cerrarSesion(@WebParam(name = "id") Long id, @WebParam(name = "clave") String clave) {
        String respuesta = "No se pudo cerrar";
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select clave from usuario where identificacion = "+id);
            String c = null;
            while (rs.next()){  c = rs.getString("clave");  }
            if(clave.contentEquals(c)){
             */
            sesion.put(id, 0);
            //System.out.println("La sesión --- "+id+" --- se ha cerrado");
            respuesta = "Sesión cerrada";
            //}
        } catch (Exception ee) {
        }
        return respuesta;
    }

    //Terminado---
    @WebMethod(operationName = "crearPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String crearPaciente(@WebParam(name = "correo") String correo, @WebParam(name = "identificacion") String identificacion, @WebParam(name = "id") String id) throws CheckVerifyFault {
        String respuesta = "Algo salio mal";
        //if((sesion.get(id) != null) && (sesion.get(id) == 1 )){
        //if(chequearUsuario(Integer.parseInt(identificacion.trim())) == false){
        try {

            Usuario prueba = new Usuario();

            prueba.setCodUsuario(identificacion);
            UsuarioService prtest = new UsuarioService();
            Usuario lal = prtest.usuariolistaId(correo);
            try {
                Long.valueOf(identificacion);
            } catch (Exception asd) {
                return "debe ser una cedula numerica";
            }

            if (prtest.test(prueba) == 0) {

                Paciente pc = new Paciente();
                Usuario uc = new Usuario();
                Asignacion asig = new Asignacion();
                PacienteService p2 = new PacienteService();

                pc.setCodPaciente(identificacion);
                pc.setCorreo(correo);
                pc.setPuntajeAlim(0);
                pc.setPuntajeEjer(0);
                pc.setEstado("a");

                p2.PacienteAdd2(pc);

                asig.setCodPaciente(pc);
                asig.setEstado("a");
                ProfesionalService profserv = new ProfesionalService();
                AsignacionService asigser = new AsignacionService();
                asig.setCodProfesional(profserv.profesionalListaId(id));
                asigser.asignacionAdd(asig);
                sendEmail(correo);
                respuesta = " Paciente creado";
            } else {
                respuesta = "Ya existe un usuario con esa cedula";
            }
            return respuesta;
        } catch (Exception e) {
//            LOGGER.error("", e);
            e.printStackTrace();
            return "Error interno";
        }
        // }else{respuesta = "El paciente ya existe";}
        //}

    }

    @WebMethod(operationName = "crearFamiliar")
    public String crearFamiliar(@WebParam(name = "correo") String correo, @WebParam(name = "identificacion") String identificacion, @WebParam(name = "id") String id) {
        String respuesta = "Sesión inválida";
        try {
            Usuario prueba = new Usuario();

            prueba.setCodUsuario(identificacion);
            UsuarioService prtest = new UsuarioService();
            Usuario lal = prtest.usuariolistaId(correo);
            try {
                Long.valueOf(identificacion);
            } catch (Exception asd) {
                return "debe ser una cedula numerica";
            }

            if (prtest.test(prueba) == 0 && lal == null) {

                ApoyoSocial ap = new ApoyoSocial();
                ap.setCodApoyosocial(identificacion);
                ap.setCorreo(correo);
                ap.setEstado("c");
                ApoyoSocialService apse = new ApoyoSocialService();
                apse.apoyoSocialAdd(ap);
                PacienteService prof = new PacienteService();
                AsignacionService asigser = new AsignacionService();
                List<Asignacion> asig = asigser.asignacionPacientelista(prof.pacienteListaId(id));
                asig.get(0).setCodApoyosocial(ap);
                asigser.asignacionEdit(asig.get(0));
                return "creado";
            } else {
                return "ya existe un usuario con esta cedula";
            }

        } catch (Exception e) {
            return e.toString();
        }
    }

    //Para evitar la doble creación de pacientes, profesionales o familiares
    //terminado
    public @WebResult(partName = "checkVerifyResponse")
    boolean chequearUsuario(String id) throws CheckVerifyFault {
        ArrayList<String> tabla = new ArrayList<String>();
        boolean respuesta = false;
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            ResultSet rs = st.executeQuery("select * from usuario where identificacion = "+Long.parseLong(id));
            while (rs.next()){
                tabla.add(rs.getString("identificacion"));
            }
            if(tabla.size() > 0){ respuesta = true;}*/
            //==========================================================================================
            UsuarioService us = new UsuarioService();
            Usuario user = us.usuariolistaId(id);
            if (user != null) {
                respuesta = true;
            }

        } catch (Exception ee) {
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "di")
    public String di(@WebParam(name = "id") String id, @WebParam(name = "tension1") String tension1, @WebParam(name = "tension2") String tension2, @WebParam(name = "trigli") String trigli, @WebParam(name = "hba1c") String hba1c, @WebParam(name = "peso") String peso, @WebParam(name = "talla") String talla) {
        String respuesta = "No se pudo completar";
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into diagnosticoinicial values("+
                    Long.valueOf(id.trim())+
                    ",now(),"+
                    Integer.parseInt(tension1.trim())+","+
                    Integer.parseInt(tension2.trim())+","+
                    Integer.parseInt(trigli.trim())+","+
                    Float.parseFloat(hba1c.trim())+","+
                    Integer.parseInt(peso.trim())+","+
                    Integer.parseInt(talla.trim())+")");
            //st.executeUpdate("insert into diagnosticoinicial values(105,now(),200,80,175,4.7,150,180)");
            //st.executeUpdate("insert into diagnosticoinicial values("+id+", now(), "+tension1+", "+tension2+", "+trigli+", "+hba1c+", "+peso+", "+talla+")");
            System.out.println("enviado sql");

            //Calculo del imc  OJO NO BORRAR
            double imcTemp= Double.parseDouble(peso.trim())/((Double.parseDouble(talla.trim())*Double.parseDouble(talla.trim()))/10000);
            int imcFinal = (int) Math.round(imcTemp);
            //Inserción en las tablas de seguimiento o evolución histórica del paciente
            st.executeUpdate("insert into pesoimc values("+Long.valueOf(id.trim())+",now(),"+Integer.parseInt(peso.trim())+","+imcFinal+")");
            st.executeUpdate("insert into trigliceridos values("+Long.valueOf(id.trim())+",now(),"+Integer.parseInt(trigli.trim())+")");
            st.executeUpdate("insert into tension values("+Long.valueOf(id.trim())+",now(),"+tension1+","+tension2+")");
            st.executeUpdate("insert into hba1c values("+Long.valueOf(id.trim())+",now(),"+Float.parseFloat(hba1c.trim())+")");
            con.close();
            //==============================================================================================================================

             */
            java.util.Date date = new Date();
            PacienteService ps = new PacienteService();
            IndicadorService is = new IndicadorService();
            Paciente pc = ps.pacienteListaId(id);

            PruebaDiagnosticaService pds = new PruebaDiagnosticaService();

            Indicador ten1 = new Indicador();
            Indicador ten2 = new Indicador();
            Indicador trigliceridos = new Indicador();
            Indicador hba1cInd = new Indicador();
            Indicador pesoInd = new Indicador();
            Indicador tallaInd = new Indicador();
            Indicador imc = new Indicador();
            //asociar indicadores a prueba
            PruebaDiagnostica pi = new PruebaDiagnostica();

            pi.setCodPaciente(pc);
            pi.setFechaPrueba(date);
            pi.setEstado("a");
            pi.setNombrePrueba("actualizarTension");
            pds.pruebaDiagnosticaAdd(pi);

            ten1.setIdPrueba(pi);

            PruebaDiagnostica pi2 = new PruebaDiagnostica();
            pi2.setCodPaciente(pc);
            pi2.setFechaPrueba(date);
            pi2.setEstado("a");
            pi2.setNombrePrueba("actualizarTension2");
            pds.pruebaDiagnosticaAdd(pi2);

            ten2.setIdPrueba(pi2);

            PruebaDiagnostica pi3 = new PruebaDiagnostica();
            pi3.setCodPaciente(pc);
            pi3.setFechaPrueba(date);
            pi3.setEstado("a");
            pi3.setNombrePrueba("actualizarTriglicerios");
            pds.pruebaDiagnosticaAdd(pi3);

            trigliceridos.setIdPrueba(pi3);

            PruebaDiagnostica pi4 = new PruebaDiagnostica();
            pi4.setCodPaciente(pc);
            pi4.setFechaPrueba(date);
            pi4.setEstado("a");
            pi4.setNombrePrueba("actualizarHba1c");
            pds.pruebaDiagnosticaAdd(pi4);
            hba1cInd.setIdPrueba(pi4);

            PruebaDiagnostica pi6 = new PruebaDiagnostica();
            pi6.setCodPaciente(pc);
            pi6.setFechaPrueba(date);
            pi6.setEstado("a");
            pi6.setNombrePrueba("talla");
            pds.pruebaDiagnosticaAdd(pi6);
            tallaInd.setIdPrueba(pi6);

            PruebaDiagnostica pi5 = new PruebaDiagnostica();
            pi5.setCodPaciente(pc);
            pi5.setFechaPrueba(date);
            pi5.setEstado("a");
            pi5.setNombrePrueba("actualizarPeso");
            pds.pruebaDiagnosticaAdd(pi5);
            pesoInd.setIdPrueba(pi5);
            imc.setIdPrueba(pi5);

            //establecer tipo de indicador
            TipoIndicadorService inser = new TipoIndicadorService();
            ten1.setIdTipoIndicador(inser.tipoIndicadorListaId(1));
            ten2.setIdTipoIndicador(inser.tipoIndicadorListaId(1));
            trigliceridos.setIdTipoIndicador(inser.tipoIndicadorListaId(3));
            hba1cInd.setIdTipoIndicador(inser.tipoIndicadorListaId(2));
            pesoInd.setIdTipoIndicador(inser.tipoIndicadorListaId(4));
            tallaInd.setIdTipoIndicador(inser.tipoIndicadorListaId(5));
            imc.setIdTipoIndicador(inser.tipoIndicadorListaId(6));

            // establecer valor indicador
            ten1.setValorIndicador(Float.valueOf(tension1));
            ten2.setValorIndicador(Float.valueOf(tension2));
            trigliceridos.setValorIndicador(Float.valueOf(trigli));
            hba1cInd.setValorIndicador(Float.valueOf(hba1c));
            pesoInd.setValorIndicador(Float.valueOf(peso));
            String ttt = talla;
            double kk = Double.parseDouble(ttt);
            int peso2 = Integer.parseInt(peso);
            double kk2 = kk / 100;
            double imc2 = peso2 / (kk2 * kk2);

            tallaInd.setValorIndicador((float) 170);

            imc.setValorIndicador((float) imc2);
            //Establecer el estado
            ten1.setEstado("a");
            ten2.setEstado("a");
            trigliceridos.setEstado("a");
            hba1cInd.setEstado("a");
            pesoInd.setEstado("a");
            tallaInd.setEstado("a");
            imc.setEstado("a");
            //usar service para ingresar indicadores a la base de datos
            is.indicadorAdd(ten1);
            is.indicadorAdd(ten2);
            is.indicadorAdd(trigliceridos);
            is.indicadorAdd(hba1cInd);
            is.indicadorAdd(pesoInd);
            is.indicadorAdd(tallaInd);
            is.indicadorAdd(imc);
            respuesta = "Ingreso correcto";
            return respuesta;
        } catch (Exception e) {
            return e.toString();
        }

    }

    //Terminado
    @WebMethod(operationName = "crearComorbilidad")
    public String crearComorbilidad(@WebParam(name = "paciente") String paciente, @WebParam(name = "a") boolean a, @WebParam(name = "b") boolean b, @WebParam(name = "c") boolean c, @WebParam(name = "d") boolean d, @WebParam(name = "e") boolean e, @WebParam(name = "f") boolean f, @WebParam(name = "g") boolean g, @WebParam(name = "h") boolean h, @WebParam(name = "i") boolean i, @WebParam(name = "j") boolean j, @WebParam(name = "idUsuario") Long usuario) {
        String respuesta = "No se ha insertado";
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into comorbilidad values("+Long.parseLong(paciente.trim())+",now(),"+a+","+b+","+c+","+d+", "+e+","+f+","+g+","+h+","+i+","+j+")");
            respuesta = "Paciente Ingresado";
            con.close();
            tf(usuario,"crearPaciente"); //Medición de tiempo de uso*/
            //NUEVO======================================================================
            //creacion prueba diagnostica
            java.util.Date date = new Date();
            EncuestaService encser = new EncuestaService();
            Encuesta co = new Encuesta();
            PacienteService paser = new PacienteService();
            TipoEncuestaService tiencser = new TipoEncuestaService();
            TipoPreguntaService tipopreser = new TipoPreguntaService();
            PreguntaService preser = new PreguntaService();
            co.setCodPaciente(paser.pacienteListaId(paciente));

            co.setFecha(date);
            co.setNomEncuesta("comorbilidad");
            co.setEstado("a");
            co.setIdTipoEncuesta(tiencser.tipoencuestalistaId(1));
            encser.encuestaAdd(co);
            //Creacion preguntas:
            Pregunta pre = new Pregunta();
            //dislipidemia
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(2));
            pre.setEnunciado("dislipidemia");

            pre.setIdEncuesta(co);
            pre.setRespuesta(Boolean.toString(a));
            pre.setEstado("a");
            preser.preguntaAdd(pre);

            //hipertension
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(3));
            pre.setEnunciado("hipertension");
            pre.setRespuesta(Boolean.toString(b));
            preser.preguntaAdd(pre);
            //enfermedad pulmonar
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(4));
            pre.setEnunciado("enfermedad pulmonar");
            pre.setRespuesta(Boolean.toString(c));
            preser.preguntaAdd(pre);
            //hipotiroidismo
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(5));
            pre.setEnunciado("hipotiroidismo");
            pre.setRespuesta(Boolean.toString(d));
            preser.preguntaAdd(pre);
            //retinopatia
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(6));
            pre.setEnunciado("retinopatia");
            pre.setRespuesta(Boolean.toString(e));
            preser.preguntaAdd(pre);
            //nefropatia
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(7));
            pre.setEnunciado("nefropatia");
            pre.setRespuesta(Boolean.toString(f));
            preser.preguntaAdd(pre);
            //neuropatia
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(8));
            pre.setEnunciado("neuropatia");
            pre.setRespuesta(Boolean.toString(g));
            preser.preguntaAdd(pre);
            //pie diabetico
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(9));
            pre.setEnunciado("pie diabetico");
            pre.setRespuesta(Boolean.toString(h));
            preser.preguntaAdd(pre);
            //enfermedad cardiovadcular
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(10));
            pre.setEnunciado("enfermedad cardiovascular");
            pre.setRespuesta(Boolean.toString(i));
            preser.preguntaAdd(pre);
            //cardicaloclusiva
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(11));
            pre.setEnunciado("cardicaloclusiva");
            pre.setRespuesta(Boolean.toString(j));
            preser.preguntaAdd(pre);

        } catch (Exception ee) {
        }
        return respuesta;
    }

    @WebMethod(operationName = "listaPacientes")
    public List listaPacientes(@WebParam(name = "id") long id) {
        List<String> tabla = new ArrayList();

        try {
            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            ResultSet rs = st.executeQuery("select nombres, apellidos, identificacion from usuario, paciente, tratantes where usuario.identificacion = paciente.id AND paciente.id = tratantes.paciente and tratantes.idprofesional = "+id);
            while (rs.next()){
                tabla.add(rs.getString("identificacion"));
                tabla.add(rs.getString("nombres"));
                tabla.add(rs.getString("apellidos"));
                //Validación de pacientes ------------- evitando los paciente incompletos
                ResultSet rs1 = st2.executeQuery("select * from comorbilidad where paciente = "+rs.getString("identificacion"));
                //0 o falso si es un id malo y debe entrar
                boolean ignorar = rs1.next();
                if(ignorar == false){
                    //st2.executeUpdate("insert into prueba values("+rs.getString("identificacion")+")");/*
                    st2.executeUpdate("delete from usuario where identificacion ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from paciente where id ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from tratantes where paciente ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from avances where identificacion ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from animo where paciente ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from diagnosticoinicial where paciente ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from pesoimc where paciente ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from trigliceridos where id ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from tension where paciente ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from hba1c where paciente ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from notificaciones where id ="+rs.getString("identificacion"));
                    st2.executeUpdate("delete from metas where id ="+rs.getString("identificacion"));

                    tabla.remove(tabla.size()-1);
                    tabla.remove(tabla.size()-1);
                    tabla.remove(tabla.size()-1);
                }
            }
            con.close(); */
            ProfesionalService ps = new ProfesionalService();

            //Profesional pp= ps.profesionalListaId(String.valueOf(id));
            List<Asignacion> uu = ps.busListUsuarios(String.valueOf(id));
            for (int i = 0; i < uu.size(); i++) {
                if (uu.get(i).getCodPaciente().getNombre() != null && uu.get(i).getCodPaciente().getApellido() != null && !uu.get(i).getCodPaciente().getNombre().toString().isEmpty() && !uu.get(i).getCodPaciente().getApellido().toString().isEmpty()) {
                    tabla.add(uu.get(i).getCodPaciente().getCodPaciente());
                    tabla.add(uu.get(i).getCodPaciente().getNombre());
                    tabla.add(uu.get(i).getCodPaciente().getApellido());
                }
            }

            return tabla;
        } catch (Exception e) {
            List<String> error = new ArrayList();

            error.add(e.toString());
            return error;
        }

    }

    //Terminado
    @WebMethod(operationName = "usuario")
    public String usuario(@WebParam(name = "id") Long id, @WebParam(name = "clave") String clave, @WebParam(name = "nombres") String nombres,
            @WebParam(name = "apellidos") String apellidos, @WebParam(name = "sexo") String sexo, @WebParam(name = "telefono") String telefono,
            @WebParam(name = "direccion") String direccion, @WebParam(name = "ciudad") int ciudad, @WebParam(name = "nacimiento") String nacimiento) {
        String respuesta = "Datos incorrectos";

        try {
            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update usuario set clave='"+clave+"', nombres='"+nombres+"',apellidos='"+apellidos+"',sexo='"+sexo+"',telefono='"+telefono+
                    "',direccion='"+direccion+"',ciudad="+ciudad+", fechaingreso= now(), estado=1 where usuario.identificacion="+id);
            st.executeUpdate("update paciente set fechanacimiento = '"+nacimiento+"' where id = "+id);
            st.executeUpdate("update avances set completado = 1 where identificacion="+id);
            con.close();
             */
            //nuevo===================================================================================================================================

            UsuarioService usser = new UsuarioService();
            PacienteService pacienteser = new PacienteService();
            Paciente pac = pacienteser.pacienteListaC(Long.toString(id));

            pac.setNombre(nombres);
            pac.setApellido(apellidos);
            pac.setSexo(sexo);
            pac.setTelefono(telefono);
            pac.setDireccion(direccion);
            pac.setEstado("a");
            pac.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(nacimiento));
            pacienteser.PacienteEdit(pac);

            java.util.Date date = new Date();
            Usuario usbusqueda = new Usuario();
            usbusqueda.setCodUsuario(Long.toString(id));
            Usuario user = (Usuario) usser.sqlbuscCod(usbusqueda);
            user.setClave(clave);
            user.setFechaRegistro(date);

            usser.usuarioEdit(user);

            respuesta = "Actualización correcta";

        } catch (Exception e) {
            return e.toString();
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "usuarioFamiliar")
    public String usuarioFamiliar(@WebParam(name = "id") Long id, @WebParam(name = "clave") String clave, @WebParam(name = "nombres") String nombres,
            @WebParam(name = "apellidos") String apellidos, @WebParam(name = "sexo") String sexo, @WebParam(name = "telefono") String telefono,
            @WebParam(name = "direccion") String direccion, @WebParam(name = "ciudad") int ciudad, @WebParam(name = "nacimiento") String nacimiento) {
        String respuesta = "Datos incorrectos";

        try {
            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update usuario set clave='"+clave+"', nombres='"+nombres+"',apellidos='"+apellidos+"',sexo='"+sexo+"',telefono='"+telefono+
                    "',direccion='"+direccion+"',ciudad="+ciudad+", fechaingreso= now(), estado=1 where usuario.identificacion="+id);
            st.executeUpdate("update usuario set aceptaterminos = 1 where identificacion="+id);
            con.close();*/

            //nuevo===============================================================================================================
            UsuarioService usser = new UsuarioService();
            ApoyoSocialService apser = new ApoyoSocialService();
            ApoyoSocial apoyo = apser.apoyoSocialListaIdC(Long.toString(id));

            apoyo.setNombre(nombres);
            apoyo.setApellido(apellidos);
            apoyo.setSexo(sexo);
            apoyo.setTelefono(telefono);

            apoyo.setDirección(direccion);
            apoyo.setEstado("a");

            apoyo.setFechaNacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(nacimiento));
            apser.apoyoSocialEdit(apoyo);

            java.util.Date date = new Date();
            Usuario usbusqueda = new Usuario();
            usbusqueda.setCodUsuario(Long.toString(id));
            Usuario user = (Usuario) usser.sqlbuscCod(usbusqueda);
            user.setClave(clave);
            user.setFechaRegistro(date);

            usser.usuarioEdit(user);
            respuesta = "Actualización correcta";

        } catch (Exception e) {
            return e.toString();
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "crearfyc") //Factores y conocimiento de la enfermedad
    public String crearfyc(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") boolean a, @WebParam(name = "b") int b, @WebParam(name = "c") boolean c, @WebParam(name = "d") boolean d, @WebParam(name = "e") int e, @WebParam(name = "f") boolean f, @WebParam(name = "g") boolean g, @WebParam(name = "h") boolean h, @WebParam(name = "i") String i) {
        String respuesta = "No se ha guardado";
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into fyc values("+paciente+", now(),"+a+","+b+","+c+","+d+","+e+","+f+","+g+","+h+",'"+i+"')");
            st.executeUpdate("update avances set completado = 2 where identificacion="+paciente);
            respuesta = "Datos guardados";
            con.close();
             */
            //nuevos=========================================================================================================
            //creacion encuesta
            PacienteService pacser = new PacienteService();
            TipoEncuestaService tipoencser = new TipoEncuestaService();
            EncuestaService encser = new EncuestaService();
            Encuesta fyc = new Encuesta();
            java.util.Date date = new Date();
            fyc.setCodPaciente(pacser.pacienteListaId(Long.toString(paciente)));
            fyc.setFecha(date);
            fyc.setNomEncuesta("Factores y conocimiento de la enfermedad");
            fyc.setEstado("a");
            fyc.setIdTipoEncuesta(tipoencser.tipoencuestalistaId(2));
            encser.encuestaAdd(fyc);
            //Creacion preguntas______________________________________________________

            //fuma
            PreguntaService preser = new PreguntaService();
            Pregunta pre = new Pregunta();
            TipoPreguntaService tipopreser = new TipoPreguntaService();
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(12));
            pre.setEnunciado("fuma");
            pre.setIdEncuesta(fyc);
            pre.setRespuesta(Boolean.toString(a));
            pre.setEstado("a");
            preser.preguntaAdd(pre);

            //cuantos fuma
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(13));
            pre.setEnunciado("cuantos cigarrillos fuma");
            pre.setRespuesta(Integer.toString(b));
            preser.preguntaAdd(pre);
            //consecuencias de fumar
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(14));
            pre.setEnunciado("conoce las concecuencias de fumar?");
            pre.setRespuesta(Boolean.toString(c));
            preser.preguntaAdd(pre);
            //alcohol
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(15));
            pre.setEnunciado("consume alcohol?");
            pre.setRespuesta(Boolean.toString(d));
            preser.preguntaAdd(pre);
            //cantidad alcohol
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(16));
            pre.setEnunciado("cantidad alcohol");
            pre.setRespuesta(Integer.toString(e));
            preser.preguntaAdd(pre);
            //consecuencias alcohol
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(17));
            pre.setEnunciado("consecuencias del acohol");
            pre.setRespuesta(Boolean.toString(f));
            preser.preguntaAdd(pre);
            //dt2todavida
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(18));
            pre.setEnunciado("dt2 toda la vida");
            pre.setRespuesta(Boolean.toString(g));
            preser.preguntaAdd(pre);
            //controlcondym
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(19));
            pre.setEnunciado("controlcondym");
            pre.setRespuesta(Boolean.toString(h));
            preser.preguntaAdd(pre);
            //organos
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(20));
            pre.setEnunciado("organos");
            pre.setRespuesta(i);
            preser.preguntaAdd(pre);

            UsuarioService us = new UsuarioService();
            CodusuarioService cu = new CodusuarioService();
            Usuario uuu = new Usuario();
            uuu.setCodUsuario(String.valueOf(paciente));
            Usuario uu = us.sqlbuscCod(uuu);
            Codusuario cd = new Codusuario();
            cd.setNomUsuario(uu.getNomUsuario());
            Codusuario hh = cu.busNombre(cd);
            hh.setAvances(2);
            cu.codusuarioEdit(hh);

            return "Se completo";
        } catch (Exception ee) {
            return ee.toString();
        }

    }

    //Terminado
    @WebMethod(operationName = "apoyoSocial")
    public String apoyoSocial(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") int a, @WebParam(name = "b") int b, @WebParam(name = "c") int c, @WebParam(name = "d") int d, @WebParam(name = "e") int e, @WebParam(name = "f") int f, @WebParam(name = "g") int g, @WebParam(name = "h") int h) {
        String respuesta = "No se ha guardado";
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into apoyosocial values("+paciente+", now(),"+a+","+b+","+c+","+d+","+e+","+f+","+g+","+h+")");
            st.executeUpdate("update avances set completado = 3 where identificacion="+paciente);
                   con.close();
             */


            // nuevo _______________________________________________________________________________
            PacienteService pacser = new PacienteService();
            TipoEncuestaService tipoencser = new TipoEncuestaService();
            EncuestaService encser = new EncuestaService();
            Encuesta apoyoSocial = new Encuesta();
            java.util.Date date = new Date();
            apoyoSocial.setCodPaciente(pacser.pacienteListaId(Long.toString(paciente)));
            apoyoSocial.setFecha(date);
            apoyoSocial.setNomEncuesta("Apoyo Social Paciente");
            apoyoSocial.setEstado("a");
            apoyoSocial.setIdTipoEncuesta(tipoencser.tipoencuestalistaId(3));
            encser.encuestaAdd(apoyoSocial);
            //Creacion preguntas______________________________________________________

            //familia apoya
            PreguntaService preser = new PreguntaService();
            Pregunta pre = new Pregunta();
            TipoPreguntaService tipopreser = new TipoPreguntaService();
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(21));
            pre.setEnunciado("familia apoya");
            pre.setIdEncuesta(apoyoSocial);
            pre.setRespuesta(Integer.toString(a));
            pre.setEstado("a");
            preser.preguntaAdd(pre);

            //amigos apoyan
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(22));
            pre.setEnunciado("amigos apoyan");
            pre.setRespuesta(Integer.toString(b));
            preser.preguntaAdd(pre);
            //satsfecho apoyado
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(23));
            pre.setEnunciado("satisfecho apoyado");
            pre.setRespuesta(Integer.toString(c));
            preser.preguntaAdd(pre);
            //puede hablar
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(24));
            pre.setEnunciado("puede hablar");
            pre.setRespuesta(Integer.toString(d));
            preser.preguntaAdd(pre);
            //reuno amigos
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(25));
            pre.setEnunciado("reuno amigos");
            pre.setRespuesta(Integer.toString(e));
            preser.preguntaAdd(pre);
            //sepreocupanpormi
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(26));
            pre.setEnunciado("se preocupan por mi ");
            pre.setRespuesta(Integer.toString(f));
            preser.preguntaAdd(pre);
            //habla problemas
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(27));
            pre.setEnunciado("habla problemas");
            pre.setRespuesta(Integer.toString(g));
            preser.preguntaAdd(pre);
            //atiendo consejos
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(28));
            pre.setEnunciado("atiendo consejos");
            pre.setRespuesta(Integer.toString(h));
            preser.preguntaAdd(pre);

            UsuarioService us = new UsuarioService();
            CodusuarioService cu = new CodusuarioService();
            Usuario uuu = new Usuario();
            uuu.setCodUsuario(String.valueOf(paciente));
            Usuario uu = us.sqlbuscCod(uuu);
            Codusuario cd = new Codusuario();
            cd.setNomUsuario(uu.getNomUsuario());
            Codusuario hh = cu.busNombre(cd);
            hh.setAvances(3);
            cu.codusuarioEdit(hh);
            respuesta = "Datos guardados";

        } catch (Exception ee) {
            return ee.toString();
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "estiloVida")
    public String crearEstiloVida(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") int a, @WebParam(name = "b") int b, @WebParam(name = "c") int c, @WebParam(name = "d") int d, @WebParam(name = "e") int e, @WebParam(name = "f") int f, @WebParam(name = "g") int g, @WebParam(name = "h") int h, @WebParam(name = "i") int i) {
        String respuesta = "No se ha guardado";
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into estilovida values("+paciente+", now(),"+a+","+b+","+c+","+d+","+e+","+f+","+g+","+h+","+i+")");
            st.executeUpdate("update avances set completado = 4 where identificacion="+paciente);
           
            con.close();
             */
            //nuevo
            PacienteService ps = new PacienteService();
            EncuestaService es = new EncuestaService();
            TipoEncuestaService tes = new TipoEncuestaService();
            TipoPreguntaService tps = new TipoPreguntaService();
            PreguntaService pres = new PreguntaService();
            Encuesta estilodevida = new Encuesta();
            Pregunta pre = new Pregunta();

            //crear y agregar encuesta
            java.util.Date date = new Date();
            estilodevida.setCodPaciente(ps.pacienteListaId(Long.toString(paciente)));
            estilodevida.setFecha(date);
            estilodevida.setNomEncuesta("Estilo de vida");
            estilodevida.setEstado("a");
            estilodevida.setIdTipoEncuesta(tes.tipoencuestalistaId(2));
            es.encuestaAdd(estilodevida);

            //crear y añadir preguntas
            pre.setIdTipoPregunta(tps.tipopreguntalistaId(29));
            pre.setIdEncuesta(estilodevida);
            pre.setEnunciado("Come cada 3 o 4 horas");
            pre.setRespuesta(Integer.toString(a));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(30));
            pre.setEnunciado("Resiste la tentación");
            pre.setRespuesta(Integer.toString(b));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(31));
            pre.setEnunciado("Elije bien");
            pre.setRespuesta(Integer.toString(c));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(32));
            pre.setEnunciado("frutas 3 verduras 2");
            pre.setRespuesta(Integer.toString(d));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(33));
            pre.setEnunciado("comer sano");
            pre.setRespuesta(Integer.toString(e));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(34));
            pre.setEnunciado("no fritos");
            pre.setRespuesta(Integer.toString(f));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(35));
            pre.setEnunciado("dieta Especializada");
            pre.setRespuesta(Integer.toString(g));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(36));
            pre.setEnunciado("Come Sal");
            pre.setRespuesta(Integer.toString(h));

            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(37));
            pre.setEnunciado("Fibra");
            pre.setRespuesta(Integer.toString(i));

            pres.preguntaAdd(pre);

            UsuarioService us = new UsuarioService();
            CodusuarioService cu = new CodusuarioService();
            Usuario uuu = new Usuario();
            uuu.setCodUsuario(String.valueOf(paciente));
            Usuario uu = us.sqlbuscCod(uuu);
            Codusuario cd = new Codusuario();
            cd.setNomUsuario(uu.getNomUsuario());
            Codusuario hh = cu.busNombre(cd);
            hh.setAvances(4);
            cu.codusuarioEdit(hh);
            respuesta = "Datos guardados";

            //       tf(paciente,"ingresoPaciente1"); //Tiempo final que se demoró el paciente
        } catch (Exception ee) {
            return ee.toString();
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "actividadFisica") //Actividad física y autocuidado
    public String crearActividadFisica(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") int a, @WebParam(name = "b") int b, @WebParam(name = "c") int c, @WebParam(name = "d") int d, @WebParam(name = "e") int e, @WebParam(name = "f") int f, @WebParam(name = "g") int g, @WebParam(name = "h") int h, @WebParam(name = "i") int i) {
        String respuesta = "No se ha guardado";
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into actividadfisica values("+paciente+", now(),"+a+","+b+","+c+","+d+")");
            st.executeUpdate("insert into autocuidado values("+paciente+", now(),"+e+","+f+","+g+","+h+")");
            st.executeUpdate("update avances set completado = 5 where identificacion="+paciente);
          
            con.close();*/
            // Nuevo ==========================================================
            PacienteService pacser = new PacienteService();
            TipoEncuestaService tipoencser = new TipoEncuestaService();
            EncuestaService encser = new EncuestaService();
            Encuesta actFisica = new Encuesta();
            java.util.Date date = new Date();
            actFisica.setCodPaciente(pacser.pacienteListaId(Long.toString(paciente)));
            actFisica.setFecha(date);
            actFisica.setNomEncuesta("Actividad Fisica");
            actFisica.setEstado("a");
            actFisica.setIdTipoEncuesta(tipoencser.tipoencuestalistaId(5));
            encser.encuestaAdd(actFisica);
            //Preguntas=============================================================
            //ejercicios 30 minutos
            PreguntaService preser = new PreguntaService();
            Pregunta pre = new Pregunta();
            TipoPreguntaService tipopreser = new TipoPreguntaService();
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(49));
            pre.setEnunciado("ejercicios 30 minutos");
            pre.setIdEncuesta(actFisica);
            pre.setRespuesta(Integer.toString(a));
            pre.setEstado("a");
            preser.preguntaAdd(pre);

            //escoje ejercicio
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(50));
            pre.setEnunciado("esoje ejercicio");
            pre.setRespuesta(Integer.toString(b));
            preser.preguntaAdd(pre);
            //ejercicio con otros
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(51));
            pre.setEnunciado("ejercicio con otros");
            pre.setRespuesta(Integer.toString(c));
            preser.preguntaAdd(pre);
            //limitaciones a ejercicios
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(52));
            pre.setEnunciado("limitaciones a ejercicios");
            pre.setRespuesta(Integer.toString(d));
            preser.preguntaAdd(pre);
            //segunda encuesta_______________________________________________________________________________________________________________________
            Encuesta autocuidado = new Encuesta();

            autocuidado.setCodPaciente(pacser.pacienteListaId(Long.toString(paciente)));
            autocuidado.setFecha(date);
            autocuidado.setNomEncuesta("autocuidado");
            autocuidado.setEstado("a");
            autocuidado.setIdTipoEncuesta(tipoencser.tipoencuestalistaId(6));
            encser.encuestaAdd(autocuidado);
            //Preguntas=============================================================
            //tiempo en mi cuidado

            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(53));
            pre.setEnunciado("tiempo en mi cuidado");
            pre.setIdEncuesta(autocuidado);
            pre.setRespuesta(Integer.toString(e));
            pre.setEstado("a");
            preser.preguntaAdd(pre);

            //busco informacion
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(54));
            pre.setEnunciado("busco informacion");
            pre.setRespuesta(Integer.toString(f));
            preser.preguntaAdd(pre);
            //conozco medicamentos
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(55));
            pre.setEnunciado("conozco medicamentos");
            pre.setRespuesta(Integer.toString(g));
            preser.preguntaAdd(pre);
            //tengo precaucion
            pre.setIdTipoPregunta(tipopreser.tipopreguntalistaId(56));
            pre.setEnunciado("tengo precaucion");
            pre.setRespuesta(Integer.toString(h));
            preser.preguntaAdd(pre);

            UsuarioService us = new UsuarioService();
            CodusuarioService cu = new CodusuarioService();
            Usuario uuu = new Usuario();
            uuu.setCodUsuario(String.valueOf(paciente));
            Usuario uu = us.sqlbuscCod(uuu);
            Codusuario cd = new Codusuario();
            cd.setNomUsuario(uu.getNomUsuario());
            Codusuario hh = cu.busNombre(cd);
            hh.setAvances(5);
            cu.codusuarioEdit(hh);
            respuesta = "Datos guardados";

        } catch (Exception ee) {
        }
        return respuesta;
    }

//Terminado
    @WebMethod(operationName = "aep") // Contine a autoeficacia, estado de ánimo, percepción del riesgo
    public String crearAEP(@WebParam(name = "id") long id, @WebParam(name = "a") int a, @WebParam(name = "b") int b, @WebParam(name = "c") int c, @WebParam(name = "d") int d, @WebParam(name = "e") int e, @WebParam(name = "f") int f, @WebParam(name = "g") int g, @WebParam(name = "h") boolean h, @WebParam(name = "i") boolean i, @WebParam(name = "j") boolean j, @WebParam(name = "k") boolean k) {
        String respuesta = "No se pudo guardar";
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into autoeficacia values("+id+",now(),"+a+","+b+","+c+","+d+")");
            st.executeUpdate("insert into estadoanimo values("+id+",now(),"+e+","+f+","+g+")");
            st.executeUpdate("insert into percepcionriesgo values("+id+",now(),"+h+","+i+","+j+","+k+")");
            st.executeUpdate("update avances set completado = 7 where identificacion="+id);
            
            con.close();
             */            //nuevo
            PacienteService ps = new PacienteService();
            EncuestaService es = new EncuestaService();
            TipoEncuestaService tes = new TipoEncuestaService();
            TipoPreguntaService tps = new TipoPreguntaService();
            PreguntaService pres = new PreguntaService();
            Encuesta aep = new Encuesta();
            Pregunta pre = new Pregunta();

            //crear y agregar encuesta
            java.util.Date date = new Date();
            aep.setCodPaciente(ps.pacienteListaId(Long.toString(id)));
            aep.setFecha(date);
            aep.setNomEncuesta("AEP");
            aep.setEstado("a");
            aep.setIdTipoEncuesta(tes.tipoencuestalistaId(2));
            es.encuestaAdd(aep);

            //crear y agregar preguntas
            pre.setIdTipoPregunta(tps.tipopreguntalistaId(38));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("metas realistas");
            pre.setRespuesta(Integer.toString(a));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(39));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("metas plan");
            pre.setRespuesta(Integer.toString(b));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(40));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("Conoce motivaciones");
            pre.setRespuesta(Integer.toString(c));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(41));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("Acepta recomendaciones");
            pre.setRespuesta(Integer.toString(d));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(42));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("puede cuidarse");
            pre.setRespuesta(Integer.toString(e));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(43));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("optimista futuro");
            pre.setRespuesta(Integer.toString(f));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(44));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("Se enoja");
            pre.setRespuesta(Integer.toString(g));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(45));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("infarto");
            pre.setRespuesta(Boolean.toString(h));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(46));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("presion alta");
            pre.setRespuesta(Boolean.toString(i));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(47));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("caida");
            pre.setRespuesta(Boolean.toString(j));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            pre.setIdTipoPregunta(tps.tipopreguntalistaId(48));
            pre.setIdEncuesta(aep);
            pre.setEnunciado("enfermedad del corazon");
            pre.setRespuesta(Boolean.toString(j));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

//            tf(id,"ingresoPaciente2"); //Tiempo final que se demoró el paciente
            UsuarioService us = new UsuarioService();
            CodusuarioService cu = new CodusuarioService();
            Usuario uuu = new Usuario();
            uuu.setCodUsuario(String.valueOf(id));
            Usuario uu = us.sqlbuscCod(uuu);
            Codusuario cd = new Codusuario();
            cd.setNomUsuario(uu.getNomUsuario());
            Codusuario hh = cu.busNombre(cd);
            hh.setAvances(6);
            cu.codusuarioEdit(hh);
            respuesta = "Datos guardados!";

        } catch (Exception ee) {
            return ee.toString();
        }
        return respuesta;
    }

    // El valor de la encuesta va a ser de 0 a 5
    //falta probar
    @WebMethod(operationName = "crearAnimo")
    public @WebResult(partName = "checkVerifyResponse")
    String crearAnimo(@WebParam(name = "id") long id, @WebParam(name = "nivel") int nivel) throws CheckVerifyFault {
        try {
            /*     Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into animo values("+id+",now(),"+nivel+")");
             */
            // nuevo
            PacienteService ps = new PacienteService();
            TipoPreguntaService tps = new TipoPreguntaService();
            EncuestaService es = new EncuestaService();
            TipoEncuestaService tes = new TipoEncuestaService();
            PreguntaService pres = new PreguntaService();
            Pregunta pre = new Pregunta();
            Encuesta animo = new Encuesta();

            // crear agregar encuesta
            //all crear asignar animo
            java.util.Date date = new Date();
            animo.setCodPaciente(ps.pacienteListaId(Long.toString(id)));
            animo.setFecha(date);
            animo.setNomEncuesta("Animo");
            animo.setEstado("a");

            animo.setIdTipoEncuesta(tes.tipoencuestalistaId(7));
            es.encuestaAdd(animo);

            //crear preguntas
            pre.setIdTipoPregunta(tps.tipopreguntalistaId(57));
            pre.setIdEncuesta(animo);
            pre.setEnunciado("Animo");
            pre.setRespuesta(String.valueOf(nivel));
            pre.setEstado("a");
            pres.preguntaAdd(pre);

            return "hola";
        } catch (Exception e) {
            return e.toString();
        }

    }

    //Terminado
    @WebMethod(operationName = "modificarRecurso")
    public String modificarRecurso(@WebParam(name = "imgenvieja") String imgenvieja, @WebParam(name = "autor") String autor, @WebParam(name = "titulo") String titulo, @WebParam(name = "mensaje") String mensaje, @WebParam(name = "recurso") String recurso, @WebParam(name = "link") String video) {
        String respuesta = "No se pudo modificar";

        try {
            Calendar c = new GregorianCalendar();
            String nom = null;
            if (recurso.length() < 5) { //Caso en el que no se cambia la imagen
                nom = imgenvieja;
            } else {
                nom = Long.toString(c.getTimeInMillis());
                byte[] decodificar = Base64.decodeBase64(recurso.getBytes());
                //OutputStream salida = new FileOutputStream("D:/Java/DT2/web/Imagenes/" + nom); //
                OutputStream salida = new FileOutputStream(destinoRecurso + nom);
                salida.write(decodificar);
                salida.close();
            }
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update recursos set fecha=now(),titulo='"+titulo+"',mensaje='"+mensaje+"',recurso='"+nom+"',video='"+video+"',responsable="+autor+" where recurso='"+
                    imgenvieja+"'");
            st.executeUpdate("update notificaciones set recurso = 2 "); */
            java.util.Date date = new Date();
            RecursoApoyoService ra = new RecursoApoyoService();
            UsuarioService us = new UsuarioService();
            RecursoApoyo r = new RecursoApoyo();
            Usuario tt = new Usuario();
            tt.setCodUsuario(autor);

            r.setIdRecapoyo(ra.busImagen(imgenvieja).getIdRecapoyo());
            r.setNomUsuario(us.sqlbuscCod(tt));
            r.setFecha(date);
            r.setTituloRec(titulo);
            r.setVideo(video);
            r.setImagen(nom);
            r.setContenidoApoyo(mensaje);
            r.setEstado("a");
            ra.recursoApoyoEdit(r);

            return respuesta = "Recurso actualizado!";
        } catch (Exception e) {
            return e.toString();
        }

    }

    //Terminado
    @WebMethod(operationName = "recursos")
    public ArrayList recursos(@WebParam(name = "recurso") String recurso, @WebParam(name = "op") String op) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            // Connection con = conexionBD();
            //Statement st = con.createStatement();
            // ResultSet rs = st.executeQuery("select * from recursos");
            RecursoApoyoService ra = new RecursoApoyoService();
            if (op.contains("c")) {
                /*
            while (rs.next()){
                tabla.add(rs.getString("fecha"));
                tabla.add(rs.getString("titulo"));
                tabla.add(rs.getString("mensaje"));
                tabla.add(rs.getString("recurso"));
                tabla.add(rs.getString("video"));
                String responsable = "";
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery("select nombres, apellidos from usuario where identificacion = '"+rs.getString("responsable")+"'");
                    while (rs2.next()){
                        responsable = rs2.getString("nombres")+" "+rs2.getString("apellidos");
                    }
                tabla.add(responsable);
            }
                 */
                List<RecursoApoyo> tt = ra.recursoApoyolista();
                for (int i = 0; i < tt.size(); i++) {
                    if (tt.get(i).getEstado().equals("a")) {
                        tabla.add(String.valueOf(String.valueOf(tt.get(i).getFecha())));
                        tabla.add(tt.get(i).getTituloRec());
                        tabla.add(tt.get(i).getContenidoApoyo());
                        tabla.add(tt.get(i).getImagen());
                        tabla.add(tt.get(i).getVideo());
                        tabla.add(String.valueOf(tt.get(i).getNomUsuario().getNomUsuario()));

                    }
                }

            } else if (op.contains("e")) {
                // st.executeUpdate("delete from recursos where recurso = "+recurso);
                RecursoApoyoService ra2 = new RecursoApoyoService();
                RecursoApoyo aa = ra2.busImagen(recurso);
                aa.setEstado("b");
                ra2.recursoApoyoEdit(aa);
                tabla.add("Recurso eliminado!");

                MisionTipoRecursoService mts = new MisionTipoRecursoService();

                MisionTipoRecurso gg = mts.sqlbuscPorRecurso(aa.getIdRecapoyo());
                gg.setEstado("b");
                mts.misionTipoRecursoEdit(gg);

            }
            //con.close();
        } catch (Exception e) {
            ArrayList<String> tabla2 = new ArrayList<String>();
            tabla2.add(e.toString());
            return tabla2;

        }

        return tabla;
    }

    //Terminado
    //Terminado
    @WebMethod(operationName = "paciente")
    public ArrayList paciente(@WebParam(name = "id") long id, @WebParam(name = "op") int op) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs;
             */
            if (op == 0) { // Caso consulta
                // rs = st.executeQuery("select * from usuario, paciente where usuario.identificacion = paciente.id and usuario.identificacion ="+id);
                //  while (rs.next()){
                /*   tabla.add(rs.getString("correo"));
                    tabla.add(rs.getString("nombres"));
                    tabla.add(rs.getString("apellidos"));
                    tabla.add(rs.getString("estado"));
                    tabla.add(rs.getString("telefono"));
                    tabla.add(rs.getString("direccion"));
                    tabla.add(rs.getString("fecharegistro"));
                    tabla.add(rs.getString("fechaingreso"));
                    tabla.add(rs.getString("fechanacimiento"));
                    tabla.add(rs.getString("ocupacion"));
                    tabla.add(rs.getString("estadocivil"));
                    tabla.add(rs.getString("hijos"));
                    tabla.add(rs.getString("sexo"));
                        }
                con.close();*/
                PacienteService ps = new PacienteService();
                Paciente pa = ps.pacienteListaId(String.valueOf(id));
                UsuarioService us = new UsuarioService();

                tabla.add(pa.getCorreo());
                tabla.add(pa.getNombre());
                tabla.add(pa.getApellido());
                if (pa.getEstado().equals("a")) {
                    tabla.add("1");
                } else {
                    tabla.add("2");
                }
                tabla.add(pa.getTelefono());
                tabla.add(pa.getDireccion());

                Usuario yyy = new Usuario();
                yyy.setCodUsuario(String.valueOf(id));
                Usuario hh = us.sqlbuscCod(yyy);

                tabla.add(format.format(hh.getFechaRegistro())); //fecharegistro
                if (hh.getFechaIngreso() == null) {
                    tabla.add("1");
                } else {
                    tabla.add(format.format(hh.getFechaIngreso()));
                }

                tabla.add(format.format(pa.getFechaNacimiento()));
                tabla.add(pa.getOcupacion());
                tabla.add(pa.getEstadoCivil());
                tabla.add(String.valueOf(pa.getNumHijo()));
                tabla.add(pa.getSexo());

            } else if (op == 1) { // Caso desactivación del paciente
                /*st.executeUpdate("update usuario set rol = 'pacienteinactivo' where identificacion ="+id);
                st.executeUpdate("update usuario set clave = 'pacienteinactivo' where identificacion ="+id);
                st.executeUpdate("update usuario set identificacion = "+id+1+" where identificacion ="+id);
                con.close();
                 */
                UsuarioService us = new UsuarioService();
                us.usuarioEditB2(us.usuariolistaId(String.valueOf(id)));
                tabla.add("Desactivado permanentemente!");
            }
        } catch (Exception e) {
        }
        return tabla;
    }

    // Terminado --------------
    @WebMethod(operationName = "consultarPesoImc")
    public ArrayList consultarPesoImc(@WebParam(name = "id") long id) throws CheckVerifyFault {

        try {

            PruebaDiagnosticaService pd = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();

            Paciente pac = ps.pacienteListaId(String.valueOf(id));

            ArrayList<String> mostrar = new ArrayList<String>();

            List a = pd.buscarTipoPrueba(pac, "imc", "actualizarPeso");

            mostrar.add((String) a.get(0));
            mostrar.add((String) a.get(1));
            List b = pd.buscarTipoPrueba(pac, "peso", "actualizarPeso");
            mostrar.add((String) b.get(1));
            return mostrar;
        } catch (Exception e) {

            ArrayList<String> error = new ArrayList();

            error.add(e.toString());
            return error;

        }

    }

    // terminado --------------
    @WebMethod(operationName = "consultarAnimo")
    public List<String> consultarAnimo(@WebParam(name = "id") long id) {
        ArrayList<String> mostrar = new ArrayList<String>();
        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from animo where paciente ="+id);
            while (rs.next()){
                tabla.add(rs.getString("fecha"));
                tabla.add(rs.getString("animo"));
                }
            con.close();
             */
            PacienteService ps = new PacienteService();
            EncuestaService es = new EncuestaService();

            Paciente pac = ps.pacienteListaId(String.valueOf(id));

            List a = es.busEncuestaPa2(pac, "emocion 1-5");

            mostrar.add((String) a.get(0));
            mostrar.add((String) a.get(1));

            return mostrar;
        } catch (Exception e) {
            List<String> error = new ArrayList();

            error.add(e.toString());
            return error;
        }

    }

    // terminado --------------
    @WebMethod(operationName = "consultarHba1c")
    public List<String> consultarHba1c(@WebParam(name = "id") long id) {

        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from hba1c where paciente ="+id);
            while (rs.next()){
                tabla.add(rs.getString("fecha"));
                tabla.add(rs.getString("hba1c"));
                }
            con.close();
             */
            PruebaDiagnosticaService pd = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();

            Paciente pac = ps.pacienteListaId(String.valueOf(id));

            List<String> mostrar = new ArrayList();

            List a = pd.buscarTipoPrueba(pac, "hba1c", "actualizarHba1c");

            mostrar.add((String) a.get(0));

            mostrar.add((String) a.get(1));

            return mostrar;

        } catch (Exception e) {
            List<String> error = new ArrayList();

            error.add(e.toString());
            return error;
        }

    }

    //  tipo recurso --------------
    @WebMethod(operationName = "listaMensajes")
    public List<String> listaMensajes(@WebParam(name = "id") long id) {
        try {

            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT identificacion, fecha, nombres, apellidos, mensaje from usuario, mensajes where usuario.identificacion = mensajes.id and destinatario = "+id+" order by fecha");
            while (rs.next()){
                tabla.add(rs.getString("identificacion"));// del remitente
                tabla.add(rs.getString("fecha"));
                tabla.add(rs.getString("nombres"));
                tabla.add(rs.getString("apellidos"));
                tabla.add(rs.getString("mensaje"));
                }
            st.executeUpdate("update notificaciones set mensaje = 0 where id = "+id);

            con.close()
             */
            MensajeService ms = new MensajeService();

            List<Mensaje> mens = ms.BuscarMensajedest(String.valueOf(id));
            List<String> salida = new ArrayList<>();
            for (int a = 0; a < mens.size(); a++) {
                salida.add(mens.get(a).getNomUsuario().getCodUsuario());
                salida.add(format.format(mens.get(a).getFecha()));
                salida.add(mens.get(a).getNomUsuario().getNomUsuario());
                salida.add("...");
                salida.add(mens.get(a).getContenidoMensaje());
            }
            /*
           List<Notificacion> nt= ns.busNotificacionUusuario( us.usuariolistaId(String.valueOf(id)));
            for (int i = 0; i < nt.size(); i++) {
            Notificacion gg= nt.get(i);
            gg.setEstado("v");
            ns.notificacionEdit(gg);
            }
             */
            return salida;

        } catch (Exception e) {
            List<String> error = new ArrayList();

            error.add(e.toString());
            return error;
        }

    }

    //
    @WebMethod(operationName = "crearMensaje")
    public String crearMensaje(@WebParam(name = "jsonString") String jsonString) {
        String respuesta = "No se pudo enviar";
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            //trae Puntaje obtenido por el paciente
            String id = jobj.get("id").getAsString().toString();
            String mensaje = jobj.get("mensaje").getAsString().toString();
            String destinatario = jobj.get("destinatario").getAsString().toString();
            MensajeService ms = new MensajeService();
            UsuarioService us = new UsuarioService();

            Mensaje m = new Mensaje();
            Usuario usus = new Usuario();
            usus.setCodUsuario(String.valueOf(destinatario));
            Usuario a = us.sqlbuscCod(usus);
            m.setNomDestinatario(a.getNomUsuario());
            Usuario gg = new Usuario();
            gg.setCodUsuario(String.valueOf(id));
            Usuario usu = us.sqlbuscCod(gg);
            m.setNomUsuario(usu);
            m.setContenidoMensaje(mensaje);

            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Date date = ts;
            m.setFecha(date);
            m.setEstado("a");
            ms.mensajeAdd(m);

            respuesta = "Mensaje enviado!";
            return respuesta;
        } catch (Exception e) {
            return e.toString();
        }

    }

    /*
    @WebMethod(operationName = "ciudades")
    public ArrayList ciudades() {
        ArrayList <String> tabla = new ArrayList<String>();
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from ciudad");
            while (rs.next()){
                tabla.add(rs.getString("id"));// del remitente
                tabla.add(rs.getString("nombre"));
                tabla.add(rs.getString("pais"));
                }
            con.close();


        } catch (Exception e) {}
        return tabla;
    }
     */
    //Terminado
    @WebMethod(operationName = "comunidad")
    public List<String> comunidad() {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from comunidad group by tema");
            while (rs.next()){
                tabla.add(rs.getString("fecha"));// del remitente
                tabla.add(rs.getString("tema"));
                tabla.add(rs.getString("autor"));
                tabla.add(rs.getString("participante"));
                tabla.add(rs.getString("mensaje"));
                }
            con.close();
             */
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            ArrayList mostrar = new ArrayList<String>();
            ComentarioService cs = new ComentarioService();
            TemaService ts = new TemaService();
            List<Tema> tt = ts.temaLista();
            Comentario co = new Comentario();
            for (int i = 0; i < tt.size(); i++) {
                co = cs.busTema(tt.get(i));

                mostrar.add(String.valueOf(format.format(co.getFecha())));
                mostrar.add(co.getIdTema().getTituloTema());
                mostrar.add(String.valueOf(co.getCreador()));
                mostrar.add(String.valueOf(co.getNomUsuario().getCodUsuario()));
                mostrar.add(co.getMensajeComent());
            }

            return mostrar;

        } catch (Exception e) {
            List<String> error = new ArrayList();
            error.add(e.toString());
            return error;
        }

    }

    //terminado
    @WebMethod(operationName = "crearComunidad")
    public String crearComunidad(@WebParam(name = "id") long id, @WebParam(name = "tema") String tema, @WebParam(name = "mensaje") String mensaje) {

        try {
            String result = "No se pudo publicar";
            java.util.Date date = new Date();
            ComentarioService cs = new ComentarioService();
            TemaService ts = new TemaService();
            Comentario cc = new Comentario();
            UsuarioService usu = new UsuarioService();
            Usuario a = new Usuario();
            Tema nuevo = new Tema();

            nuevo.setTituloTema(tema);
            nuevo.setEstado("a");
            int cod = ts.temaAddobj(nuevo);

            cc.setCreador(String.valueOf(id));
            Usuario aa = new Usuario();
            aa.setCodUsuario(String.valueOf(id));
            cc.setNomUsuario(usu.sqlbuscCod(aa));
            cc.setIdTema(ts.temalistaId(cod));
            cc.setEstado("a");
            cc.setFecha(date);
            cc.setMensajeComent(mensaje);
            cs.comentarioAdd(cc);
            result = "Funciono";


            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into comunidad values(now(),'"+tema+"',"+id+",0, '"+mensaje+"')");
             */
 /*

           cc.setFecha(date);
           cc.setMensajeComent("");

           cc.setNomUsuario(null);
           cc.setCreador(String.valueOf(id));
           cc.setIdTema(ts.busNombreTema(tema));
           cc.setEstado("a");

           //cs.comentarioAdd(cc);
             */
            return result;
        } catch (Exception e) {
            return "Tema con nombre repetido";
        }

    }

    //terminado
    @WebMethod(operationName = "crearProfesional")
    public String crearProfesional(@WebParam(name = "nombre") String nombre, @WebParam(name = "cedula") long cedula, @WebParam(name = "email") String email, @WebParam(name = "clave") String clave, @WebParam(name = "telefono") long telefono, @WebParam(name = "apellidos") String apellidos, @WebParam(name = "direccion") String direccion, @WebParam(name = "creador") long creador) throws CheckVerifyFault {

        String respuesta = "Creado";
        try {

            Usuario prueba = new Usuario();
            prueba.setCodUsuario(String.valueOf(cedula));
            UsuarioService prtest = new UsuarioService();
            Usuario lal = prtest.usuariolistaId(email);

            if (prtest.test(prueba) == 0 && lal == null) {
                Profesional pa = new Profesional();
                ProfesionalService ps = new ProfesionalService();

                pa.setCodProfesional(String.valueOf(cedula));
                pa.setTelefono(String.valueOf(telefono));
                pa.setNombre(nombre);
                pa.setApellido(apellidos);
                pa.setEspecialidad("");
                pa.setEstado("a");
                pa.setCorreo(email);
                ps.profesionalAdd(pa);

            } else {
                return "esta cedula ya existe";
            }

        } catch (Exception e) {
            return e.toString() + " error ya estaba creado";
        }

        return respuesta;

    }

    //Terminado y  se camio el id de long a string
    @WebMethod(operationName = "usuariosAdmin")
    public ArrayList usuariosAdmin(@WebParam(name = "op") String op, @WebParam(name = "id") String id) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            Connection con = conexionBD();
            Statement st = con.createStatement();
            UsuarioService us = new UsuarioService();
            PacienteService ps = new PacienteService();
            Usuario uu = new Usuario();
            Paciente paa = new Paciente();
            if (op.contains("consultar")) {
                /* ResultSet rs = st.executeQuery("select identificacion,rol,nombres,apellidos from usuario");
                while (rs.next()){
                    tabla.add(rs.getString("identificacion"));
                    tabla.add(rs.getString("rol"));
                    tabla.add(rs.getString("nombres"));
                    tabla.add(rs.getString("apellidos"));
                }
                 */

                List<Usuario> tt = us.usuariolista();
                for (int i = 0; i < tt.size(); i++) {

                    paa = ps.pacienteListaId(tt.get(i).getCodUsuario());
                    tabla.add(String.valueOf(tt.get(i).getCodUsuario()));
                    tabla.add(tt.get(i).getIdPerfil().getNomPerfil());
                    tabla.add(paa.getNombre());
                    tabla.add(paa.getApellido());

                }
            } else if (op.contains("borrar")) {
                /* st.executeUpdate("delete from usuario where identificacion ="+id);
                st.executeUpdate("delete from tratantes where paciente ="+id);
                st.executeUpdate("delete from tratantes where idprofesional ="+id);
                st.executeUpdate("delete from tratantes where idfamiliar1 ="+id);
                st.executeUpdate("delete from notificaciones where id ="+id);
                st.executeUpdate("delete from avances where identificacion ="+id);

                st.executeUpdate("delete from actividadfisica where paciente ="+id);
                st.executeUpdate("delete from animo where paciente ="+id);
                st.executeUpdate("delete from apoyosocial where paciente ="+id);
                st.executeUpdate("delete from autocuidado where paciente ="+id);
                st.executeUpdate("delete from autoeficacia where paciente ="+id);
                st.executeUpdate("delete from comorbilidad where paciente ="+id);
                st.executeUpdate("delete from diagnosticoinicial where paciente ="+id);
                st.executeUpdate("delete from estadoanimo where paciente ="+id);
                st.executeUpdate("delete from estilovida where paciente ="+id);
                st.executeUpdate("delete from fyc where paciente ="+id);
                st.executeUpdate("delete from hba1c where paciente ="+id);
                st.executeUpdate("delete from medicamento where paciente ="+id);
                st.executeUpdate("delete from mensajes where destinatario ="+id);
                st.executeUpdate("delete from mensajes where id ="+id);
                st.executeUpdate("delete from metas where id ="+id);
                st.executeUpdate("delete from observaciones where paciente ="+id);
                st.executeUpdate("delete from percepcionriesgo where paciente ="+id);
                st.executeUpdate("delete from pesoimc where paciente ="+id);
                st.executeUpdate("delete from trigliceridos where id ="+id);
                st.executeUpdate("delete from tension where paciente ="+id);
                st.executeUpdate("delete from tiemposdeuso where usuario ="+id);
                st.executeUpdate("delete from paciente where id ="+id);
                st.executeUpdate("delete from glucosa where id ="+id);

                 */

                Usuario ss = us.usuariolistaId(id);
                ss.setEstado("b");
                us.usuarioEdit(ss);

            }
            con.close();

        } catch (Exception e) {
        }
        return tabla;
    }

    /**
     * FALTA !!
     */
    @WebMethod(operationName = "notificador")
    public String notificador(@WebParam(name = "id") long id) {
        /*La notificacion:
        0 = nada
        1 = mensaje
        2 = recurso nuevo
        3 = recordatorio de cualquier otra cosa general
         */
        String respuesta = "0";
        ArrayList<String> tabla = new ArrayList<String>();
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
          /*  ResultSet rs = st.executeQuery("select * from notificaciones where id = "+id);
                while (rs.next()){
                    tabla.add(rs.getString("id"));
                    tabla.add(rs.getString("fecha"));
                    tabla.add(rs.getString("mensaje"));
                    tabla.add(rs.getString("recurso"));
                    tabla.add(rs.getString("recordatorio"));
            
                }

            respuesta = tabla.get(2)+";"+tabla.get(3)+";"+tabla.get(4);

         st.executeUpdate("update notificaciones set mensaje = 0, recurso = 0, recordatorio = 0 where id = "+id);
            con.close();
             */

        } catch (Exception e) {
        }
        return respuesta;
    }

    @WebMethod(operationName = "borrarMensajes")
    public String borrarMensajes(@WebParam(name = "id") long id) {
        String respuesta = "No se han borrado";
        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("delete from mensajes where destinatario = "+id);
             */
            NotificacionService nn = new NotificacionService();
            Notificacion ot = nn.notificacionlistaId((int) id);
            ot.setEstado("b");
            nn.notificacionEdit(ot);

            respuesta = "Mensajes borrados!";

        } catch (Exception e) {
        }
        return respuesta;
    }

    @WebMethod(operationName = "detallePaciente")
    public ArrayList detallePaciente(@WebParam(name = "id") long id) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {/*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from usuario where identificacion = "+id);
             */
            PacienteService ps = new PacienteService();
            Paciente pp = ps.pacienteListaId(String.valueOf(id));
            UsuarioService uss = new UsuarioService();
            Usuario uu = uss.usuariolistaId(pp.getCorreo());
            tabla.add("DATOS GENERALES");
            tabla.add("Email: ");
            tabla.add(pp.getCorreo());
            tabla.add("Nombres: ");
            tabla.add(pp.getNombre());
            tabla.add("Apellidos: ");
            tabla.add(pp.getApellido());
            tabla.add("Sexo: ");
            tabla.add(pp.getSexo());
            tabla.add("Dirección: ");
            tabla.add(pp.getDireccion());
            tabla.add("Teléfono de contacto: ");
            tabla.add(pp.getTelefono());
            tabla.add("Fecha de registro: ");
            tabla.add(String.valueOf(uu.getFechaRegistro()));
            tabla.add("Fecha de ingreso: ");
            tabla.add(String.valueOf(uu.getFechaIngreso()));

            //---Consulta en diagnosticoinicial
            /* rs = st.executeQuery("select * from diagnosticoinicial where paciente = "+id);*/
            PruebaDiagnosticaService pdd = new PruebaDiagnosticaService();
            List<String> ten1 = pdd.buscarTipoPrueba(pp, "tension", "actualizarTension");
            List<String> ten2 = pdd.buscarTipoPrueba(pp, "tension", "actualizarTension2");
            List<String> tri = pdd.buscarTipoPrueba(pp, "triglicerios", "actualizarTriglicerios");
            List<String> hba1c = pdd.buscarTipoPrueba(pp, "hba1c", "actualizarHba1c");
            List<String> peso = pdd.buscarTipoPrueba(pp, "peso", "actualizarPeso");
            List<String> talla = pdd.buscarTipoPrueba(pp, "talla", "talla");

            //tabla.add("DIANÓSTICO INICIAL ");
            tabla.add("Fecha de la información: ");
            tabla.add(ten1.get(0));
            tabla.add("Tensión arterial: ");
            tabla.add(ten1.get(1) + "/" + ten2.get(1) + " mm Hg");
            tabla.add("Triglicéridos: ");
            tabla.add(tri.get(1));
            tabla.add("Hemoglobina glicosilada: ");
            tabla.add(hba1c.get(1));
            tabla.add("Peso en kilogramos: ");
            tabla.add(peso.get(1));
            tabla.add("Talla en centímetros: ");
            float a = Float.parseFloat(talla.get(1));
            float b = a / 10;
            tabla.add(String.valueOf(b));

            //---Consulta en comorbilidad
            //rs = st.executeQuery("select * from comorbilidad where paciente = "+id);
            EncuestaService ess = new EncuestaService();
            TipoEncuesta tp = new TipoEncuesta();
            tp.setIdTipoEncuesta(1);

            List<String> en1 = ess.busEncuestaPa3(pp, "Estilo de vida");
            //tabla.add("COMORBILIDAD Y COMPLICACIONES ");
            tabla.add("Fecha de la información: ");
            tabla.add(en1.get(0));
            tabla.add("dislipidemia: ");
            tabla.add(en1.get(1));
            tabla.add("Hipertension: ");
            tabla.add(en1.get(2));
            tabla.add("Enfermedad pulmonar: ");
            tabla.add(en1.get(3));
            tabla.add("Hipotiroidismo: ");
            tabla.add(en1.get(4));
            tabla.add("Retinopatia: ");
            tabla.add(en1.get(5));
            tabla.add("Neuropatia: ");
            tabla.add(en1.get(6));
            tabla.add("Pie diabetico: ");
            tabla.add(en1.get(7));
            tabla.add("Enfermedad cardiovascular: ");
            tabla.add(en1.get(8));
            tabla.add("Enfermedad cordical oclusiva: ");
            tabla.add(en1.get(9));
            //---Consulta en fyc
            //    rs = st.executeQuery("select * from fyc where paciente = "+id);
            List<String> en2 = ess.busEncuestaPa3(pp, "Factores de conocimiento");
            //tabla.add("FACTORES DE RIESGO Y CONOCIMIENTO SOBRE LA ENFERMEDAD ");
            tabla.add("Fecha de la información: ");
            tabla.add(en2.get(0));
            tabla.add("Es fumador/a: ");
            tabla.add(en2.get(1));
            tabla.add("Cantidad de cigarrillos mensuales: ");
            tabla.add(en2.get(2));
            tabla.add("Conoce las consecuencias: ");
            tabla.add(en2.get(3));
            tabla.add("Consume alcohol: ");
            tabla.add(en2.get(4));
            tabla.add("Conoce las consecuencias de tomar: ");
            tabla.add(en2.get(5));
            tabla.add("¿La diabetes representa una enfermedad para toda la vida?: ");
            tabla.add(en2.get(6));
            tabla.add("¿Se puede controlar con dieta y medicación?: ");
            tabla.add(en2.get(7));
            tabla.add("Cite dos o más órganos que se vean afectados por la elevación de las cifras de glucemia: ");
            tabla.add(en2.get(8));

            //---Consulta en Apoyo Social
            //   rs = st.executeQuery("select * from apoyosocial where paciente = "+id);
            List<String> en3 = ess.busEncuestaPa3(pp, "emocion 1-5");
            //tabla.add("APOYO SOCIAL ");
            tabla.add("Fecha de la información: ");
            tabla.add(en3.get(0));
            tabla.add("Mi familia me apoya en el proceso de tratamiento: ");
            tabla.add(en3.get(1));
            tabla.add("Mis amigos me apoyan en el tratamiento: ");
            tabla.add(en3.get(2));
            tabla.add("Me siento satisfecho con la manera en que mi familia me apoya en el tratamiento : ");
            tabla.add(en3.get(3));
            tabla.add("Tengo con quien hablar sobre mi enfermedad: ");
            tabla.add(en3.get(4));
            tabla.add("Me reuno con mis amigos y familiares?: ");
            tabla.add(en3.get(5));
            tabla.add("Cuento con personas que se preocupan de lo que me sucede: ");
            tabla.add(en3.get(6));
            tabla.add("Tengo la posibilidad de hablar de mis problemas: ");
            tabla.add(en3.get(7));
            tabla.add("Atiendo consejos útiles cuando me ocurre algún acontecimiento importante: ");
            tabla.add(en3.get(8));

            //---Consulta en Estilo de Vida - ahora categorizado como DIETA - NUTRICION
            //rs = st.executeQuery("select * from estilovida where paciente = "+id);
            //tabla.add("DIETA Y NUTRICIÓN ");
            tabla.add("Fecha de la información: ");
            tabla.add(en2.get(10));
            tabla.add("Podría consumir alimentos cada 3 ó 4 horas todos los días, incluyendo el desayuno: ");
            tabla.add(en2.get(11));
            tabla.add("Soy capaz de continuar con mi dieta cuando tengo que preparar o compartir alimentos con personas que no tienen diabetes: ");
            tabla.add(en2.get(12));
            tabla.add("Me siento seguro de poder escoger los alimentos apropiados para comer: ");
            tabla.add(en2.get(13));
            tabla.add("Consumo tres frutas y dos verduras en mi dieta diaria: ");
            tabla.add(en2.get(14));
            tabla.add("Puedo incluir diariamente en mi dieta verduras, frutas, integrales, arroz integral, avena y lácteos bajos en grasa: ");
            tabla.add(en2.get(15));
            tabla.add("Tengo fuera de mi dieta los paquetes de alimentos procesados (papitas, chitos..): ");
            tabla.add(en2.get(16));
            tabla.add("¿Sigue una dieta especializada para el control de su diabetes?: ");
            tabla.add(en2.get(17));
            tabla.add("¿Consume más de una cucharadita de sal al día?: ");
            tabla.add(en2.get(18));
            tabla.add("Consume alimentos ricos en fibra, como granos, avena, arroz integral: ");
            tabla.add(en2.get(19));

            //---Consulta en actividadfisica y autocuidado
            //   rs = st.executeQuery("select * from actividadfisica where paciente = "+id);
            List<String> en4 = ess.busEncuestaPa3(pp, "Actividad Fisica");
            //tabla.add("ACTIVIDAD FÍSICA ");
            tabla.add("Fecha de la información: ");
            tabla.add(en4.get(0));
            tabla.add("Realizo actividad física  30 minutos al día: ");
            tabla.add(en4.get(2));
            tabla.add("Puedo escoger caminar, montar bicicleta, nadar o correr como parte de mi actividad física.: ");
            tabla.add(en4.get(3));
            tabla.add("Tengo la posibilidad de realizar actividad física con otras personas.: ");
            tabla.add(en4.get(4));
            tabla.add("Tengo limitaciones para realizar actividad física: ");
            tabla.add(en4.get(5));

            // rs = st.executeQuery("select * from autocuidado where paciente = "+id);
            List<String> en5 = ess.busEncuestaPa3(pp, "autocuidado");
            //tabla.add("AUTOCUIDADO ");
            tabla.add("Fecha de la información: ");
            tabla.add(en5.get(0));
            tabla.add("Invierto tiempo en mi propio cuidado: ");
            tabla.add(en5.get(1));
            tabla.add("Busco información y orientación sobre el manejo de mi enfermedad: ");
            tabla.add(en5.get(2));
            tabla.add("Conózco mis medicamentos y  para que sirven: ");
            tabla.add(en5.get(3));
            tabla.add("Las medidas que he tomado con respecto ala diabetes permiten garantizar mi bienestar y el de mi familia: ");
            tabla.add(en5.get(4));

            //---Consulta en autoeficacia, percepcion del riesgo y estado de ánimo
            // rs = st.executeQuery("select * from autoeficacia where paciente = "+id);
            //  while (rs.next()){
            //tabla.add("AUTOEFICACIA ");
            tabla.add("Fecha de la información: ");
            tabla.add(en2.get(20));
            tabla.add("¿Puedo cumplir metas realistas en el cuidado de mi diabetes?: ");
            tabla.add(en2.get(21));
            tabla.add("¿Sigo un plan  para alcanzar mis metas del tratamiento?: ");
            tabla.add(en2.get(22));
            tabla.add("¿Conozco lo que me motiva para cuidar de mi diabetes?: ");
            tabla.add(en2.get(23));
            tabla.add("¿Estoy en capacidad de aceptar recomendaciones para cuidar de mi diabetes?: ");
            tabla.add(en2.get(24));
            //  }
            //rs = st.executeQuery("select * from estadoanimo where paciente = "+id);
            //while (rs.next()){
            //tabla.add("ESTADO DE ÁNIMO ");
            tabla.add("Fecha de la información: ");
            tabla.add(en2.get(32));
            tabla.add("¿Siente que su estado de ánimo le permite cuidarse? : ");
            tabla.add(en2.get(33));
            tabla.add("¿Piensa de manera  optimista sobre su futuro?: ");
            tabla.add(en2.get(34));
            tabla.add("¿Se enoja con facilidad?: ");
            tabla.add(en2.get(35));
            // }
            // rs = st.executeQuery("select * from percepcionriesgo where paciente = "+id);
            //while (rs.next()){
            //tabla.add("PERCEPCIÓN DEL RIESGO ");
            tabla.add("Fecha de la información: ");
            tabla.add(en2.get(32));
            tabla.add("¿Cree que  tenga en un momento de su vida tenga un infarto?: ");
            tabla.add(en2.get(40));
            tabla.add("¿Cree que  tenga en un momento de su vida tenga presión arterial alta?: ");
            tabla.add(en2.get(41));
            tabla.add("¿Cree que  tenga en un momento de su vida tenga una caida?:  ");
            tabla.add(en2.get(42));
            tabla.add("¿Cree que  tenga en un momento de su vida tenga una enfermedad del corazón?: ");
            tabla.add(en2.get(43));
            //}

            // con.close();
        } catch (Exception e) {
        }

        return tabla;
    }

    //Terminado
    @WebMethod(operationName = "consultarRol")
    public String consultarRol(@WebParam(name = "id") long id) {
        ArrayList<String> tabla = new ArrayList<String>();
        String respuesta = null;
        try {
            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT rol from usuario where identificacion ="+id);
                while (rs.next()){
                    tabla.add(rs.getString("rol"));
                }
            respuesta = tabla.get(0);
            con.close();
             */
            PacienteService ps = new PacienteService();
            UsuarioService us = new UsuarioService();
            Paciente gg = ps.pacienteListaId(String.valueOf(id));
            Usuario yyy = new Usuario();
            yyy.setCodUsuario(gg.getCodPaciente());
            Usuario usu = us.sqlbuscCod(yyy);

            return (String) usu.getIdPerfil().getNomPerfil();
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarTemas")
    public List<String> consultarTemas(@WebParam(name = "tema") String tema) {

        try {
            /*   Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select fecha, nombres, apellidos, mensaje from comunidad, usuario where comunidad.participante = usuario.identificacion and comunidad.tema = '"+tema+"'");
            while (rs.next()){
                tabla.add(rs.getString("fecha"));// del remitente
                    String participante = rs.getString("nombres")+" "+rs.getString("apellidos");
                tabla.add(participante);
                tabla.add(rs.getString("mensaje"));
                }
            con.close();
             */
            List<String> mostrar = new ArrayList();
            TemaService ts = new TemaService();
            ComentarioService cs = new ComentarioService();
            Tema tt = ts.busNombreTema(tema);
            List<Comentario> com = tt.getComentarioList();
            for (int a = 0; a < com.size(); a++) {
                Comentario cc = com.get(a);
                mostrar.add(format.format(cc.getFecha()));
                mostrar.add(cc.getNomUsuario().getNomUsuario());
                mostrar.add(cc.getMensajeComent());
            }

            return mostrar;
        } catch (Exception e) {
            List<String> error = new ArrayList();
            error.add(e.toString());
            return error;
        }

    }

    //Falta por terminar
    @WebMethod(operationName = "participarComunidad")
    public String participarComunidad(@WebParam(name = "id") long id, @WebParam(name = "tema") String tema, @WebParam(name = "mensaje") String mensaje) {
        String respuesta = "No se pudo publicar";
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into comunidad values(now(),'"+tema+"', 0,"+id+", '"+mensaje+"')");
                respuesta = "Mensaje publicado!";
             */
            java.util.Date date = new Date();
            ComentarioService cs = new ComentarioService();
            UsuarioService us = new UsuarioService();
            TemaService ts = new TemaService();
            Comentario cc = cs.busTema(ts.busNombreTema(tema));
            Usuario yyy = new Usuario();
            yyy.setCodUsuario(String.valueOf(id));
            cc.setNomUsuario(us.sqlbuscCod(yyy));

            cc.setMensajeComent(mensaje);
            cc.setFecha(date);

            cs.comentarioAdd(cc);
            respuesta = "se publico";
        } catch (Exception e) {
        }
        return respuesta;
    }

    //Probar
    @WebMethod(operationName = "borrarTema")
    public String borrarTema(@WebParam(name = "tema") String tema) {
        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("delete from comunidad where tema = '"+tema+"'");
             */
            ComentarioService cs = new ComentarioService();
            TemaService ts = new TemaService();
            Tema tt = ts.busNombreTema(tema);
            List<Comentario> cc = cs.busTemaList(tt);
            for (int i = 0; i < cc.size(); i++) {
                cc.get(i).setEstado("b");
                cs.comentarioEdit(cc.get(i));
            }
            tt.setEstado("b");
            ts.temaEdit(tt);

        } catch (Exception e) {
        }
        return null;
    }

//MIrar
    @WebMethod(operationName = "consentimiento")
    public String consentimiento(@WebParam(name = "id") long id) {
        String respuesta = "error";
        try {
            //Connection con = conexionBD();
            //Statement st = con.createStatement();
            //  st.executeUpdate("update usuario set aceptaterminos = 1 where identificacion ="+id);

            CodusuarioService cs = new CodusuarioService();
            UsuarioService us = new UsuarioService();
            Usuario yyy = new Usuario();
            yyy.setCodUsuario(String.valueOf(id));
            Usuario aa = us.sqlbuscCod(yyy);
            Codusuario cu = new Codusuario();
            cu.setNomUsuario(aa.getNomUsuario());
            Codusuario yy = cs.busNombre(cu);
            yy.setTerminos(true);
            cs.codusuarioEdit(yy);
            respuesta = "Terminos aceptados";
            return respuesta;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "codusuarioId")
    public int codusuarioId(@WebParam(name = "id") String nom) {
        try {
            //  Connection con = conexionBD();
            //Statement st = con.createStatement();
            //st.executeUpdate("update usuario set aceptaterminos = 1 where identificacion ="+id);

            CodusuarioService cs = new CodusuarioService();

            Codusuario ccc = new Codusuario();

            Codusuario yy = cs.busNombre(ccc);

            return yy.getIdUsuario();
        } catch (Exception e) {
            return 0;
        }

    }

//MIrar
    @WebMethod(operationName = "listaInformacion") // Información general sobre la dmt2
    public ArrayList listaInformacion() {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            /*    Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from informacion");
            while (rs.next()){
                tabla.add(rs.getString("titulo"));
                tabla.add(rs.getString("descripcion"));
                tabla.add(rs.getString("contenido"));
                }
            con.close();
             */
        } catch (Exception e) {
        }
        return tabla;
    }

//Terminado
    @WebMethod(operationName = "actualizarHba1c")
    public String actualizarHba1c(@WebParam(name = "id") Long id, @WebParam(name = "hba1c") String h) {
        String respuesta = "No se pudo completar";

        try {

            /*
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into hba1c values("+id+",now(),"+Float.parseFloat(h.trim())+")");
            respuesta = "Ingreso correcto";
            con.close();
             */
            java.util.Date date = new Date();
            PruebaDiagnosticaService pd = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();
            PruebaDiagnostica pdd = new PruebaDiagnostica();
            TipoIndicadorService ttt = new TipoIndicadorService();
            IndicadorService is = new IndicadorService();
            Indicador ii = new Indicador();

            pdd.setEstado("a");
            pdd.setCodPaciente(ps.pacienteListaId(String.valueOf(id)));
            pdd.setFechaPrueba(date);
            pdd.setNombrePrueba("actualizarHba1c");

            pd.pruebaDiagnosticaAdd(pdd);

            ii.setIdTipoIndicador(ttt.tipoIndicadorListaId(2));
            ii.setValorIndicador(Integer.parseInt(h));
            ii.setEstado("a");
            List<PruebaDiagnostica> hhhh = pd.busUsuarioId(ps.pacienteListaId(String.valueOf(id)));
            ii.setIdPrueba(hhhh.get(hhhh.size() - 1));
            is.indicadorAdd(ii);

        } catch (Exception e) {
            return e.toString();
        }
        return respuesta;
    }

    @WebMethod(operationName = "actualizarPeso")
    public String actualizarPeso(@WebParam(name = "id") Long id, @WebParam(name = "peso") String p) {
        String respuesta = "No se pudo completar";

        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select talla from diagnosticoinicial where paciente = '"+id+"'");
            String talla = null;
            while (rs.next()){    talla = rs.getString("talla");    }
            //Calculo del imc

            double imcTemp= Double.parseDouble(p.trim())/((Double.parseDouble(talla)*Double.parseDouble(talla))/10000);
            int imcFinal = (int) Math.round(imcTemp);

            st.executeUpdate("insert into pesoimc values("+id+",now(),"+Integer.parseInt(p)+","+imcTemp+")");
            respuesta = "Ingreso correcto";
            con.close();
             */
            java.util.Date date = new Date();
            PruebaDiagnosticaService pd = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();
            PruebaDiagnostica pdd = new PruebaDiagnostica();
            TipoIndicadorService ttt = new TipoIndicadorService();
            IndicadorService is = new IndicadorService();
            Indicador ii = new Indicador();

            Indicador ii2 = new Indicador();

            pdd.setEstado("a");
            pdd.setCodPaciente(ps.pacienteListaId(String.valueOf(id)));
            pdd.setFechaPrueba(date);
            pdd.setNombrePrueba("actualizarPeso");

            pd.pruebaDiagnosticaAdd(pdd);

            ii.setIdTipoIndicador(ttt.tipoIndicadorListaId(4));
            ii.setValorIndicador(Integer.parseInt(p));
            ii.setEstado("a");
            List<PruebaDiagnostica> hhhh = pd.busUsuarioId(ps.pacienteListaId(String.valueOf(id)));
            PruebaDiagnostica jjj = hhhh.get(hhhh.size() - 1);
            ii.setIdPrueba(jjj);
            is.indicadorAdd(ii);

            Paciente pac = ps.pacienteListaId(String.valueOf(id));

            List a = pd.buscarTipoPrueba(pac, "talla", "talla");

            String talla = (String) a.get(a.size() - 1);
            float kk = Float.parseFloat(talla);

            ii2.setEstado("a");
            ii2.setIdTipoIndicador(ttt.tipoIndicadorListaId(6));
            int peso = Integer.parseInt(p);
            float imc = (peso / (kk * kk)) * 10000;
            ii2.setValorIndicador(imc);

            ii2.setIdPrueba(jjj);
            is.indicadorAdd(ii2);

            respuesta = "Exito";
        } catch (Exception e) {

            return e.toString();
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "crearObservacion")
    public String crearObservacion(@WebParam(name = "id") long id, @WebParam(name = "paciente") long paciente, @WebParam(name = "texto") String t) {
        String respuesta = "No se pudo guardar";
        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();

            st.executeUpdate("insert into observaciones values("+id+","+paciente+",now(),'"+t+"')");
             */
            java.util.Date date = new Date();
            CodusuarioService gg = new CodusuarioService();
            PacienteService ps = new PacienteService();
            Codusuario ud = new Codusuario();
            Paciente pepe = ps.pacienteListaId(String.valueOf(paciente));
            ud.setNomUsuario(pepe.getCorreo());
            Codusuario cc = gg.busNombre(ud);
            cc.setObservacion(t);
            cc.setFechaObservacion(date);
            gg.codusuarioEdit(cc);
            respuesta = "Información guardada";
        } catch (Exception e) {
        }
        return respuesta;
    }

    //Terminado
    @WebMethod(operationName = "consultarObservaciones") // Información general sobre la dmt2
    public ArrayList consultarObservaciones(@WebParam(name = "id") long id, @WebParam(name = "paciente") long paciente) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from observaciones where id ="+id+" and paciente = "+paciente+" order by fecha");

            while (rs.next()){
                tabla.add(rs.getString("fecha"));
                tabla.add(rs.getString("observacion"));
                }
            con.close();
             */
            CodusuarioService gg = new CodusuarioService();
            PacienteService ps = new PacienteService();
            Codusuario ud = new Codusuario();
            Paciente pepe = ps.pacienteListaId(String.valueOf(paciente));
            ud.setNomUsuario(pepe.getCorreo());
            Codusuario cc = gg.busNombre(ud);
            tabla.add(String.valueOf(cc.getFechaObservacion()));
            tabla.add(cc.getObservacion());

        } catch (Exception ee) {
        }
        return tabla;
    }

    //Falta
    @WebMethod(operationName = "actualizarObservacion")
    public String actualizarObservacion(@WebParam(name = "texto") String t, @WebParam(name = "old") String old) {
        String respuesta = "No se pudo actualizar";
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update observaciones set observacion ='"+t+"' where observacion ='"+old+"'");
                
             */

            respuesta = "Información actualizada";
        } catch (Exception ee) {
        }
        return respuesta;
    }

    @WebMethod(operationName = "crearMedicamento")
    public String crearMedicamento(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") String nombreMedicamento, @WebParam(name = "b") String medioAplicacion, @WebParam(name = "c") int dosis, @WebParam(name = "d") int intervaloHoras, @WebParam(name = "e") boolean recordar, @WebParam(name = "f") String observaciones, @WebParam(name = "g") String hora) {
        String respuesta = "error";
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            System.out.println("El valor de el tiempo es: "+ g);
            st.executeUpdate("insert into medicamento values(null, "+paciente+",now(),'"+a+"','"+b+"',"+c+","+d+","+e+",'"+f+"','"+g+"', 1)");

            con.close();
             */
            java.util.Date date = new Date();
            PacienteService ps = new PacienteService();
            MisionPacienteService jjj = new MisionPacienteService();
            MisionPaciente mp = new MisionPaciente();
            MisionService m = new MisionService();

            mp.setIdMisionPaciente(0);
            mp.setCodPaciente(ps.pacienteListaId(String.valueOf(paciente)));

            mp.setCompletada("0");

            mp.setEstado("m");
            jjj.misionPacienteAdd(mp);

            MedicamentoService ms = new MedicamentoService();
            Medicamento medi = new Medicamento();
            List<Medicamento> mediList = ms.medicamentolista();
            String id2;
            if (mediList.size() < 1) {
                id2 = "1";
            } else {
                id2 = mediList.get(mediList.size() - 1).getCodMedicamento();
            }

            medi.setCodMedicamento(String.valueOf(Integer.parseInt(id2) + 1));
            medi.setNombre(nombreMedicamento);
            medi.setTipo(medioAplicacion);
            medi.setEstado("a");
            ms.medicamentoAdd(medi);

            MedicamentoMisionService mms = new MedicamentoMisionService();
            MedicamentoMision med = new MedicamentoMision();
            med.setDosificación(dosis);
            med.setFecha(date);

            med.setCodMedicamento(ms.busMedicamento(medi));
            med.setIdMisionPaciente(jjj.MisionMedi(ps.pacienteListaId(String.valueOf(paciente))));
            med.setObservaciones(observaciones);
            med.setPosologia(intervaloHoras);
            int myInt = recordar ? 1 : 0;
            med.setRecordar((short) myInt);
            Date date2;
            DateFormat formatter;
            formatter = new SimpleDateFormat("hh:mm");
            date2 = formatter.parse(hora);
            med.setUltimaToma(date2);
            med.setEstado("a");

            mms.MedicamentoMisionAdd(med);

            respuesta = "Se creo correctamente";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ee.toString();
        }
        return respuesta;
    }

    @WebMethod(operationName = "consultarMedicamento")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarMedicamento(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            MedicamentoService consulta = new MedicamentoService();
            Medicamento medicamento = consulta.medicamentoalistaId(jobj.get("codMedicamento").getAsString());
            MedicamentoDTO medicamentoDTO = new MedicamentoDTO(medicamento);
            System.out.println(gson.toJson(medicamentoDTO));
            return gson.toJson(medicamentoDTO);
        } catch (Exception ee) {
            return ee.toString();
        }
    }

    @WebMethod(operationName = "consultarMedicamentos") // Información general sobre la dmt2
    public ArrayList consultarMedicamentos(@WebParam(name = "paciente") long paciente) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from medicamento where paciente ="+paciente+" and estado = 1 order by fecha");
            while (rs.next()){
                tabla.add(rs.getString("nombre"));
                tabla.add(rs.getString("tipo"));
                tabla.add(rs.getString("dosificacion"));
                tabla.add(rs.getString("posologia"));
                tabla.add(rs.getString("recordar"));
                tabla.add(rs.getString("observaciones"));
                tabla.add(rs.getString("ultimatoma"));
                tabla.add(rs.getString("idfila"));
                }
            con.close();
            //--------------------------------------
             */
            MisionPacienteService mpps = new MisionPacienteService();
            Paciente pp = new Paciente();
            pp.setCodPaciente(String.valueOf(paciente));
            MisionPaciente gg = mpps.MisionMedi(pp);

            MedicamentoMisionService mms = new MedicamentoMisionService();

            SimpleDateFormat format = new SimpleDateFormat("hh:mm");

            MedicamentoMision jjj = mms.busMedicamentoMision(gg);
            tabla.add(String.valueOf(jjj.getCodMedicamento().getNombre()));
            tabla.add(String.valueOf(jjj.getCodMedicamento().getTipo()));
            tabla.add(String.valueOf(jjj.getDosificación()));
            tabla.add(String.valueOf(jjj.getPosologia()));
            tabla.add(String.valueOf(jjj.getRecordar()));
            tabla.add(String.valueOf(jjj.getObservaciones()));
            tabla.add(String.valueOf(format.format(jjj.getUltimaToma())));
            tabla.add(String.valueOf(jjj.getIdMedicamentoMision()));

        } catch (Exception ee) {
            ArrayList<String> error = new ArrayList();

            error.add(ee.toString());
            return error;
        }
        return tabla;
    }

    public String consultarMedicamentodos() {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            MedicamentoService n = new MedicamentoService();
            return gson.toJson(n.medicamentolista());
        } catch (Exception n) {
            return "";
        }
    }

    @WebMethod(operationName = "actualizarMedicamento")
    public void actualizarMedicamento(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") String nombreMedicamento, @WebParam(name = "b") String tipoMedicamento,
            @WebParam(name = "c") String dosificacion, @WebParam(name = "d") String posologia, @WebParam(name = "e") String recordar, @WebParam(name = "f") String observaciones,
            @WebParam(name = "g") String ultimatoma, @WebParam(name = "h") String idMisionMedicamento) {
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update medicamento set paciente ="+paciente+", fecha = now(),  nombre ='"+a+"', tipo ='"+b+"', dosificacion ="+c+", posologia ="+d+", recordar ="+e+", observaciones = '"+f+"', ultimatoma ='"+g+"' where idfila = "+h);
            con.close();
             */
            java.util.Date date = new Date();
            PacienteService ps = new PacienteService();
            MisionPacienteService jjj = new MisionPacienteService();
            MisionPaciente mp = new MisionPaciente();
            MisionService m = new MisionService();

            MedicamentoMisionService mms = new MedicamentoMisionService();

            MedicamentoMision med = mms.MedicamentoMisionalistaId(idMisionMedicamento!=null?Integer.parseInt(idMisionMedicamento):0);
            med.setDosificación(dosificacion!=null?Integer.parseInt(dosificacion):0);
            med.setFecha(date);
            med.setObservaciones(observaciones);
            med.setPosologia(posologia!=null?Integer.parseInt(posologia):0);
            int myInt = recordar.equals("1") ? 1 : 0;
            med.setRecordar((short) myInt);
            Date date2;
            DateFormat formatter;
            formatter = new SimpleDateFormat("hh:mm");
            date2 = formatter.parse(ultimatoma);
            med.setUltimaToma(date2);

            mms.MedicamentoMisionEdit(med);
            MedicamentoService ms = new MedicamentoService();

            Medicamento medi = new Medicamento();

            medi.setCodMedicamento(String.valueOf(med.getCodMedicamento().getCodMedicamento()));
            medi.setNombre(nombreMedicamento);
            medi.setTipo(tipoMedicamento);
            medi.setEstado("a");
            ms.medicamentoEdit(medi);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @WebMethod(operationName = "desactivarMedicamento")
    public void desactivarMedicamento(@WebParam(name = "paciente") long paciente, @WebParam(name = "h") int h) {
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update medicamento set estado = 0 where idfila = "+h);
             */
            MedicamentoMisionService mm = new MedicamentoMisionService();
            MedicamentoMision mms = mm.MedicamentoMisionalistaId(h);

            mms.setEstado("b");
            mm.MedicamentoMisionEdit(mms);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @WebMethod(operationName = "consultarMetas")
    public ArrayList consultarMetas(@WebParam(name = "paciente") long paciente) {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            /*Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from metas where id ="+paciente);
            while (rs.next()){
                tabla.add(rs.getString("meta1"));
                tabla.add(rs.getString("meta2"));
                tabla.add(rs.getString("meta3"));
                tabla.add(rs.getString("meta4"));
                tabla.add(rs.getString("meta5"));
                tabla.add(rs.getString("meta6"));
                tabla.add(rs.getString("meta7"));
                tabla.add(rs.getString("meta8"));
                tabla.add(rs.getString("meta9"));
                tabla.add(rs.getString("meta10"));
                }
            con.close();
             */
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return tabla;
    }

    @WebMethod(operationName = "crearMetas")
    public String crearMetas(@WebParam(name = "paciente") long paciente, @WebParam(name = "a") boolean a, @WebParam(name = "b") boolean b, @WebParam(name = "c") boolean c, @WebParam(name = "d") boolean d, @WebParam(name = "e") boolean e, @WebParam(name = "f") boolean f, @WebParam(name = "g") boolean g, @WebParam(name = "h") boolean h, @WebParam(name = "i") boolean i, @WebParam(name = "j") boolean j) {
        String respuesta = "No se pudo guardar!";
        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update metas set fecha = now(), meta1="+a+", meta2="+b+", meta3="+c+", meta4="+d+", meta5="+e+", meta6="+f+", meta7="+g+", meta8="+h+", meta9 ="+i+", meta10 ="+j+" where id = "+paciente);
            con.close();
            respuesta = "Metas guardadas!";
             */
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return respuesta;
    }

    @WebMethod(operationName = "crearHabito")
    public String crearHabito(@WebParam(name = "titulo") String titulo, @WebParam(name = "descripcion") String descripcion, @WebParam(name = "contenido") String contenido, @WebParam(name = "imagen") String imagen) {
        String respuesta = "No se pudo cargar la información";
        try {
            Calendar c = new GregorianCalendar();
            String nom = Long.toString(c.getTimeInMillis());
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into habitos values (null,'" + titulo + "','" + descripcion + "','" + contenido + "','" + nom + "')");
            st.executeUpdate("update notificaciones set recordatorio = 1 ");
            byte[] decodificar = Base64.decodeBase64(imagen.getBytes());
            OutputStream salida = new FileOutputStream(destinoRecurso + nom);
            salida.write(decodificar);
            salida.close();
            con.close();
            respuesta = "Información cargada con éxito!";
        } catch (Exception e) {
        }
        return respuesta;
    }

    @WebMethod(operationName = "modificarHabito")
    public String modificarHabito(@WebParam(name = "titulo") String titulo, @WebParam(name = "descripcion") String descripcion, @WebParam(name = "contenido") String contenido, @WebParam(name = "imagen") String imagen, @WebParam(name = "idfila") String idfila, @WebParam(name = "imgenvieja") String imgenvieja) {
        String respuesta = "No se pudo modificar";
        try {
            Calendar c = new GregorianCalendar();
            String nom = null;
            if (imagen.length() < 5) { //Caso en el que no se cambia la imagen
                nom = imgenvieja;
            } else {
                nom = Long.toString(c.getTimeInMillis());
                byte[] decodificar = Base64.decodeBase64(imagen.getBytes());
                OutputStream salida = new FileOutputStream(destinoRecurso + nom);
                salida.write(decodificar);
                salida.close();
            }
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update habitos set titulo='" + titulo + "',descripcion='" + descripcion + "',contenido='" + contenido + "',imagen='" + nom + "' where idfila="
                    + Integer.parseInt(idfila) + "");
            con.close();
            return respuesta = "Hábito actualizado!";
        } catch (Exception e) {
        }
        return null;
    }

    @WebMethod(operationName = "habitos")
    public ArrayList habitos() {
        ArrayList<String> tabla = new ArrayList<String>();
        try {
            Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from habitos");
            while (rs.next()) {
                tabla.add(rs.getString("idfila"));
                tabla.add(rs.getString("titulo"));
                tabla.add(rs.getString("descripcion"));
                tabla.add(rs.getString("contenido"));
                tabla.add(rs.getString("imagen"));
            }
            con.close();
        } catch (Exception e) {
        }
        return tabla;
    }

//@WebMethod(operationName = "borrarBD")
    public void borrarBD() {
        String respuesta = "No se pudo cargar la información";
        try {
            Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("delete from trigliceridos where 1");
            st.executeUpdate("delete from tratantes where 1");
            st.executeUpdate("delete from tension where 1");
            st.executeUpdate("delete from recursos where 1");
            st.executeUpdate("delete from pesoimc where 1");
            st.executeUpdate("delete from percepcionriesgo where 1");
            st.executeUpdate("delete from paciente where 1");
            st.executeUpdate("delete from observaciones where 1");
            st.executeUpdate("delete from notificaciones where 1");
            st.executeUpdate("delete from metas where 1");
            st.executeUpdate("delete from mensajes where 1");
            st.executeUpdate("delete from medicamento where 1");
            //st.executeUpdate("delete from informacion where 1");
            st.executeUpdate("delete from hba1c where 1");
            //st.executeUpdate("delete from habitos where 1");
            st.executeUpdate("delete from fyc where 1");
            st.executeUpdate("delete from estilovida where 1");
            st.executeUpdate("delete from estadoanimo where 1");
            st.executeUpdate("delete from diagnosticoinicial where 1");
            st.executeUpdate("delete from comunidad where 1");
            st.executeUpdate("delete from comorbilidad where 1");
            st.executeUpdate("delete from avances where 1");
            st.executeUpdate("delete from autoeficacia where 1");
            st.executeUpdate("delete from autocuidado where 1");
            st.executeUpdate("delete from apoyosocial where 1");
            st.executeUpdate("delete from animo where 1");
            st.executeUpdate("delete from actividadfisica where 1");
            st.executeUpdate("delete from usuario where rol = 'paciente' or rol = 'familiar' or rol = 'profesional'");
            con.close();
        } catch (Exception e) {
        }
    }

    //Estadísticas  ------------------------------------------------------------
    Hashtable<Long, Long> tiempos = new Hashtable<>();

    @WebMethod(operationName = "ti")
    public void ti(@WebParam(name = "id") Long id) {
        tiempos.put(id, System.currentTimeMillis()); //El id y el tiempo en que entra en la actividad
    }
    //no aplica

    public void tf(Long id, String nombreMedicion) {
        try {
            Connection con = conexionBD();
            Statement st = con.createStatement();
            //Caso 1: Paciente desde usuario hasta estilo de vida
            if (nombreMedicion.contains("ingresoPaciente1")) {
                st.executeUpdate("insert into tiemposdeuso values (" + id + ",now(),0," + (System.currentTimeMillis() - tiempos.get(id)) / 1000 + ",0,0,0)");
            }
            //Caso 2: Paciente desde Actividad física hasta AEP
            if (nombreMedicion.contains("ingresoPaciente2")) {
                st.executeUpdate("insert into tiemposdeuso values (" + id + ",now(),0,0," + (System.currentTimeMillis() - tiempos.get(id)) / 1000 + ",0,0)");
            }
            //Caso 3: Creación de paciente
            if (nombreMedicion.contains("crearPaciente")) {
                st.executeUpdate("insert into tiemposdeuso values (" + id + ",now()," + (System.currentTimeMillis() - tiempos.get(id)) / 1000 + ",0,0,0,0)");
            }
            //Caso 4: Creación de recurso
            if (nombreMedicion.contains("crearRecurso")) {
                st.executeUpdate("insert into tiemposdeuso values (" + id + ",now(),0,0,0," + (System.currentTimeMillis() - tiempos.get(id)) / 1000 + ",0)");
            }
        } catch (Exception e) {
        }
    }
    //encuestas

    @WebMethod(operationName = "encuestaPro")
    public void encuestaPro(@WebParam(name = "id") long id) {
        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("update avances set completado = 2 where identificacion ="+id ); //Poner en 2 completado de un profesional no activa más la encuesta
            con.close();
             */
            ProfesionalService pss = new ProfesionalService();
            Profesional pro = pss.profesionalListaId(String.valueOf(id));
            CodusuarioService css = new CodusuarioService();
            Codusuario cu = new Codusuario();
            cu.setNomUsuario(pro.getCorreo());
            Codusuario ccc = css.busNombre(cu);
            ccc.setAvances(2);
            css.codusuarioEdit(ccc);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    //Terminado
    @WebMethod(operationName = "crearGlucosa")
    public void crearGlucosa(@WebParam(name = "id") long id, @WebParam(name = "glucosa") int glucosa) {
        try {
            /*  Connection con = conexionBD();
            Statement st = con.createStatement();
            st.executeUpdate("insert into glucosa values("+id+",now(),"+glucosa+")" );
            con.close();
             */
            java.util.Date date = new Date();
            PruebaDiagnosticaService pd = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();
            PruebaDiagnostica pdd = new PruebaDiagnostica();
            TipoIndicadorService ttt = new TipoIndicadorService();
            IndicadorService is = new IndicadorService();
            Indicador ii = new Indicador();

            pdd.setEstado("a");
            pdd.setCodPaciente(ps.pacienteListaId(String.valueOf(id)));
            pdd.setFechaPrueba(date);
            pdd.setNombrePrueba("actualizarGlucosa");

            pd.pruebaDiagnosticaAdd(pdd);

            ii.setIdTipoIndicador(ttt.tipoIndicadorListaId(7));
            ii.setValorIndicador(Float.parseFloat(String.valueOf(glucosa)));
            ii.setEstado("a");
            List<PruebaDiagnostica> hhhh = pd.busUsuarioId(ps.pacienteListaId(String.valueOf(id)));
            ii.setIdPrueba(hhhh.get(hhhh.size() - 1));
            is.indicadorAdd(ii);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @WebMethod(operationName = "consultarGlucosa")
    public List<String> consultarGlucosa(@WebParam(name = "id") long id) {

        try {
            /* Connection con = conexionBD();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from glucosa where id ="+id+" order by fechahora");
            while (rs.next()){
                tabla.add(rs.getString("fechahora"));
                tabla.add(rs.getString("glucosa"));
                }
            con.close();
             */
            PruebaDiagnosticaService pd = new PruebaDiagnosticaService();
            PacienteService ps = new PacienteService();

            Paciente pac = ps.pacienteListaId(String.valueOf(id));

            List<String> mostrar = new ArrayList();

            List a = pd.buscarTipoPrueba(pac, "glucosa", "actualizarGlucosa");

            mostrar.add((String) a.get(0));
            mostrar.add((String) a.get(1));

            return mostrar;

        } catch (Exception e) {
            List<String> error = new ArrayList();

            error.add(e.toString());
            return error;
        }

    }

    //==========================Web services usando la nueva BD================================================
//======================================================================
    @WebMethod(operationName = "consultarCategorias")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarCategorias() throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            CategoriaService a = new CategoriaService();
            return gson.toJson(a.categroiaLista());
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod(operationName = "consultarCategoriaId")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarCategoriasId(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            CategoriaService a = new CategoriaService();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            return gson.toJson(a.categorialistaId(jobj.get("idPaso").getAsInt()));
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod(operationName = "modificarCategoria")
    public @WebResult(partName = "checkVerifyResponse")
    void modificarCategoria(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            CategoriaService a = new CategoriaService();
            a.categoriaEdit(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "modificarComentario")
    public @WebResult(partName = "checkVerifyResponse")
    String modificarComentario(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        Comentario recibido = gson.fromJson(jsonString, Comentario.class);
        ComentarioService ne = new ComentarioService();
        Comentario original = ne.comentariolistaId(recibido.getIdComentario());
        //clonar(original,recibido);
        ne.comentarioEdit(recibido);
        return "editado";
    }

    @WebMethod(operationName = "borrarCategoria")
    public @WebResult(partName = "checkVerifyResponse")
    void borrarCategoria(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            CategoriaService a = new CategoriaService();
            a.categoriaDelete(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "crearCategorias")
    public @WebResult(partName = "checkVerifyResponse")
    void crearCategorias(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            CategoriaService a = new CategoriaService();
            a.categoriaAdd(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "crearPaso")
    public @WebResult(partName = "checkVerifyResponse")
    String crearPaso(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            PasoService a = new PasoService();
            int cod = a.pasoAdd(jsonString);

            return gson.toJson(cod);
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarPasos")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarPasos() throws CheckVerifyFault {
        try {
            PasoService a = new PasoService();
            return gson.toJson(a.pasolista());
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod(operationName = "consultarPasoId")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarPasoId(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            PasoService a = new PasoService();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            return gson.toJson(a.pasolistaId(jobj.get("idPaso").getAsInt()));
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod(operationName = "modificarPaso")
    public @WebResult(partName = "checkVerifyResponse")
    void modificarPaso(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            PasoService a = new PasoService();
            a.pasoEdit(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "borrarPaso")
    public @WebResult(partName = "checkVerifyResponse")
    void borrarPaso(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            PasoService a = new PasoService();
            a.pasoDelete(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "crearLogro")
    public @WebResult(partName = "checkVerifyResponse")
    void crearlogro(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            LogroService a = new LogroService();
            a.logroAdd(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "consultarLogros")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarLogros() {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            LogroService a = new LogroService();
            return gson.toJson(a.logrolista());

        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "consultarLogroId")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarLogroId(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            LogroService a = new LogroService();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            return gson.toJson(a.logrolistaId(jobj.get("idLogro").getAsInt()));
        } catch (Exception e) {
            return null;
        }
    }

    @WebMethod(operationName = "modificarLogro")
    public @WebResult(partName = "checkVerifyResponse")
    void modificarLogro(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            LogroService a = new LogroService();
            a.logroEdit(jsonString);
        } catch (Exception e) {
        }
    }

    @WebMethod(operationName = "borrarLogro")
    public @WebResult(partName = "checkVerifyResponse")
    String borrarLogro(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            LogroService a = new LogroService();
            a.logroDelete(jsonString);
            return "ok";
        } catch (Exception e) {
            return "error";
        }

    }

    /*@WebMethod(operationName = "testdrools")
    public @WebResult(partName="checkVerifyResponse") String testdrools() throws CheckVerifyFault    {
        try{


        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        KieFileSystem kfs = ks.newKieFileSystem();

        kfs.write(ResourceFactory.newClassPathResource("rules/rules.drl", this.getClass()));

        KieBuilder kb = ks.newKieBuilder(kfs);

        kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }

        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());

        KieSession kSession = kContainer.newKieSession();
        Paciente a=new Paciente();
        a.setApellido("bermudez");
        kSession.insert(a);
        kSession.fireAllRules();

               return a.getMensaje();

        }
        catch(Exception e){
            return e.toString();
 }
  }  */
    @WebMethod(operationName = "consultaPasoscat")
    public @WebResult(partName = "checkVerifyResponse")
    String consultapasoscat(@WebParam(name = "jsonString") String jsonString) {
        try {
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            Integer cat = Integer.parseInt(jobj.get("categoria").toString());
            PasoService pasos = new PasoService();
            List<Paso> salida = pasos.consultapasocat(cat);
            return gson.toJson(salida);
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "crearRecurso")
    public @WebResult(partName = "checkVerifyResponse")
    String crearRecurso(@WebParam(name = "jsonString") String jsonString) {

        try {
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            byte[] decodificar = Base64.decodeBase64(jobj.get("imgCod").getAsString().getBytes());
            Calendar c = new GregorianCalendar();
            String nom = Long.toString(c.getTimeInMillis());
            OutputStream salida = new FileOutputStream(destinoRecurso + nom);
            salida.write(decodificar);
            salida.close();
            jobj.addProperty("imagen", nom);
            jobj.remove("imgCod");
            //hola

            RecursoApoyoService recser = new RecursoApoyoService();
            RecursoApoyo rec = gson.fromJson(jobj, RecursoApoyo.class);
            recser.recursoApoyoAdd2(rec);
            // tf(Long.parseLong(rec.getNomUsuario().getNomUsuario()),"crearRecurso");

            return " recurso creado";
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarMisiones")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarMisiones() throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            ArrayList<String> nombres = new ArrayList<String>();
            MisionService misiones = new MisionService();
            List<Mision> asd = misiones.misionLista();
            return gson.toJson(asd);
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarAIIUDA")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarAIIUDA(String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            MisionService edicion = new MisionService();
            Mision cambio = gson.fromJson(jsonString, Mision.class);
            Mision original = edicion.misionlistaId(cambio.getIdMision());
            clonar(cambio, original);
            edicion.misionEdit(original);
            return "modificado exitosamente";
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarMisionId")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarMisionId(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {

        try {
            MisionService a = new MisionService();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            return gson.toJson(a.misionlistaId(jobj.get("idMision").getAsInt()));
        } catch (Exception e) {
            return null;
        }
    }

    public @WebResult(partName = "checkVerifyResponse")
    String medmision(@WebParam(name = "jsonString") String jsonString) {

        try {

            JsonObject job = gson.fromJson(jsonString, JsonObject.class);
            Medicamento crear = new Medicamento();
            MedicamentoService aniadir = new MedicamentoService();
            crear.setCodMedicamento(job.get("codMedicamento").toString());
            crear.setNombre(job.get("nombre").toString());
            crear.setTipo(job.get("tipo").toString());
            crear.setEstado("a");
            aniadir.medicamentoAdd(crear);

            MedicamentoMision memision = new MedicamentoMision();
            memision.setCodMedicamento(crear);
            memision.setIdMisionPaciente(null);

            memision.setEstado("a");

            return null;
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @WebMethod(operationName = "crearMision")
    public @WebResult(partName = "checkVerifyResponse")
    String crearMision(@WebParam(name = "jsonString") String jsonString) {

        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            MisionService misiser = new MisionService();
            JsonObject job = new JsonObject();
            job.addProperty("cod", misiser.misionAdd2(jsonString));
            return gson.toJson(job).toString();

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarRecursos")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarRecursos() throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            RecursoApoyoService n = new RecursoApoyoService();
            List<RecursoApoyo> lista = new ArrayList<>();
            for (int i = 0; i < n.recursoApoyolista().size(); i++) {
                if (n.recursoApoyolista().get(i).getEstado().equals("a")) {
                    lista.add(n.recursoApoyolista().get(i));
                }
            }
            return gson.toJson(lista);

        } catch (Exception e) {

            return e.toString();
        }

    }

    @WebMethod(operationName = "crearmedmision")
    public @WebResult(partName = "checkVerifyResponse")
    String crearmedmision(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        JsonObject job = gson.fromJson(jsonString, JsonObject.class);
        JsonObject medicamento = new JsonObject();
        JsonArray medicamentoMisionList = new JsonArray();
        medicamento.add("codMedicamento", job.get("codMedicamento"));
        medicamento.add("nombre", job.get("nombre"));
        medicamento.add("tipo", job.get("tipo"));
        medicamento.add("medicamentoMisionList", medicamentoMisionList);
        medicamento.addProperty("estado", "a");

        MedicamentoService med = new MedicamentoService();
        MedicamentoMisionService medi = new MedicamentoMisionService();
        Medicamento medobj = new Medicamento();
        MedicamentoMision mesion = new MedicamentoMision();
        try {
            medobj = gson.fromJson(medicamento, Medicamento.class);
            //--------------------------------------------
            JsonObject medmision = new JsonObject();
            medmision.add("idMedicamentoMision", job.get("idMedicamentoMision"));
            medmision.add("fecha", job.get("fecha"));
            medmision.add("dosificación", job.get("dosificación"));
            medmision.add("posologia", job.get("posologia"));
            medmision.add("recordar", job.get("recordar"));
            medmision.add("ultimaToma", job.get("ultimaToma"));
            medmision.add("observaciones", job.get("observaciones"));
            medmision.addProperty("estado", "a");
            medmision.add("codMedicamento", medicamento);

            //--------------------------------------------
            mesion = gson.fromJson(medmision, MedicamentoMision.class);
            med.medicamentoAdd(medobj);
            medi.MedicamentoMisionAdd(mesion);
            return medmision.toString();
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "modificarPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String ModificarPaciente(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            PacienteService ps = new PacienteService();
            ps.PacienteEdit(jsonString);
            return jsonString;
        } catch (Exception e) {
            return e.toString() + jsonString;
        }
    }

    @WebMethod(operationName = "consultarPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarPaciente() throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            PacienteService n = new PacienteService();
            return gson.toJson(n.PacienteLista());
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarMPlogro")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarMPlogro() throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            MisionPasoLogroService n = new MisionPasoLogroService();
            return gson.toJson(n.mpasologroLista());
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "borrarPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String borrarPaciente(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            PacienteService a = new PacienteService();
            a.PacienteDelete(jsonString);
            return "ok";
        } catch (Exception e) {
            return "error";
        }

    }

    @WebMethod(operationName = "crearPasos")
    public @WebResult(partName = "checkVerifyResponse")
    void crearPasos(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        Type listType = new TypeToken<List<Paso>>() {
        }.getType();
        ArrayList<Paso> posts = gson.fromJson(jsonString, listType);
        PasoService creacion = new PasoService();
        for (int a = 0; a < posts.size(); a++) {
            creacion.pasobjAdd(posts.get(a));
        }

    }

    @WebMethod(operationName = "crearMPLogro")
    public @WebResult(partName = "checkVerifyResponse")
    String crearMPLogro(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {

        JsonObject hola = gson.fromJson(jsonString, JsonObject.class);
        JsonObject entrada;
        JsonArray asd = hola.get("cod").getAsJsonArray();
        MisionPasoLogroService aniadir = new MisionPasoLogroService();
        MisionPasoLogro n;
        Logro log = new Logro();
        Mision mis = new Mision();
        Paso pas = new Paso();
        for (int a = 0; a < asd.size(); a++) {
            entrada = asd.get(a).getAsJsonObject();
            n = new MisionPasoLogro();
            if (entrada.get("logro").getAsInt() == -1 || entrada.get("logro").getAsInt() == 0) {
                log.setIdLogro(38);

            } else {
                log.setIdLogro(entrada.get("logro").getAsInt());

            }
            n.setIdLogro(log);
            mis.setIdMision(entrada.get("mision").getAsInt());
            n.setIdMision(mis);
            pas.setIdPaso(entrada.get("paso").getAsInt());
            n.setIdPaso(pas);
            n.setPasoNumero(entrada.get("pasoNumero").getAsInt());
            n.setEstado("a");
            aniadir.misionPasoLogroAdd(n);
        }
        /* Type listType = new TypeToken<List<MisionPasoLogro>>(){}.getType();
        ArrayList<MisionPasoLogro> posts = gson.fromJson(jsonString, listType);
        MisionPasoLogroService creacion=new MisionPasoLogroService();
        for(int a=0;a<posts.size();a++){
            creacion.misionPasoLogroAdd(posts.get(a));
        }*/
        return "Se creo";
    }

    @WebMethod(operationName = "consultarpanom")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarpanom(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            CategoriaService x = new CategoriaService();
            PasoService pasi = new PasoService();
            int codigo = x.busCategoriaNom(jsonString);
            return gson.toJson(pasi.consultapasocat(codigo));

        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "consultarMisPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarMisPaciente(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            String codigo = jobj.get("codPaciente").getAsString();
            MisionPacienteService mispacser = new MisionPacienteService();

            Paciente pac = new Paciente();
            pac.setCodPaciente(codigo);

            List<MisionPaciente> lista = new ArrayList<MisionPaciente>();

            try {
                lista = mispacser.misionPacientelista(pac);
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getCompletada().equals("0") || lista.get(i).getCompletada().equals("2")) {
                        lista.remove(i);
                    }
                }
            } catch (Exception e) {

                return gson.toJson(lista);
            };

            return gson.toJson(lista);

        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "consultarpanomdos")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarpanomdos(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonArray salida = new JsonArray();
            CategoriaService x = new CategoriaService();
            PasoService pasi = new PasoService();
            MisionPasoLogroService consultas = new MisionPasoLogroService();
            MisionService n = new MisionService();
            int codigo = x.busCategoriaNom(jsonString);
            Mision m = n.misionget(jsonString);
            List<Paso> pasos = pasi.consultapasocat(codigo);
            for (Paso elemento : pasos) {
                JsonObject elemen = gson.toJsonTree(elemento).getAsJsonObject();
                elemen.addProperty("logro", consultas.mplbusc(m, elemento));
                salida.add(elemen);
            }

            return gson.toJson(salida);

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "borrarMisiones")
    public @WebResult(partName = "checkVerifyResponse")
    String borrarMisiones(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            MisionService n = new MisionService();

            n.misionDelete(jsonString);
            return "La misión se ha borrado con exito";

        } catch (Exception e) {
            return e.toString() + jsonString;
        }

    }

    @WebMethod(operationName = "modificarMision")
    public @WebResult(partName = "checkVerifyResponse")
    String modificarMision(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            /*
              MisionService mic=new MisionService();
              Mision mision = gson.fromJson(jsonString,Mision.class);
              Mision origen = mic.misionlistaId(mision.getIdMision());
              clonar(mision,origen);
              mic.misionEdit(mision);

             */
            MisionService ps = new MisionService();
            ps.misionEdit(jsonString);

            return "Funciono";

        } catch (Exception e) {
            return e.toString() + jsonString;
        }

    }

    public static Object clonar(Object object_a, Object object_b) throws Exception {
        Method[] gettersAndSetters = object_a.getClass().getMethods();
        for (int i = 0; i < gettersAndSetters.length; i++) {
            String methodName = gettersAndSetters[i].getName();
            try {
                if (methodName.startsWith("get")) {
                    if (gettersAndSetters[i].invoke(object_a, null) != null) {
                        object_a.getClass().getMethod(methodName.replaceFirst("get", "set"), gettersAndSetters[i].getReturnType()).invoke(object_b, gettersAndSetters[i].invoke(object_a, null));
                    }
                } else if (methodName.startsWith("is")) {
                    object_a.getClass().getMethod(methodName.replaceFirst("is", "set"), gettersAndSetters[i].getReturnType()).invoke(object_b, gettersAndSetters[i].invoke(object_a, null));
                }

            } catch (NoSuchMethodException e) {
                // TODO: handle exception
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
            }
        }
        return object_a;
    }

    @WebMethod(operationName = "consNuevasMisionesPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String consNuevasMisionesPaciente(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            //trae Puntaje obtenido por el paciente
            String codigo = jobj.get("codPaciente").getAsString().toString();
            PacienteService pacser = new PacienteService();
            Paciente pac = new Paciente();
            pac.setCodPaciente(codigo);
            Paciente paciente = pacser.busPaciente(pac);

            int alim = paciente.getPuntajeAlim();
            int ejer = paciente.getPuntajeEjer();
            NivelPacienteService nivSer = new NivelPacienteService();
            MisionPacienteService mpss = new MisionPacienteService();
            MisionService miseSer = new MisionService();
            NivelService ns = new NivelService();
            List<Mision> todaM = miseSer.misionLista();
            List<Mision> filtroM = mpss.filtroMisionCompletad(paciente);
            List<Mision> misionv = new ArrayList<>();
            List<Mision> misionA = new ArrayList<>();
            List<NivelPaciente> nivelPacienteAlim = nivSer.filtroNivelAlimentacion(paciente);
            List<NivelPaciente> nivelPacienteEjer = nivSer.filtroNivelEjercicio(paciente);

            for (int i = 0; i < filtroM.size(); i++) {

                todaM.remove(filtroM.get(i));

            }

            for (int i = 0; i < nivelPacienteAlim.size(); i++) {
                misionA.addAll(miseSer.sqlbuscNombreAlimentacion(ns.sqlbuscNivel(nivelPacienteAlim.get(i))));

            }

            for (int i = 0; i < nivelPacienteEjer.size(); i++) {

                misionv.addAll(miseSer.sqlbuscNombreEjercicio(ns.sqlbuscNivel(nivelPacienteEjer.get(i))));
            }
            for (int k = 0; k < todaM.size(); k++) {
                //if(!misionv.get(j).getIdNivel().getNombre().equals(todaM.get(k).getIdNivel().getNombre()) && misionv.get(j).getIdMision().equals(todaM.get(k).getIdMision())){
                if (!misionv.contains(todaM.get(k)) && !misionA.contains(todaM.get(k))) {
                    todaM.remove(todaM.get(k));
                }
            }

            for (int i = 0; i < todaM.size(); i++) {
                if (todaM.get(i).getIdTipoMision().getIdTipoMision() == 2) {
                    todaM.remove(i);
                }
            }

            return gson.toJson(todaM);
        } catch (Exception e) {
            return e.toString() + jsonString;
        }
    }

    @WebMethod(operationName = "consultarTodosPasos")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarTodosPasos(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        Mision jobj = gson.fromJson(jsonString, Mision.class);

        MisionPasoLogroService consultas = new MisionPasoLogroService();

        List<MisionPasoLogro> pp = consultas.mplbuscMis(jobj);

        if (pp.size() == 0) {
            return "null";
        } else {
            return gson.toJson(pp);
        }
    }

    @WebMethod(operationName = "consultarRecursoMision")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarRecursoMision(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            MisionTipoRecursoService mtps = new MisionTipoRecursoService();
            Mision mision = gson.fromJson(jsonString, Mision.class);
            RecursoApoyoService rap = new RecursoApoyoService();
            List<MisionTipoRecurso> tm = mtps.sqlbuscRecursoMision(mision);
            List<RecursoApoyo> ra = new ArrayList<RecursoApoyo>();
            for (int i = 0; i < tm.size(); i++) {
                if (tm.get(i).getEstado().equals("a")) {
                    ra.add(rap.recursoApoyolistaId(tm.get(i).getIdRecurso()));
                }
            }

            return gson.toJson(ra);

        } catch (Exception e) {

        }
        return "Murio el recurso mision";

    }

    @WebMethod(operationName = "consultarForoMision")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarForoMision(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            MisionTipoRecursoService mtps = new MisionTipoRecursoService();
            Mision mision = gson.fromJson(jsonString, Mision.class);
            TemaService rap = new TemaService();
            List<MisionTipoRecurso> tm = mtps.sqlbuscForoMision(mision);
            List<Tema> ra = new ArrayList<Tema>();
            for (int i = 0; i < tm.size(); i++) {
                ra.add(rap.temalistaId(tm.get(i).getIdRecurso()));
            }

            return gson.toJson(ra);

        } catch (Exception e) {

        }
        return "Murio el foro mision";

    }

    @WebMethod(operationName = "asignarMisionPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String asignarMisionPaciente(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        java.util.Date date = new Date();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
        //trae Puntaje obtenido por el paciente
        String codigo = jobj.get("codPaciente").getAsString();
        int idMision = jobj.get("idMision").getAsInt();
        PacienteService pacser = new PacienteService();
        MisionPacienteService mispacser = new MisionPacienteService();
        Paciente pac = new Paciente();
        pac.setCodPaciente(codigo);
        MisionService misser = new MisionService();
        Paciente paciente = pacser.busPaciente(pac);
        Mision mision = misser.misionlistaId((idMision));
        MisionPaciente mispac = new MisionPaciente();
        mispac.setCodPaciente(paciente);
        mispac.setIdMisionPaciente(0);
        mispac.setIdMision(mision);
        mispac.setCompletada("1");
        mispac.setFechaInicio(date);
        mispac.setEstado("a");
        try {
            mispacser.misionPacienteAdd(mispac);
        } catch (Exception ex) {
            return "null";
        }

        return "MisionCreada";

    }
    //-----------

    @WebMethod(operationName = "crearVerificacion")
    public @WebResult(partName = "checkVerifyResponse")
    String crearVerificacion(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        java.util.Date date = new Date();
        String mm = "No se creo la verificacion";
        int yy = 0;
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            VerificacionService vs = new VerificacionService();

            int MisionPaciente = jobj.get("idMisionPaciente").getAsInt();

            int numeroDia = jobj.get("numeroDia").getAsInt();
            boolean vPaciente = jobj.get("verifPaciente").getAsBoolean();

            Verificacion vv = new Verificacion();
            MisionPaciente mps = new MisionPaciente();
            MisionPacienteService mpss = new MisionPacienteService();
            MisionPasoLogroService misionps = new MisionPasoLogroService();
            PacienteService ps = new PacienteService();
            mps.setIdMisionPaciente(MisionPaciente);

            List<Verificacion> listVerificacion = vs.sqlbuscVerificacionPorMPN(mps, numeroDia);
            if (listVerificacion != null && listVerificacion.size() != 0 && listVerificacion.get(0).getNumeroDia() == numeroDia) {
                listVerificacion.get(0).setVerifPaciente(vPaciente);
                vs.verificacionEdit(listVerificacion.get(0));
            } else {
                vv.setIdMisionPaciente(mps);
                vv.setNumeroDia(numeroDia);
                vv.setVerifPaciente(vPaciente);
                vv.setFecha(date);
                vv.setEstado("a");
                vs.verificacionAdd(vv);
            }

            List<MisionPasoLogro> pp = misionps.mplbuscMis(mpss.misionPacienteListaId(MisionPaciente).getIdMision());

            if (vPaciente == true && listVerificacion.get(0).getVerifApoyoSocial() == true && pp.get(0).getIdPaso().getDiasDuracion() == numeroDia) {
                MisionPaciente mppo = mpss.misionPacienteListaId(MisionPaciente);
                mppo.setCompletada("2");
                mpss.misionPacienteEdit(mppo);
            }

            Paciente p = ps.pacienteListaId(mpss.misionPacienteListaId(MisionPaciente).getCodPaciente().getCodPaciente());
            if (pp.get(0).getIdMision().getIdCategoria().getIdCategoria() == 2) {
                yy = p.getPuntajeEjer() + pp.get(numeroDia - 1).getIdLogro().getPuntos();
                p.setPuntajeEjer(yy);
            } else if (pp.get(0).getIdMision().getIdCategoria().getIdCategoria() == 3) {

                yy = p.getPuntajeAlim() + pp.get(numeroDia - 1).getIdLogro().getPuntos();
                p.setPuntajeAlim(yy);

            }

            ps.PacienteEdit(p);

            mm = "Se creo la verificacion";
            return mm;

        } catch (Exception e) {
            return e.toString();
        }

    }
    //.-----------

    @WebMethod(operationName = "consultarVerificacionPorMP")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarVerificacionPorMP(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {

        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            MisionPaciente mp = gson.fromJson(jsonString, MisionPaciente.class);

            VerificacionService vs = new VerificacionService();

            return gson.toJson(vs.sqlbuscVerificacionPorMP(mp));

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "modpasos")
    public @WebResult(partName = "checkVerifyResponse")
    String modpasos(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            JsonObject entrada = gson.fromJson(jsonString, JsonObject.class);
            Mision m = new Mision();
            m.setIdMision(entrada.get("idMision").getAsInt());
            MisionPasoLogroService busc = new MisionPasoLogroService();
            List<MisionPasoLogro> borrados = busc.mplbuscMis(m);
            int cod = borrados.get(0).getIdPaso().getIdPaso();
            //-------------------------------------------------------------------------------
            for (int a = 0; a < borrados.size(); a++) {
                MisionPasoLogro borrado = borrados.get(a);
                busc.misionPasoLogroDelete(borrado.getIdMisionPasoLogro());
            }
            //-------------------------------------------------------------------------------
            PasoService ca = new PasoService();
            Paso cambio = gson.fromJson(entrada.get("paso").getAsJsonObject().toString(), Paso.class);
            cambio.setIdPaso(cod);
            ca.pasoEdit(cambio);
            return String.valueOf(cod);

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarPasoIdm")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarPasoIdm(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            MisionPasoLogroService a = new MisionPasoLogroService();
            return a.consultarPasoIdm(jsonString);
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "consultarPasoVerificacion")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarPasoVerificacion(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            int mision = jobj.get("idMision").getAsInt();
            Mision mm = new Mision();
            mm.setIdMision(mision);
            int pasoNumero = jobj.get("pasoNumero").getAsInt();

            MisionPasoLogroService mps = new MisionPasoLogroService();
            return gson.toJson(mps.pasoVeri(mm, pasoNumero).getIdLogro());

        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "asignarRecurso")
    public @WebResult(partName = "checkVerifyResponse")
    String asignarRecurso(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        String aa = "No se pudo asignar";
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            int recapoyo = jobj.get("idRecapoyo").getAsInt();

            int mision = jobj.get("idMision").getAsInt();

            MisionTipoRecursoService tr = new MisionTipoRecursoService();
            MisionTipoRecurso tt = new MisionTipoRecurso();
            MisionService ms = new MisionService();
            tt.setIdMision(ms.misionlistaId(mision));
            tt.setIdTipoRecurso(1);
            tt.setIdRecurso(recapoyo);
            tt.setEstado("a");
            tr.misionTipoRecursoAdd(tt);
            aa = "Se asigno correctamente";
            return aa;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "desasignarRecurso")
    public @WebResult(partName = "checkVerifyResponse")
    String desasignarRecurso(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        String aa = "No se pudo asignar";
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            int recapoyo = jobj.get("idRecapoyo").getAsInt();
            int codmis = jobj.get("idMision").getAsInt();
            Mision mision = new Mision();
            mision.setIdMision(codmis);
            MisionTipoRecursoService tr = new MisionTipoRecursoService();
            MisionTipoRecurso mts = tr.sqlbuscPorRecursoYMision(recapoyo, mision);

            mts.setEstado("b");
            tr.misionTipoRecursoEdit(mts);
            aa = "Se borro correctamente";
            return aa;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "modificarMPLogro")
    public @WebResult(partName = "checkVerifyResponse")
    String modificarMPLogro(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {

        JsonObject hola = gson.fromJson(jsonString, JsonObject.class);
        JsonObject entrada;
        JsonArray asd = hola.get("cod").getAsJsonArray();
        MisionPasoLogroService aniadir = new MisionPasoLogroService();
        MisionPasoLogro n;
        Logro log = new Logro();
        Mision mis = new Mision();
        Paso pas = new Paso();

        entrada = asd.get(0).getAsJsonObject();

        int idmpl = entrada.get("mision").getAsInt();
        Mision hhh = new Mision();
        hhh.setIdMision(idmpl);
        List<MisionPasoLogro> mpoo = aniadir.mplbuscMis(hhh);
        for (int i = 0; i < mpoo.size(); i++) {

            aniadir.misionPasoLogroDelete(mpoo.get(i).getIdMisionPasoLogro());

        }

        for (int a = 0; a < asd.size(); a++) {
            entrada = asd.get(a).getAsJsonObject();
            n = new MisionPasoLogro();
            //------------------------------------------------------------
            if (entrada.get("logro").getAsInt() != 0 && entrada.get("logro").getAsInt() != -1) {
                log.setIdLogro(entrada.get("logro").getAsInt());
                n.setIdLogro(log);
            } else {
                log.setIdLogro(38);
                n.setIdLogro(log);
            }
            //------------------------------------------------------------
            mis.setIdMision(entrada.get("mision").getAsInt());
            n.setIdMision(mis);
            pas.setIdPaso(entrada.get("paso").getAsInt());
            n.setIdPaso(pas);
            n.setPasoNumero(entrada.get("pasoNumero").getAsInt());
            n.setEstado("a");
            aniadir.misionPasoLogroAdd(n);
        }
        /* Type listType = new TypeToken<List<MisionPasoLogro>>(){}.getType();
        ArrayList<MisionPasoLogro> posts = gson.fromJson(jsonString, listType);
        MisionPasoLogroService creacion=new MisionPasoLogroService();
        for(int a=0;a<posts.size();a++){
            creacion.misionPasoLogroAdd(posts.get(a));
        }*/
        return "Se modifico";
    }

    @WebMethod(operationName = "crearGforms")
    public @WebResult(partName = "checkVerifyResponse")
    String crearGforms(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {
            GformsService google = new GformsService();
            google.gformsAdd(jsonString);

        } catch (Exception e) {
            return e.toString();
        }
        return "Se creo con exito la encuesta";
    }

    @WebMethod(operationName = "consultarGforms")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarGforms(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            int forms = jobj.get("idGform").getAsInt();
            GformsService google = new GformsService();
            return gson.toJson(google.gformslistaId(forms));

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarGformsTodo")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarGformsTodo(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            GformsService google = new GformsService();
            return gson.toJson(google.gformslista());

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "borrarGforms")
    public @WebResult(partName = "checkVerifyResponse")
    String borrarGforms(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {

            GformsService google = new GformsService();
            google.gformsDelete(jsonString);

        } catch (Exception e) {
            return e.toString();
        }
        return "Se borro con exito ";
    }

    @WebMethod(operationName = "modificarGforms")
    public @WebResult(partName = "checkVerifyResponse")
    String modificarGforms(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {

            GformsService google = new GformsService();
            google.gformsEdit(jsonString);

        } catch (Exception e) {
            return e.toString();
        }
        return "Se modifico con exito ";
    }

    @WebMethod(operationName = "perfilPaciente")
    public @WebResult(partName = "checkVerifyResponse")
    String perfilPaciente(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {
            java.util.Date date = new Date();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            String info = jobj.get("info").getAsString();
            String res = jobj.get("respuesta").getAsString();
            String pa = jobj.get("idPaciente").getAsString();
            EncuestaService ess = new EncuestaService();
            TipoEncuestaService tes = new TipoEncuestaService();
            PacienteService psp = new PacienteService();

            Encuesta een = new Encuesta();
            een.setNomEncuesta("PerfilPaciente");
            een.setCodPaciente(psp.pacienteListaId(pa));
            een.setIdTipoEncuesta(tes.tipoencuestalistaId(8));
            een.setFecha(date);
            een.setEstado("a");
            ess.encuestaAdd(een);

            PreguntaService ps = new PreguntaService();
            TipoPreguntaService tps = new TipoPreguntaService();
            Pregunta pp = new Pregunta();
            pp.setIdTipoPregunta(tps.tipopreguntalistaId(58));
            pp.setEnunciado("Determine el perfil de tratamiento del paciente");
            pp.setRespuesta(res);
            pp.setEstado("a");
            List<Encuesta> ehh = ess.busEncuestaPorTipoPac(psp.pacienteListaId(pa), tes.busTipoEncuestaId("Perfil Tratamiento"));
            pp.setIdEncuesta(ehh.get(ehh.size() - 1));
            ps.preguntaAdd(pp);

            if (res.equals("6")) {

                Pregunta pp2 = new Pregunta();
                pp2.setIdTipoPregunta(tps.tipopreguntalistaId(58));
                pp2.setEnunciado("Especificar si le falta una extremeidad y cual? ");
                pp2.setRespuesta(info);
                pp2.setEstado("a");

                pp2.setIdEncuesta(ehh.get(ehh.size() - 1));
                ps.preguntaAdd(pp2);

            }
        } catch (Exception e) {
            return e.toString();
        }
        return "Se crearon las encuestas con exito";
    }

    @WebMethod(operationName = "consultarNivelAlimentacion")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarNivelAlimentacion(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        int a = 0;
        try {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);

            String id = jobj.get("codPaciente").getAsString();

            PacienteService pas = new PacienteService();
            Paciente pa = pas.pacienteListaId(id);
            pa.getPuntajeAlim();
            NivelPacienteService ns = new NivelPacienteService();
            List<NivelPaciente> nn2 = new ArrayList<>();
            List<NivelPaciente> nn = ns.nivelPacientelista();
            for (int i = 0; i < nn.size(); i++) {
                if (nn.get(i).getEstado().equals("a")) {
                    nn2.add(nn.get(i));
                }

            }

            for (int i = 0; i < nn2.size(); i++) {
                if (nn.get(i).getPuntajeReq() <= pa.getPuntajeAlim() && nn.get(i).getNombreNivel().equals("Basico")) {
                    a = (pa.getPuntajeAlim() * 100) / nn.get(i + 1).getPuntajeReq();

                    jobj.addProperty("Porcentaje", a);
                    jobj.addProperty("nombreNivel", nn.get(i).getNombreNivel());

                } else if (nn.get(i).getPuntajeReq() <= pa.getPuntajeAlim() && nn.get(i).getNombreNivel().equals("Medio")) {
                    a = (pa.getPuntajeAlim() * 100) / nn.get(i + 1).getPuntajeReq();
                    jobj.addProperty("Porcentaje", a);
                    jobj.addProperty("nombreNivel", nn.get(i).getNombreNivel());
                } else if (nn.get(i).getPuntajeReq() <= pa.getPuntajeAlim() && nn.get(i).getNombreNivel().equals("Avanzado")) {
                    a = (pa.getPuntajeAlim() * 100) / 40000;
                    jobj.addProperty("Porcentaje", a);
                    jobj.addProperty("nombreNivel", nn.get(i).getNombreNivel());
                }

            }
            ;
            return gson.toJson(jobj);
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarNivelEjercicio")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarNivelEjercicio(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        int a = 0;
        try {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            String id = jobj.get("codPaciente").getAsString();

            PacienteService pas = new PacienteService();
            Paciente pa = pas.pacienteListaId(id);
            pa.getPuntajeAlim();
            NivelPacienteService ns = new NivelPacienteService();
            List<NivelPaciente> nn2 = new ArrayList<>();
            List<NivelPaciente> nn = ns.nivelPacientelista();
            for (int i = 0; i < nn.size(); i++) {
                if (nn.get(i).getEstado().equals("e")) {
                    nn2.add(nn.get(i));
                }

            }

            for (int i = 0; i < nn2.size(); i++) {
                if (nn.get(i).getPuntajeReq() <= pa.getPuntajeEjer() && nn.get(i).getNombreNivel().equals("Basico")) {
                    a = (pa.getPuntajeAlim() * 100) / nn.get(i + 1).getPuntajeReq();
                    jobj.addProperty("Porcentaje", a);
                    jobj.addProperty("nombreNivel", nn.get(i).getNombreNivel());
                } else if (nn.get(i).getPuntajeReq() <= pa.getPuntajeEjer() && nn.get(i).getNombreNivel().equals("Medio")) {
                    a = (pa.getPuntajeAlim() * 100) / nn.get(i + 1).getPuntajeReq();
                    jobj.addProperty("Porcentaje", a);
                    jobj.addProperty("nombreNivel", nn.get(i).getNombreNivel());
                } else if (nn.get(i).getPuntajeReq() <= pa.getPuntajeEjer() && nn.get(i).getNombreNivel().equals("Avanzado")) {
                    a = (pa.getPuntajeAlim() * 100) / 40000;
                    jobj.addProperty("Porcentaje", a);
                    jobj.addProperty("nombreNivel", nn.get(i).getNombreNivel());
                }

            }
            return gson.toJson(jobj);
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultarLogroGanado")
    public @WebResult(partName = "checkVerifyResponse")
    String consultarLogroGanado(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        int a = 0;
        try {

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            int id = jobj.get("idMisionPaciente").getAsInt();
            int paso = jobj.get("numeroDia").getAsInt();
            MisionPacienteService mps = new MisionPacienteService();
            MisionPasoLogroService mpls = new MisionPasoLogroService();

            List<MisionPasoLogro> kk = mpls.mplbuscMis(mps.misionPacienteListaId(id).getIdMision());
            for (int i = 0; i < kk.size(); i++) {
                if (kk.get(i).getPasoNumero() == paso) {
                    return gson.toJson(kk.get(i).getIdLogro());
                }

            }
            return null;
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "notificacionRefuerzo")
    public @WebResult(partName = "checkVerifyResponse")
    String notificacionRefuerzo(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            String paciente = jobj.get("idPaciente").getAsString();
            TipoRecursoService ns = new TipoRecursoService();
            List<TipoRecurso> not = ns.tipoRecursolista();
            java.util.Date date = new Date();
            Notificacion no = new Notificacion();
            NotificacionService ntif = new NotificacionService();
            UsuarioService us = new UsuarioService();

            Usuario uu = new Usuario();
            uu.setCodUsuario(paciente);

            TipoRecurso ttr = not.get((int) (Math.random() * not.size() - 1));
            no.setEstado("a");
            no.setIdTipoRecurso(ttr);
            no.setCodRecurso("1");
            no.setFecha(date);
            no.setNomUsuario(us.sqlbuscCod(uu));
            ntif.notificacionAdd2(no);

            return gson.toJson(ttr);
        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "encuesta2")

    public @WebResult(partName = "checkVerifyResponse")
    String encuesta2(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {

        try {
            java.util.Date date = new Date();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            String paciente = jobj.get("paciente").getAsString();
            String sug = jobj.get("sugerencias").getAsString();

            PreguntaService pss = new PreguntaService();
            TipoPreguntaService tps = new TipoPreguntaService();
            Encuesta en = new Encuesta();
            EncuestaService ens = new EncuestaService();
            PacienteService ps = new PacienteService();
            Pregunta p = new Pregunta();
            TipoEncuestaService tens = new TipoEncuestaService();
            en.setCodPaciente(ps.pacienteListaId(String.valueOf(paciente)));

            en.setEstado("a");
            en.setIdTipoEncuesta(tens.tipoencuestalistaId(9));
            en.setNomEncuesta("SugerenciaNutricion");
            en.setFecha(date);
            ens.encuestaAdd(en);

            p.setIdEncuesta(en);
            p.setEnunciado("Por favor proporcione sugerencias con respecto a la alimentacion del paciente: ");
            p.setRespuesta(sug);
            p.setIdTipoPregunta(tps.tipopreguntalistaId(60));
            p.setEstado("a");
            pss.preguntaAdd(p);

            return "Se creo correctamente";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "encuesta1")

    public @WebResult(partName = "checkVerifyResponse")
    String encuesta1(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {

        try {
            java.util.Date date = new Date();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            String paciente = jobj.get("paciente").getAsString();
            String sug = jobj.get("metas").getAsString();

            PreguntaService pss = new PreguntaService();
            TipoPreguntaService tps = new TipoPreguntaService();
            Encuesta en = new Encuesta();
            EncuestaService ens = new EncuestaService();
            PacienteService ps = new PacienteService();
            Pregunta p = new Pregunta();
            TipoEncuestaService tens = new TipoEncuestaService();
            en.setCodPaciente(ps.pacienteListaId(String.valueOf(paciente)));

            en.setEstado("a");
            en.setIdTipoEncuesta(tens.tipoencuestalistaId(10));
            en.setNomEncuesta("MetasEjercicio");
            en.setFecha(date);
            ens.encuestaAdd(en);

            p.setIdEncuesta(en);
            p.setEnunciado("Por favor proporcione las metas del tratamiento con respecto al ejercicio: ");
            p.setRespuesta(sug);
            p.setIdTipoPregunta(tps.tipopreguntalistaId(61));
            p.setEstado("a");
            pss.preguntaAdd(p);

            return "Se creo correctamente";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "diasCompletados")

    public @WebResult(partName = "checkVerifyResponse")
    String diasCompletados(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {

        try {
            java.util.Date date = new Date();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            int mpaciente = jobj.get("idMisionPaciente").getAsInt();
            MisionPacienteService mps = new MisionPacienteService();
            MisionPaciente mp = mps.misionPacienteListaId(mpaciente);
            VerificacionService vs = new VerificacionService();
            List<Verificacion> ver = vs.sqlbuscVerificacionPorMP(mp);
            int count = 0;
            for (int i = 0; i < ver.size(); i++) {
                if (ver.get(i).getVerifPaciente() == true && ver.get(i).getVerifPaciente() == true) {
                    count = count + 1;
                }
                ver.get(i).getVerifApoyoSocial();

            }

            JsonObject obj = new JsonObject();
            obj.addProperty("num", (count));

            return obj.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    @WebMethod(operationName = "generarExcel")
    public @WebResult(partName = "checkVerifyResponse")
    String generarExcel(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {

        try {
            java.util.Date date = new Date();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            String paciente = jobj.get("codPaciente").getAsString();

            Workbook workbook = new HSSFWorkbook();
            //Crea hoja nueva
            Sheet sheet = workbook.createSheet("Reporte");

            PacienteService ps = new PacienteService();
            Paciente pa = ps.pacienteListaId(paciente);
            List<PruebaDiagnostica> pd = pa.getPruebaDiagnosticaList();
            int rowNum = 1;

            Row rowini = sheet.createRow(0);
            Cell cellini = rowini.createCell(0);
            cellini.setCellValue("Indicadores");

            Cell cellini2 = rowini.createCell(7);
            cellini2.setCellValue("Encuestas");

            List<Encuesta> enn = pa.getEncuestaList();

            int a = pd.size() - 1;

            for (int i = 0; i < enn.size(); i++) {
                Row row = sheet.createRow(rowNum++);
                if (i < a) {
                    Cell cell = row.createCell(1);
                    cell.setCellValue(pd.get(i).getNombrePrueba());

                    Cell cell2 = row.createCell(0);
                    cell2.setCellValue(pd.get(i).getIdPrueba());
                    Cell cell3 = row.createCell(2);
                    cell3.setCellValue(pd.get(i).getFechaPrueba().toString());

                    Cell cell4 = row.createCell(3);
                    List<Indicador> inn = pd.get(i).getIndicadorList();
                    for (int j = 0; j < inn.size(); j++) {

                        cell4.setCellValue(inn.get(j).getValorIndicador());
                    }
                }
                Cell cell7 = row.createCell(6);
                cell7.setCellValue(enn.get(i).getIdEncuesta());

                Cell cell8 = row.createCell(7);
                cell8.setCellValue(enn.get(i).getNomEncuesta());

                Cell cell9 = row.createCell(8);
                cell9.setCellValue(enn.get(i).getFecha().toString());
                List<Pregunta> pp = enn.get(i).getPreguntaList();

                for (int j = 0; j < pp.size(); j++) {

                    Cell cell10 = row.createCell(9);

                    Cell cell11 = row.createCell(10);

                    cell10.setCellValue(pp.get(j).getEnunciado());
                    cell11.setCellValue(pp.get(j).getRespuesta());
                }

            }

            for (int i = 0; i < enn.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream out = new FileOutputStream(new File("/home/dmt2/apache-tomcat-8.5.38/webapps/DT3/reportes/" + paciente + ".xlsx"));
            workbook.write(out);
            out.close();
            return gson.toJson("http://91.121.72.151:9090/DT3/reportes/" + paciente + ".xlsx");

        } catch (Exception e) {
            return e.toString();
        }

    }

    @WebMethod(operationName = "consultachat")
    public String consultachat(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        JsonObject chat_datos = gson.fromJson(jsonString, JsonObject.class);
        String us = chat_datos.get("id_usuario").getAsString();
        String emi = chat_datos.get("id_emi_usuario").getAsString();
        MensajeService n = new MensajeService();
        //return gson.toJson(n.consultachat("asabogala@gmail.com","asabogala@unbosque.edu.co"));
        UsuarioService usu = new UsuarioService();
        Usuario u = new Usuario();
        Usuario emis = new Usuario();
        u.setCodUsuario(us);
        emis.setCodUsuario(emi);

        Usuario usu2 = usu.sqlbuscCod(u);
        emis = usu.sqlbuscCod(emis);
        return gson.toJson(n.consultachat(usu2.getNomUsuario(), emis.getNomUsuario()));
    }

    @WebMethod(operationName = "idProfecional")
    public String idProfecional(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        JsonObject chat_datos = gson.fromJson(jsonString, JsonObject.class);
        String us = chat_datos.get("id_usuario").getAsString();
        AsignacionService gg = new AsignacionService();
        Asignacion salida = gg.sqlbusc(us);
        return salida.getCodProfesional().getCodProfesional();
    }

    @WebMethod(operationName = "idApoyoSocial")
    public String idApoyoSocial(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault, Exception {
        JsonObject chat_datos = gson.fromJson(jsonString, JsonObject.class);
        String us = chat_datos.get("id_ApoyoSocial").getAsString();
        AsignacionService gg = new AsignacionService();
        Asignacion salida = gg.sqlbuscFamiliar(us);
        return salida.getCodPaciente().getCodPaciente();
    }

    @WebMethod(operationName = "crearVerificacionApoyo")
    public @WebResult(partName = "checkVerifyResponse")
    String crearVerificacionApoyo(@WebParam(name = "jsonString") String jsonString) throws CheckVerifyFault {
        java.util.Date date = new Date();
        String mm = "No se creo la verificacion";
        try {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonObject jobj = gson.fromJson(jsonString, JsonObject.class);
            VerificacionService vs = new VerificacionService();
            int MisionPaciente = jobj.get("idMisionPaciente").getAsInt();
            int numeroDia = jobj.get("numeroDia").getAsInt();
            boolean vApoyoSocial = jobj.get("verifApoyoSocial").getAsBoolean();
            Verificacion vv = new Verificacion();
            MisionPaciente mps = new MisionPaciente();
            MisionPacienteService mpss = new MisionPacienteService();
            MisionPasoLogroService misionps = new MisionPasoLogroService();
            PacienteService ps = new PacienteService();
            mps.setIdMisionPaciente(MisionPaciente);
            List<Verificacion> listVerificacion = vs.sqlbuscVerificacionPorMPN(mps, numeroDia);
            if (listVerificacion != null && listVerificacion.size() != 0 && listVerificacion.get(0).getNumeroDia() == numeroDia) {
                listVerificacion.get(0).setVerifApoyoSocial(vApoyoSocial);
                vs.verificacionEdit(listVerificacion.get(0));
            } else {
                vv.setIdMisionPaciente(mps);
                vv.setNumeroDia(numeroDia);
                vv.setVerifApoyoSocial(vApoyoSocial);
                vv.setFecha(date);
                vv.setEstado("a");
                vs.verificacionAdd(vv);
            }
            List<MisionPasoLogro> pp = misionps.mplbuscMis(mpss.misionPacienteListaId(MisionPaciente).getIdMision());

            if (vApoyoSocial == true && listVerificacion.get(0).getVerifPaciente() == true && pp.get(0).getIdPaso().getDiasDuracion() == numeroDia) {
                MisionPaciente mppo = mpss.misionPacienteListaId(MisionPaciente);
                mppo.setCompletada("2");
                mpss.misionPacienteEdit(mppo);
            }
            mm = "Se creo la verificacion";
            return mm;

        } catch (Exception e) {
            return e.toString();
        }
    }

    private final Properties properties = new Properties();
    private Session session;

    private void init() {

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.mail.sender", "incendine@gmail.com");
        properties.put("mail.smtp.user", "Equipo de Investigacion");
        properties.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(properties);
    }

    public void sendEmail(String receptor) {

        init();
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            message.setSubject("Confirmación de acceso a MiDT2 con Psicoeducación");
            message.setText("Hola, bienvenido/a a la App MiDT2 con Psicoeducación, para poder acceder su usuario será su email y su clave temporal será: bosque \n"
                    + " La App, se puede descargar aquí:\n"
                    + "https://drive.google.com/drive/folders/1AiRVZBEQR7mObYK61FbDVyyNV4EE4y9g?usp=sharing\n"
                    + "\n"
                    + "\n"
                    + "Cordialmente\n"
                    + "\n"
                    + "Equipo de Investigación de Ingeniería de sistemas y psicología");
            Transport t = session.getTransport("smtp");
            t.connect("smtp.gmail.com", "incendine@gmail.com", "Enterman2.0");
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            //Aqui se deberia o mostrar un mensaje de error o en lugar
            //de no hacer nada con la excepcion, lanzarla para que el modulo
            //superior la capture y avise al usuario con un popup, por ejemplo.
            me.printStackTrace();
            return;
        }

    }

    @WebMethod(operationName = "cantidadNotificaciones")
    public String cantidadNotificaciones(@WebParam(name = "nombreDestinatario") String nombreDestinatario) {
        MensajeService mensajesService = new MensajeService();
        return mensajesService.cantidadNotificaciones(nombreDestinatario);
    }
}
