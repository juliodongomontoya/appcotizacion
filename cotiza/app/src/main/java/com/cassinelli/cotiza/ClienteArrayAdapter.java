package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ClienteArrayAdapter extends ArrayAdapter<Cliente> {
    public Context context;
    public List<Cliente> detalle;

    public ClienteArrayAdapter(Context context, List<Cliente> detalle) {
        super(context, R.layout.list_cliente_cont, detalle);
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

            view = layoutInflater.inflate(R.layout.list_cliente_cont, null);
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
        Cliente cliente = getItem(position);
        holder.txtCodigo.setText(cliente.co_clie);
        holder.txtDescripcion.setText(cliente.no_clie);

        return view;
    }

    private class Holder {

        TextView txtCodigo;
        TextView txtDescripcion;

    }
}
