package com.cassinelli.cotiza;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

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

public class ListaActivityStock extends ListActivity {
    ProgressDialog dialog = null;

    private EditText txtCo_item_stock;
    private EditText txtDe_item_stock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitystock);


        txtCo_item_stock = (EditText) findViewById(R.id.txtco_item_stock);
        txtDe_item_stock = (EditText) findViewById(R.id.txtde_item_larg);
        txtCo_item_stock.setText(getIntent().getStringExtra("CO_ITEM"));
        txtDe_item_stock.setText(getIntent().getStringExtra("DE_ITEM"));
        if (txtCo_item_stock.getText().toString().length() > 0) {

            ConsultarDB c = new ConsultarDB();
            c.execute();

        }
    }


    private class ConsultarDB extends
            AsyncTask<Void, Integer, StockArrayAdapter> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPostExecute(StockArrayAdapter result) {
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            app.co_item = txtCo_item_stock.getText().toString();
            dialog = new ProgressDialog(ListaActivityStock.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();

        }

        @Override
        protected StockArrayAdapter doInBackground(Void... params) {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaStock";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaStock";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_ITEM", app.co_item);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Stock> stock = new ArrayList<Stock>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Stock[] listaStocks = new Stock[resSoap.getPropertyCount()];

                for (int i = 0; i < listaStocks.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Stock con = new Stock();
                    con.co_alma = ic.getProperty(0).toString();
                    con.de_alma = ic.getProperty(1).toString();
                    con.ca_actu = df.format(Double.parseDouble(ic.getProperty(2).toString()));
                    con.ov_pend = df.format(Double.parseDouble(ic.getProperty(3).toString()));
                    con.pe_desp = df.format(Double.parseDouble(ic.getProperty(4).toString()));
                    con.oc_pend = df.format(Double.parseDouble(ic.getProperty(5).toString()));
                    stock.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            StockArrayAdapter adapter = new StockArrayAdapter(
                    getApplicationContext(), stock);

            return adapter;
        }

    }

}
