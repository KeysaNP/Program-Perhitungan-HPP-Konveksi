/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class ProdukModel {
    private String kode_produk;
    private String nama_produk;
    private String kategori;
    private String deskripsi;
    
    public ProdukModel(){
        
    }
    
    public String getKode(){
        return kode_produk;
    }
    
    public void setKode(String kode_produk){
        this.kode_produk = kode_produk;
    }
    
    public String getNama(){
        return nama_produk;
    }
    
    public void setNama(String nama_produk){
        this.nama_produk = nama_produk;
    }
    
    public String getKategori(){
        return kategori;
    }
    
    public void setKategori(String kategori){
        this.kategori = kategori;
    }
    
    public String getDeskripsi(){
        return deskripsi;
    }
    
    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }
    
}
