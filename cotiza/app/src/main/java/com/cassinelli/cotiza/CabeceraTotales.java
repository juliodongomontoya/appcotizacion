package com.cassinelli.cotiza;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CabeceraTotales {

    public void Totales_cabecera() {

        final String NAMESPACE = "http://app.cassinelli.com/";
        final String URL = variables_publicas.direccionIp + "/Service.asmx";
        final String METHOD_NAME = "ListaTotalesCoti";
        final String SOAP_ACTION = "http://app.cassinelli.com/ListaTotalesCoti";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("sCO_EMPR", variables_publicas.co_empr.toString());
        request.addProperty("sCO_UNID", variables_publicas.co_unid.toString());
        request.addProperty("sCO_TIEN", variables_publicas.co_tien.toString());
        request.addProperty("sNU_COTI",
                variables_publicas.nu_coti_fina.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        List<Totales> totales = new ArrayList<Totales>();

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();
            Totales[] listaTotales = new Totales[resSoap.getPropertyCount()];

            for (int i = 0; i < listaTotales.length; i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);
                Totales tot = new Totales();
                variables_publicas.tot_soles = ic.getProperty(0).toString();
                variables_publicas.tot_dolar = ic.getProperty(1).toString();
                variables_publicas.pc_perc = ic.getProperty(2).toString();
                variables_publicas.im_perc = ic.getProperty(3).toString();
                totales.add(tot);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
