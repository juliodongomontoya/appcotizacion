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
import android.support.v4.content.FileProvider;
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


public class Imprimir extends Activity {
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
    private Context mContext = Imprimir.this;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_dialog);

        // Compruebe si esta aplicación tiene permiso de escritura de almacenamiento externo o no.
        //int permissionCheck = ContextCompat.checkSelfPermission(this,
        //        Manifest.permission.WRITE_EXTERNAL_STORAGE);
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


        try {


            Imprimir.ImprimirCotiAsyn tarea = new Imprimir.ImprimirCotiAsyn();

            tarea.execute();

        } catch (Exception e) {
            e.getMessage();
        }

    }

    private class ImprimirCotiAsyn extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(Imprimir.this);
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
                String sdCardRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + FILE;

          /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    File file = new File(sdCardRoot);
                    Uri uri = FileProvider.getUriForFile(getBaseContext(),getPackageName()+ ".provider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        getBaseContext().startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "No hay ninguna aplicación que pueda leer PDF", Toast.LENGTH_SHORT).show();
                    }

                } else {
          */
                File file = new File(sdCardRoot);
                if (file.exists()) {

                    //target.setDataAndType(Uri.fromFile(file), "application/pdf");
                    Uri uri = FileProvider.getUriForFile(getBaseContext(),getPackageName()+ ".provider", file);
                    //Uri uri = Uri.parse(file.getAbsolutePath());
                    Intent target = new Intent(Intent.ACTION_VIEW);
                    target.setDataAndType(uri,"application/pdf");

                    //intent = Intent.createChooser(intent,"Open File");
                    target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "No hay ninguna aplicación que pueda leer PDF", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "La ruta del archivo es incorrecto", Toast.LENGTH_LONG).show();
                }
                //}

                finish();

            } else {
                Toast.makeText(getBaseContext(), "No se creo el pdf!",
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
                String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + FILE;
                //String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE;
               // String baseDir = Environment.getExternalStorageDirectory().toString();

                File file = new File(baseDir);
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

                            PdfPCell ca = new PdfPCell(new Paragraph(variables_publicas.no_empr, subFont));
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
                            if (!ic.getProperty(3).toString().equals("anyType{}")) {
                                ca = new PdfPCell(new Paragraph("CTRAL.TELF", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph(ic.getProperty(3).toString(), smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);
                            } else {
                                ca = new PdfPCell(new Paragraph("", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                tablecabecera.addCell(ca);

                                ca = new PdfPCell(new Paragraph("", smallBold));
                                ca.setHorizontalAlignment(Element.ALIGN_LEFT);
                                ca.setBorderWidth(0f);   // removes border
                                ca.setColspan(2);
                                tablecabecera.addCell(ca);

                            }
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

                            ca = new PdfPCell(new Paragraph(variables_publicas.nu_rucs_empr, smallBold));
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

                    c9 = new PdfPCell(new Paragraph("ESTIMADO CLIENTE ANTES DE RETIRARSE DE LA TIENDA FAVOR DE VERIFICAR LOS DATOS DE SU FACTURA/BOLETA QUE CORRESPONDE RAZON SOCIAL,RUC Y DIRECCION, NO SE ACEPTARAN CAMBIO DE DOCUMENTOS", smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c9.setColspan(8);
                    table.addCell(c9);

                    c9 = new PdfPCell(new Paragraph("", smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c9.setColspan(8);
                    table.addCell(c9);

                    c9 = new PdfPCell(new Paragraph("TODO PAGO DEBE SER REALIZADO UNICAMENTE EN NUESTRAS CAJAS DE TIENDAS O ATRAVES DE DEPOSITOS Y/O TRANSFERENCIAS EN NUESTRAS CUENTAS BANCARIAS A NOMBRE DE CENTRO CERAMICO LAS FLORES S.A.C", smallBold));
                    c9.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c9.setColspan(8);
                    c9.setBorderWidth(0f);
                    table.addCell(c9);



                    preface.add(table);
                    document.add(preface);
                    if (variables_publicas.co_empr.equals("03")) {
                        Paragraph preface_pie = new Paragraph();
                        PdfPTable tablepie = new PdfPTable(6); // 7 columns
                        tablepie.setWidthPercentage(100);
                        float[] columnWidths = {1.5f, 0.5f, 1.5f, 2.5f, 1.5f,
                                1.0f};
                        tablepie.setWidths(columnWidths);
                        addEmptyLine(preface_pie, 1);

                        PdfPCell c10 = new PdfPCell(new Paragraph("BANCOS", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("MON", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("CUENTA CORRIENTE", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("CUENTA INTERBANCARIA", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("CODIGO CREDIPAGO", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("CODIGO AGENTE", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        PdfPCell c11 = new PdfPCell(new Paragraph("CREDITO", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("191-1110662-0-07", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("002-191-001110662-0-07-50", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("20466010101", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("430", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        PdfPCell c12 = new PdfPCell(new Paragraph("CREDITO", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("191-1105042-1-50", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("002-191-001105042-1-50-59", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        PdfPCell c13 = new PdfPCell(new Paragraph("CONTINENTAL", smallBold));
                        c13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c13);

                        c13 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c13);

                        c13 = new PdfPCell(new Paragraph("0011-0353-0100017644-04", smallBold));
                        c13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c13);

                        c13 = new PdfPCell(new Paragraph("011-353-000100017644-04", smallBold));
                        c13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c13);

                        c13 = new PdfPCell(new Paragraph("Recaudación : 6533", smallBold));
                        c13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c13);

                        c13 = new PdfPCell(new Paragraph("", smallBold));
                        c13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c13);

                        PdfPCell c14 = new PdfPCell(new Paragraph("CONTINENTAL", smallBold));
                        c14.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c14);

                        c14 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c14.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c14);

                        c14 = new PdfPCell(new Paragraph("0011-0586-0100049306-52", smallBold));
                        c14.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c14);

                        c14 = new PdfPCell(new Paragraph("011-586-000100049306-52", smallBold));
                        c14.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c14);

                        c14 = new PdfPCell(new Paragraph("", smallBold));
                        c14.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c14);

                        c14 = new PdfPCell(new Paragraph("", smallBold));
                        c14.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c14);

                        PdfPCell c15 = new PdfPCell(new Paragraph("SCOTIABANK", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("000-9463500", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("009-170-000009463500-20", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        PdfPCell c16 = new PdfPCell(new Paragraph("SCOTIABANK", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("000-504433", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("009-170-000005004433-28", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        PdfPCell c17 = new PdfPCell(new Paragraph("INTERBANK", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("041-3000227005", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("003-041-003000227005-13", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        PdfPCell c18 = new PdfPCell(new Paragraph("INTERBANK", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("200-3001852466", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("003-200-003001852466-37", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);


                        preface_pie.add(tablepie);
                        document.add(preface_pie);

                    } else {
                        Paragraph preface_pie = new Paragraph();
                        PdfPTable tablepie = new PdfPTable(4); // 4 columns
                        tablepie.setWidthPercentage(100);
                        float[] columnWidths = {1.5f, 0.5f, 1.5f, 2.5f};
                        tablepie.setWidths(columnWidths);
                        addEmptyLine(preface_pie, 1);

                        PdfPCell c10 = new PdfPCell(new Paragraph("BANCOS", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("MON", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("CUENTA CORRIENTE", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);

                        c10 = new PdfPCell(new Paragraph("CUENTA INTERBANCARIA", smallBold));
                        c10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c10);


                        PdfPCell c11 = new PdfPCell(new Paragraph("CREDITO", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("390-2169625-0-95", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);

                        c11 = new PdfPCell(new Paragraph("00239000216962509536", smallBold));
                        c11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c11);


                        PdfPCell c12 = new PdfPCell(new Paragraph("CREDITO", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("390-2400927-1-89", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);

                        c12 = new PdfPCell(new Paragraph("00239000240092718000", smallBold));
                        c12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c12);


                        PdfPCell c15 = new PdfPCell(new Paragraph("SCOTIABANK", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("000-1769712", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);

                        c15 = new PdfPCell(new Paragraph("009-170-000001769712-29", smallBold));
                        c15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c15);


                        PdfPCell c16 = new PdfPCell(new Paragraph("SCOTIABANK", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("000-5106231", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);

                        c16 = new PdfPCell(new Paragraph("009-170-000005106231-23", smallBold));
                        c16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c16);


                        PdfPCell c17 = new PdfPCell(new Paragraph("INTERBANK", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("SOL", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("200-3003678448", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);

                        c17 = new PdfPCell(new Paragraph("003-200-003003678448-33", smallBold));
                        c17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c17);


                        PdfPCell c18 = new PdfPCell(new Paragraph("INTERBANK", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("DOL", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("200-3003678455", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);

                        c18 = new PdfPCell(new Paragraph("003-200-003003678455-38", smallBold));
                        c18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c18);


                        PdfPCell c19 = new PdfPCell(new Paragraph("DE LA NACION-DETRACCIONES", smallBold));
                        c19.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c19);

                        c19 = new PdfPCell(new Paragraph("", smallBold));
                        c19.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c19);

                        c19 = new PdfPCell(new Paragraph("00-521-052228", smallBold));
                        c19.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c19);

                        c19 = new PdfPCell(new Paragraph("", smallBold));
                        c19.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tablepie.addCell(c19);


                        preface_pie.add(tablepie);
                        document.add(preface_pie);
                    }


                }
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
