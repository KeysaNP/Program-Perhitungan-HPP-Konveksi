/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class BomModel {
    private String kode_bom;
    private String kode_produk;
    private String ukuran;
    private String kombinasi_warna;
    private String keterangan;
    
    public BomModel(){
        
    }
    
    public String getKodeBom(){
        return kode_bom;
    }
    
    public void setKodeBom(String kode_bom){
        this.kode_bom = kode_bom;
    }
    
    public String getKodeProduk(){
        return kode_produk;
    }
    
    public void setKodeProduk(String kode_produk){
        this.kode_produk = kode_produk;
    }
    
    public String getUkuran(){
        return ukuran;
    }
    
    public void setUkuran(String ukuran){
        this.ukuran = ukuran;
    }
    
    public String getKombinasi(){
        return kombinasi_warna;
    }
    
    public void setKombinasi(String kombinasi_warna){
        this.kombinasi_warna = kombinasi_warna;
    }
    
    public String getKet(){
        return keterangan;
    }
    
    public void setKet(String keterangan){
        this.keterangan = keterangan;
    }
}
