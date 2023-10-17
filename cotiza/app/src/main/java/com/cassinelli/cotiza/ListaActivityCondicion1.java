package com.cassinelli.cotiza;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;


public class ListaActivityCondicion1 extends Activity {
    ListView listView;
    private EditText txtBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaactivity);
        listView = (ListView) findViewById(R.id.lista1);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
    }

    public void btnBuscar_Onclick(View v) {


        final String NAMESPACE = "http://app.cassinelli.com/";
        final String URL = variables_publicas.direccionIp + "/Service.asmx";
        final String METHOD_NAME = "ListaCondicion";
        final String SOAP_ACTION = "http://app.cassinelli.com/ListaCondicion";

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("cond", txtBuscar.getText().toString());

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

            LocalArrayAdapter adapter = new LocalArrayAdapter(this,
                    condicion);

            listView.setAdapter(adapter);

        } catch (Exception e) {

        }

    }

    private class LocalArrayAdapter extends ArrayAdapter<Condicion> {

        private Context context;
        private List<Condicion> detalle;

        public LocalArrayAdapter(Context context, List<Condicion> detalle) {

            super(context, R.layout.list_cond_cont1, detalle);
            this.context = context;
            this.detalle = detalle;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);
            View view = null;

            if (convertView == null) {

                LayoutInflater layoutInflater =
                        (LayoutInflater) context.getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);

                view = layoutInflater.inflate(
                        R.layout.list_cond_cont1,
                        null);

                final Holder holder = new Holder();

                holder.txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
                holder.txtDescripcion = (TextView) view.findViewById(R.id.txtDescrip);

                view.setTag(holder);
                holder.txtCodigo.setTag(this.detalle.get(position));
            } else {
                view = convertView;
                ((Holder) view.getTag()).txtCodigo.setTag(this.detalle.get(position));
            }

            Condicion condicion = this.detalle.get(position);
            Holder holder = ((Holder) view.getTag());
            holder.txtCodigo.setText(condicion.co_cond_vent);
            holder.txtDescripcion.setText(condicion.de_cond_vent);

            return view;
        }

    }


    private class Holder {

        TextView txtCodigo;
        TextView txtDescripcion;

    }
}// final