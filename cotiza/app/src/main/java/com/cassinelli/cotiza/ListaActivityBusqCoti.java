package com.cassinelli.cotiza;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaActivityBusqCoti extends ListActivity {
    static final int DATE_DIALOG_INICIAL = 0;
    static final int DATE_DIALOG_FINAL = 1;
    ProgressDialog dialog = null;
    private TextView txtco_clie_busq_coti;
    //private TextView txtno_clie_busq_coti;
    private TextView mDateDisplayInicial;
    private TextView mDateDisplayFinal;
    private ImageButton mPickDateInicial;
    private ImageButton mPickDateFinal;
    private ImageButton btnBuscar;
    private int mYear;
    private int mMonth;
    private int mDay;
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListenerInicial = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplayIncial();

        }
    };
    private DatePickerDialog.OnDateSetListener mDateSetListenerFinal = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplayFinal();

        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivitybusquedacotizacion);

        txtco_clie_busq_coti = (TextView) findViewById(R.id.txtCo_clie_busq);
        //txtno_clie_busq_coti = (TextView) findViewById(R.id.txtNo_clie_busq);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar_cotizacion_busq);
        // capture our View elements
        mDateDisplayInicial = (TextView) findViewById(R.id.dateInicial);
        mPickDateInicial = (ImageButton) findViewById(R.id.pickDateInicial);
        mDateDisplayFinal = (TextView) findViewById(R.id.dateFinal);
        mPickDateFinal = (ImageButton) findViewById(R.id.pickDateFinal);

        // add a click listener to the button
        mPickDateInicial.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                showDialog(DATE_DIALOG_INICIAL);
                // get the current date

            }
        });
        // add a click listener to the button
        mPickDateFinal.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                showDialog(DATE_DIALOG_FINAL);
                // get the current date

            }
        });
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        // display the current date (this method is below)
        updateDisplayIncial();
        updateDisplayFinal();

    }

    // updates the date in the TextView
    private void updateDisplayIncial() {
        mDateDisplayInicial.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay).append("/").append(mMonth + 1).append("/")
                .append(mYear).append(" "));
    }

    private void updateDisplayFinal() {
        mDateDisplayFinal.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mDay).append("/").append(mMonth + 1).append("/")
                .append(mYear).append(" "));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_INICIAL:
                return new DatePickerDialog(this, mDateSetListenerInicial, mYear,
                        mMonth, mDay);
            case DATE_DIALOG_FINAL:
                return new DatePickerDialog(this, mDateSetListenerFinal, mYear,
                        mMonth, mDay);

        }

        return null;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Item item = (Item) l.getAdapter().getItem(position);
        Cotizacion_busq elegido = (Cotizacion_busq) l
                .getItemAtPosition(position);
        String nu_coti_busq = elegido.nu_coti_busq;
        Intent data = getIntent();
        data.putExtra("NU_COTI", nu_coti_busq);
        setResult(RESULT_OK, data);
        finish();

    }

    public void btnBuscar_Cotizacion_busq_Onclick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtco_clie_busq_coti.getWindowToken(), 0);
        ConsultarDB tarea = new ConsultarDB();
        tarea.execute();
    }

    private class ConsultarDB extends
            AsyncTask<Void, Integer, Cotizacion_busqArrayAdapter> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPostExecute(Cotizacion_busqArrayAdapter result) {
            super.onPostExecute(result);
            setListAdapter(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            app.co_clie_busq = txtco_clie_busq_coti.getText().toString();
            app.dfe_inic = mDateDisplayInicial.getText()
                    .toString();
            app.dfe_fina = mDateDisplayFinal.getText()
                    .toString();
            dialog = new ProgressDialog(ListaActivityBusqCoti.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Procesando..");
            dialog.setCancelable(true);
            dialog.setProgress(0);
            dialog.show();


        }

        @Override
        protected Cotizacion_busqArrayAdapter doInBackground(Void... params) {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ListaCotizacionesCliente";
            final String SOAP_ACTION = "http://app.cassinelli.com/ListaCotizacionesCliente";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("sCO_EMPR", variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID", variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN", variables_publicas.co_tien.toString());
            request.addProperty("sCO_CLIE", app.co_clie_busq
                    .toString());
            request.addProperty("dFE_INIC", app.dfe_inic
                    .toString());
            request.addProperty("dFE_FINA", app.dfe_fina
                    .toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            List<Cotizacion_busq> cotizacion_busq = new ArrayList<Cotizacion_busq>();

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Cotizacion_busq[] listaCotizacion_busq = new Cotizacion_busq[resSoap
                        .getPropertyCount()];

                for (int i = 0; i < listaCotizacion_busq.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);
                    Cotizacion_busq con = new Cotizacion_busq();
                    con.nu_coti_busq = ic.getProperty(0).toString();
                    con.co_clie_busq = ic.getProperty(1).toString();
                    con.no_clie_busq = ic.getProperty(2).toString();
                    con.fe_coti_busq = ic.getProperty(3).toString();
                    con.im_tota_busq = df.format(Double.parseDouble(ic.getProperty(4).toString()));
                    con.st_docu = ic.getProperty(5).toString();

                    cotizacion_busq.add(con);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Cotizacion_busqArrayAdapter adapter = new Cotizacion_busqArrayAdapter(
                    getApplicationContext(), cotizacion_busq);

            return adapter;
        }

    }

}
