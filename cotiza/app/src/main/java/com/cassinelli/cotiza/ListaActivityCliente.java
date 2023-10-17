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

public class ListaActivityCliente extends ListActivity {
    ProgressDialog dialog = null;
    // ListView listView;
    private EditText txtBuscar;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Item item = (Item) l.getAdapter().getItem(position);
        Cliente elegido = (Cliente) l.getItemAtPosition(position);
        String co_clie = elegido.co_clie;
        Intent data = getIntent();
        data.putExtra("CO_CLIE", co_clie);
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitycliente);

        // listView = (ListView)findViewById(R.this.);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        // registerForContextMenu(getListView());
    }

    public void btnBuscar_cliente_Onclick(View v) {
        if (txtBuscar.getText().toString().length() > 5) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);
            ConsultarDB c = new ConsultarDB();
            c.execute();

        } else {
            txtBuscar.setError("Debe ingresar al menos 6 caracteres");
        }
    }

    private class ConsultarDB extends
            AsyncTask<Void, Integer, ClienteArrayAdapter> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPostExecute(ClienteArrayAdapter result) {
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            app.de_busq = txtBuscar.getText().toString();
            dialog = new ProgressDialog(ListaActivityCliente.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected ClienteArrayAdapter doInBackground(Void... params) {

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaCliente";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaCliente";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sNO_CLIE", app.de_busq.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Cliente> cliente = new ArrayList<Cliente>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Cliente[] listaCliente = new Cliente[resSoap.getPropertyCount()];

                for (int i = 0; i < listaCliente.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Cliente con = new Cliente();
                    con.co_clie = ic.getProperty(0).toString();
                    con.no_clie = ic.getProperty(1).toString();

                    cliente.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ClienteArrayAdapter adapter = new ClienteArrayAdapter(
                    getApplicationContext(), cliente);

            return adapter;
        }

    }

}
