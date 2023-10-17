package com.cassinelli.cotiza;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaActivityCondicion2 extends ListActivity {
    ProgressDialog dialog = null;
    // ListView listView;
    private EditText txtBuscar;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Item item = (Item) l.getAdapter().getItem(position);
        Condicion elegido = (Condicion) l.getItemAtPosition(position);
        String valor = elegido.co_cond_vent;
        Intent data = getIntent();
        data.putExtra("CONDICION", valor);
        setResult(RESULT_OK, data);
        finish();
        // Toast.makeText(this, valor.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitycondicion);

        // listView = (ListView)findViewById(R.this.);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        registerForContextMenu(getListView());
    }

    public void btnBuscar_Onclick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);
        ConsultarDB c = new ConsultarDB();
        c.execute();
    }

    private class ConsultarDB extends
            AsyncTask<Void, Integer, CondicionArrayAdapter> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPostExecute(CondicionArrayAdapter result) {
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            app.de_busq = txtBuscar.getText().toString();
            dialog = new ProgressDialog(ListaActivityCondicion2.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected CondicionArrayAdapter doInBackground(Void... params) {

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaCondicion";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaCondicion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR", variables_publicas.co_empr.toString());
            request.addProperty("sDE_COND_VENT", app.de_busq.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Condicion> condicion = new ArrayList<Condicion>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Condicion[] listaCondicion = new Condicion[resSoap
                        .getPropertyCount()];

                for (int i = 0; i < listaCondicion.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Condicion con = new Condicion();
                    con.co_cond_vent = ic.getProperty(0).toString();
                    con.de_cond_vent = ic.getProperty(1).toString();

                    condicion.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            CondicionArrayAdapter adapter = new CondicionArrayAdapter(
                    getApplicationContext(), condicion);

            return adapter;
        }

    }

}
