/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class OverheadModel {
    private int id_overhead;
    private String kode_produksi;
    private String jenis_biaya;
    private double nominal;
    private String periode;
    
    public OverheadModel(){
        
    }
    
    public int getId(){
        return id_overhead;
    }
    
    public void setId(int id_overhead){
        this.id_overhead = id_overhead;
    }
    
    public String getKode(){
        return kode_produksi;
    }
    
    public void setKode(String kode_produksi){
        this.kode_produksi = kode_produksi;
    }
    
    public String getJenis(){
        return jenis_biaya;
    }
    
    public void setJenis(String jenis_biaya){
        this.jenis_biaya = jenis_biaya;
    }
    
    public double getNominal(){
        return nominal;
    }
    
    public void setNominal(double nominal){
        this.nominal = nominal;
    }
    
    public String getPeriode(){
        return periode;
    }
    
    public void setPeriode(String periode){
        this.periode = periode;
    }
}   

