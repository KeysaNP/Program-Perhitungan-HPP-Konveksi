/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class HppModel {
    private String kode_hpp;
    private String kode_produksi;
    private double total_bahan;
    private double total_tenaga_kerja;
    private double total_overhead;
    private double total_hpp;
    private double hpp_per_produk;
    
    public HppModel(){
        
    }
    
    public String getKodeHpp(){
        return kode_hpp;
    }
    
    public void setKodeHpp(String kode_hpp){
        this.kode_hpp = kode_hpp;
    }
    
    public String getKodeProduksi(){
        return kode_produksi;
    }
    
    public void setKodeProduksi(String kode_produksi){
        this.kode_produksi = kode_produksi;
    }
    
    public double getTotalBahan(){
        return total_bahan;
    }
    
    public void setTotalBahan(double total_bahan){
        this.total_bahan = total_bahan;
    }
    
    public double getTtlTk(){
        return total_tenaga_kerja;
    }
    
    public void setTtlTk(double total_tenaga_kerja){
        this.total_tenaga_kerja = total_tenaga_kerja;
    }
    
    public double getTtlOverhead(){
        return total_overhead;
    }
    
    public void setTtlOverhead(double total_overhead){
        this.total_overhead = total_overhead;
    }
    
    public double getTtlHpp(){
        return total_hpp;
    }
    
    public void setTtlHpp(double total_hpp){
        this.total_hpp = total_hpp;
    }
    
    public double getHppPerProduk(){
        return hpp_per_produk;
    }
    
    public void setHppPerProduk(double hpp_per_produk){
        this.hpp_per_produk = hpp_per_produk;
    }
}
