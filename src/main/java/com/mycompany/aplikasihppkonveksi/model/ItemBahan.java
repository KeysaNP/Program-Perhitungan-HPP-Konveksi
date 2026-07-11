/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.model;

/**
 *
 * @author Keysa
 */
public class ItemBahan {
    private String kodeBahan;
    private String namaBahan;

    public ItemBahan(String kodeBahan, String namaBahan) {
        this.kodeBahan = kodeBahan;
        this.namaBahan = namaBahan;
    }

    public String getKodeBahan() {
        return kodeBahan;
    }

    public String getNamaBahan() {
        return namaBahan;
    }

    @Override
    public String toString() {
        return namaBahan;
    }
}
