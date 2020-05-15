package Servicios;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;

@WebFault(name="CheckVerifyFault",
    targetNamespace="http://www.example.com")
public class CheckVerifyFault extends Exception {

    /**
     * Java type that goes as soapenv:Fault detail element.
     */
    private CheckFaultBean faultInfo;

    public CheckVerifyFault(String message, CheckFaultBean faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public CheckVerifyFault(String message, CheckFaultBean faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public CheckFaultBean getFaultInfo() {
        return faultInfo;
    }
}