package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StockArrayAdapter extends ArrayAdapter<Stock> {
    public Context context;
    public List<Stock> detalle;

    public StockArrayAdapter(Context context, List<Stock> detalle) {
        super(context, R.layout.list_stock_cont, detalle);
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

            view = layoutInflater.inflate(R.layout.list_stock_cont, null);
            TextView txtCo_alma = (TextView) view.findViewById(R.id.txtCo_alma);
            TextView txtDe_alma = (TextView) view
                    .findViewById(R.id.txtDe_alma);
            TextView txtCa_actu = (TextView) view
                    .findViewById(R.id.txtCa_actu);
            TextView txtOv_pend = (TextView) view
                    .findViewById(R.id.txtOvpend);
            TextView txtPe_desp = (TextView) view
                    .findViewById(R.id.txtPend_desp);
            TextView txtOc_pend = (TextView) view
                    .findViewById(R.id.txtOc_pend);


            txtCo_alma.setTextColor(Color.BLACK);
            txtDe_alma.setTextColor(Color.BLACK);
            txtCa_actu.setTextColor(Color.BLACK);
            txtOv_pend.setTextColor(Color.BLACK);
            txtPe_desp.setTextColor(Color.BLACK);
            txtOc_pend.setTextColor(Color.BLACK);
            Holder holder = new Holder();

            holder.txtCo_alma = (TextView) view.findViewById(R.id.txtCo_alma);
            holder.txtDe_alma = (TextView) view
                    .findViewById(R.id.txtDe_alma);
            holder.txtCa_actu = (TextView) view
                    .findViewById(R.id.txtCa_actu);
            holder.txtOv_pend = (TextView) view
                    .findViewById(R.id.txtOvpend);
            holder.txtPe_desp = (TextView) view
                    .findViewById(R.id.txtPend_desp);
            holder.txtOc_pend = (TextView) view
                    .findViewById(R.id.txtOc_pend);

            view.setTag(holder);
            holder.txtCo_alma.setTag(this.detalle.get(position));
        } else {
            view = convertView;
            ((Holder) view.getTag()).txtCo_alma.setTag(this.detalle
                    .get(position));
        }

        Holder holder = ((Holder) view.getTag());
        Stock stock = getItem(position);
        holder.txtCo_alma.setText(stock.co_alma);
        holder.txtDe_alma.setText(stock.de_alma);
        holder.txtCa_actu.setText(stock.ca_actu);
        holder.txtOv_pend.setText(stock.ov_pend);
        holder.txtPe_desp.setText(stock.pe_desp);
        holder.txtOc_pend.setText(stock.oc_pend);
        return view;
    }

    private class Holder {

        TextView txtCo_alma;
        TextView txtDe_alma;
        TextView txtCa_actu;
        TextView txtOv_pend;
        TextView txtPe_desp;
        TextView txtOc_pend;

    }

}
