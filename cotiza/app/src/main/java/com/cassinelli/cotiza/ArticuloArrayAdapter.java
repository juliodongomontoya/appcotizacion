package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticuloArrayAdapter extends ArrayAdapter<Articulo> {
    public Context context;
    public List<Articulo> detalle;

    public ArticuloArrayAdapter(Context context, List<Articulo> detalle) {
        super(context, R.layout.list_articulo_cont, detalle);
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

            view = layoutInflater.inflate(R.layout.list_articulo_cont, null);

            Holder holder = new Holder();

            holder.txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
            holder.txtCorr = (TextView) view.findViewById(R.id.txtCorr);
            holder.txtVenta = (TextView) view.findViewById(R.id.txtVenta);
            holder.txtCons = (TextView) view.findViewById(R.id.txtCons);
            holder.txtCent = (TextView) view.findViewById(R.id.txtCent);
            holder.txtBloq = (TextView) view.findViewById(R.id.txtBloq);
            holder.txtPerc = (TextView) view.findViewById(R.id.txtSt_perc);
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
        Articulo articulo = getItem(position);
        holder.txtCodigo.setText(articulo.co_item);
        holder.txtCorr.setText(articulo.nu_corr);
        holder.txtVenta.setText(articulo.st_venta);
        holder.txtCons.setText(articulo.st_consignacion);
        holder.txtCent.setText(articulo.st_central);
        holder.txtBloq.setText(articulo.st_bloq);
        holder.txtPerc.setText(articulo.st_perc);
        holder.txtDescripcion.setText(articulo.de_item_larg);
        if (holder.txtBloq.getText().toString().equals("S")) {
            // view.setBackgroundColor(Color.RED);
            holder.txtCodigo.setTextColor(Color.MAGENTA);
            holder.txtCorr.setTextColor(Color.MAGENTA);
            holder.txtVenta.setTextColor(Color.MAGENTA);
            holder.txtCons.setTextColor(Color.MAGENTA);
            holder.txtCent.setTextColor(Color.MAGENTA);
            holder.txtBloq.setTextColor(Color.MAGENTA);
            holder.txtPerc.setTextColor(Color.MAGENTA);
            holder.txtDescripcion.setTextColor(Color.MAGENTA);
        } else if ((Double.parseDouble(holder.txtVenta.getText().toString()) <= 0)
                && (Double.parseDouble(holder.txtCons.getText().toString()) <= 0)
                && (Double.parseDouble(holder.txtCent.getText().toString()) <= 0)) {
            // view.setBackgroundColor(Color.RED);
            holder.txtCodigo.setTextColor(Color.RED);
            holder.txtCorr.setTextColor(Color.RED);
            holder.txtVenta.setTextColor(Color.RED);
            holder.txtCons.setTextColor(Color.RED);
            holder.txtCent.setTextColor(Color.RED);
            holder.txtBloq.setTextColor(Color.RED);
            holder.txtPerc.setTextColor(Color.RED);
            holder.txtDescripcion.setTextColor(Color.RED);

        } else {
            holder.txtCodigo.setTextColor(Color.BLUE);
            holder.txtCorr.setTextColor(Color.BLUE);
            holder.txtVenta.setTextColor(Color.BLUE);
            holder.txtCons.setTextColor(Color.BLUE);
            holder.txtCent.setTextColor(Color.BLUE);
            holder.txtBloq.setTextColor(Color.BLUE);
            holder.txtPerc.setTextColor(Color.BLUE);
            holder.txtDescripcion.setTextColor(Color.BLUE);
        }

        return view;
    }

    private class Holder {

        TextView txtCodigo;
        TextView txtCorr;
        TextView txtVenta;
        TextView txtCons;
        TextView txtCent;
        TextView txtBloq;
        TextView txtPerc;
        TextView txtDescripcion;

    }

}
