/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author Keysa
 */
public class ItemProduk {
    private String kodeProduk;
    private String namaProduk;

    public ItemProduk(String kodeProduk, String namaProduk) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    @Override
    public String toString() {
        return namaProduk;
    }
}
