package com.cassinelli.cotiza;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

public class Correo extends AppCompatActivity {
    private final static String FILE = "/Cotizacion.pdf";
    private static final int REQUEST = 112;
    static String file = null;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    //private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
    //Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);
    private static Font smallBold = FontFactory.getFont("Arial", 10, Font.BOLD);
    //private static Font smallBold1 = new Font(Font.FontFamily.TIMES_ROMAN, 16,
    //	Font.NORMAL);
    ProgressDialog progressDialog;
    OutputStream fileOut;
    private Context mContext = Correo.this;

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void addMetaData(Document document) {
        document.addTitle("Cotizacion");
        document.addSubject("Cotizacion");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Julio Dongo");
        document.addCreator("Julio Dongo");
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(getApplicationContext(), "El app no esta permitido escribir en su storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.correoactivity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
            } else {
                //do here
            }
        } else {
            //do here
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.correomenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        int id = item.getItemId();
        switch (id) {
            case R.id.mnuEnviarCorreo: {

                try {

                    Correo.ImprimirCotiAsyn tarea = new Correo.ImprimirCotiAsyn();

                    tarea.execute();

                    return true;
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
        return false;


    }

    private class ImprimirCotiAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Correo.this);
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
                String sdCardRoot = Environment.getExternalStorageDirectory().getPath() + FILE;
                Uri uri = Uri.fromFile(new File(sdCardRoot));
                EditText etEmail = (EditText) findViewById(R.id.txtde_para);
                EditText etSubject = (EditText) findViewById(R.id.txtde_asunto);
                EditText etBody = (EditText) findViewById(R.id.txtde_mensaje);
                Intent itSend = new Intent(Intent.ACTION_SEND);
                // vamos a enviar texto plano a menos que el checkbox est� marcado
                //itSend.setType(HTTP.PLAIN_TEXT_TYPE);
                itSend.putExtra(Intent.EXTRA_EMAIL, new String[]{etEmail.getText().toString()});
                itSend.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                itSend.putExtra(Intent.EXTRA_TEXT, etBody.getText());
                itSend.putExtra(Intent.EXTRA_STREAM, uri);
                itSend.setType("*/*");
                try {
                    if (itSend.resolveActivity(getPackageManager()) != null) {
                        startActivity(itSend);
                    }
                    //startActivity(Intent.createChooser(itSend, "Send email using:"));
                    //startActivity(itSend);
                    //finish();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "No hay ninguna aplicación para enviar correos", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getBaseContext(), "No se envio el correo!",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected String doInBackground(Integer... params) {
            String res = "";
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#########.00", simbolos);
            Paragraph preface = new Paragraph();
            Double SumaDcto = 0.00;
            Double ValorNeto = 0.00;
            Double Valorventa = 0.00;
            Double Pc_Igv = 0.00;
            Double Im_Igv = 0.00;
            Double PrecioVenta = 0.00;
            String Des_perc = "";
            Double Im_perc = 0.00;
            Double Tot_gene = 0.00;

            final String NAMESPACE = "http://app.cassinelli.com/";
            final String URL = variables_publicas.direccionIp + "/Service.asmx";
            final String METHOD_NAME = "ImpresionCotizacion";
            final String SOAP_ACTION = "http://app.cassinelli.com/ImpresionCotizacion";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("sCO_EMPR", variables_publicas.co_empr.toString());
            request.addProperty("sCO_UNID", variables_publicas.co_unid.toString());
            request.addProperty("sCO_TIEN", variables_publicas.co_tien.toString());
            request.addProperty("sNU_COTI",
                    variables_publicas.nu_coti_fina.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                Document document = new Document();

                String baseDir = Environment.getExternalStorageDirectory().toString();

                File file = new File(baseDir, FILE);
                if (file.exists()) {
                    fileOut = new FileOutputStream(file);
                } else {
                    file.createNewFile();
                    fileOut = new FileOutputStream(file);
                }
                PdfWriter.getInstance(document, fileOut);
                document.open();
                addMetaData(document);


                transporte.call(SOAP_ACTION, envelope);
                SoapObject resSoap = (SoapObject) envelope.getResponse();
                ImpresionCotizacion[] listaImpresion = new ImpresionCotizacion[resSoap
                        .getPropertyCount()];
                PdfPTable table = new PdfPTable(8); // 3 columns

                if (listaImpresion.length > 0) {
                    for (int i = 0; i < listaImpresion.length; i++) {
                        SoapObject ic = (SoapObject) resSoap.getProperty(i);

                        if (i == 0) {
                            Paragraph preface_cabe = new Paragraph();
                            PdfPTable tablecabecera = new PdfPTable(3); // 3 columns
                            tablecabecera.setWidthPercentage(100);
                            float[] columnWidths_cabe = {5f, 6f, 5f};
                            tablecabecera.setWidths(columnWidths_cabe);

                            PdfPCell ca = new PdfPCell(new Paragraph("SANIHOLD S.A.C", subFont));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setColspan(3);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph(ic.getProperty(0).toString(), smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setColspan(3);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);


                            Calendar cal1 = Calendar.getInstance(TimeZone.getDefault());
                            ca = new PdfPCell(new Paragraph("Fecha", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);


                            int imes = (cal1.get(Calendar.MONTH) + 1);
                            String mes = imes + "";
                            if (imes < 10) mes = "0" + imes;


                            int idia = cal1.get(Calendar.DAY_OF_MONTH);
                            String dia = idia + "";
                            if (idia < 10)
                                dia = "0" + idia;
                            ca = new PdfPCell(new Paragraph(dia + "/" + mes
                                    + "/" + cal1.get(Calendar.YEAR) + "    HORA:" + cal1.get(Calendar.HOUR)
                                    + ":" + cal1.get(Calendar.MINUTE) + ":" + cal1.get(Calendar.SECOND), smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("CTRAL.TELF", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph(ic.getProperty(3).toString(), smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("SERV.TEC", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("0800-12150", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("241-5746   541-5753", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("RUC", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("20514737364", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("COTIZACION", catFont));
                            ca.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(3);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph(variables_publicas.nu_coti_fina, catFont));
                            ca.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(3);
                            tablecabecera.addCell(ca);


                            ca = new PdfPCell(new Paragraph("CLIENTE", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph(ic.getProperty(5).toString() + "-"
                                    + ic.getProperty(6).toString(), smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            if (!ic.getProperty(27).toString().equals("anyType{}")) {
                                ca = new PdfPCell(new Paragraph("RUC", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph(ic.getProperty(27).toString(), smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            } else {
                                ca = new PdfPCell(new Paragraph("RUC", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph("", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            }

                            if (!ic.getProperty(29).toString().equals("anyType{}")) {
                                ca = new PdfPCell(new Paragraph("TELEFONO", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph(ic.getProperty(29).toString(), smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            } else {
                                ca = new PdfPCell(new Paragraph("TELEFONO", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph("", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            }
                            if (!ic.getProperty(28).toString().equals("anyType{}")) {
                                ca = new PdfPCell(new Paragraph("FAX", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph(ic.getProperty(28).toString(), smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            } else {
                                ca = new PdfPCell(new Paragraph("FAX", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph("", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            }
                            ca = new PdfPCell(new Paragraph("COND.VENTA", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph(ic.getProperty(9).toString() + "-"
                                    + ic.getProperty(10).toString(), smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph("VENDEDOR", smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            tablecabecera.addCell(ca);

                            ca = new PdfPCell(new Paragraph(ic.getProperty(7).toString() + "-"
                                    + ic.getProperty(8).toString(), smallBold));
                            ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                            ca.setBorderWidth(0f);   // removes border
                            ca.setColspan(2);
                            tablecabecera.addCell(ca);

                            preface_cabe.add(tablecabecera);
                            document.add(preface_cabe);


                            addEmptyLine(preface, 1);

                            table.setWidthPercentage(100);
                            float[] columnWidths = {1.3f, 1.0f, 0.5f, 1.5f, 8f,
                                    1.7f, 1.7f, 1.7f};
                            table.setWidths(columnWidths);

                            PdfPCell cb = new PdfPCell(new Paragraph("DESCTO %", smallBold));
                            cb.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cb.setColspan(2);
                            table.addCell(cb);

                            cb = new PdfPCell(new Paragraph("HORA", smallBold));
                            cb.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cb.setColspan(2);
                            table.addCell(cb);

                            cb = new PdfPCell(new Paragraph("FECHA", smallBold));
                            cb.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cb.setColspan(4);
                            table.addCell(cb);

                            cb = new PdfPCell(new Paragraph(ic.getProperty(11).toString(), smallBold));
                            cb.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cb.setColspan(2);
                            table.addCell(cb);

                            cb = new PdfPCell(new Paragraph(ic.getProperty(12).toString(), smallBold));
                            cb.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cb.setColspan(2);
                            table.addCell(cb);

                            cb = new PdfPCell(new Paragraph(ic.getProperty(13).toString(), smallBold));
                            cb.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cb.setColspan(4);
                            table.addCell(cb);

                            PdfPCell c1 = new PdfPCell(new Paragraph("COD", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("UN", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("P", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("CANT", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("DESCRIPCION", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("P/U INC.IGV", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("% DCTO", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(c1);

                            c1 = new PdfPCell(new Paragraph("P/V INC.IGV", smallBold));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);

                            table.addCell(c1);
                            //table.setHeaderRows(1);
                            ValorNeto = Double.parseDouble(ic.getProperty(24)
                                    .toString());
                            Pc_Igv = Double.parseDouble(ic.getProperty(22)
                                    .toString());
                            Im_Igv = Double.parseDouble(ic.getProperty(23)
                                    .toString());
                            PrecioVenta = Double.parseDouble(ic.getProperty(25)
                                    .toString());
                            Des_perc = ic.getProperty(43).toString();
                            Im_perc = Double.parseDouble(ic.getProperty(44)
                                    .toString());
                            Tot_gene = Double.parseDouble(ic.getProperty(45)
                                    .toString());

                        }
                        SumaDcto = SumaDcto
                                + (Double
                                .parseDouble(ic.getProperty(19).toString()) + Double
                                .parseDouble(ic.getProperty(21).toString()));

                        PdfPCell c2 = new PdfPCell(new Paragraph(ic.getProperty(36)
                                .toString(), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(c2);

                        c2 = new PdfPCell(new Paragraph(ic.getProperty(15).toString(), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(c2);

                        if (!ic.getProperty(42).toString().equals("anyType{}")) {
                            c2 = new PdfPCell(new Paragraph(ic.getProperty(42)
                                    .toString(), smallBold));
                        } else {
                            c2 = new PdfPCell(new Paragraph(""));
                        }
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c2);

                        c2 = new PdfPCell(new Paragraph(df.format(Double
                                .parseDouble(ic.getProperty(17).toString())), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c2);

                        c2 = new PdfPCell(new Paragraph(ic.getProperty(16).toString(), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(c2);

                        c2 = new PdfPCell(new Paragraph(df.format(Double
                                .parseDouble(ic.getProperty(37).toString())), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c2);

                        c2 = new PdfPCell(new Paragraph(df.format(Double
                                .parseDouble(ic.getProperty(20).toString())), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c2);

                        c2 = new PdfPCell(new Paragraph(df.format(Double
                                .parseDouble(ic.getProperty(40).toString())), smallBold));
                        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c2);

                    }
                    Valorventa = ValorNeto + SumaDcto;
                    PdfPCell c3 = new PdfPCell(new Paragraph("VALOR VENTA", smallBold));
                    c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c3.setColspan(7);
                    table.addCell(c3);
                    c3 = new PdfPCell(new Paragraph(df.format(Double
                            .parseDouble(Valorventa.toString())), smallBold));
                    c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c3);

                    PdfPCell c4 = new PdfPCell(new Paragraph("DESCUENTO", smallBold));
                    c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c4.setColspan(7);
                    table.addCell(c4);

                    c4 = new PdfPCell(new Paragraph(SumaDcto.toString(), smallBold));
                    c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c4);

                    PdfPCell c5 = new PdfPCell(new Paragraph("VALOR NETO", smallBold));
                    c5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c5.setColspan(7);
                    table.addCell(c5);

                    c5 = new PdfPCell(new Paragraph(ValorNeto.toString(), smallBold));
                    c5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c5);

                    PdfPCell c6 = new PdfPCell(new Paragraph("IGV% " + Pc_Igv, smallBold));
                    c6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c6.setColspan(7);
                    table.addCell(c6);

                    c6 = new PdfPCell(new Paragraph(Im_Igv.toString(), smallBold));
                    c6.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c6);

                    PdfPCell c7 = new PdfPCell(new Paragraph("PRECIO VENTA", smallBold));
                    c7.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c7.setColspan(7);
                    table.addCell(c7);

                    c7 = new PdfPCell(new Paragraph(PrecioVenta.toString(), smallBold));
                    c7.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c7);

                    if (!Des_perc.toString().equals("anyType{}")) {
                        PdfPCell c8 = new PdfPCell(new Paragraph(Des_perc.toString(), smallBold));
                        c8.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        c8.setColspan(7);
                        table.addCell(c8);
                        c8 = new PdfPCell(new Paragraph(Im_perc.toString(), smallBold));
                        c8.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c8);
                    } else {
                        PdfPCell c8 = new PdfPCell(new Paragraph("", smallBold));
                        c8.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        c8.setColspan(7);
                        table.addCell(c8);
                        c8 = new PdfPCell(new Paragraph(Im_perc.toString(), smallBold));
                        c8.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(c8);
                    }


                    PdfPCell c9 = new PdfPCell(new Paragraph("TOTAL GENERAL", smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c9.setColspan(7);
                    table.addCell(c9);
                    c9 = new PdfPCell(new Paragraph(Tot_gene.toString(), smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(c9);

                    c9 = new PdfPCell(new Paragraph("PRECIOS SUJETOS A VARIACION SIN PREVIO AVISO", smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c9.setColspan(8);
                    table.addCell(c9);
                    c9 = new PdfPCell(new Paragraph("LAS COTIZACIONES NO GARANTIZAN EL STOCK DE LOS PRODUCTOS", smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c9.setColspan(8);
                    table.addCell(c9);

                    preface.add(table);

                }
                document.add(preface);
                document.close();
                res = "OK";
            } catch (Exception e) {
                res = "KO";
                e.getMessage();
            }

            return res;

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

    }


}
