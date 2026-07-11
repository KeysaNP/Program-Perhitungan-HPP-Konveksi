/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author Keysa
 */
public class ItemBom {
    private String kodeBom;
    private String keterangan;

    public ItemBom(String kodeBom, String keterangan) {
        this.kodeBom = kodeBom;
        this.keterangan = keterangan;
    }

    public String getKodeBom() {
        return kodeBom;
    }

    public String getKeterangan() {
        return keterangan;
    }

    @Override
    public String toString() {
        return keterangan;
    }
}
