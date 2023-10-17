package com.cassinelli.cotiza;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ListaActivityMaster extends ListActivity {
    ProgressDialog dialog = null;
    String id_spinner = "0";
    // ListView listView;
    private EditText txtBuscar;
    private Spinner spinner_master;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Item item = (Item) l.getAdapter().getItem(position);
        Master elegido = (Master) l.getItemAtPosition(position);
        String co_mast = elegido.co_mast;
        String de_mast = elegido.de_mast;
        String pr_mast = elegido.pr_vent_soles;
        Intent data = getIntent();
        data.putExtra("CO_MAST", co_mast);
        data.putExtra("DE_MAST", de_mast);
        data.putExtra("PR_MAST", pr_mast);
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitymaster);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_master);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_master = ArrayAdapter.createFromResource(this,
                R.array.criteriomaster, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_master.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter_master);


        // listView = (ListView)findViewById(R.this.);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        //registerForContextMenu(getListView());
        spinner_master = (Spinner) findViewById(R.id.spinner_master);
        spinner_master.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                if (String.valueOf(arg2).equals("0")) {
                    id_spinner = "1";
                } else if (String.valueOf(arg2).equals("1")) {
                    id_spinner = "2";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void btnBuscar_master_Onclick(View v) {

        ConsultarDB c = new ConsultarDB();
        c.execute();


    }

    private class ConsultarDB extends
            AsyncTask<Void, Integer, MasterArrayAdapter> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPostExecute(MasterArrayAdapter result) {
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            app.de_busq = txtBuscar.getText().toString();
            dialog = new ProgressDialog(ListaActivityMaster.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected MasterArrayAdapter doInBackground(Void... params) {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListarMaster";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListarMaster";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR", variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID", variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN", variables_publicas.co_tien.toString());
            request.addProperty("sST_CRIT", id_spinner.toString());
            request.addProperty("sDE_CRIT", app.de_busq.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Master> master = new ArrayList<Master>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Master[] listaMaster = new Master[resSoap
                        .getPropertyCount()];

                for (int i = 0; i < listaMaster.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Master con = new Master();
                    con.co_mast = ic.getProperty(0).toString();
                    con.de_mast = ic.getProperty(1).toString();
                    con.pr_vent_soles = df.format(Double.parseDouble(ic.getProperty(2).toString()));
                    con.pr_vent_dolar = df.format(Double.parseDouble(ic.getProperty(3).toString()));

                    master.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            MasterArrayAdapter adapter = new MasterArrayAdapter(
                    getApplicationContext(), master);

            return adapter;
        }

    }


}
