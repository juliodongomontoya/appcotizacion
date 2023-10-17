package com.cassinelli.cotiza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MasterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    EditText txtnu_coti_mast;
    EditText txtca_mast;
    EditText txtco_mast;
    EditText txtde_mast;
    EditText txtpr_mast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        GlobalApp app = (GlobalApp) getApplicationContext();
        app.menuGrabarMaster = true;
        txtnu_coti_mast = (EditText) findViewById(R.id.txtnu_coti_mast);
        txtco_mast = (EditText) findViewById(R.id.txtco_mast);
        txtde_mast = (EditText) findViewById(R.id.txtde_mast);
        txtca_mast = (EditText) findViewById(R.id.txtca_mast);
        txtpr_mast = (EditText) findViewById(R.id.txtpr_mast);
        txtnu_coti_mast.setText(getIntent().getStringExtra("NU_COTI"));
        txtco_mast.setText(getIntent().getStringExtra("CO_MAST"));
        txtde_mast.setText(getIntent().getStringExtra("DE_MAST"));
        txtpr_mast.setText(getIntent().getStringExtra("PR_MAST"));

        txtca_mast.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mastermenu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        GlobalApp app = (GlobalApp) getApplicationContext();
        MenuItem grabarmaster = menu.findItem(R.id.mnuGrabarMaster);
        grabarmaster.setVisible(app.menuGrabarMaster);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalApp app = (GlobalApp) getApplicationContext();
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuGrabarMaster: {
                if (txtca_mast.getText().toString().length() == 0) {
                    txtca_mast.setError("Debe ingresar cantidad");
                } else if (app.st_docu.equals("CON")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("Ya no se puede modificar porque la cotizaci�n esta convertida");
                    dialog.setCancelable(false);
                    dialog.setTitle("Aviso");
                    dialog.setIcon(android.R.drawable.stat_notify_error);

                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                } else {
                    ValidarMastAsyn tarea = new ValidarMastAsyn();

                    tarea.execute();

                }

            }

            default:
                return false;

        }
    }

    private class ValidarMastAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.co_mast = txtco_mast.getText()
                    .toString();
            app.ca_mast = txtca_mast.getText()
                    .toString();
            progressDialog = new ProgressDialog(MasterActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.toString().equals("OK")) {

                GrabarMastAsyn tarea = new GrabarMastAsyn();

                tarea.execute();

            } else {
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG)
                        .show();
            }

        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {
            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ValidaMaster";
            final String SOAP_ACTION = "http://app.cassinelli.com/ValidaMaster";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sCO_ITEM_MAST", app.co_mast
                    .toString());
            request.addProperty("nCA_DOCU_MAST", app.ca_mast
                    .toString());
            request.addProperty("sCO_ALMA_VENT",
                    variables_publicas.co_alma_vent.toString());
            request.addProperty("sCO_ALMA_CONS",
                    variables_publicas.co_alma_cons.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();

            } catch (Exception e) {

            }
            return res;
        }

    }

    private class GrabarMastAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.nu_coti = txtnu_coti_mast.getText()
                    .toString();
            app.co_mast = txtco_mast.getText()
                    .toString();
            app.ca_mast = txtca_mast.getText()
                    .toString();
            progressDialog = new ProgressDialog(MasterActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.toString().equals("OK")) {

                Toast.makeText(getBaseContext(), "El Master grabo satisfactoriamente", Toast.LENGTH_LONG)
                        .show();
                finish();

            } else {
                Toast.makeText(getBaseContext(), "El Master no se grabo !Verificar", Toast.LENGTH_LONG)
                        .show();
            }

        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {
            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "AnadirMaster";
            final String SOAP_ACTION = "http://app.cassinelli.com/AnadirMaster";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti
                    .toString());
            request.addProperty("sCO_MAST", app.co_mast
                    .toString());
            request.addProperty("nCA_DOCU", app.ca_mast
                    .toString());
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();

            } catch (Exception e) {

            }
            return res;
        }

    }

}
