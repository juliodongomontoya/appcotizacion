package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MasterArrayAdapter extends ArrayAdapter<Master> {
    public Context context;
    public List<Master> detalle;

    public MasterArrayAdapter(Context context, List<Master> detalle) {
        super(context, R.layout.list_master_cont, detalle);
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

            view = layoutInflater.inflate(R.layout.list_master_cont, null);

            TextView txtcodigo = (TextView) view.findViewById(R.id.txtCodigo);
            TextView txtpr_Soles = (TextView) view.findViewById(R.id.txtPr_Soles);
            TextView txtpr_Dolar = (TextView) view.findViewById(R.id.txtPr_Dolar);
            TextView txtDescripcion = (TextView) view
                    .findViewById(R.id.txtDescrip);
            // if (Double.parseDouble(txtVenta.getText().toString())==0.00) {
            txtcodigo.setTextColor(Color.BLACK);
            txtpr_Soles.setTextColor(Color.BLACK);
            txtpr_Dolar.setTextColor(Color.BLACK);
            txtDescripcion.setTextColor(Color.BLACK);
            //}

            Holder holder = new Holder();

            holder.txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
            holder.txtPr_Soles = (TextView) view.findViewById(R.id.txtPr_Soles);
            holder.txtPr_Dolar = (TextView) view.findViewById(R.id.txtPr_Dolar);

            holder.txtDescripcion = (TextView) view
                    .findViewById(R.id.txtDescrip);

            view.setTag(holder);

            holder.txtCodigo.setTag(this.detalle.get(position));
        } else {
            view = convertView;
            ((Holder) view.getTag()).txtCodigo.setTag(this.detalle
                    .get(position));
        }

        Holder holder = ((Holder) view.getTag());
        Master master = getItem(position);
        holder.txtCodigo.setText(master.co_mast);
        holder.txtPr_Soles.setText(master.pr_vent_soles);
        holder.txtPr_Dolar.setText(master.pr_vent_dolar);
        holder.txtDescripcion.setText(master.de_mast);
        return view;
    }

    private class Holder {

        TextView txtCodigo;
        TextView txtPr_Soles;
        TextView txtPr_Dolar;
        TextView txtDescripcion;

    }

}
