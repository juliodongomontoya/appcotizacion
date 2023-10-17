package com.cassinelli.cotiza;

import android.app.Application;

public class GlobalApp extends Application {

    // cotizacion
    public String nu_coti;
    public String de_cond_vent;
    public String pc_dcto_cabe;
    public String to_soles;
    public String to_dolar;
    public String co_cond_vent;
    public String st_docu;
    public String de_plaz;
    public String pa_usua;
    // cliente
    public String co_clie;
    public String no_clie;
    public String no_razo_soci;
    public String no_clie_natu;
    public String ap_clie_natu;
    public String am_clie_natu;
    public String de_dire;
    public String nu_docu_iden;
    public String st_prom;
    public String st_rete;
    public String co_ubic_geog;
    public String no_ubic_geog;
    public String nu_tlf1;
    public String nu_tlf2;
    public String nu_tlf3;
    public String de_dire_mail;
    public String st_comp_perc;
    public String co_esta_clie;

    //articulos
    public String co_item;
    public String de_item_larg;
    public String st_actu;
    public String co_unme;
    public String nu_empa;
    public String pr_vent;
    public String co_alma_detalle;
    public String ca_actu;
    public String pc_dcto_detalle;
    public String pr_cigv;
    public String de_unme;
    public String de_alma;
    public String ca_item;
    public String de_obse;
    public String nu_corr;
    public String de_obse_detalle;
    public String pc_dcto_mdocu;
    public String pc_dcto_marti;
    public String st_bloq;
    public String de_busq;
    public String dfe_inic;
    public String dfe_fina;
    public String co_clie_busq;
    public String co_mast;
    public String ca_mast;
    //menus
    public Boolean menuGrabarDatos;
    public Boolean menuModificarDatos;
    public Boolean menuLimpiarDatos;

    public Boolean menuLimpiarCliente;
    public Boolean menuGrabarCliente;
    public Boolean menuModificarCliente;

    public Boolean menuLimpiarDetalle;
    public Boolean menuGrabarDetalle;
    public Boolean menuModificarDetalle;
    public Boolean menuEliminarDetalle;
    public Boolean menuMasterDetalle;

    public Boolean menuDuplicarCoti;
    public Boolean menuConvertirCoti;
    public Boolean menuConvertirDocu;
    public Boolean menuAplicarDescto;
    public Boolean menuAplicaDctoItem;


    public Boolean menuGrabarMaster;
    public Boolean menuProcesarUtil;


}
