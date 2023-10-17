package com.cassinelli.cotiza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Tab2Activity extends AppCompatActivity {
    public static final int HELP_ARTICULO = 1;
    public static final int HELP_COTIZACION = 2;
    public static final int HELP_MASTER = 3;
    ProgressDialog progressDialog;
    EditText txtco_mone;
    EditText txtnu_coti_deta;
    EditText txtco_item;
    EditText txtde_item_larg;
    EditText txtco_unme;
    EditText txtde_unme;
    EditText txtco_alma_detalle;
    EditText txtde_alma_detalle;
    EditText txtca_item;
    EditText txtpr_vent;
    EditText txtca_conv;
    EditText txtim_dscto_detalle;
    EditText txtempaque;
    EditText txtca_actu;
    EditText txtde_obse_detalle;
    EditText txtst_bloq;
    ImageButton btn_buscar_articulo;

    public static double Redondear(double numero, int digitos) {
        int cifras = (int) Math.pow(10, digitos);
        return Math.rint(numero * cifras) / cifras;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        GlobalApp app = (GlobalApp) getApplicationContext();
        ImageButton btn_buscar_articulo = (ImageButton) findViewById(R.id.btn_buscar_articulo);
        txtnu_coti_deta = (EditText) findViewById(R.id.txtnu_coti_deta);
        txtnu_coti_deta.setText(variables_publicas.nu_coti_fina.toString());
        txtde_item_larg = (EditText) findViewById(R.id.txtde_item_larg);
        txtco_unme = (EditText) findViewById(R.id.txtco_unme);
        txtde_unme = (EditText) findViewById(R.id.txtde_unme);
        txtco_alma_detalle = (EditText) findViewById(R.id.txtco_alma_detalle);
        txtde_alma_detalle = (EditText) findViewById(R.id.txtde_alma_detalle);
        txtca_item = (EditText) findViewById(R.id.txtca_item);
        txtpr_vent = (EditText) findViewById(R.id.txtpr_vent);
        txtca_conv = (EditText) findViewById(R.id.txtca_conv);
        txtim_dscto_detalle = (EditText) findViewById(R.id.txtim_dscto_deta);
        txtempaque = (EditText) findViewById(R.id.txtco_empa);
        txtca_actu = (EditText) findViewById(R.id.txtca_actu);
        txtde_obse_detalle = (EditText) findViewById(R.id.txtde_obse_deta);
        txtst_bloq = (EditText) findViewById(R.id.txtst_bloq);
        txtco_mone = (EditText) findViewById(R.id.txtco_mone);

        txtco_mone.setText("SOL");

        app.menuGrabarDetalle = true;
        app.menuModificarDetalle = false;
        app.menuEliminarDetalle = false;
        app.menuLimpiarDetalle = true;
        app.menuMasterDetalle = true;

        txtco_item = (EditText) findViewById(R.id.txtco_item);

        txtco_item.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                if (hasFocus == false) {
                    if (txtco_item.getText().toString().length() > 0) {
                        ValidaArticulo();
                    }

                }
            }
        });

        btn_buscar_articulo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                txtco_item.requestFocus();
                Intent intent = new Intent(getApplicationContext(),
                        ListaActivityArticulo.class);
                startActivityForResult(intent, HELP_ARTICULO);

            }
        });
        txtca_item.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Double nCONV_CANT;
                if (hasFocus == false) {
                    if (txtca_item.getText().toString().length() > 0) {

                        nCONV_CANT = Redondear(
                                Double.parseDouble(txtca_item.getText()
                                        .toString())
                                        / Double.parseDouble(txtempaque
                                        .getText().toString()), 2);

                        double num = nCONV_CANT;
                        int p_ent = (int) num;

                        double p_dec = num - p_ent;

                        if (p_dec != 0) {
                            txtca_item
                                    .setError("Cantidad debe ser multipo de la conversión");
                            txtca_item.setText("");
                            txtca_conv.setText("");
                        } else {
                            txtca_conv.setText(nCONV_CANT.toString());
                        }
                    }
                }
            }
        });

        txtim_dscto_detalle
                .setOnFocusChangeListener(new OnFocusChangeListener() {
                    GlobalApp app = (GlobalApp) getApplicationContext();

                    @Override
                    public void onFocusChange(View arg0, boolean arg1) {
                        // TODO Auto-generated method stub
                        if (arg1 == false) {
                            if (Double.parseDouble(txtim_dscto_detalle
                                    .getText().toString()) != 0) {

                                if (Double.parseDouble(txtim_dscto_detalle
                                        .getText().toString()) > Double
                                        .parseDouble(app.pc_dcto_marti)) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                                            Tab2Activity.this);

                                    dialog.setMessage("Maximo procentaje de descuento por articulo del usuario es "
                                            + app.pc_dcto_marti.toString()
                                            + "%");
                                    dialog.setCancelable(false);
                                    dialog.setTitle("Aviso");
                                    dialog.setIcon(android.R.drawable.stat_notify_error);

                                    dialog.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    txtim_dscto_detalle
                                                            .setText("0.00");
                                                    dialog.cancel();
                                                }
                                            });
                                    dialog.show();
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            Tab2Activity.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("Validar Usuario");
                                    // Get the layout inflater
                                    LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                                    // LayoutInflater inflater =
                                    // Tab1Activity.this
                                    // .getLayoutInflater();
                                    final View layout = inflater.inflate(
                                            R.layout.validausuario, null);
                                    final EditText txtUsuario1 = ((EditText) layout
                                            .findViewById(R.id.txtUsuario1));
                                    final EditText txtPassword1 = ((EditText) layout
                                            .findViewById(R.id.txtPassword1));
                                    // Inflate and set the layout for the dialog
                                    // Pass null as the parent view because its
                                    // going in
                                    // the dialog layout
                                    builder.setView(layout);

                                    // Add action buttons
                                    builder.setPositiveButton(
                                            android.R.string.yes,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    app.de_plaz = txtUsuario1
                                                            .getText()
                                                            .toString();
                                                    app.pa_usua = txtPassword1
                                                            .getText()
                                                            .toString();
                                                    ValidaDescuentoArticulo();
                                                    // if
                                                    // (app.menuModificarDetalle
                                                    // == true) {
                                                    // ModificarArticuloAsyn
                                                    // tarea = new
                                                    // ModificarArticuloAsyn();
                                                    // tarea.execute();
                                                    // }
                                                    // if (app.menuGrabarDetalle
                                                    // == true) {
                                                    // GrabarArticuloAsyn tarea
                                                    // = new
                                                    // GrabarArticuloAsyn();
                                                    // tarea.execute();
                                                    // }

                                                }
                                            });
                                    builder.setNegativeButton(
                                            android.R.string.no,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    txtim_dscto_detalle
                                                            .setText("0.00");
                                                    dialog.cancel();

                                                }
                                            });
                                    builder.show();

                                }

                            }
                        }
                    }
                });
        txtca_conv.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Double nCONV_CANT;
                if (hasFocus == false) {
                    if (txtca_conv.getText().toString().length() > 0) {
                        if (txtempaque.getText().toString().length() > 0) {
                            if (Double.parseDouble(txtca_conv.getText()
                                    .toString())
                                    * Double.parseDouble(txtempaque.getText()
                                    .toString()) > 0) {
                                nCONV_CANT = Redondear(
                                        Double.parseDouble(txtca_conv.getText()
                                                .toString())
                                                * Double.parseDouble(txtempaque
                                                .getText().toString()),
                                        2);
                                txtca_item.setText(nCONV_CANT.toString());
                            }
                        } else {
                            nCONV_CANT = Redondear(Double
                                    .parseDouble(txtca_conv.getText()
                                            .toString()) * 1, 2);
                            txtca_item.setText(nCONV_CANT.toString());
                        }

                    }
                }
            }
        });

    }

    public void ValidaDescuentoArticulo() {

        ValidaDescuentoArtAsyn tarea1 = new ValidaDescuentoArtAsyn();
        tarea1.execute();
    }

    public void PintarTab() {
        Container ta = (Container) this.getParent();
        TabHost th = ta.getTabHost();
        for (int i = 0; i < th.getTabWidget().getChildCount(); i++) {
            th.getTabWidget().getChildAt(i)
                    .setBackgroundColor(Color.parseColor("#3300CC")); // unselected
        }
        th.getTabWidget().getChildAt(th.getCurrentTab())
                .setBackgroundColor(Color.parseColor("#FF9900")); // selected

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (variables_publicas.tab1 == true) {
            LimpiarDetalle();
            variables_publicas.tab1 = false;
        }
        if (txtnu_coti_deta.getText().toString().length() > 0) {
            txtnu_coti_deta.setText(variables_publicas.nu_coti_fina);
        }

    }

    public void ValidaArticulo() {
        ValidaArticuloAsyn tarea = new ValidaArticuloAsyn();
        tarea.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (HELP_ARTICULO == requestCode) {
            if (RESULT_OK == resultCode) {
                txtca_item = (EditText) findViewById(R.id.txtca_item);
                txtco_item = (EditText) findViewById(R.id.txtco_item);
                txtco_item.setText(data.getStringExtra("CO_ITEM"));
                // if (txtco_item.getText().toString().length() > 0) {
                // ValidaArticulo();

                // }
                txtca_item.requestFocus();
            }
        } else if (HELP_COTIZACION == requestCode) {
            if (RESULT_OK == resultCode) {
                txtca_item = (EditText) findViewById(R.id.txtca_item);
                txtco_item = (EditText) findViewById(R.id.txtco_item);
                txtco_item.setText(data.getStringExtra("CO_ITEM"));
                // if (txtco_item.getText().toString().length() > 0) {
                // ValidaArticulo();
                // }
                txtca_item.requestFocus();
            }
        } else if (HELP_MASTER == requestCode) {
            if (RESULT_OK == resultCode) {
                String co_mast;
                String de_mast;
                String pr_mast;
                co_mast = data.getStringExtra("CO_MAST");
                de_mast = data.getStringExtra("DE_MAST");
                pr_mast = data.getStringExtra("PR_MAST");
                if (co_mast.toString().length() > 0) {
                    Intent intent = new Intent(this, MasterActivity.class);
                    intent.putExtra("NU_COTI", txtnu_coti_deta.getText()
                            .toString());
                    intent.putExtra("CO_MAST", co_mast.toString());
                    intent.putExtra("DE_MAST", de_mast.toString());
                    intent.putExtra("PR_MAST", pr_mast.toString());
                    startActivity(intent);
                }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detallemenu, menu);

        GlobalApp app = (GlobalApp) getApplicationContext();

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        GlobalApp app = (GlobalApp) getApplicationContext();
        MenuItem modificadetalle = menu.findItem(R.id.mnuModificarDetalle);
        modificadetalle.setVisible(app.menuModificarDetalle);
        MenuItem agregardatos = menu.findItem(R.id.mnuGrabarDetalle);
        agregardatos.setVisible(app.menuGrabarDetalle);
        MenuItem eliminardatos = menu.findItem(R.id.mnuEliminarDetalle);
        eliminardatos.setVisible(app.menuEliminarDetalle);
        MenuItem limpiardatos = menu.findItem(R.id.mnuLimpiarDetalle);
        limpiardatos.setVisible(app.menuLimpiarDetalle);
        MenuItem maestrodatos = menu.findItem(R.id.mnuMasterDetalle);
        maestrodatos.setVisible(app.menuMasterDetalle);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalApp app = (GlobalApp) getApplicationContext();
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuGrabarDetalle: {

                if (txtco_item.getText().toString().length() == 0) {
                    txtco_item.setError("Debe ingresar el Articulo");
                } else if (txtca_item.getText().toString().length() == 0) {
                    txtca_item.setError("Debe ingresar Cantidad del Articulo");
                } else if (txtca_conv.getText().toString().length() == 0) {
                    txtca_conv.setError("Debe ingresar la conversión");
                } else if (txtim_dscto_detalle.getText().toString().length() == 0) {
                    txtca_conv.setError("Debe ingresar el Descuento");
                } else if (app.st_bloq.equals("BLO")
                        && (Double.parseDouble(txtca_actu.getText().toString()) <= 0)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("El articulo esta bloqueado y no tiene stock");
                    dialog.setCancelable(false);
                    dialog.setTitle("Aviso");
                    dialog.setIcon(android.R.drawable.stat_notify_error);

                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                    LimpiarDetalle();
                } else if (app.st_docu.equals("CON")) {
                    txtco_item
                            .setError("Ya no se puede modificar porque la cotización esta convertida ");
                    txtco_item.setText("");

                    LimpiarDetalle();
                } else {

                    GrabarArticuloAsyn tarea = new GrabarArticuloAsyn();

                    tarea.execute();

                }

                return true;
            }

            case R.id.mnuLimpiarDetalle: {
                LimpiarDetalle();

                return true;

            }
            case R.id.mnuModificarDetalle: {
                if (txtco_item.getText().toString().length() == 0) {
                    txtco_item.setError("Debe ingresar el Articulo");
                } else if (txtca_item.getText().toString().length() == 0) {
                    txtca_item.setError("Debe ingresar Cantidad del Articulo");
                } else if (txtca_conv.getText().toString().length() == 0) {
                    txtca_conv.setError("Debe ingresar la conversión");
                } else if (txtim_dscto_detalle.getText().toString().length() == 0) {
                    txtca_conv.setError("Debe ingresar el Descuento");
                } else if (app.st_bloq.equals("BLO")
                        && (Double.parseDouble(txtca_actu.getText().toString()) <= 0)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("El articulo esta bloqueado y no tiene stock");
                    dialog.setCancelable(false);
                    dialog.setTitle("Aviso");
                    dialog.setIcon(android.R.drawable.stat_notify_error);

                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                    LimpiarDetalle();
                } else if (app.st_docu.equals("CON")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("Ya no se puede modificar porque la cotización esta convertida");
                    dialog.setCancelable(false);
                    dialog.setTitle("Aviso");
                    dialog.setIcon(android.R.drawable.stat_notify_error);

                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();

                    LimpiarDetalle();

                } else {
                    ModificarArticuloAsyn tarea = new ModificarArticuloAsyn();

                    tarea.execute();

                }

                return true;
            }
            case R.id.mnuEliminarDetalle: {

                if (txtco_item.getText().toString().length() == 0) {
                    txtco_item.setError("Debe ingresar el Articulo");
                } else if (txtca_item.getText().toString().length() == 0) {
                    txtca_item.setError("Debe ingresar Cantidad del Articulo");
                } else if (txtim_dscto_detalle.getText().toString().length() == 0) {
                    txtca_conv.setError("Debe ingresar el Descuento");
                } else if (app.st_docu.equals("CON")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("Ya no se puede modificar porque la cotización esta convertida");
                    dialog.setCancelable(false);
                    dialog.setTitle("Aviso");
                    dialog.setIcon(android.R.drawable.stat_notify_error);

                    dialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();

                    LimpiarDetalle();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("¿Desea Eliminar?");
                    dialog.setCancelable(false);
                    dialog.setTitle("Aviso");
                    dialog.setIcon(android.R.drawable.stat_notify_error);

                    dialog.setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    EliminarArtAsyn tarea = new EliminarArtAsyn();
                                    tarea.execute();
                                }
                            });
                    dialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    dialog.show();
                }

                return true;
            }

            case R.id.mnuListaDetalle: {
                LimpiarDetalle();
                Intent intent = new Intent(getApplicationContext(),
                        ListaActivityCotizacion.class);
                // startActivity(intent1);
                startActivityForResult(intent, HELP_COTIZACION);

                return true;
            }
            case R.id.mnuMasterDetalle: {
                LimpiarDetalle();
                Intent intent = new Intent(getApplicationContext(),
                        ListaActivityMaster.class);
                // startActivity(intent1);
                startActivityForResult(intent, HELP_MASTER);

                return true;
            }

            default:
                return false;

        }

    }

    public void LimpiarDetalle() {
        GlobalApp app = (GlobalApp) getApplicationContext();
        txtco_item.setText("");
        txtde_item_larg.setText("");
        txtco_unme.setText("");
        txtde_unme.setText("");
        txtco_alma_detalle.setText("");
        txtde_alma_detalle.setText("");
        txtca_item.setText("");
        txtpr_vent.setText("");
        txtim_dscto_detalle.setText("");
        txtca_conv.setText("");
        txtempaque.setText("");
        txtca_actu.setText("");
        txtde_obse_detalle.setText("");
        txtst_bloq.setText("");
        txtco_item.requestFocus();
        app.co_item = "";
        app.de_item_larg = "";
        app.co_unme = "";
        app.de_unme = "";
        app.ca_item = "";
        app.de_obse_detalle = "";
        app.st_bloq = "";
        app.menuGrabarDetalle = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("¿Estas seguro?")
                    .setNegativeButton(android.R.string.cancel, null)
                    // sin listener
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {// un
                                // listener
                                // que al
                                // pulsar,
                                // cierre la
                                // aplicacion
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // Salir
                                    Tab2Activity.this.finish();
                                }
                            }).show();
            // Si el listener devuelve true, significa que el evento esta
            // procesado, y nadie debe hacer nada mas
            return true;
        }
        // para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);

    }

    public void ModificaCotizacion() {
        GlobalApp app = (GlobalApp) getApplicationContext();
        String resultado = "";
        final String NAMESPACE1 = "http://app.cassinelli.com/";
        final String URL1 = variables_publicas.direccionIp + "/Service.asmx";
        final String METHOD_NAME1 = "ModificarCotizacion";
        final String SOAP_ACTION1 = "http://app.cassinelli.com/ModificarCotizacion";

        SoapObject request1 = new SoapObject(NAMESPACE1, METHOD_NAME1);

        request1.addProperty("sCO_EMPR", variables_publicas.co_empr.toString());
        request1.addProperty("sCO_UNID", variables_publicas.co_unid.toString());
        request1.addProperty("sCO_TIEN", variables_publicas.co_tien.toString());
        request1.addProperty("sCO_ALMA",
                variables_publicas.co_alma_vent.toString());
        request1.addProperty("sCO_VEND", variables_publicas.co_vend.toString());
        request1.addProperty("sCO_COND_VENT", app.co_cond_vent.toString());
        request1.addProperty("sCO_CLIE", app.co_clie.toString());
        request1.addProperty("sNU_COTI", txtnu_coti_deta.getText().toString());
        request1.addProperty("nPC_DCTO_REFE", app.pc_dcto_cabe.toString());
        request1.addProperty("sDE_OBSE", app.de_obse.toString());
        request1.addProperty("sDE_PLAZ", app.de_plaz.toString());
        request1.addProperty("sCO_USUA", variables_publicas.co_usua.toString());
        request1.addProperty("sCO_ESTA_DOCU", app.st_docu.toString());
        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope1.dotNet = true;

        envelope1.setOutputSoapObject(request1);

        HttpTransportSE transporte1 = new HttpTransportSE(URL1);

        try {
            transporte1.call(SOAP_ACTION1, envelope1);
            SoapPrimitive resultado_xml = (SoapPrimitive) envelope1
                    .getResponse();
            resultado = resultado_xml.toString();

        } catch (Exception e) {

        }
    }

    private class ValidaDescuentoArtAsyn extends
            AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progressDialog = new ProgressDialog(Tab1Activity.this);
            // progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            // progressDialog.setMessage("Procesando...");
            // progressDialog.setProgress(0);

            // progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressDialog.dismiss();
            GlobalApp app = (GlobalApp) getApplicationContext();

            if (result.equals("N")) {

                txtim_dscto_detalle.setText("0.00");

            } else {
                app.pc_dcto_detalle = txtim_dscto_detalle.getText().toString();
                app.st_docu = "ACE";

            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {
            GlobalApp app = (GlobalApp) getApplicationContext();
            String res = "N";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "UsuarioDescuento";
            final String SOAP_ACTION = "http://app.cassinelli.com/UsuarioDescuento";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_USUA", app.de_plaz.toString());
            request.addProperty("sCLAVE", app.pa_usua.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return res;
        }

    }

    private class ValidaArticuloAsyn extends
            AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();
        String res = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.co_item = txtco_item.getText().toString();
            progressDialog = new ProgressDialog(Tab2Activity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.equals("KO")) {
                txtco_item.setText("");
                txtco_item.requestFocus();
                Toast.makeText(getBaseContext(), "Codigo no existe",
                        Toast.LENGTH_LONG).show();
            } else {

                DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                simbolos.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#########.00", simbolos);
                DecimalFormat df4 = new DecimalFormat("#########.0000", simbolos);
                txtco_item.setText(app.co_item);
                txtco_unme.setText(app.co_unme);
                // txtempaque.setText(app.nu_empa);
                Double empaque = Double.parseDouble(app.nu_empa);

                if (empaque == 0) {
                    txtempaque.setText("1");
                    app.nu_empa = "1";
                } else {
                    txtempaque.setText(app.nu_empa);
                }

                txtpr_vent.setText(df4.format(Double.parseDouble(app.pr_cigv)));
                txtco_alma_detalle.setText(app.co_alma_detalle);
                txtca_actu.setText(df.format(Double.parseDouble(app.ca_actu)));
                txtde_item_larg.setText(app.de_item_larg);
                txtim_dscto_detalle.setText(app.pc_dcto_detalle);
                txtde_alma_detalle.setText(app.de_alma);
                txtde_unme.setText(app.co_unme);
                txtca_item.setText(app.ca_item);
                txtde_obse_detalle.setText(app.de_obse_detalle);
                txtst_bloq.setText(app.st_bloq);
                txtca_item.requestFocus();

            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected String doInBackground(Integer... params) {

            final String NAMESPACE1 = "http://app.cassinelli.com/";
            final String URL1 = variables_publicas.direccionIp
                    + "/Service.asmx";
            final String METHOD_NAME1 = "ValidaArticuloCoti";
            final String SOAP_ACTION1 = "http://app.cassinelli.com/ValidaArticuloCoti";

            SoapObject request1 = new SoapObject(NAMESPACE1, METHOD_NAME1);

            request1.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request1.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request1.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request1.addProperty("sNU_COTI",
                    variables_publicas.nu_coti_fina.toString());
            request1.addProperty("sCO_ITEM", app.co_item.toString());

            SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope1.dotNet = true;
            envelope1.setOutputSoapObject(request1);

            HttpTransportSE transporte1 = new HttpTransportSE(URL1);
            try {

                transporte1.call(SOAP_ACTION1, envelope1);
                SoapObject resSoap1 = (SoapObject) envelope1.getResponse();
                Articulo[] listaArticulo1 = new Articulo[resSoap1
                        .getPropertyCount()];
                if (listaArticulo1.length > 0) {
                    for (int i = 0; i < listaArticulo1.length; i++) {
                        SoapObject ic1 = (SoapObject) resSoap1.getProperty(i);

                        app.co_item = ic1.getProperty(0).toString();
                        app.nu_corr = ic1.getProperty(1).toString();
                        app.de_item_larg = ic1.getProperty(2).toString();
                        app.co_unme = ic1.getProperty(3).toString();
                        app.de_unme = ic1.getProperty(4).toString();
                        app.nu_empa = ic1.getProperty(5).toString();
                        if (!ic1.getProperty(6).toString().equals("anyType{}")) {
                            app.st_bloq = ic1.getProperty(6).toString();
                        } else {
                            app.st_bloq = "";
                        }
                        app.co_alma_detalle = ic1.getProperty(8).toString();
                        app.de_alma = ic1.getProperty(9).toString();
                        app.ca_actu = ic1.getProperty(10).toString();
                        app.pc_dcto_detalle = ic1.getProperty(11).toString();
                        app.pr_cigv = ic1.getProperty(12).toString();
                        app.ca_item = ic1.getProperty(16).toString();
                        if (!ic1.getProperty(17).toString().equals("anyType{}")) {
                            app.de_obse_detalle = ic1.getProperty(17)
                                    .toString();
                        } else {
                            app.de_obse_detalle = "";
                        }

                    } // for
                    res = "OK";
                    app.menuModificarDetalle = true;
                    app.menuEliminarDetalle = true;
                    app.menuGrabarDetalle = false;

                } else {
                    final String NAMESPACE = "http://app.cassinelli.com/";
                    final String URL = variables_publicas.direccionIp
                            + "/Service.asmx";
                    final String METHOD_NAME = "ValidaArticulo";
                    final String SOAP_ACTION = "http://app.cassinelli.com/ValidaArticulo";

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("sCO_EMPR",
                            variables_publicas.co_empr.toString());
                    request.addProperty("sCO_UNID",
                            variables_publicas.co_unid.toString());
                    request.addProperty("sCO_TIEN",
                            variables_publicas.co_tien.toString());

                    request.addProperty("sCO_ITEM", app.co_item
                            .toString());
                    request.addProperty("sCO_ALMA_VENT",
                            variables_publicas.co_alma_vent.toString());
                    request.addProperty("sCO_ALMA_CONS",
                            variables_publicas.co_alma_cons.toString());

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE transporte = new HttpTransportSE(URL);

                    try {
                        transporte.call(SOAP_ACTION, envelope);
                        SoapObject resSoap = (SoapObject) envelope
                                .getResponse();
                        Articulo[] listaArticulo = new Articulo[resSoap
                                .getPropertyCount()];
                        if (listaArticulo.length > 0) {
                            for (int i = 0; i < listaArticulo.length; i++) {
                                SoapObject ic = (SoapObject) resSoap
                                        .getProperty(i);

                                app.co_item = ic.getProperty(0).toString();
                                app.de_item_larg = ic.getProperty(1).toString();
                                app.co_unme = ic.getProperty(2).toString();
                                app.de_unme = ic.getProperty(3).toString();
                                app.nu_empa = ic.getProperty(4).toString();
                                app.pr_cigv = ic.getProperty(12).toString();
                                app.co_alma_detalle = ic.getProperty(7)
                                        .toString();
                                app.de_alma = ic.getProperty(8).toString();
                                app.ca_actu = ic.getProperty(9).toString();
                                app.pc_dcto_detalle = ic.getProperty(10)
                                        .toString();
                                if (!ic.getProperty(5).toString()
                                        .equals("anyType{}")) {
                                    app.st_bloq = ic.getProperty(5).toString();
                                } else {
                                    app.st_bloq = "";
                                }

                            }
                            res = "OK";
                            app.menuModificarDetalle = false;
                            app.menuEliminarDetalle = false;
                            app.menuGrabarDetalle = true;

                        } else {
                            res = "KO";

                        }

                    } catch (Exception e) {
                        // Toast.makeText(getBaseContext(), e.getMessage(),
                        // Toast.LENGTH_LONG)
                        // .show();
                    }

                }

            } catch (Exception e) {
                // TODO: handle exception
            }

            return res;
        }
    }

    private class GrabarArticuloAsyn extends
            AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.nu_coti = txtnu_coti_deta.getText().toString();
            app.co_item = txtco_item.getText().toString();
            app.co_unme = txtco_unme.getText().toString();
            app.co_alma_detalle = txtco_alma_detalle.getText().toString();
            app.ca_item = txtca_item.getText().toString();
            app.pr_vent = txtpr_vent.getText().toString();
            app.pc_dcto_detalle = txtim_dscto_detalle.getText().toString();
            app.de_obse_detalle = txtde_obse_detalle.getText().toString();

            progressDialog = new ProgressDialog(Tab2Activity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            GlobalApp app = (GlobalApp) getApplicationContext();
            if (result.toString().length() > 0) {
                if (result.toString().equals("OK")) {
                    app.menuGrabarDetalle = true;
                    app.menuModificarDetalle = false;
                    app.menuLimpiarDetalle = true;

                    Toast.makeText(getBaseContext(),
                            "Se grabo satisfactoriamente", Toast.LENGTH_LONG)
                            .show();
                    //if (app.st_docu.toString().equals("ACE")) {
                    //ModificaCotizacion();
                    //}
                    LimpiarDetalle();

                } else {
                    Toast.makeText(getBaseContext(), result.toString(),
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getBaseContext(), "No se grabo Verificar!",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {

            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "GrabarArticulo";
            final String SOAP_ACTION = "http://app.cassinelli.com/GrabarArticulo";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti
                    .toString());
            request.addProperty("pContador", "0");
            request.addProperty("sCO_ITEM", app.co_item.toString());
            request.addProperty("sCO_UNME", app.co_unme.toString());
            request.addProperty("sCO_ALMA_DETA", app.co_alma_detalle
                    .toString());
            request.addProperty("nCA_DOCU", app.ca_item.toString());
            request.addProperty("nPR_VENT", app.pr_vent.toString());
            request.addProperty("nPC_DCTO_0001", app.pc_dcto_cabe.toString());
            request.addProperty("nPC_DCTO_ITEM", app.pc_dcto_detalle
                    .toString());
            request.addProperty("sDE_OBSE_DETA", app.de_obse_detalle
                    .toString());
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();

            } catch (Exception e) {

            }
            if (res.toString().equals("OK")) {

                if (app.st_docu.toString().equals("ACE")) {


                    final String NAMESPACE1 = "http://app.cassinelli.com/";
                    final String URL1 = variables_publicas.direccionIp
                            + "/Service.asmx";
                    final String METHOD_NAME1 = "ModificarCotizacion";
                    final String SOAP_ACTION1 = "http://app.cassinelli.com/ModificarCotizacion";

                    SoapObject request1 = new SoapObject(NAMESPACE1,
                            METHOD_NAME1);

                    request1.addProperty("sCO_EMPR",
                            variables_publicas.co_empr.toString());
                    request1.addProperty("sCO_UNID",
                            variables_publicas.co_unid.toString());
                    request1.addProperty("sCO_TIEN",
                            variables_publicas.co_tien.toString());
                    request1.addProperty("sCO_ALMA",
                            variables_publicas.co_alma_vent.toString());
                    request1.addProperty("sCO_VEND",
                            variables_publicas.co_vend.toString());
                    request1.addProperty("sCO_COND_VENT",
                            app.co_cond_vent.toString());
                    request1.addProperty("sCO_CLIE", app.co_clie.toString());
                    request1.addProperty("sNU_COTI", app.nu_coti
                            .toString());
                    request1.addProperty("nPC_DCTO_REFE",
                            app.pc_dcto_cabe.toString());
                    request1.addProperty("sDE_OBSE", app.de_obse.toString());
                    request1.addProperty("sDE_PLAZ", app.de_plaz.toString());
                    request1.addProperty("sCO_USUA",
                            variables_publicas.co_usua.toString());
                    request1.addProperty("sCO_ESTA_DOCU", app.st_docu.toString());
                    SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    envelope1.dotNet = true;

                    envelope1.setOutputSoapObject(request1);

                    HttpTransportSE transporte1 = new HttpTransportSE(URL1);

                    try {
                        transporte1.call(SOAP_ACTION1, envelope1);
                        SoapPrimitive resultado_xml = (SoapPrimitive) envelope1
                                .getResponse();
                        res = resultado_xml.toString();

                    } catch (Exception e) {

                    }
                }
            }
            return res;
        }

    }

    private class ModificarArticuloAsyn extends
            AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.nu_coti = txtnu_coti_deta.getText()
                    .toString();
            app.co_item = txtco_item.getText().toString();
            app.ca_item = txtca_item.getText().toString();
            app.co_unme = txtco_unme.getText().toString();
            app.co_alma_detalle = txtco_alma_detalle.getText()
                    .toString();
            app.pr_cigv = txtpr_vent.getText()
                    .toString();
            //app.pc_dcto_cabe=txt);
            app.pc_dcto_detalle = txtim_dscto_detalle.getText()
                    .toString();
            app.de_obse_detalle = txtde_obse_detalle.getText()
                    .toString();

            progressDialog = new ProgressDialog(Tab2Activity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result.toString().length() > 0) {
                if (result.toString().equals("OK")) {
                    app.menuGrabarDetalle = true;
                    app.menuModificarDetalle = false;
                    app.menuLimpiarDetalle = true;

                    Toast.makeText(getBaseContext(),
                            "Se Modifico satisfactoriamente", Toast.LENGTH_LONG)
                            .show();
                    //if (app.st_docu.toString().equals("ACE")) {
                    //ModificaCotizacion();
                    //}
                    LimpiarDetalle();

                } else {
                    Toast.makeText(getBaseContext(), result.toString(),
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getBaseContext(), "No se modifico Verificar!",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {

            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ModificarArticulos";
            final String SOAP_ACTION = "http://app.cassinelli.com/ModificarArticulos";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti
                    .toString());
            request.addProperty("sCO_ITEM", app.co_item.toString());
            request.addProperty("nCA_DOCU", app.ca_item.toString());
            request.addProperty("sCO_UNME", app.co_unme.toString());
            request.addProperty("sCO_ALMA", app.co_alma_detalle
                    .toString());
            request.addProperty("nPR_VENT_CIGV", app.pr_cigv
                    .toString());
            request.addProperty("nPC_DCTO_0001", app.pc_dcto_cabe
                    .toString());
            request.addProperty("nPC_DCTO_DETA", app.pc_dcto_detalle
                    .toString());
            request.addProperty("nNU_SECU", app.nu_corr.toString());

            request.addProperty("sDE_OBSE_DETA", app.de_obse_detalle
                    .toString());
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString().toUpperCase());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();

            } catch (Exception e) {

            }
            if (res.toString().equals("OK")) {
                GlobalApp app = (GlobalApp) getApplicationContext();
                if (app.st_docu.toString().equals("ACE")) {


                    final String NAMESPACE1 = "http://app.cassinelli.com/";
                    final String URL1 = variables_publicas.direccionIp
                            + "/Service.asmx";
                    final String METHOD_NAME1 = "ModificarCotizacion";
                    final String SOAP_ACTION1 = "http://app.cassinelli.com/ModificarCotizacion";

                    SoapObject request1 = new SoapObject(NAMESPACE1,
                            METHOD_NAME1);

                    request1.addProperty("sCO_EMPR",
                            variables_publicas.co_empr.toString());
                    request1.addProperty("sCO_UNID",
                            variables_publicas.co_unid.toString());
                    request1.addProperty("sCO_TIEN",
                            variables_publicas.co_tien.toString());
                    request1.addProperty("sCO_ALMA",
                            variables_publicas.co_alma_vent.toString());
                    request1.addProperty("sCO_VEND",
                            variables_publicas.co_vend.toString());
                    request1.addProperty("sCO_COND_VENT",
                            app.co_cond_vent.toString());
                    request1.addProperty("sCO_CLIE", app.co_clie.toString());
                    request1.addProperty("sNU_COTI", app.nu_coti
                            .toString());
                    request1.addProperty("nPC_DCTO_REFE",
                            app.pc_dcto_cabe.toString());
                    request1.addProperty("sDE_OBSE", app.de_obse.toString());

                    request1.addProperty("sDE_PLAZ", app.de_plaz.toString());
                    request1.addProperty("sCO_USUA",
                            variables_publicas.co_usua.toString());
                    request1.addProperty("sCO_ESTA_DOCU", app.st_docu.toString());
                    SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    envelope1.dotNet = true;

                    envelope1.setOutputSoapObject(request1);

                    HttpTransportSE transporte1 = new HttpTransportSE(URL1);

                    try {
                        transporte1.call(SOAP_ACTION1, envelope1);
                        SoapPrimitive resultado_xml = (SoapPrimitive) envelope1
                                .getResponse();
                        res = resultado_xml.toString();

                    } catch (Exception e) {

                    }
                }
            }
            return res;
        }

    }

    private class EliminarArtAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.co_item = txtco_item.getText().toString();
            progressDialog = new ProgressDialog(Tab2Activity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.toString().equals("OK")) {

                // CabeceraTotales cabtot = new CabeceraTotales();
                // cabtot.Totales_cabecera();

                // txttot_dolar.setText(variables_publicas.tot_dolar.toString());
                // txttot_soles.setText(variables_publicas.tot_soles.toString());
                LimpiarDetalle();
                Toast.makeText(getBaseContext(),
                        "Se Elimino satisfactoriamente", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getBaseContext(), "No se Elimino Verificar!",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {
            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "EliminarArticulo";
            final String SOAP_ACTION = "http://app.cassinelli.com/EliminarArticulo";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI",
                    variables_publicas.nu_coti_fina.toString());
            request.addProperty("sCO_ITEM", app.co_item.toString());
            request.addProperty("nNU_SECU", app.nu_corr.toString());
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();


            } catch (Exception e) {

            }
            return res;
        }

    }

    private class ModificarCotiAsyn extends AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.nu_coti = txtnu_coti_deta.getText().toString();
            // progressDialog = new ProgressDialog(Tab2Activity.this);
            // progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            // progressDialog.setMessage("Procesando...");
            // progressDialog.setProgress(0);
            // progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressDialog.dismiss();
            if (result.toString().equals("OK")) {
                //GlobalApp app = (GlobalApp) getApplicationContext();

                Toast.makeText(getBaseContext(),
                        "Se Modifico satisfactoriamente", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getBaseContext(), "No se modifico Verificar!",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void finalize() throws Throwable {
            // TODO Auto-generated method stub
            super.finalize();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            // progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Integer... params) {
            //GlobalApp app = (GlobalApp) getApplicationContext();
            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ModificarCotizacion";
            final String SOAP_ACTION = "http://app.cassinelli.com/ModificarCotizacion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sCO_ALMA",
                    variables_publicas.co_alma_vent.toString());
            request.addProperty("sCO_VEND",
                    variables_publicas.co_vend.toString());
            request.addProperty("sCO_COND_VENT", app.co_cond_vent.toString());
            request.addProperty("sCO_CLIE", app.co_clie.toString());
            request.addProperty("sNU_COTI", app.nu_coti
                    .toString());
            request.addProperty("nPC_DCTO_REFE", app.pc_dcto_cabe.toString());
            request.addProperty("sDE_OBSE", app.de_obse.toString());
            request.addProperty("sCO_ESTA", app.st_docu.toString());
            request.addProperty("sDE_PLAZ", app.de_plaz.toString());
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope
                        .getResponse();
                res = resultado_xml.toString();

            } catch (Exception e) {

            }
            return res;
        }

    }

}