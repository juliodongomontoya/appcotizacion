package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CotizacionArrayAadapter extends ArrayAdapter<Cotizacion> {
    public Context context;
    public List<Cotizacion> detalle;
    //ProgressDialog progressDialog;
    public String nu_corr, co_item;

    public CotizacionArrayAadapter(Context context, List<Cotizacion> detalle) {
        super(context, R.layout.list_cotizacion_cont, detalle);
        this.context = context;
        this.detalle = detalle;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.list_cotizacion_cont, null);
            TextView txtnu_corr = (TextView) view.findViewById(R.id.txtNu_corr_lista);
            TextView txtCodigo = (TextView) view.findViewById(R.id.txtCodigo_lista);
            TextView txtUnidad = (TextView) view.findViewById(R.id.txtUnidad_lista);
            TextView txtAlmacen = (TextView) view.findViewById(R.id.txtAlmacen_lista);
            TextView txtCantidad = (TextView) view.findViewById(R.id.txtCantidad_lista);
            TextView txtPrecio = (TextView) view.findViewById(R.id.txtPrecio_lista);
            TextView txtDescuento = (TextView) view.findViewById(R.id.txtDescuento_lista);
            TextView txtDescrip = (TextView) view.findViewById(R.id.txtDescrip_lista);
            //	TextView txttotsoles = (TextView) view.findViewById(R.id.txttot_soles);
            //	TextView txttotdolar = (TextView) view.findViewById(R.id.txttot_dolar);
            txtnu_corr.setTextColor(Color.BLACK);
            txtCodigo.setTextColor(Color.BLACK);
            txtUnidad.setTextColor(Color.BLACK);
            txtAlmacen.setTextColor(Color.BLACK);
            txtCantidad.setTextColor(Color.BLACK);
            txtPrecio.setTextColor(Color.BLACK);
            txtDescuento.setTextColor(Color.BLACK);
            txtDescrip.setTextColor(Color.BLACK);
            //  txttotdolar.setTextColor(Color.BLACK);
            //  txttotsoles.setTextColor(Color.BLACK);
            final Holder holder = new Holder();

            holder.txtNu_corr_list = (TextView) view.findViewById(R.id.txtNu_corr_lista);
            holder.txtCodigo_list = (TextView) view.findViewById(R.id.txtCodigo_lista);
            holder.txtUnidad_list = (TextView) view.findViewById(R.id.txtUnidad_lista);
            holder.txtAlmacen_list = (TextView) view.findViewById(R.id.txtAlmacen_lista);
            holder.txtCantidad_list = (TextView) view.findViewById(R.id.txtCantidad_lista);
            holder.txtPrecio_list = (TextView) view.findViewById(R.id.txtPrecio_lista);
            holder.txtDescuento_list = (TextView) view.findViewById(R.id.txtDescuento_lista);
            holder.txtDescrip_list = (TextView) view.findViewById(R.id.txtDescrip_lista);
            //holder.txtto_soles_list = (TextView) view.findViewById(R.id.txttot_soles);
            //holder.txtto_dolar_list = (TextView) view.findViewById(R.id.txttot_dolar);
/*			holder.btnEliminar_list = (ImageButton)view.findViewById(R.id.btnEliminar);
			holder.btnEliminar_list.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						Cotizacion c = (Cotizacion)holder.txtNu_corr_list.getTag();
						nu_corr=c.nu_corr_lista.toString();
						co_item = c.co_item_lista.toString();
						//progressDialog = new ProgressDialog(this);
						//ProgressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
						//progressDialog.setMessage("Procesando");
						//progressDialog.setProgress(0);

						EliminarArtAsyn tarea = new EliminarArtAsyn();

						tarea.execute();
						
						detalle.remove(c);
						//notifyDataSetChanged();
						notifyDataSetInvalidated();
					}
				});
	*/

            view.setTag(holder);
            holder.txtNu_corr_list.setTag(this.detalle.get(position));
        } else {
            view = convertView;
            ((Holder) view.getTag()).txtNu_corr_list.setTag(this.detalle
                    .get(position));
        }

        Holder holder = ((Holder) view.getTag());
        Cotizacion cotizacion = getItem(position);
        holder.txtNu_corr_list.setText(cotizacion.nu_corr_lista);
        holder.txtCodigo_list.setText(cotizacion.co_item_lista);
        holder.txtUnidad_list.setText(cotizacion.co_unme_lista);
        holder.txtAlmacen_list.setText(cotizacion.co_alma_lista);
        holder.txtCantidad_list.setText(cotizacion.ca_item_lista);
        holder.txtPrecio_list.setText(cotizacion.pr_vent_lista);
        holder.txtDescuento_list.setText(cotizacion.pc_dcto_lista);
        holder.txtDescrip_list.setText(cotizacion.de_item_larg_lista);
        //holder.txtto_dolar_list.setText(variables_publicas.tot_dolar.toString());
        //holder.txtto_soles_list.setText(variables_publicas.tot_soles.toString());

        return view;
    }

    private class Holder {
        TextView txtNu_corr_list;
        TextView txtCodigo_list;
        TextView txtUnidad_list;
        TextView txtAlmacen_list;
        TextView txtCantidad_list;
        TextView txtPrecio_list;
        TextView txtDescuento_list;
        TextView txtDescrip_list;
        //TextView txtto_dolar_list;
        //TextView txtto_soles_list;
        //	ImageButton btnEliminar_list;

    }

}
