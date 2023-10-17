package com.cassinelli.cotiza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class UtilitarioActivity extends AppCompatActivity {
    RadioGroup radiogruputil;
    RadioButton radioanulacion;
    RadioButton radioduplicado;
    RadioButton radiocierre;
    RadioButton radioinicializacion;
    RadioButton radiocopiavoucher;
    RadioButton radioescanear;
    private static final int VNP_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utilitario_niubiz);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        GlobalApp app = (GlobalApp) getApplicationContext();
        app.menuProcesarUtil = true;
        radiogruputil = findViewById(R.id.radiogruputil);
        radioanulacion = findViewById(R.id.radioanulacion);
        radioduplicado = findViewById(R.id.radioduplicado);
        radiocierre = findViewById(R.id.radiocierre);
        radiocopiavoucher = findViewById(R.id.radiocopiavoucher);
        radioinicializacion = findViewById(R.id.radioinicializacion);
        radioescanear = findViewById(R.id.radioescanear);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.utilmenu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // GlobalApp app = (GlobalApp) getApplicationContext();
        // MenuItem utilitario = menu.findItem(R.id.mnuProcesarNiubiz);
        // utilitario.setVisible(app.menuProcesarUtil);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalApp app = (GlobalApp) getApplicationContext();
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuUtilNiubiz: {
                if(radioanulacion.isChecked()){
                    openMuxiMobile("2","0");
                }
                if(radiocierre.isChecked()){
                    openMuxiMobile("11","0");
                }
                if(radiocopiavoucher.isChecked()){
                    openMuxiMobile("13","0");
                }
                if(radioduplicado.isChecked()){
                    openMuxiMobile("9","0");
                }
                if(radioinicializacion.isChecked()){
                    openMuxiMobile("0","0");
                }
                if(radioescanear.isChecked()){
                    openMuxiMobile("40","0");
                }
            }
            default:
                return false;
        }
    }
    public void openMuxiMobile(String sOperacion, String nMonto){
        String url = String.format("posweb://transact/?EXTCALLER=Cassinelli-point&EXTOP=%s&EXTMONTO=%s",sOperacion,nMonto);
        Intent i  = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivityForResult(i,VNP_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VNP_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String mPWRIPARAMS = "";
            Bundle props = data.getExtras();
            if(data.getAction()!=null && (data.getAction().equals(Intent.ACTION_VIEW) || data.getAction().equals(Intent.ACTION_MAIN))) {
                Uri newuri = data.getData();
                if (newuri != null)
                    mPWRIPARAMS = newuri.getEncodedQuery();
            }
            else if (props != null){
                mPWRIPARAMS = props.getString("PWRIPARAMS");
            }
            String res="";
            if (!mPWRIPARAMS.isEmpty()) {
                res = mPWRIPARAMS.substring(24, 26);
                if(!res.isEmpty()) {
                    //openResponse(res);

                }
            }
            Log.d("onActivityResult", mPWRIPARAMS);
        }
    }
}
