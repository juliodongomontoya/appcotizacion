package com.cassinelli.cotiza;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tab4Activity extends ListActivity {
    ProgressDialog dialog = null;
    EditText txtnu_coti_lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitycotizacion);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtnu_coti_lista = (EditText) findViewById(R.id.txtnu_coti_lista);
        txtnu_coti_lista.setText(variables_publicas.nu_coti_fina.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listadomenu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        ConsultarCotizacion p = new ConsultarCotizacion();
        p.execute();

    }


    private class ConsultarCotizacion extends
            AsyncTask<Void, Integer, CotizacionArrayAadapter> {

        @Override
        protected void onPostExecute(CotizacionArrayAadapter result) {
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Tab4Activity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected CotizacionArrayAadapter doInBackground(Void... params) {

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaCotizacion";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaCotizacion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI",
                    variables_publicas.nu_coti_fina.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Cotizacion> cotizacion = new ArrayList<Cotizacion>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Cotizacion[] listaCotizaciones = new Cotizacion[resSoap
                        .getPropertyCount()];

                for (int i = 0; i < listaCotizaciones.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Cotizacion con = new Cotizacion();
                    con.nu_corr_lista = ic.getProperty(0).toString();
                    con.co_item_lista = ic.getProperty(1).toString();
                    con.co_unme_lista = ic.getProperty(2).toString();
                    con.co_alma_lista = ic.getProperty(3).toString();
                    con.ca_item_lista = ic.getProperty(4).toString();
                    con.pr_vent_lista = ic.getProperty(5).toString();
                    con.pc_dcto_lista = ic.getProperty(6).toString();
                    con.de_item_larg_lista = ic.getProperty(7).toString();

                    cotizacion.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            CotizacionArrayAadapter adapter = new CotizacionArrayAadapter(
                    getApplicationContext(), cotizacion);

            return adapter;
        }

    }

}
