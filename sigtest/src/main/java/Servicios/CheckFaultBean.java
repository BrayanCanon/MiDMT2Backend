package Servicios;


import javax.xml.ws.WebFault;

public class CheckFaultBean {

    private String message;

    public CheckFaultBean() { }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}