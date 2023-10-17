package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class Cotizacion_busqArrayAdapter extends ArrayAdapter<Cotizacion_busq> {
    public Context context;
    public List<Cotizacion_busq> detalle;
    EditText txtNu_coti_busq;
    EditText txtCo_clie_busq;
    EditText txtNo_clie_busq;
    EditText txtFe_coti_busq;
    EditText txtIm_tota_coti_busq;
    EditText txtSt_docu_coti_busq;

    public Cotizacion_busqArrayAdapter(Context context,
                                       List<Cotizacion_busq> detalle) {
        super(context, R.layout.list_cotizacionbusq_cont, detalle);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.detalle = detalle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.list_cotizacionbusq_cont,
                    null);

            TextView txtNu_coti_busq = (TextView) view
                    .findViewById(R.id.txtnu_coti_busq);
            TextView txtCo_clie_busq = (TextView) view
                    .findViewById(R.id.txtco_clie_coti_busq);
            TextView txtNo_clie_busq = (TextView) view
                    .findViewById(R.id.txtno_clie_coti_busq);
            TextView txtFe_coti_busq = (TextView) view
                    .findViewById(R.id.txtfecha_coti_busq);
            TextView txtIm_tota_coti_busq = (TextView) view
                    .findViewById(R.id.txtim_tota_coti_busq);
            TextView txtSt_docu_coti_busq = (TextView) view
                    .findViewById(R.id.txtst_docu_coti_busq);

            txtNu_coti_busq.setTextColor(Color.BLACK);
            txtCo_clie_busq.setTextColor(Color.BLACK);
            txtNo_clie_busq.setTextColor(Color.BLACK);
            txtFe_coti_busq.setTextColor(Color.BLACK);
            txtIm_tota_coti_busq.setTextColor(Color.BLACK);
            txtSt_docu_coti_busq.setTextColor(Color.BLACK);

            Holder holder = new Holder();

            holder.txtNu_coti_busq = (TextView) view
                    .findViewById(R.id.txtnu_coti_busq);
            holder.txtCo_clie_coti_busq = (TextView) view
                    .findViewById(R.id.txtco_clie_coti_busq);
            holder.txtNo_clie_coti_busq = (TextView) view
                    .findViewById(R.id.txtno_clie_coti_busq);
            holder.txtFe_coti_busq = (TextView) view
                    .findViewById(R.id.txtfecha_coti_busq);
            holder.txtIm_tota_coti_busq = (TextView) view
                    .findViewById(R.id.txtim_tota_coti_busq);
            holder.txtSt_docu_coti_busq = (TextView) view
                    .findViewById(R.id.txtst_docu_coti_busq);

            view.setTag(holder);
            holder.txtNu_coti_busq.setTag(this.detalle.get(position));
        } else {
            view = convertView;
            ((Holder) view.getTag()).txtNu_coti_busq.setTag(this.detalle
                    .get(position));
        }

        Holder holder = ((Holder) view.getTag());
        Cotizacion_busq cotizacion_busq = getItem(position);
        holder.txtNu_coti_busq.setText(cotizacion_busq.nu_coti_busq);
        holder.txtCo_clie_coti_busq.setText(cotizacion_busq.co_clie_busq);
        holder.txtNo_clie_coti_busq.setText(cotizacion_busq.no_clie_busq);
        holder.txtFe_coti_busq.setText(cotizacion_busq.fe_coti_busq);
        holder.txtIm_tota_coti_busq.setText(cotizacion_busq.im_tota_busq);
        holder.txtSt_docu_coti_busq.setText(cotizacion_busq.st_docu);

        return view;
    }

    private class Holder {

        TextView txtNu_coti_busq;
        TextView txtCo_clie_coti_busq;
        TextView txtNo_clie_coti_busq;
        TextView txtFe_coti_busq;
        TextView txtIm_tota_coti_busq;
        TextView txtSt_docu_coti_busq;

    }
}
