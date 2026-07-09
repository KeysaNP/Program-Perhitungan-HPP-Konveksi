/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author User
 */
public class DetailBomModel {
    private String kode_detial_bom;
    private String kode_bom;
    private String kode_bahan;
    private String komponen;
    private double qty;
    
    public DetailBomModel(){
        
    }
    
    public String getKodeDtlBom(){
        return kode_detial_bom;
    }
    
    public void setKodeDtlBom(String kode_detial_bom){
        this.kode_detial_bom = kode_detial_bom;
    }
    
    public String getKodeBom(){
        return kode_bom;
    }
    
    public void setKodeBom(String kode_bom){
        this.kode_bom = kode_bom;
    }
    
    public String getKodeBahan(){
        return kode_bahan;
    }
    
    public void setKodeBahan(String kode_bahan){
        this.kode_bahan = kode_bahan;
    }
    
    public String getKomponen(){
        return komponen;
    }
    
    public void setKomponen(String komponen){
        this.komponen = komponen;
    }
    
    public double getQty(){
        return qty;
    }
    
    public void setQty(double qty){
        this.qty = qty;
    }
}
