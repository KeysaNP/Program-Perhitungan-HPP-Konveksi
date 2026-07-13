/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class TkModel {
    private String kode_tk;
    private String nama_proses;
    private double biaya_harian;
    
    public TkModel(){
        
    }
    
    public String getKode(){
        return kode_tk;
    }
    
    public void setKode(String kode_tk){
        this.kode_tk = kode_tk;
    }
    
    public String getNama(){
        return nama_proses;
    }
    
    public void setNama(String nama_proses){
        this.nama_proses = nama_proses;
    }
    
    public Double getBiaya(){
        return biaya_harian;
    }
    
    public void setBiaya(double biaya_harian){
        this.biaya_harian = biaya_harian;
    }
    
    
}
