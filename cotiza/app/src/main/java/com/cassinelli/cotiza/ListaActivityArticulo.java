package com.cassinelli.cotiza;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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

public class ListaActivityArticulo extends ListActivity {
    ProgressDialog dialog = null;
    ListView lv;
    String id_spinner = "0";
    String co_item_seleccionado = "";
    String de_item_seleccionado = "";
    private EditText txtBuscar;
    private Spinner spinner_articulo;

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Item item = (Item) l.getAdapter().getItem(position);
        Articulo elegido = (Articulo) l.getItemAtPosition(position);
        String co_item = elegido.co_item;
        Intent data = getIntent();
        data.putExtra("CO_ITEM", co_item);
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivityarticulo);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_articulo);
        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter_articulo = ArrayAdapter
                .createFromResource(this, R.array.criterioarticulo,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_articulo
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter_articulo);
        lv = getListView();
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub

                registerForContextMenu(lv);
                return false;
            }
        });

        // listView = (ListView)findViewById(R.this.);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        // registerForContextMenu(getListView());
        spinner_articulo = (Spinner) findViewById(R.id.spinner_articulo);
        spinner_articulo
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub

                        if (String.valueOf(arg2).equals("0")) {
                            id_spinner = "1";
                        } else if (String.valueOf(arg2).equals("1")) {
                            id_spinner = "2";
                        } else if (String.valueOf(arg2).equals("2")) {
                            id_spinner = "3";
                        } else {
                            id_spinner = "4";
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Articulo elegido = (Articulo) lv.getItemAtPosition(info.position);
        co_item_seleccionado = elegido.co_item;
        de_item_seleccionado = elegido.de_item_larg;
        getMenuInflater().inflate(R.menu.articulomenucontextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.mnuf10:
                Intent intent = new Intent(this, ListaActivityStock.class);
                intent.putExtra("CO_ITEM", co_item_seleccionado
                        .toString());
                intent.putExtra("DE_ITEM", de_item_seleccionado
                        .toString());
                startActivity(intent);
                break;

            default:
                break;
        }

        return super.onContextItemSelected(item);

    }

    public void btnBuscar_articulo_Onclick(View v) {
        if (txtBuscar.getText().toString().length() > 4) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(
                    txtBuscar.getWindowToken(), 0);

            ConsultarDB c = new ConsultarDB();
            c.execute();

        } else {
            txtBuscar.setError("Debe ingresar al menos 5 caracteres");
        }

    }

    private class ConsultarDB extends
            AsyncTask<Void, Integer, ArticuloArrayAdapter> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPostExecute(ArticuloArrayAdapter result) {
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            app.de_busq = txtBuscar.getText().toString();
            dialog = new ProgressDialog(ListaActivityArticulo.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected ArticuloArrayAdapter doInBackground(Void... params) {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaArticulo";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaArticulo";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sST_CRIT", id_spinner.toString());
            request.addProperty("sDE_CRIT", app.de_busq.toString());
            request.addProperty("sCO_ALMA_VENT",
                    variables_publicas.co_alma_vent.toString());
            request.addProperty("sCO_ALMA_CONS",
                    variables_publicas.co_alma_cons.toString());
            request.addProperty("sCO_ALMA_CENT",
                    variables_publicas.co_alma_cent.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Articulo> articulo = new ArrayList<Articulo>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Articulo[] listaArticulo = new Articulo[resSoap
                        .getPropertyCount()];

                for (int i = 0; i < listaArticulo.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Articulo con = new Articulo();
                    con.co_item = ic.getProperty(0).toString();
                    con.nu_corr = ic.getProperty(1).toString();
                    con.st_venta = df.format(Double.parseDouble(ic.getProperty(
                            2).toString()));
                    con.st_consignacion = df.format(Double.parseDouble(ic
                            .getProperty(3).toString()));
                    con.st_central = df.format(Double.parseDouble(ic
                            .getProperty(4).toString()));

                    con.st_perc = ic.getProperty(5).toString();
                    con.st_bloq = ic.getProperty(6).toString();
                    con.de_item_larg = ic.getProperty(7).toString();
                    articulo.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ArticuloArrayAdapter adapter = new ArticuloArrayAdapter(
                    getApplicationContext(), articulo);

            return adapter;
        }

    }

}
