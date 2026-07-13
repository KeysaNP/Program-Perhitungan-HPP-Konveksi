/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.controller;

import com.mycompany.aplikasihppkonveksi.config.koneksi;
import com.mycompany.aplikasihppkonveksi.model.BahanBakuModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class BahanBakuController {
    private final DecimalFormat df = new DecimalFormat("#");

    public BahanBakuController() {
        df.setMaximumFractionDigits(2); // Menjaga desimal asli jika ada koma
    }
    
    public void tampilData(JTable table){
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("KODE BAHAN");
        model.addColumn("NAMA BAHAN");
        model.addColumn("SATAUAN");
        model.addColumn("QUANTITY BELI");
        model.addColumn("HARGA BELI");
        model.addColumn("HARGA SATUAN");
        
        try{
            Connection conn = koneksi.getKoneksi();
            
            String sql = "SELECT * FROM bahan_baku ";
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_bahan"),
                    rs.getString("nama_bahan"),
                    rs.getString("satuan"),
                    df.format(rs.getDouble("qty_beli")),
                    df.format(rs.getDouble("harga_beli")),
                    df.format(rs.getDouble("harga_satuan")),
                });
            }
            table.setModel(model);
        }catch(Exception e){
            System.out.println("Gagal Menampilkan Data :" + e.getMessage());
            
        }
    }
    
    public double hitungHargaSatuan(double hargaBeli, double qtyBeli){
        if(qtyBeli == 0){
            return 0;
        }
        return hargaBeli / qtyBeli;

    }
    
    public void simpanData(BahanBakuModel model){
        try{
            // Validasi field kosong
            if(model.getKode().trim().isEmpty() ||
               model.getNama().trim().isEmpty() ||
               model.getSatuan().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            //validasi harga beli
            if(model.getHargaBeli() < 0){
            JOptionPane.showMessageDialog(null,
                    "Harga beli tidak boleh negatif!");
            return;
            }
            
            // Hitung otomatis
            model.setHargaSatuan(
                hitungHargaSatuan(
                    model.getHargaBeli(),
                    model.getQtyBeli()
                )
            );
            Connection conn=koneksi.getKoneksi();
            
            String cek = "SELECT kode_bahan FROM bahan_baku WHERE kode_bahan = ?";

            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, model.getKode());

            ResultSet rs = psCek.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(null,
                        "Kode bahan sudah digunakan!");
                return;
            }
            
            //simpan data
            String sql="INSERT INTO bahan_baku (kode_bahan,nama_bahan,satuan,qty_beli,harga_beli,harga_satuan) VALUES (?,?,?,?,?,?)";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKode());
            ps.setString(2, model.getNama());
            ps.setString(3, model.getSatuan());
            ps.setDouble(4, model.getQtyBeli());
            ps.setDouble(5, model.getHargaBeli());
            ps.setDouble(6, model.getHargaSatuan());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data" +e.getMessage());
        }
    }
    
    public void ubahData(BahanBakuModel model){
        try{
            // Validasi field kosong
            if(model.getKode().trim().isEmpty() ||
               model.getNama().trim().isEmpty() ||
               model.getSatuan().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            // Validasi angka (harga beli, qty beli dan harga satuan)
            if(model.getQtyBeli() <= 0){
                JOptionPane.showMessageDialog(null,
                        "Quantity beli harus lebih dari 0!");
                return;
            }

            if(model.getHargaBeli() <= 0){
                JOptionPane.showMessageDialog(null,
                        "Harga beli harus lebih dari 0!");
                return;
            }

            // Hitung otomatis
            model.setHargaSatuan(
                hitungHargaSatuan(
                    model.getHargaBeli(),
                    model.getQtyBeli()
                )
            );
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="UPDATE bahan_baku SET nama_bahan=?,satuan=?,qty_beli=?,harga_beli=?,harga_satuan=? where kode_bahan=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getNama());
            ps.setString(2, model.getSatuan());
            ps.setDouble(3, model.getQtyBeli());
            ps.setDouble(4, model.getHargaBeli());
            ps.setDouble(5, model.getHargaSatuan());
            ps.setString(6, model.getKode());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengubah data" +e.getMessage());
        }
    }
    
    public void hapusData(BahanBakuModel model){
        try{
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="DELETE from bahan_baku where kode_bahan=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
       
            ps.setString(1, model.getKode());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menghapus data" +e.getMessage());
        }
    }
    
    public void cariData(JTable table,String keyword){

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("KODE BAHAN");
        model.addColumn("NAMA BAHAN");
        model.addColumn("SATUAN");
        model.addColumn("QUANTITY BELI");
        model.addColumn("HARGA BELI");
        model.addColumn("HARGA SATUAN");

        try{
            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT * FROM bahan_baku WHERE kode_bahan LIKE ? OR nama_bahan LIKE ? OR satuan LIKE ? OR qty_beli LIKE ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_bahan"),
                    rs.getString("nama_bahan"),
                    rs.getString("satuan"),
                    df.format(rs.getDouble("qty_beli")),
                    df.format(rs.getDouble("harga_beli")),
                    df.format(rs.getDouble("harga_satuan"))
                });
            }

            table.setModel(model);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
