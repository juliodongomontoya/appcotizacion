package com.cassinelli.cotiza;


import android.app.ListActivity;
import android.os.Bundle;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class ListaActivityCondicion extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivity);
        final String NAMESPACE = "http://app.cassinelli.com/";
        final String URL = "http://app.cassinelli.com:8080/Service.asmx";
        final String METHOD_NAME = "ListaCondicion";
        final String SOAP_ACTION = "http://app.cassinelli.com/ListaCondicion";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        List<Condicion> condicion = new ArrayList<Condicion>();
        try {
            transporte.call(SOAP_ACTION, envelope);
            SoapObject resSoap = (SoapObject) envelope.getResponse();
            Condicion[] listaCondicion = new Condicion[resSoap.getPropertyCount()];

            for (int i = 0; i < listaCondicion.length; i++) {
                SoapObject ic = (SoapObject) resSoap.getProperty(i);
                Condicion con = new Condicion();
                con.co_cond_vent = ic.getProperty(0).toString();
                con.de_cond_vent = ic.getProperty(1).toString();

                condicion.add(con);
            }

            //CondicionArrayAdapter adapter = new CondicionArrayAdapter(this, condicion);

            //setListAdapter(adapter);


        } catch (Exception e) {

        }
    }

}
