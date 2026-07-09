/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

import java.sql.Date;

/**
 *
 * @author User
 */
public class ProduksiModel {
    private String kode_produksi;
    private String customer;
    private String kode_bom;
    private Integer jumlah_produksi;
    private Date tanggal;
    private String kode_tk;
    private int id_overhead;
    private double nominal_overhead;
    private String keterangan;
    
    public ProduksiModel(){
        
    }
    
    public String getKode(){
        return kode_produksi;
    }
    
    public void setKode(String kode_produksi){
        this.kode_produksi = kode_produksi;
    }
    
    public String getCust(){
        return customer;
    }
    
    public void setCust(String customer){
        this.customer = customer;
    }
    
    public String getKdBom(){
        return kode_bom;
    }
    
    public void setKdBom(String kode_bom){
        this.kode_bom = kode_bom;
    }
    
    public Date getTgl(){
        return tanggal;
    }
    
    public void setTgl(Date tanggal){
        this.tanggal = tanggal;
    }
    
    public int getJmlhProduksi(){
        return jumlah_produksi;
    }
    
    public void setJmlhProduksi(int jumlah_produksi){
        this.jumlah_produksi = jumlah_produksi;
    }
    
    public int getIdOverhead(){
        return id_overhead;
    }
    
    public void setIdOverhead(int id_overhead){
        this.id_overhead = id_overhead;
    }
    
    public double getNominal(){
        return nominal_overhead;
    }
    
    public void setNominal(double nominal_overhead){
        this.nominal_overhead = nominal_overhead;
    }
    
    public String getKet(){
        return keterangan;
    }
    
    public void setKet(String keterangan){
        this.keterangan = keterangan;
    }
    
    public String getKdTk(){
        return kode_tk;
    }
    
    public void setKdTk(String kode_tk){
        this.kode_tk = kode_tk;
    }
}
