package com.cassinelli.cotiza;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DistritoArrayAdapter extends ArrayAdapter<Dsitrito> {
    List<Dsitrito> detalle;
    private Context context;

    public DistritoArrayAdapter(Context context, int textViewResourceId,
                                List<Dsitrito> objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.detalle = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView lbl = new TextView(context);
        lbl.setTextColor(Color.BLACK);
        String texto = String.format("%s", this.detalle.get(position).de_ubic_geog);
        lbl.setText(texto);
        return lbl;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView lbl = new TextView(context);
        lbl.setTextColor(Color.BLUE);

        lbl.setText(this.detalle.get(position).de_ubic_geog);
        lbl.setTextSize(20);
        return lbl;
    }

    public int ubicar(String codigo) {
        for (Dsitrito d : detalle) {
            if (d.co_ubic_geog.equals(codigo)) {
                return detalle.indexOf(d);
            }
        }

        return -1;
    }

}
