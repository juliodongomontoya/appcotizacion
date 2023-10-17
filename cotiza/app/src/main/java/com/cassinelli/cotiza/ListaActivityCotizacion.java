package com.cassinelli.cotiza;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class ListaActivityCotizacion extends ListActivity {
    ProgressDialog dialog = null;
    EditText txtnu_coti_lista;
    EditText txtnu_corr_lista;
    EditText txtco_item_lista;
    EditText txttot_dolar;
    EditText txttot_soles;
    EditText txtpc_perc;
    EditText txtim_perc;
    LinearLayout lEliminar;
    private static final int VNP_REQUEST_CODE = 1;
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        // super.onListItemClick(l, v, position, id);
        Cotizacion elegido = (Cotizacion) l.getItemAtPosition(position);
        String co_item = elegido.co_item_lista;
        Intent data = getIntent();
        data.putExtra("CO_ITEM", co_item);
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        ConsultarCotizacion p = new ConsultarCotizacion();
        p.execute();

        // ListaCotizacion_gene();

    }

    public void ListaCotizacion_gene() {
        CabeceraTotales cabtot = new CabeceraTotales();
        cabtot.Totales_cabecera();
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#########.00", simbolos);
        txttot_dolar.setText(df.format(Double
                .parseDouble(variables_publicas.tot_dolar.toString())));
        txttot_soles.setText(df.format(Double
                .parseDouble(variables_publicas.tot_soles.toString())));
        txtpc_perc.setText(df.format(Double
                .parseDouble(variables_publicas.pc_perc.toString())));
        txtim_perc.setText(df.format(Double
                .parseDouble(variables_publicas.im_perc.toString())));

        final String NAMESPACE = "http://app.cassinelli.com/";
        final String URL = variables_publicas.direccionIp + "/Service.asmx";
        final String METHOD_NAME = "ListaCotizacion";
        final String SOAP_ACTION = "http://app.cassinelli.com/ListaCotizacion";

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
                con.ca_item_lista = df.format(Double.parseDouble(ic
                        .getProperty(4).toString()));
                con.pr_vent_lista = df.format(Double.parseDouble(ic
                        .getProperty(5).toString()));
                con.pc_dcto_lista = df.format(Double.parseDouble(ic
                        .getProperty(6).toString()));
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

        setListAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitycotizacion);
        AppCompatCallback callback = new AppCompatCallback() {
            @Override
            public void onSupportActionModeStarted(ActionMode mode) {

            }

            @Override
            public void onSupportActionModeFinished(ActionMode mode) {

            }

            @Nullable
            @Override
            public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
                return null;
            }
        };
        AppCompatDelegate delegate = AppCompatDelegate.create(this, callback);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.listaactivitycotizacion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        delegate.setSupportActionBar(toolbar);
        //delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        delegate.getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        //toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavUtils.navigateUpFromSameTask(SavedReportActivity.this);
            }
        });*/

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtnu_coti_lista = (EditText) findViewById(R.id.txtnu_coti_lista);
        txtnu_coti_lista.setText(variables_publicas.nu_coti_fina.toString());
        txttot_dolar = (EditText) findViewById(R.id.txttot_dolar);
        txttot_soles = (EditText) findViewById(R.id.txttot_soles);
        txtpc_perc = (EditText) findViewById(R.id.txtpc_perc);
        txtim_perc = (EditText) findViewById(R.id.txtim_perc);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listadomenu, menu);

        GlobalApp app = (GlobalApp) getApplicationContext();

        app.menuConvertirCoti = true;
        app.menuDuplicarCoti = true;
        app.menuAplicaDctoItem = true;
        app.menuConvertirDocu = true;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        GlobalApp app = (GlobalApp) getApplicationContext();
        MenuItem convertircoti = menu.findItem(R.id.mnuConvetir);
        convertircoti.setVisible(app.menuConvertirCoti);
        MenuItem duplicarcoti = menu.findItem(R.id.mnuDuplicarCoti);
        duplicarcoti.setVisible(app.menuDuplicarCoti);
        MenuItem aplicadctoitem = menu.findItem(R.id.mnuAplicarDsctoItem);
        aplicadctoitem.setVisible(app.menuAplicaDctoItem);
        MenuItem convertirdocu = menu.findItem(R.id.mnuConvetirF);
        convertircoti.setVisible(app.menuConvertirDocu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalApp app = (GlobalApp) getApplicationContext();
        int id = item.getItemId();
        switch (id) {

            case R.id.mnuAplicarDsctoItem: {
                if (txtnu_coti_lista.getText().toString().length() == 0) {
                    txtnu_coti_lista.setError("Debe ingresar una cotizacion");

                } else {
                    if (app.st_docu.toString().equals("CON")) {
                        Builder builder = new Builder(
                                ListaActivityCotizacion.this);
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setMessage("La Cotización ya esta convertida ");
                        builder.setTitle("Aviso");
                        builder.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
                        builder.create().show();
                    } else if (app.st_docu.toString().equals("ACE")) {
                        Builder builder = new Builder(
                                ListaActivityCotizacion.this);
                        builder.setMessage("Cotización ya fue aplicada la tabla de descuentos ");
                        builder.setTitle("Aviso");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
                        builder.create().show();


                    } else if (app.st_docu.toString().equals("APL")) {
                        Builder builder = new Builder(
                                ListaActivityCotizacion.this);
                        builder.setMessage("Cotización ya fue aplicada la tabla de descuentos ");
                        builder.setTitle("Aviso");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
                        builder.create().show();


                    } else {
                        AplicarDesctoItemAsyn tarea = new AplicarDesctoItemAsyn();
                        tarea.execute();
                    }
                }
                return true;

            }
            case R.id.mnuDuplicarCoti: {
                if (txtnu_coti_lista.getText().toString().length() == 0) {
                    txtnu_coti_lista.setError("Debe ingresar una cotizacion");

                } else {

                    DuplicarCotiAsyn tarea = new DuplicarCotiAsyn();
                    tarea.execute();

                }

                return true;
            }

            case R.id.mnuConvetir: {
                if (txtnu_coti_lista.getText().toString().length() == 0) {
                    txtnu_coti_lista.setError("Debe ingresar una cotizacion");

                } else {
                    if (!app.st_docu.toString().equals("CON")) {
                        ConvertirCotiAsyn tarea = new ConvertirCotiAsyn();
                        tarea.execute();
                    } else {
                        Builder builder = new Builder(
                                ListaActivityCotizacion.this);
                        builder.setMessage("La Cotización ya esta convertida ");
                        builder.setTitle("Aviso");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
                        builder.create().show();
                    }
                }
                return true;

            }
            case R.id.mnuConvetirF: {
                if (txtnu_coti_lista.getText().toString().length() == 0) {
                    txtnu_coti_lista.setError("Debe ingresar una cotizacion");

                } else {
                    if (!app.st_docu.toString().equals("CON")) {
                        ConvertirCotiAsyn tarea = new ConvertirCotiAsyn();
                        tarea.execute();
                    } else {
                        Builder builder = new Builder(
                                ListaActivityCotizacion.this);
                        builder.setMessage("La Cotización ya esta convertida ");
                        builder.setTitle("Aviso");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        });
                        builder.create().show();
                    }
                }
                return true;

            }
            case R.id.mnuPago: {
                if (txtnu_coti_lista.getText().toString().length() == 0) {
                    txtnu_coti_lista.setError("Debe ingresar una cotizacion");

                } else {
                    if (!app.st_docu.toString().equals("CON")) {
                        openMuxiMobile("1","10.50");
                    } else {
                        Builder builder = new Builder(
                                ListaActivityCotizacion.this);
                        builder.setMessage("La Cotización ya esta convertida ");
                        builder.setTitle("Aviso");
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                        builder.setPositiveButton("Ok", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub

                                finish();
                            }
                        });
                        builder.create().show();
                    }
                }
                return true;

            }
            default:
                return false;

        }

    }
    public void openMuxiMobile(String sOperacion, String nMonto){
        String url = String.format("posweb://transact/?EXTCALLER=Cassinelli-point&EXTOP=%s&EXTMONTO=%s",sOperacion,String.valueOf(nMonto));
        Intent i  = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivityForResult(i,VNP_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VNP_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String mPWRIPARAMS = "";
            Bundle props = data.getExtras();
            if(data.getAction()!=null && (data.getAction().equals(Intent.ACTION_VIEW) || data.getAction().equals(Intent.ACTION_MAIN))) {
                Uri newuri = data.getData();
                if (newuri != null)
                    mPWRIPARAMS = newuri.getEncodedQuery();
            }
            else if (props != null){
                 mPWRIPARAMS = props.getString("PWRIPARAMS");
            }
            String res="";
            String tarjeta="";

            if (!mPWRIPARAMS.isEmpty()) {
                res = mPWRIPARAMS.substring(24, 26);
                if(!res.isEmpty()) {
                    if (res=="00"){
                        tarjeta = mPWRIPARAMS.substring(26, 28);
                    }
                }
            }
            Log.d("onActivityResult", mPWRIPARAMS);
        }
    }

    public void CambiodeTab() {
        Container ta = (Container) this.getParent();
        TabHost th = ta.getTabHost();
        th.getTabWidget().getChildAt(1).setVisibility(View.GONE);
        th.setCurrentTab(0);

    }

    private class ConsultarCotizacion extends
            AsyncTask<Void, Integer, CotizacionArrayAadapter> {

        @Override
        protected void onPostExecute(CotizacionArrayAadapter result) {
            setListAdapter(result);
            CabeceraTotales cabtot = new CabeceraTotales();
            cabtot.Totales_cabecera();
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);
            txttot_dolar.setText(df.format(Double
                    .parseDouble(variables_publicas.tot_dolar.toString())));
            txttot_soles.setText(df.format(Double
                    .parseDouble(variables_publicas.tot_soles.toString())));
            txtpc_perc.setText(df.format(Double
                    .parseDouble(variables_publicas.pc_perc.toString())));
            txtim_perc.setText(df.format(Double
                    .parseDouble(variables_publicas.im_perc.toString())));
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ListaActivityCotizacion.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected CotizacionArrayAadapter doInBackground(Void... params) {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);
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
                    con.ca_item_lista = df.format(Double.parseDouble(ic
                            .getProperty(4).toString()));
                    con.pr_vent_lista = df.format(Double.parseDouble(ic
                            .getProperty(5).toString()));
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

    private class DuplicarCotiAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(ListaActivityCotizacion.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.toString().length() > 0) {
                // Toast.makeText(getBaseContext(), "Cotizacion Duplicada N� " +
                // result.toString(),
                // Toast.LENGTH_LONG).show();

                Builder builder = new Builder(
                        ListaActivityCotizacion.this);
                builder.setMessage("Cotización Duplicada " + result.toString());
                builder.setTitle("Aviso");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
                builder.create().show();

            } else {
                Toast.makeText(getBaseContext(), "No se Duplico Verificar!",
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
            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "DuplicarCotizacion";
            final String SOAP_ACTION = "http://app.cassinelli.com/DuplicarCotizacion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sCO_ALMA",
                    variables_publicas.co_alma_vent.toString());
            request.addProperty("sCO_CLIE", app.co_clie.toString());
            request.addProperty("sNU_COTI", app.nu_coti
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

    private class ConvertirCotiAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(ListaActivityCotizacion.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            String numerodepedidos = "";
            if (result.toString().length() > 0) {
                String[] pedidos = result.split("\\|");
                for (int i = 0; i < pedidos.length; i++) {
                    if (pedidos[i].length() > 0) {
                        numerodepedidos += pedidos[i] + "\n";
                    }
                }
                Builder builder = new Builder(
                        ListaActivityCotizacion.this);
                builder.setMessage("Cotización Convertida " + "\n"
                        + numerodepedidos.toString());
                builder.setTitle("Aviso");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub

                        app.st_docu = "CON";
                        finish();

                    }
                });
                builder.create().show();

            } else {
                Toast.makeText(getBaseContext(),
                        "Verificar conversión a pedido fallo !",
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
            String res = "";
            String res1 = "";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ValidaStockCotiza";
            final String SOAP_ACTION = "http://app.cassinelli.com/ValidaStockCotiza";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti
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
                res1 = resultado_xml.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!res1.toString().equals("N")) {
                Builder builder = new Builder(
                        ListaActivityCotizacion.this);
                builder.setMessage("No se puede convertir porque el codigo "
                        + res.toString() + " esta bloqueado y no tiene stock");
                builder.setTitle("Aviso");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub

                        finish();

                    }
                });
                builder.create().show();
            } else {

                final String NAMESPACE1 = "http://app.cassinelli.com/";
                final String URL1 = variables_publicas.direccionIp
                        + "/Service.asmx";
                final String METHOD_NAME1 = "ConvertirPedido";
                final String SOAP_ACTION1 = "http://app.cassinelli.com/ConvertirPedido";

                SoapObject request1 = new SoapObject(NAMESPACE1, METHOD_NAME1);

                request1.addProperty("sCO_EMPR",
                        variables_publicas.co_empr.toString());
                request1.addProperty("sCO_UNID",
                        variables_publicas.co_unid.toString());
                request1.addProperty("sCO_TIEN",
                        variables_publicas.co_tien.toString());
                request1.addProperty("sNU_COTI", app.nu_coti
                        .toString());
                request1.addProperty("sCO_USUA", variables_publicas.co_usua
                        .toString());
                SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                envelope1.dotNet = true;

                envelope1.setOutputSoapObject(request1);

                HttpTransportSE transporte1 = new HttpTransportSE(URL1);

                try {
                    transporte1.call(SOAP_ACTION1, envelope1);
                    SoapPrimitive resultado_xml = (SoapPrimitive) envelope1
                            .getResponse();
                    res = resultado_xml.toString();

                } catch (Exception e) {

                }

            }
            return res;
        }
    }

    private class AplicarDesctoItemAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(ListaActivityCotizacion.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.toString().equals("OK")) {
                Builder builder = new Builder(
                        ListaActivityCotizacion.this);

                ConsultarCotizacion p = new ConsultarCotizacion();
                p.execute();

                builder.setMessage("Descuento aplicado");
                builder.setTitle("Aviso");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Ok", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        finish();
                        app.st_docu = "ACE";
                    }
                });
                builder.create().show();

            } else {
                Toast.makeText(getBaseContext(),
                        "Descuento no aplicado",
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
            String res = "";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "AplicarDescuentoDeta";
            final String SOAP_ACTION = "http://app.cassinelli.com/AplicarDescuentoDeta";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString());
            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti
                    .toString());

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
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return res;
        }
    }
}
