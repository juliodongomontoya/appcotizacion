package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CondicionArrayAdapter extends ArrayAdapter<Condicion> {
    public Context context;
    public List<Condicion> detalle;

    public CondicionArrayAdapter(Context context, List<Condicion> detalle) {
        super(context, R.layout.list_cond_cont1, detalle);
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

            view = layoutInflater.inflate(R.layout.list_cond_cont1, null);
            TextView txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
            TextView txtDescripcion = (TextView) view
                    .findViewById(R.id.txtDescrip);
            txtCodigo.setTextColor(Color.BLACK);
            txtDescripcion.setTextColor(Color.BLACK);
            Holder holder = new Holder();

            holder.txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
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
        Condicion condicion = getItem(position);
        holder.txtCodigo.setText(condicion.co_cond_vent);
        holder.txtDescripcion.setText(condicion.de_cond_vent);

        return view;
    }

    private class Holder {

        TextView txtCodigo;
        TextView txtDescripcion;

    }

}
