package com.cassinelli.cotiza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    public final int dialogo_alert = 0;
    public String msje = "";
    ProgressDialog progressDialog;
    // referencia a la clase
    variables_publicas variables = new variables_publicas();
    private EditText txtUsuario;
    private EditText txtPassword;
    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnIniciar = (Button) findViewById(R.id.btnIngresar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InicioSesion(view);
            }
        });
       // if (android.os.Build.VERSION.SDK_INT > 9) {
       //     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       //     StrictMode.setThreadPolicy(policy);
      //     }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void InicioSesion(View v) {
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        if (txtUsuario.getText().toString().length() == 0) {
            txtUsuario.setError("Debe ingresar un usuario");
        } else if (txtPassword.getText().toString().length() == 0) {
            txtPassword.setError("Debe ingresar un password");
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando");
            progressDialog.setProgress(0);

            InicioSesionAsyn tarea = new InicioSesionAsyn();

            tarea.execute();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: {
                txtUsuario = (EditText) findViewById(R.id.txtUsuario);
                txtPassword = (EditText) findViewById(R.id.txtPassword);

                if (txtUsuario.getText().toString().length() == 0) {
                    txtUsuario.setError("Debe ingresar un usuario");
                } else if (txtPassword.getText().toString().length() == 0) {
                    txtPassword.setError("Debe ingresar un password");
                } else {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Procesando");
                    progressDialog.setProgress(0);

                    InicioSesionAsyn tarea = new InicioSesionAsyn();

                    tarea.execute();

                }
                //InicioSesion(MainActivity);
            }
            default:
                return false;
        }
    }

    private class InicioSesionAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            variables_publicas.co_usua = txtUsuario.getText().toString();
            variables_publicas.pa_usua = txtPassword.getText().toString();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.equals("0")) {
                GlobalApp app = (GlobalApp) getApplicationContext();
                app.co_clie = "";

                Toast.makeText(getBaseContext(),
                        "Bienvenido " + variables_publicas.no_usua,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),
                        Container.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(getBaseContext(), "Acceso restringuido",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... arg0) {
            //String SOAP_ACTION = "";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "LoginUsuario";
            final String SOAP_ACTION = "http://app.cassinelli.com/LoginUsuario";
            String msg = "";


            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("user", variables_publicas.co_usua.toString());
            request.addProperty("password", variables_publicas.pa_usua.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            envelope.implicitTypes = true;


            // disableConnectionReuseIfNecessary();
            try {
                //envelope.bodyOut = request;
                HttpTransportSE transporte = new HttpTransportSE(URL);
                //transporte.debug = true;

                transporte.call(SOAP_ACTION, envelope);
                //transporte.call(null, envelope);
                //SoapObject resSoap;
                //resSoap = (SoapObject) envelope.getResponse();
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                //SoapObject resSoap = (SoapObject) envelope.bodyIn;
                //SoapPrimitive resSoap = (SoapPrimitive) envelope.getResponse();

                Usuario[] listaUsuario = new Usuario[resSoap.getPropertyCount()];
                if (listaUsuario.length > 0) {
                    for (int i = 0; i < listaUsuario.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);
                        variables_publicas.co_empr = ic.getProperty(0).toString();
                        variables_publicas.co_unid = ic.getProperty(1).toString();
                        variables_publicas.no_usua = ic.getProperty(2)
                                .toString();
                        variables_publicas.co_vend = ic.getProperty(3)
                                .toString();
                        variables_publicas.co_tien = ic.getProperty(4)
                                .toString();
                        variables_publicas.co_grup = ic.getProperty(5)
                                .toString();
                        //variables_publicas.co_usua = txtUsuario.getText()
                        //		.toString();
                        // verificar descuentos maximos por usuario
                        GlobalApp app = (GlobalApp) getApplicationContext();
                        final String NAMESPACE1 = "http://app.cassinelli.com/";
                        final String URL1 = variables_publicas.direccionIp
                                + "/Service.asmx";
                        final String METHOD_NAME1 = "DescuentoValida";
                        final String SOAP_ACTION1 = "http://app.cassinelli.com/DescuentoValida";

                        SoapObject request1 = new SoapObject(NAMESPACE1,
                                METHOD_NAME1);

                        request1.addProperty("sCO_EMPR",
                                variables_publicas.co_empr.toString());
                        request1.addProperty("sCO_USUA", variables_publicas.co_usua
                                .toString());

                        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                                SoapEnvelope.VER11);

                        envelope1.dotNet = true;

                        envelope1.setOutputSoapObject(request1);

                        HttpTransportSE transporte1 = new HttpTransportSE(URL1);
                        try {
                            transporte1.call(SOAP_ACTION1, envelope1);
                            SoapObject resSoap1 = (SoapObject) envelope1
                                    .getResponse();
                            Descuento[] listaDescuento = new Descuento[resSoap1
                                    .getPropertyCount()];
                            if (listaDescuento.length > 0) {
                                for (int x = 0; x < listaDescuento.length; x++) {
                                    SoapObject ic1 = (SoapObject) resSoap1
                                            .getProperty(0);
                                    app.pc_dcto_mdocu = ic1.getProperty(x)
                                            .toString();
                                    app.pc_dcto_marti = ic1.getProperty(x)
                                            .toString();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Toast.makeText(getBaseContext(), e.toString(),
                            // Toast.LENGTH_LONG).show();
                        }

                        msg = "0";

                    }

                } else {
                    msg = "1";
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getBaseContext(), e.toString(),
                // Toast.LENGTH_LONG).show();
            }

            return msg;


        }

    }

    public class Descuento {

        public String pc_dcto_mcab;
        public String pc_dcto_mart;

    }
    private void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}
