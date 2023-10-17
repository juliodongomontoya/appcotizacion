package com.cassinelli.cotiza;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class Container extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // creamos el contenedor de Tabs
        TabHost host = getTabHost();
        // A�adimos cada tab, que al ser pulsadas abren sus respectivas
        // Activities
        host.addTab(host.newTabSpec("tab_1").setIndicator("DATOS")
                .setContent(new Intent(this, Tab1Activity.class)));
        host.addTab(host.newTabSpec("tab_2").setIndicator("DETALLE")
                .setContent(new Intent(this, Tab2Activity.class)));
        //host.addTab(host.newTabSpec("tab_3").setIndicator("CLIENTE")
        //	.setContent(new Intent(this, Tab3Activity.class)));
        //host.addTab(host.newTabSpec("tab_4").setIndicator("LISTA")
        //	.setContent(new Intent(this, Tab4Activity.class)));
        // host.addTab(host.newTabSpec("tab_5").setIndicator("MASTER").setContent(new
        // Intent(this, Tab5Activity.class)));

        host.getTabWidget().getChildAt(1).setVisibility(View.GONE);
		
	/*	    for(int i=0;i<host.getTabWidget().getChildCount();i++)
		    {
		        host.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#3300CC")); //unselected
		    }
		    host.getTabWidget().getChildAt(host.getCurrentTab()).setBackgroundColor(Color.parseColor("#FF9900")); // selected
		    */
        // host.setCurrentTab(1); //focus en el segundo tab detalle

    }
    // @Override
    // public void onAttachedToWindow() {
    // this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    // super.onAttachedToWindow();
    // }
    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // SubMenu sub1 = menu.addSubMenu(1, 1, 1, "Salir");
    // SubMenu sub2 = menu.addSubMenu(2, 2, 2, "Informaci�n");
    //
    //
    // return super.onCreateOptionsMenu(menu);
    // }
}