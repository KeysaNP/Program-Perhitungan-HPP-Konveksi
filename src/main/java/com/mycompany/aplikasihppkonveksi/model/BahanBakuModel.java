/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class BahanBakuModel {
    private String kode_bahan;
    private String nama_bahan;
    private String satuan;
    private double qty_beli;
    private double harga_beli;
    private double harga_satuan;
    
    public BahanBakuModel(){
        
    }
    
    public String getKode(){
        return kode_bahan;
    }
    
    public void setKode(String kode_bahan){
        this.kode_bahan = kode_bahan;
    }
    
    public String getNama(){
        return nama_bahan;
    }
    
    public void setNama(String nama_bahan){
        this.nama_bahan = nama_bahan;
    }
    
    public String getSatuan(){
        return satuan;
    }
    
    public void setSatuan(String satuan){
        this.satuan = satuan;
    }
    
    public double getQtyBeli(){
        return qty_beli;
    }
    
    public void setQtyBeli(double qty_beli){
        this.qty_beli = qty_beli;
    }
    
    public double getHargaBeli(){
        return harga_beli;
    }
    
    public void setHargaBeli(double harga_beli){
        this.harga_beli = harga_beli;
    }
    
    public double getHargaSatuan(){
        return harga_satuan;
    }
    
    public void setHargaSatuan(double harga_satuan){
        this.harga_satuan = harga_satuan;
    }
    
}
