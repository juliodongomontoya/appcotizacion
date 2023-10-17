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
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Tab1Activity extends AppCompatActivity {
    public static final int HELP_CONDICION = 1;
    public static final int HELP_CLIENTE = 2;
    public static final int HELP_CLIENTE_VALIDATE = 3;
    public static final int HELP_COTIZACION_BUSQ = 4;
    ProgressDialog progressDialog;
    EditText txtnu_coti;
    EditText txtco_cond_vent;
    EditText txtde_cond_vent;
    EditText txtpc_dcto_cabe;
    EditText txtco_alma;
    EditText txtde_obse_cabe;
    TextView txtco_esta_docu;
    ImageButton btn_buscar_cond;
    ImageButton btn_buscar_cliente;
    ImageButton btnBuscarCoti;
    EditText txtco_clie;
    EditText txtno_clie;
    TextView txtco_esta_cliente;
    EditText txtUsuario1;
    EditText txtPassword1;

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (HELP_CONDICION == requestCode) {
            if (RESULT_OK == resultCode) {
                txtco_cond_vent.setText(data.getStringExtra("CONDICION"));
                // if (txtco_cond_vent.toString().length() > 0) {
                // ValidaCondicion();
                // }
                txtpc_dcto_cabe.requestFocus();
            }
        } else if (HELP_CLIENTE == requestCode) {
            if (RESULT_OK == resultCode) {
                GlobalApp app = (GlobalApp) getApplicationContext();
                txtco_clie.setText(data.getStringExtra("CO_CLIE"));
                // if (txtco_clie.toString().length() > 0) {
                // if (app.menuModificarDatos == false) {
                // ValidaCliente();
                // }

                // }

                txtco_cond_vent.requestFocus();
            }
        } else if (HELP_CLIENTE_VALIDATE == requestCode) {
            if (RESULT_OK == resultCode) {

                txtno_clie.setText(data.getStringExtra("NO_CLIE"));

                txtco_cond_vent.requestFocus();
            }
        } else if (HELP_COTIZACION_BUSQ == requestCode) {
            if (RESULT_OK == resultCode) {

                txtnu_coti.setText(data.getStringExtra("NU_COTI"));
                // if (txtnu_coti.toString().length() > 0) {
                // ValidaCabecera();
                // }
                txtco_clie.requestFocus();

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalApp app = (GlobalApp) getApplicationContext();
        variables_publicas.tab1 = true;
        txtco_esta_docu.setText(app.st_docu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.datosmenu, menu);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        GlobalApp app = (GlobalApp) getApplicationContext();
        MenuItem modificadatos = menu.findItem(R.id.mnuModificarDatos);
        modificadatos.setVisible(app.menuModificarDatos);
        MenuItem agregardatos = menu.findItem(R.id.mnuGrabarDatos);
        agregardatos.setVisible(app.menuGrabarDatos);
        MenuItem limpiardatos = menu.findItem(R.id.mnuLimpiarDatos);
        limpiardatos.setVisible(app.menuLimpiarDatos);
        MenuItem aplicardscto = menu.findItem(R.id.mnuAplicaDscto);
        aplicardscto.setVisible(app.menuAplicarDescto);

        return super.onPrepareOptionsMenu(menu);
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

    public void CambiodeTab() {
        Container ta = (Container) this.getParent();
        TabHost th = ta.getTabHost();
        th.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
        th.setCurrentTab(1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalApp app = (GlobalApp) getApplicationContext();
        int id = item.getItemId();
        switch (id) {
            case R.id.mnuSalir: {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setMessage("¿Desea Salir de la Aplicación?");
                dialog.setCancelable(false);
                dialog.setTitle("Aviso");
                dialog.setIcon(android.R.drawable.stat_notify_error);

                dialog.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                dialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();

                return true;
            }
            case R.id.mnuGrabarDatos: {

                if (txtco_clie.getText().toString().length() == 0) {
                    txtco_clie.setError("Debe ingresar Cliente");
                } else if (txtco_cond_vent.getText().toString().length() == 0) {
                    txtco_cond_vent.setError("Debe ingresar Condición de Venta");
                } else if (txtpc_dcto_cabe.getText().toString().length() == 0) {
                    txtpc_dcto_cabe.setError("Debe ingresar el Descuento");
                } else if (Double.parseDouble(txtpc_dcto_cabe.getText().toString()) > 100) {
                    txtpc_dcto_cabe
                            .setError("El Descuento no puede ser mayor 100%");
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

                    LimpiarDatos();
                } else if (app.st_docu.equals("CON")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("Ya no se puede modificar porque la cotización esta anulada");
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

                    LimpiarDatos();
                } else {

                    GrabarCotiAsyn tarea = new GrabarCotiAsyn();

                    tarea.execute();

                }

                return true;
            }

            case R.id.mnuLimpiarDatos: {
                LimpiarDatos();
                return true;

            }
            case R.id.mnuAplicaDscto: {
                if (txtnu_coti.getText().toString().length() == 0) {
                    txtnu_coti.setError("Debe ingresar una cotización");
                } else {
                    AplicarDsctoCotiAsyn tarea1 = new AplicarDsctoCotiAsyn();

                    tarea1.execute();

                }

                return true;

            }
            case R.id.mnuModificarDatos: {

                if (txtco_clie.getText().toString().length() == 0) {
                    txtco_clie.setError("Debe ingresar Cliente");
                } else if (txtco_cond_vent.getText().toString().length() == 0) {
                    txtco_cond_vent.setError("Debe ingresar Condición de Venta");
                } else if (txtpc_dcto_cabe.getText().toString().length() == 0) {
                    txtpc_dcto_cabe.setError("Debe ingresar el Descuento");
                } else if (Double.parseDouble(txtpc_dcto_cabe.getText().toString()) > 100) {
                    txtpc_dcto_cabe
                            .setError("El Descuento no puede ser mayor 100%");
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
                    LimpiarDatos();
                } else if (app.st_docu.equals("ANU")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setMessage("Ya no se puede modificar porque la cotización esta anulada");
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
                    LimpiarDatos();
                } else {

                    ModificarCotiAsyn tarea = new ModificarCotiAsyn();
                    tarea.execute();

                }

                return true;
            }
            case R.id.mnuImprimir: {

                Intent intent = new Intent(this, Imprimir.class);

                startActivity(intent);
                return true;

            }
            case R.id.mnuUtilNiubiz: {

                Intent intent = new Intent(getApplicationContext(),
                        UtilitarioActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return false;

        }

    }

    protected void LimpiarDatos() {
        GlobalApp app = (GlobalApp) getApplicationContext();

        txtco_alma.setText(variables_publicas.co_alma_vent);
        txtnu_coti.setText("");
        txtco_clie.setText("");
        txtno_clie.setText("");
        txtco_cond_vent.setText("");
        txtde_cond_vent.setText("");
        txtpc_dcto_cabe.setText("");
        txtde_obse_cabe.setText("");
        txtco_esta_cliente.setText("");
        app.st_docu = "ACT";
        app.de_plaz = "";
        app.nu_coti = "";
        app.co_clie = "";
        app.no_clie = "";
        app.co_cond_vent = "";
        app.de_cond_vent = "";
        app.pc_dcto_cabe = "";
        app.de_obse = "";
        app.co_esta_clie = "";

        txtco_esta_docu.setText(app.st_docu);
        variables_publicas.nu_coti_fina = "";

        txtnu_coti.requestFocus();
        app.menuGrabarDatos = true;
        app.menuModificarDatos = false;
        app.menuLimpiarDatos = true;
        app.menuAplicarDescto = false;
        TabInVisible();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1a);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        GlobalApp app = (GlobalApp) getApplicationContext();
        ImageButton btn_buscar_cliente = (ImageButton) findViewById(R.id.btn_buscar_cliente);
        ImageButton btn_buscar_cond = (ImageButton) findViewById(R.id.btn_buscar_cond);
        ImageButton btn_buscar_coti = (ImageButton) findViewById(R.id.btnbuscarCoti);

        app.menuGrabarDatos = true;
        app.menuModificarDatos = false;
        app.menuLimpiarDatos = true;
        app.menuAplicarDescto = false;
        app.st_docu = "ACT";
        app.de_plaz = "";
        ValidaTienda();

        txtnu_coti = (EditText) findViewById(R.id.txtnu_coti);
        txtco_alma = (EditText) findViewById(R.id.txtco_alma);
        txtco_cond_vent = (EditText) findViewById(R.id.txtco_cond_vent);
        txtde_cond_vent = (EditText) findViewById(R.id.txtde_cond_vent);
        txtco_clie = (EditText) findViewById(R.id.txtco_clie);
        txtno_clie = (EditText) findViewById(R.id.txtno_clie);
        txtpc_dcto_cabe = (EditText) findViewById(R.id.txtim_dsct_cabe);
        txtde_obse_cabe = (EditText) findViewById(R.id.txtde_obse_cabe);
        txtco_esta_docu = (TextView) findViewById(R.id.txtco_esta_docu);
        txtco_esta_cliente = (TextView) findViewById(R.id.txtco_esta_clie);
        txtco_alma.setText(variables_publicas.co_alma_vent);

        txtnu_coti.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1 == false) {
                    if (txtnu_coti.getText().toString().length() > 0) {
                        ValidaCabecera();
                    }

                }

            }
        });
        txtco_clie.setOnFocusChangeListener(new OnFocusChangeListener() {
            GlobalApp app = (GlobalApp) getApplicationContext();

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1 == false) {
                    if (txtco_clie.getText().toString().length() > 0) {
                        if (app.menuModificarDatos == false) {
                            ValidaCliente();
                        }

                    }

                }
            }
        });
        txtpc_dcto_cabe.setOnFocusChangeListener(new OnFocusChangeListener() {
            GlobalApp app = (GlobalApp) getApplicationContext();

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1 == false) {
                    if (Double
                            .parseDouble(txtpc_dcto_cabe.getText().toString()) != 0) {

                        if (Double.parseDouble(txtpc_dcto_cabe.getText()
                                .toString()) > Double
                                .parseDouble(app.pc_dcto_mdocu)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(
                                    Tab1Activity.this);

                            dialog.setMessage("Máximo procentaje de descuento por documento del usuario es "
                                    + app.pc_dcto_mdocu.toString() + "%");
                            dialog.setCancelable(false);
                            dialog.setTitle("Aviso");
                            dialog.setIcon(android.R.drawable.stat_notify_error);

                            dialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            txtpc_dcto_cabe.setText("0.00");
                                            dialog.cancel();
                                        }
                                    });
                            dialog.show();
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    Tab1Activity.this);
                            builder.setCancelable(false);
                            builder.setTitle("Validar Usuario");
                            // Get the layout inflater
                            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                            // LayoutInflater inflater = Tab1Activity.this
                            // .getLayoutInflater();
                            final View layout = inflater.inflate(
                                    R.layout.validausuario, null);
                            final EditText txtUsuario1 = ((EditText) layout
                                    .findViewById(R.id.txtUsuario1));
                            final EditText txtPassword1 = ((EditText) layout
                                    .findViewById(R.id.txtPassword1));
                            // Inflate and set the layout for the dialog
                            // Pass null as the parent view because its going in
                            // the dialog layout
                            builder.setView(layout);

                            // Add action buttons
                            builder.setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            app.de_plaz = txtUsuario1.getText()
                                                    .toString();
                                            app.pa_usua = txtPassword1
                                                    .getText().toString();
                                            ValidaDescuento();
                                            if (app.menuModificarDatos == true) {
                                                ModificarCotiAsyn tarea = new ModificarCotiAsyn();
                                                tarea.execute();
                                            }
                                            if (app.menuGrabarDatos == true) {
                                                GrabarCotiAsyn tarea = new GrabarCotiAsyn();
                                                tarea.execute();

                                            }
                                        }
                                    });
                            builder.setNegativeButton(android.R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            txtpc_dcto_cabe.setText("0.00");
                                            dialog.cancel();

                                        }
                                    });
                            builder.show();

                        }

                    }
                }
            }
        });
        btn_buscar_coti.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txtnu_coti.requestFocus();
                Intent intent = new Intent(getApplicationContext(),
                        ListaActivityBusqCoti.class);
                startActivityForResult(intent, HELP_COTIZACION_BUSQ);

            }
        });
        btn_buscar_cliente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txtco_clie.requestFocus();
                Intent intent = new Intent(getApplicationContext(),
                        ListaActivityCliente.class);
                startActivityForResult(intent, HELP_CLIENTE);
            }
        });

        txtco_cond_vent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1 == false) {
                    if (txtco_cond_vent.getText().toString().length() > 0) {
                        ValidaCondicion();
                    }

                }
            }
        });

        btn_buscar_cond.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                txtco_cond_vent.requestFocus();
                Intent intent = new Intent(getApplicationContext(),
                        ListaActivityCondicion2.class);
                startActivityForResult(intent, HELP_CONDICION);

            }
        });

        // editText3 = (EditText) findViewById(R.id.editText3);
        // editText4 = (EditText) findViewById(R.id.editText4);
        //
        // editText3.setOnKeyListener(new OnKeyListener() {
        //

    }

    public void ValidaDescuento() {

        ValidaDescuentoAsyn tarea1 = new ValidaDescuentoAsyn();

        tarea1.execute();
    }

    public void ValidaCondicion() {

        ValidaCondicionAsyn tarea = new ValidaCondicionAsyn();

        tarea.execute();
    }

    public void CambiodeTabCliente() {
        Container ta = (Container) this.getParent();
        TabHost th = ta.getTabHost();
        // th.getTabWidget().getChildAt(2).setVisibility(View.VISIBLE);
        th.setCurrentTab(2);
    }

    public void ValidaCliente() {

        ValidaClienteAsyn tarea = new ValidaClienteAsyn();
        tarea.execute();

    }

    public void ValidaTienda() {
        // progressDialog = new ProgressDialog(this);
        // progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        // progressDialog.setMessage("Procesando");
        // progressDialog.setProgress(0);

        ValidaTiendaAsyn tarea = new ValidaTiendaAsyn();

        tarea.execute();

    }

    public void ValidaCabecera() {

        ValidaCabeceraAsyn tarea = new ValidaCabeceraAsyn();

        tarea.execute();

    }

    public void TabVisible() {
        Container ta = (Container) this.getParent();
        TabHost th = ta.getTabHost();
        th.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);

    }

    public void TabInVisible() {
        Container ta = (Container) this.getParent();
        TabHost th = ta.getTabHost();
        th.getTabWidget().getChildAt(1).setVisibility(View.GONE);

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
                                    Tab1Activity.this.finish();
                                }
                            }).show();
            // Si el listener devuelve true, significa que el evento esta
            // procesado, y nadie debe hacer nada mas
            return true;
        }
        // para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);

    }

    private class GrabarCotiAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            GlobalApp app = (GlobalApp) getApplicationContext();
            app.co_clie = txtco_clie.getText().toString();
            app.co_cond_vent = txtco_cond_vent.getText().toString();
            app.pc_dcto_cabe = txtpc_dcto_cabe.getText().toString();
            app.de_obse = txtde_obse_cabe.getText().toString();
            app.st_docu = txtco_esta_docu.getText().toString();
            progressDialog = new ProgressDialog(Tab1Activity.this);
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
                GlobalApp app = (GlobalApp) getApplicationContext();
                txtnu_coti = (EditText) findViewById(R.id.txtnu_coti);
                variables_publicas.nu_coti_fina = result.toString();
                txtnu_coti.setText(result.toString());
                app.nu_coti = txtnu_coti.getText().toString();
                app.co_clie = txtco_clie.getText().toString();
                app.no_clie = txtno_clie.getText().toString();
                app.co_cond_vent = txtco_cond_vent.getText().toString();
                app.de_cond_vent = txtde_cond_vent.getText().toString();
                app.pc_dcto_cabe = txtpc_dcto_cabe.getText().toString();
                app.de_obse = txtde_obse_cabe.getText().toString();
                app.st_docu = txtco_esta_docu.getText().toString();

                app.menuGrabarDatos = false;
                app.menuModificarDatos = true;
                app.menuLimpiarDatos = true;
                app.menuAplicarDescto = true;

                // menu_datos= (MenuItem) findViewById(R.id.mnuGrabarDatos);
                // menu_datos.setVisible(false);

                CambiodeTab();

                Toast.makeText(getBaseContext(), "Se grabo satisfactoriamente",
                        Toast.LENGTH_LONG).show();
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
            GlobalApp app = (GlobalApp) getApplicationContext();
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "CrearCotizacion";
            final String SOAP_ACTION = "http://app.cassinelli.com/CrearCotizacion";

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
            request.addProperty("sCO_CLIE", app.co_clie.toString());
            request.addProperty("sCO_COND_VENT", app.co_cond_vent
                    .toString());
            request.addProperty("nPC_DCTO", app.pc_dcto_cabe
                    .toString());
            request.addProperty("sDE_OBSE", app.de_obse
                    .toString());
            request.addProperty("sCO_ESTA", app.st_docu
                    .toString());
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

    private class ModificarCotiAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            GlobalApp app = (GlobalApp) getApplicationContext();
            app.co_clie = txtco_clie.getText().toString();
            app.co_cond_vent = txtco_cond_vent.getText().toString();
            app.pc_dcto_cabe = txtpc_dcto_cabe.getText().toString();
            app.de_obse = txtde_obse_cabe.getText().toString();
            app.st_docu = txtco_esta_docu.getText().toString();

            progressDialog = new ProgressDialog(Tab1Activity.this);
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
                GlobalApp app = (GlobalApp) getApplicationContext();

                app.menuGrabarDatos = false;
                app.menuModificarDatos = true;
                app.menuLimpiarDatos = true;
                app.menuAplicarDescto = true;
                app.nu_coti = txtnu_coti.getText().toString();
                app.co_clie = txtco_clie.getText().toString();
                app.no_clie = txtno_clie.getText().toString();
                app.co_cond_vent = txtco_cond_vent.getText().toString();
                app.de_cond_vent = txtde_cond_vent.getText().toString();
                app.pc_dcto_cabe = txtpc_dcto_cabe.getText().toString();
                app.de_obse = txtde_obse_cabe.getText().toString();
                app.st_docu = txtco_esta_docu.getText().toString();
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
            GlobalApp app = (GlobalApp) getApplicationContext();
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
            request.addProperty("sCO_COND_VENT", app.co_cond_vent
                    .toString());
            request.addProperty("sCO_CLIE", app.co_clie.toString());
            request.addProperty("sNU_COTI", app.nu_coti.toString());
            request.addProperty("nPC_DCTO_REFE", app.pc_dcto_cabe
                    .toString());
            request.addProperty("sDE_OBSE", app.de_obse
                    .toString());
            //request.addProperty("sCO_ESTA", txtco_esta_docu.getText().toString());
            request.addProperty("sDE_PLAZ", app.de_plaz.toString());
            request.addProperty("sCO_USUA",
                    variables_publicas.co_usua.toString());
            request.addProperty("sCO_ESTA_DOCU",
                    app.st_docu.toString());

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

    public class UsuriosValida {

        public String st_sena;

    }

    private class ValidaDescuentoAsyn extends
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

                app.de_plaz = "";
                txtpc_dcto_cabe.setText("0.00");

            } else {
                app.pc_dcto_cabe = txtpc_dcto_cabe.getText().toString();
                app.st_docu = "ACE";
                txtco_esta_docu.setText(app.st_docu);
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

    private class ValidaCondicionAsyn extends
            AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            GlobalApp app = (GlobalApp) getApplicationContext();
            app.co_cond_vent = txtco_cond_vent.getText().toString();
            progressDialog = new ProgressDialog(Tab1Activity.this);
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

            if (result.equals("KO")) {

                Toast.makeText(getBaseContext(), "Condición no existe",
                        Toast.LENGTH_LONG).show();
                txtco_cond_vent.setText("");
                txtde_cond_vent.setText("");
                txtpc_dcto_cabe.setText("0.00");

            } else {

                txtde_cond_vent.setText(app.de_cond_vent.toString());
                txtpc_dcto_cabe.setText(app.pc_dcto_cabe.toString());

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
            // txtco_cond_vent = (EditText) findViewById(R.id.txtco_cond_vent);
            // txtde_cond_vent = (EditText) findViewById(R.id.txtde_cond_vent);
            // txtpc_dcto_cabe = (EditText) findViewById(R.id.txtim_dsct_cabe);
            String res = "";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ValidaCondicion";
            final String SOAP_ACTION = "http://app.cassinelli.com/ValidaCondicion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_COND_VENT", app.co_cond_vent
                    .toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.implicitTypes = false;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Usuario[] listaCondicion = new Usuario[resSoap
                        .getPropertyCount()];
                if (listaCondicion.length > 0) {
                    for (int i = 0; i < listaCondicion.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);
                        app.de_cond_vent = ic.getProperty(0).toString();
                        app.pc_dcto_cabe = ic.getProperty(1).toString();

                    }
                    res = "OK";
                } else {
                    res = "KO";
                    // Toast.makeText(getBaseContext(), "Condici�n no existe",
                    // Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {

            }
            return res;
        }

    }

    private class ValidaClienteAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            GlobalApp app = (GlobalApp) getApplicationContext();
            app.co_clie = txtco_clie.getText().toString();
            progressDialog = new ProgressDialog(Tab1Activity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Procesando...");
            progressDialog.setProgress(0);
            progressDialog.show();
            // TODO Auto-generated method stub

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            GlobalApp app = (GlobalApp) getApplicationContext();

            if (result.equals("KO")) {
                txtco_clie = (EditText) findViewById(R.id.txtco_clie);
                app.co_clie = txtco_clie.getText().toString();
                txtno_clie.setText("");
                app.no_clie = "";
                app.no_razo_soci = "";
                app.no_clie_natu = "";
                app.ap_clie_natu = "";
                app.am_clie_natu = "";
                app.nu_docu_iden = "";
                app.st_prom = "";
                app.st_rete = "";
                app.de_dire = "";
                app.no_ubic_geog = "";
                app.nu_tlf1 = "";
                app.nu_tlf2 = "";
                app.nu_tlf3 = "";
                app.de_dire_mail = "";
                app.st_comp_perc = "";

                Intent intent = new Intent(getApplicationContext(),
                        Tab3Activity.class);
                startActivityForResult(intent, HELP_CLIENTE_VALIDATE);

                Toast.makeText(getBaseContext(), "Cliente no existe",
                        Toast.LENGTH_LONG).show();
                // CambiodeTabCliente();

            } else if (result.equals("OK")) {
                txtco_clie = (EditText) findViewById(R.id.txtco_clie);
                txtno_clie = (EditText) findViewById(R.id.txtno_clie);
                txtco_esta_cliente = (TextView) findViewById(R.id.txtco_esta_clie);
                txtno_clie.setText(app.no_clie.toString());
                txtco_esta_cliente.setText(app.co_esta_clie);
                app.co_clie = txtco_clie.getText().toString();
                //Intent intent = new Intent(getApplicationContext(),
                //		Tab3Activity.class);
                //startActivityForResult(intent, HELP_CLIENTE_VALIDATE);

            } else {
                txtco_clie = (EditText) findViewById(R.id.txtco_clie);
                txtno_clie = (EditText) findViewById(R.id.txtno_clie);
                txtco_clie.setText("");
                app.co_clie = "";
                txtno_clie.setText("");
                app.no_clie = "";
                txtco_clie.requestFocus();
                Toast.makeText(getBaseContext(), "Cliente no valido en la sunat",
                        Toast.LENGTH_LONG).show();
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
            GlobalApp app = (GlobalApp) getApplicationContext();

            txtco_clie = (EditText) findViewById(R.id.txtco_clie);
            txtno_clie = (EditText) findViewById(R.id.txtno_clie);
            txtco_esta_cliente = (TextView) findViewById(R.id.txtco_esta_clie);
            String res = "";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ValidaCliente";
            final String SOAP_ACTION = "http://app.cassinelli.com/ValidaCliente";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_CLIE", app.co_clie.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Cliente[] listaCliente = new Cliente[resSoap.getPropertyCount()];
                if (listaCliente.length > 0) {
                    for (int i = 0; i < listaCliente.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);

                        if (!ic.getProperty(0).toString().equals("anyType{}")) {
                            app.no_clie = ic.getProperty(0).toString();
                        } else {
                            app.no_clie = "";
                        }
                        if (app.no_clie != "True") {
                            app.nu_docu_iden = ic.getProperty(1).toString();
                            app.st_prom = ic.getProperty(2).toString();
                            app.st_rete = ic.getProperty(3).toString();
                            app.de_dire = ic.getProperty(4).toString();
                            app.no_ubic_geog = ic.getProperty(5).toString();
                            app.co_ubic_geog = ic.getProperty(10).toString();
                            if (!ic.getProperty(6).toString().equals("anyType{}"))
                                app.nu_tlf1 = ic.getProperty(6).toString();
                            else
                                app.nu_tlf1 = "";
                            if (!ic.getProperty(7).toString().equals("anyType{}"))
                                app.nu_tlf2 = ic.getProperty(7).toString();
                            else
                                app.nu_tlf2 = "";
                            if (!ic.getProperty(8).toString().equals("anyType{}"))
                                app.nu_tlf3 = ic.getProperty(8).toString();
                            else
                                app.nu_tlf3 = "";
                            if (!ic.getProperty(9).toString().equals("anyType{}"))
                                app.de_dire_mail = ic.getProperty(9).toString();
                            else
                                app.de_dire_mail = "";
                            app.co_esta_clie = ic.getProperty(11).toString();
                            app.st_comp_perc = ic.getProperty(12).toString();
                            if (!ic.getProperty(0).toString().equals("anyType{}")) {
                                app.no_razo_soci = ic.getProperty(0).toString();
                            } else {
                                app.no_razo_soci = "";
                            }
                            //app.no_razo_soci = ic.getProperty(13).toString();
                            app.no_clie_natu = ic.getProperty(14).toString();
                            app.ap_clie_natu = ic.getProperty(15).toString();
                            app.am_clie_natu = ic.getProperty(16).toString();
                        } else {
                            res = "Ruc Invalido";
                        }
                    }
                    res = "OK";

                } else {
                    res = "KO";

                }

            } catch (Exception e) {
                // Toast.makeText(getBaseContext(), e.getMessage(),
                // Toast.LENGTH_LONG)
                // .show();
            }
            return res;
        }
    }

    private class ValidaTiendaAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressDialog.dismiss();
            if (result.equals("KO")) {
                Toast.makeText(getBaseContext(), "Tienda no existe",
                        Toast.LENGTH_LONG).show();

            } else {
                // txtco_alma = (EditText) findViewById(R.id.txtco_alma);
                txtco_alma.setText(variables_publicas.co_alma_vent.toString());
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
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ValidaTienda";
            final String SOAP_ACTION = "http://app.cassinelli.com/ValidaTienda";
            String res = "";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Tienda[] listaTienda = new Tienda[resSoap.getPropertyCount()];
                if (listaTienda.length > 0) {
                    for (int i = 0; i < listaTienda.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);
                        variables_publicas.de_tien = ic.getProperty(0)
                                .toString();
                        variables_publicas.co_alma_vent = ic.getProperty(1)
                                .toString();
                        variables_publicas.co_alma_cons = ic.getProperty(2)
                                .toString();
                        variables_publicas.co_alma_0009 = ic.getProperty(3)
                                .toString();
                        variables_publicas.co_alma_cent = ic.getProperty(4)
                                .toString();
                        variables_publicas.ni_prec = ic.getProperty(5)
                                .toString();
                        variables_publicas.no_empr = ic.getProperty(6).toString();
                        variables_publicas.nu_rucs_empr = ic.getProperty(7).toString();
                    }
                    res = "OK";
                } else {

                    res = "KO";

                }

            } catch (Exception e) {

            }
            return res;
        }
    }

    public class Cabecera {
        public String co_clie;
        public String no_clie;
        public String co_cond_vent;
        public String pc_dcto_refe;
        public String de_obse_0001;
        public String st_docu;
    }

    private class ValidaCabeceraAsyn extends
            AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            GlobalApp app = (GlobalApp) getApplicationContext();
            app.nu_coti = txtnu_coti.getText().toString();
            progressDialog = new ProgressDialog(Tab1Activity.this);
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

            if (result.equals("KO")) {
                txtnu_coti.setText("");
                variables_publicas.nu_coti_fina = "";
                app.co_clie = "";
                app.no_clie = "";
                app.co_cond_vent = "";
                app.pc_dcto_cabe = "";
                app.de_obse = "";
                app.st_actu = "";

                Toast.makeText(getBaseContext(), "Cotizacion no existe",
                        Toast.LENGTH_LONG).show();
                // CambiodeTabCliente();

            } else {
                txtco_clie.setText(app.co_clie.toString());
                txtno_clie.setText(app.no_clie.toString());
                txtco_cond_vent.setText(app.co_cond_vent.toString());
                txtde_cond_vent.setText(app.de_cond_vent.toString());
                txtpc_dcto_cabe.setText(app.pc_dcto_cabe.toString());
                txtde_obse_cabe.setText(app.de_obse);
                txtco_esta_docu.setText(app.st_docu.toString());
                // if (app.st_docu.toString().equals("ACT")) {
                // ActualizaCotizacionAsyn tarea = new
                // ActualizaCotizacionAsyn();
                // tarea.execute();
                // }
                TabVisible();

                app.menuGrabarDatos = false;
                app.menuModificarDatos = true;
                app.menuLimpiarDatos = true;
                app.menuAplicarDescto = true;

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

            String res = "";
            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ValidaCabeceraCoti";
            final String SOAP_ACTION = "http://app.cassinelli.com/ValidaCabeceraCoti";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                Cabecera[] listaCabecera = new Cabecera[resSoap
                        .getPropertyCount()];
                if (listaCabecera.length > 0) {
                    for (int i = 0; i < listaCabecera.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);

                        // txtno_clie.setText(ic.getProperty(0).toString());
                        // txtco_esta_cliente.setText(ic.getProperty(10)
                        // .toString());
                        variables_publicas.nu_coti_fina = app.nu_coti
                                .toString();
                        app.co_clie = ic.getProperty(0).toString();
                        app.no_clie = ic.getProperty(1).toString();
                        app.co_cond_vent = ic.getProperty(2).toString();
                        app.de_cond_vent = ic.getProperty(6).toString();
                        app.pc_dcto_cabe = ic.getProperty(3).toString();
                        if (!ic.getProperty(4).toString().equals("anyType{}")) {
                            app.de_obse = ic.getProperty(4).toString();
                        } else {
                            app.de_obse = "";
                        }
                        app.st_docu = ic.getProperty(5).toString();

                    }
                    res = "OK";

                } else {
                    res = "KO";

                }
                if (app.st_docu.toString().equals("ACT")) {
                    String act = "";
                    final String NAMESPACE1 = "http://app.cassinelli.com/";
                    final String URL1 = variables_publicas.direccionIp
                            + "/Service.asmx";
                    final String METHOD_NAME1 = "ActualizaCotizacion";
                    final String SOAP_ACTION1 = "http://app.cassinelli.com/ActualizaCotizacion";

                    SoapObject request1 = new SoapObject(NAMESPACE1,
                            METHOD_NAME1);

                    request1.addProperty("sCO_EMPR",
                            variables_publicas.co_empr.toString());
                    request1.addProperty("sCO_UNID",
                            variables_publicas.co_unid.toString());
                    request1.addProperty("sCO_TIEN",
                            variables_publicas.co_tien.toString());
                    request1.addProperty("sNU_COTI", app.nu_coti
                            .toString());
                    request1.addProperty("sCO_USUA",
                            variables_publicas.co_usua.toString());
                    SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    envelope1.dotNet = true;

                    envelope1.setOutputSoapObject(request1);

                    HttpTransportSE transporte1 = new HttpTransportSE(URL1);

                    try {
                        transporte1.call(SOAP_ACTION1, envelope1);
                        SoapPrimitive resultado_xml = (SoapPrimitive) envelope1
                                .getResponse();

                        act = resultado_xml.toString();

                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {
                // Toast.makeText(getBaseContext(), e.getMessage(),
                // Toast.LENGTH_LONG)
                // .show();
            }

            return res;
        }
    }

    private class AplicarDsctoCotiAsyn extends
            AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.co_cond_vent = txtco_cond_vent.getText().toString();
            app.co_clie = txtco_clie.getText().toString();
            app.nu_coti = txtnu_coti.getText().toString();
            app.de_obse = txtde_obse_cabe.getText().toString();
            app.st_docu = txtco_esta_docu.getText().toString();
            progressDialog = new ProgressDialog(Tab1Activity.this);
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
                // ListaCotizacion_gene();
                if (txtco_esta_docu.getText().toString().equals("ACT")) {
                    Toast.makeText(Tab1Activity.this,
                            "Cotizacion se aplico descuento correctamente",
                            Toast.LENGTH_LONG).show();
                    ValidaCabeceraAsyn tarea = new ValidaCabeceraAsyn();
                    tarea.execute();
                } else {
                    Toast.makeText(
                            Tab1Activity.this,
                            "Cotizacion se desactivo la aplicación correctamente",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getBaseContext(),
                        "Verificar no se aplico descuento !", Toast.LENGTH_LONG)
                        .show();
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
            CabeceraTotales cabtot = new CabeceraTotales();
            cabtot.Totales_cabecera();
            String res = "";

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "AplicarDescuento";
            final String SOAP_ACTION = "http://app.cassinelli.com/AplicarDescuento";

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
            request.addProperty("sCO_COND_VENT", app.co_cond_vent
                    .toString());
            request.addProperty("sCO_CLIE", app.co_clie.toString());
            request.addProperty("sNU_COTI", app.nu_coti.toString());
            request.addProperty("nPC_DCTO_REFE", app.pc_dcto_cabe.toString());
            request.addProperty("sDE_OBSE", app.de_obse
                    .toString());
            request.addProperty("sCO_ESTA", app.st_docu
                    .toString());
            request.addProperty("nIM_COTI",
                    variables_publicas.tot_soles.toString());
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

                ValidaCabecera();

            } catch (Exception e) {

            }
            return res;
        }

    }

    private class ActualizaCotizacionAsyn extends
            AsyncTask<Integer, Integer, String> {
        GlobalApp app = (GlobalApp) getApplicationContext();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            app.nu_coti = txtnu_coti.getText().toString();

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result.toString().equals("OK")) {
                // ListaCotizacion_gene();

                Toast.makeText(Tab1Activity.this,
                        "Cotización se actualizo correctamente",
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getBaseContext(),
                        "Cotización no se actualizo verificar !",
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
            final String METHOD_NAME = "ActualizaCotizacion";
            final String SOAP_ACTION = "http://app.cassinelli.com/ActualizaCotizacion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR",
                    variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID",
                    variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN",
                    variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI", app.nu_coti.toString());
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