package com.cassinelli.cotiza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tab3Activity extends AppCompatActivity {
    ProgressDialog progressDialog;
    EditText txtco_clie_datos;
    EditText txtno_clie_datos;
    EditText txtno_clie_natu;
    EditText txtap_clie_natu;
    EditText txtam_clie_natu;
    EditText txtde_dire_clie_datos;
    EditText txtnu_tlf1_datos;
    EditText txtnu_tfl2_datos;
    EditText txtnu_tlf3_datos;
    EditText txtnu_docu_iden;
    EditText txtemail_datos;
    Spinner dddistrito;
    CheckBox chkpromo;
    CheckBox chkrete;
    CheckBox chkpercep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        dddistrito = (Spinner) findViewById(R.id.dddistrito);
        CargarDistCotiAsyn tarea = new CargarDistCotiAsyn();
        tarea.execute();
        dddistrito.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                GlobalApp app = (GlobalApp) getApplicationContext();
                Dsitrito elegido = (Dsitrito) arg0.getItemAtPosition(arg2);
                app.co_ubic_geog = elegido.co_ubic_geog;
                app.no_ubic_geog = elegido.de_ubic_geog;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        GlobalApp app = (GlobalApp) getApplicationContext();
        if (app.co_clie.length() > 0) {
            txtco_clie_datos = (EditText) findViewById(R.id.txtco_clie_datos);
            txtno_clie_datos = (EditText) findViewById(R.id.txtno_clie_datos);
            txtno_clie_natu = (EditText) findViewById(R.id.txtno_clie_natu);
            txtap_clie_natu = (EditText) findViewById(R.id.txtap_clie_natu);
            txtam_clie_natu = (EditText) findViewById(R.id.txtam_clie_natu);
            txtde_dire_clie_datos = (EditText) findViewById(R.id.txtde_dire_clie);
            dddistrito = (Spinner) findViewById(R.id.dddistrito);
            txtnu_tlf1_datos = (EditText) findViewById(R.id.txtnu_tlf1);
            txtnu_tfl2_datos = (EditText) findViewById(R.id.txtnu_tlf2);
            txtnu_tlf3_datos = (EditText) findViewById(R.id.txtnu_tlf3);
            txtnu_docu_iden = (EditText) findViewById(R.id.txtnu_docu_iden);
            txtemail_datos = (EditText) findViewById(R.id.txtemail);
            chkpromo = (CheckBox) findViewById(R.id.chkpromo);
            chkrete = (CheckBox) findViewById(R.id.chkrete);
            chkpercep = (CheckBox) findViewById(R.id.chkpercep);

            String stpromo, strete, stpercep = "";
            txtco_clie_datos.setText(app.co_clie);
            txtno_clie_datos.setText(app.no_razo_soci);
            txtno_clie_natu.setText(app.no_clie_natu);
            txtap_clie_natu.setText(app.ap_clie_natu);
            txtam_clie_natu.setText(app.am_clie_natu);
            txtde_dire_clie_datos.setText(app.de_dire);
            txtnu_tlf1_datos.setText(app.nu_tlf1);
            txtnu_tfl2_datos.setText(app.nu_tlf2);
            txtnu_tlf3_datos.setText(app.nu_tlf3);
            txtnu_docu_iden.setText(app.co_clie);
            txtemail_datos.setText(app.de_dire_mail);
            stpromo = app.st_prom;

            if (stpromo.toString().equals("S")) {
                chkpromo.setChecked(true);

            } else {
                chkpromo.setChecked(false);
            }

            strete = app.st_rete;
            if (strete.toString().equals("S")) {
                chkrete.setChecked(true);

            } else {
                chkrete.setChecked(false);
            }

            stpercep = app.st_comp_perc;
            if (stpromo.toString().equals("S")) {
                chkpercep.setChecked(true);

            } else {
                chkpercep.setChecked(false);
            }
            app.menuGrabarCliente = false;
            app.menuModificarCliente = true;

        } else {
            app.menuGrabarCliente = true;
            app.menuModificarCliente = false;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clientemenu, menu);
        GlobalApp app = (GlobalApp) getApplicationContext();
        app.menuGrabarCliente = true;
        app.menuModificarCliente = false;
        app.menuLimpiarCliente = true;

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        GlobalApp app = (GlobalApp) getApplicationContext();
        MenuItem modificacliente = menu.findItem(R.id.mnuModificarCliente);
        modificacliente.setVisible(app.menuModificarCliente);
        MenuItem agregarcliente = menu.findItem(R.id.mnuGrabarCliente);
        agregarcliente.setVisible(app.menuGrabarCliente);
        MenuItem limpiarcliente = menu.findItem(R.id.mnuLimpiarCliente);
        limpiarcliente.setVisible(app.menuLimpiarCliente);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuGrabarCliente: {
                txtco_clie_datos = (EditText) findViewById(R.id.txtco_clie_datos);
                txtno_clie_datos = (EditText) findViewById(R.id.txtno_clie_datos);
                txtno_clie_natu = (EditText) findViewById(R.id.txtno_clie_natu);
                txtap_clie_natu = (EditText) findViewById(R.id.txtap_clie_natu);
                txtde_dire_clie_datos = (EditText) findViewById(R.id.txtde_dire_clie);
                txtnu_tlf1_datos = (EditText) findViewById(R.id.txtnu_tlf1);
                txtnu_tfl2_datos = (EditText) findViewById(R.id.txtnu_tlf2);
                txtnu_tlf3_datos = (EditText) findViewById(R.id.txtnu_tlf3);
                txtnu_docu_iden = (EditText) findViewById(R.id.txtnu_docu_iden);
                txtemail_datos = (EditText) findViewById(R.id.txtemail);

                if (txtco_clie_datos.getText().toString().length() == 11) {
                    if (txtno_clie_datos.getText().toString().length() == 0) {
                        txtno_clie_datos
                                .setError("Debe ingresar Nombres del Cliente");
                    }
                } else {
                    if (txtno_clie_natu.getText().toString().length() == 0) {
                        txtno_clie_natu
                                .setError("Debe ingresar Nombres del Cliente");
                    }
                    if (txtap_clie_natu.getText().toString().length() == 0) {
                        txtap_clie_natu
                                .setError("Debe ingresar Apellido Paterno del Cliente");
                    }
                    if (txtam_clie_natu.getText().toString().length() == 0) {
                        txtam_clie_natu
                                .setError("Debe ingresar Apellido Materno del Cliente");
                    }

                }
                if (txtde_dire_clie_datos.getText().toString().length() == 0) {
                    txtde_dire_clie_datos.setError("Debe ingresar la Dirección");
                } else if (txtnu_tlf1_datos.getText().toString().length() == 0) {
                    txtnu_tlf1_datos.setError("Debe ingresar el Numero Telefonico");
                } else if (txtemail_datos.getText().toString().length() == 0) {
                    txtemail_datos.setError("Debe ingresar su Email");
                } else {

                    GrabarClieAsyn tarea = new GrabarClieAsyn();

                    tarea.execute();

                }

                return true;
            }

            case R.id.mnuLimpiarCliente: {
                GlobalApp app = (GlobalApp) getApplicationContext();
                txtno_clie_datos = (EditText) findViewById(R.id.txtno_clie_datos);
                txtde_dire_clie_datos = (EditText) findViewById(R.id.txtde_dire_clie);
                txtnu_tlf1_datos = (EditText) findViewById(R.id.txtnu_tlf1);
                txtnu_tfl2_datos = (EditText) findViewById(R.id.txtnu_tlf2);
                txtnu_tlf3_datos = (EditText) findViewById(R.id.txtnu_tlf3);
                txtnu_docu_iden = (EditText) findViewById(R.id.txtnu_docu_iden);
                txtemail_datos = (EditText) findViewById(R.id.txtemail);
                chkpromo = (CheckBox) findViewById(R.id.chkpromo);
                chkrete = (CheckBox) findViewById(R.id.chkrete);
                chkpercep = (CheckBox) findViewById(R.id.chkpercep);

                txtno_clie_datos.setText("");
                txtno_clie_natu.setText("");
                txtap_clie_natu.setText("");
                txtam_clie_natu.setText("");
                txtde_dire_clie_datos.setText("");
                txtnu_tlf1_datos.setText("");
                txtnu_tfl2_datos.setText("");
                txtnu_tlf3_datos.setText("");
                txtnu_docu_iden.setText("");
                txtemail_datos.setText("");
                chkpromo.setChecked(false);
                chkpercep.setChecked(false);
                chkrete.setChecked(false);
                txtnu_docu_iden.requestFocus();
                app.menuGrabarCliente = true;
                app.menuModificarCliente = false;
                app.menuLimpiarCliente = true;

                return true;

            }
            case R.id.mnuModificarDatos: {
                return true;
            }
            default:
                return false;

        }

    }

    private class GrabarClieAsyn extends AsyncTask<Integer, Integer, String> {
        String sSt_promo = "";
        String sSt_rete = "";
        String sSt_perc = "";
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            txtco_clie_datos = (EditText) findViewById(R.id.txtco_clie_datos);
            txtno_clie_datos = (EditText) findViewById(R.id.txtno_clie_datos);
            txtno_clie_natu = (EditText) findViewById(R.id.txtno_clie_natu);
            txtap_clie_natu = (EditText) findViewById(R.id.txtap_clie_natu);
            txtam_clie_natu = (EditText) findViewById(R.id.txtam_clie_natu);
            txtde_dire_clie_datos = (EditText) findViewById(R.id.txtde_dire_clie);
            txtnu_tlf1_datos = (EditText) findViewById(R.id.txtnu_tlf1);
            txtnu_tfl2_datos = (EditText) findViewById(R.id.txtnu_tlf2);
            txtnu_tlf3_datos = (EditText) findViewById(R.id.txtnu_tlf3);
            txtnu_docu_iden = (EditText) findViewById(R.id.txtnu_docu_iden);
            txtemail_datos = (EditText) findViewById(R.id.txtemail);
            chkpromo = (CheckBox) findViewById(R.id.chkpromo);
            chkrete = (CheckBox) findViewById(R.id.chkrete);
            chkpercep = (CheckBox) findViewById(R.id.chkpercep);
            if (chkpromo.isChecked()) {
                sSt_promo = "S";
            } else {
                sSt_promo = "N";
            }

            if (chkrete.isChecked()) {
                sSt_rete = "S";
            } else {
                sSt_rete = "N";
            }

            if (chkpercep.isChecked()) {
                sSt_perc = "S";
            } else {
                sSt_perc = "N";
            }
            app.co_clie = txtco_clie_datos.getText().toString();
            app.no_clie = txtno_clie_datos.getText().toString();
            app.no_clie_natu = txtno_clie_natu.getText()
                    .toString();
            app.ap_clie_natu = txtap_clie_natu.getText().toString();
            app.am_clie_natu = txtam_clie_natu.getText().toString();
            app.de_dire = txtde_dire_clie_datos.getText().toString();
            app.nu_tlf1 = txtnu_tlf1_datos.getText().toString();
            app.nu_tlf2 = txtnu_tfl2_datos.getText().toString();
            app.nu_tlf3 = txtnu_tlf3_datos.getText().toString();
            app.de_dire_mail = txtemail_datos.getText().toString();
            progressDialog = new ProgressDialog(Tab3Activity.this);
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
            if (result.toString().length() > 0) {
                GlobalApp app = (GlobalApp) getApplicationContext();
                // txtno_clie_datos = (EditText)
                // findViewById(R.id.txtno_clie_datos);
                app.no_clie = result.toString();

                txtco_clie_datos = (EditText) findViewById(R.id.txtco_clie_datos);
                Intent data = getIntent();
                data.putExtra("CO_CLIE", txtco_clie_datos.getText().toString());
                data.putExtra("NO_CLIE", txtno_clie_datos.getText().toString());
                setResult(RESULT_OK, data);
                finish();

                // txtno_clie_datos.setText(result.toString());

                // txtnu_coti.setText(result.toString());

                // app.menuGrabarCliente=false;
                // app.menuModificarCliente = true;
                // app.menuLimpiarCliente=true;

                // CambiodeTabDatos();

                Toast.makeText(getBaseContext(), "Se grabo satisfactoriamente",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "No se grabo Verificar!",
                        Toast.LENGTH_LONG).show();
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
            GlobalApp app = (GlobalApp) getApplicationContext();

            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "CrearCliente";
            final String SOAP_ACTION = "http://app.cassinelli.com/CrearCliente";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sCO_CLIE", app.co_clie
                    .toString());
            request.addProperty("sNO_CLIE", app.no_clie
                    .toString());
            request.addProperty("sNO_CLIE_NATU", app.no_clie_natu
                    .toString());
            request.addProperty("sAP_CLIE_NATU", app.ap_clie_natu
                    .toString());
            request.addProperty("sAM_CLIE_NATU", app.am_clie_natu
                    .toString());
            request.addProperty("sST_PROM", sSt_promo.toString());
            request.addProperty("sST_RETE", sSt_rete.toString());
            request.addProperty("sST_PERC", sSt_perc.toString());
            request.addProperty("sDE_DIRE", app.de_dire.toString());
            request.addProperty("sCO_UBIC_GEOG", app.co_ubic_geog.toString());
            request.addProperty("sNU_TLF1", app.nu_tlf1.toString());
            request.addProperty("sNU_TLF2", app.nu_tlf2
                    .toString());
            request.addProperty("sNU_TLF3", app.nu_tlf3
                    .toString());
            request.addProperty("sDI_CORR_ELEC", app.de_dire_mail
                    .toString());
            request.addProperty("sNO_CONT", "");
            request.addProperty("sAP_PATE_CONT", "");
            request.addProperty("sAP_MATE_CONT", "");
            request.addProperty("sDE_DIRE_CONT", "");
            request.addProperty("sNU_TLF1_CONT", "");
            request.addProperty("sCO_UBIC_GEOG_CONT", "");
            request.addProperty("sDE_DIRE_ELEC_CONT", "");
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

    private class CargarDistCotiAsyn extends
            AsyncTask<Void, Integer, DistritoArrayAdapter> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Tab3Activity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(DistritoArrayAdapter result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.toString().length() > 0) {
                GlobalApp app = (GlobalApp) getApplicationContext();
                dddistrito = (Spinner) findViewById(R.id.dddistrito);
                dddistrito.setAdapter(result);
                if (app.co_ubic_geog != null) {
                    DistritoArrayAdapter ad = (DistritoArrayAdapter) dddistrito
                            .getAdapter();
                    dddistrito.setSelection(ad.ubicar(app.co_ubic_geog
                            .toString()));
                }
                if (app.co_clie.toString().length() == 11) {
                    txtno_clie_datos.requestFocus();
                    txtno_clie_natu.setEnabled(false);
                    txtap_clie_natu.setEnabled(false);
                    txtam_clie_natu.setEnabled(false);
                } else {
                    txtno_clie_natu.requestFocus();
                    txtno_clie_datos.setEnabled(false);

                }
            }
        }

        @Override
        protected DistritoArrayAdapter doInBackground(Void... params) {
            // TODO Auto-generated method stub

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaDistrito";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaDistrito";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Dsitrito> distrito = new ArrayList<Dsitrito>();
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Dsitrito[] listaDistrito = new Dsitrito[resSoap
                        .getPropertyCount()];
                for (int i = 0; i < listaDistrito.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Dsitrito con = new Dsitrito();
                    con.co_ubic_geog = ic.getProperty(0).toString();
                    con.de_ubic_geog = ic.getProperty(1).toString();
                    distrito.add(con);

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
            DistritoArrayAdapter adapter = new DistritoArrayAdapter(
                    getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item, distrito);
            return adapter;
        }

    }

}
