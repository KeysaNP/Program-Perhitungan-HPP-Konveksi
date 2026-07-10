/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.controller;

import com.mycompany.aplikasihppkonveksi.config.koneksi;
import com.mycompany.aplikasihppkonveksi.model.BomModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class BomController {
    public void tampilData(JTable table){
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("KODE BOM");
        model.addColumn("KODE PRODUK");
        model.addColumn("UKURAN");
        model.addColumn("KOMBINASI WARNA ");
        model.addColumn("KETERANGAN");
        
        try{
            Connection conn = koneksi.getKoneksi();
            
            String sql = "SELECT * FROM bom ";
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
                
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_bom"),
                    rs.getString("kode_produk"),
                    rs.getString("ukuran"),
                    rs.getString("kombinasi_warna"),
                    rs.getString("keterangan"),
                });
            }
            table.setModel(model);
        }catch(Exception e){
            System.out.println("Gagal Menampilkan Data :" + e.getMessage());
            
        }
    }
    
    public void isiComboBoxProduk(javax.swing.JComboBox<String> comboBox) {
        // Kosongkan combo box terlebih dahulu agar data tidak menumpuk saat di-refresh
        comboBox.removeAllItems();
        comboBox.addItem("- Pilih Kode Produk -");

        try {
            Connection conn = koneksi.getKoneksi();
            // Ambil data kode_produk murni dari tabel produk
            String sql = "SELECT kode_produk FROM produk ORDER BY kode_produk ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                // Masukkan kode produk satu per satu ke dalam JComboBox
                comboBox.addItem(rs.getString("kode_produk"));
            }
        } catch (Exception e) {
            System.out.println("Gagal memuat data combo box produk: " + e.getMessage());
        }
    }
    
    public void simpanData(BomModel model){
        try{
            // Validasi field kosong
            if(model.getKodeBom().trim().isEmpty() ||
               model.getKodeProduk().trim().isEmpty() ||
               model.getUkuran().trim().isEmpty()||
               model.getKombinasi().trim().isEmpty()||
               model.getKet().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            String cek = "SELECT kode_bom FROM bom WHERE kode_bom= ?";

            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, model.getKodeBom());

            ResultSet rs = psCek.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(null,
                        "Kode Bom sudah digunakan!");
                return;
            }
            
            //simpan data
            String sql="INSERT INTO bom (kode_bom,kode_produk,ukuran,kombinasi_warna,keterangan) VALUES (?,?,?,?,?)";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKodeBom());
            ps.setString(2, model.getKodeProduk());
            ps.setString(3, model.getUkuran());
            ps.setString(4, model.getKombinasi());
            ps.setString(5, model.getKet());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data" +e.getMessage());
        }
    }
    
    public void ubahData(BomModel model){
        try{
            // Validasi field kosong
            if(model.getKodeBom().trim().isEmpty() ||
               model.getKodeProduk().trim().isEmpty() ||
               model.getUkuran().trim().isEmpty()||
               model.getKombinasi().trim().isEmpty()||
               model.getKet().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="UPDATE bom SET kode_produk=?,ukuran=?,kombinasi_warna=?,keterangan=? where kode_bom=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKodeProduk());
            ps.setString(2, model.getUkuran());
            ps.setString(3, model.getKombinasi());
            ps.setString(4, model.getKet());
            ps.setString(5, model.getKodeBom());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengubah data" +e.getMessage());
        }
    }
    
    public void hapusData(BomModel model){
        try{
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="DELETE from bom where kode_bom=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
       
            ps.setString(1, model.getKodeBom());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menghapus data" +e.getMessage());
        }
    }
    
    public void cariData(JTable table, String field, String keyword){

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("KODE BOM");
        model.addColumn("KODE PRODUK");
        model.addColumn("UKURAN");
        model.addColumn("KOMBINASI WARNA");
        model.addColumn("KETERANGAN");

        try{
            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT * FROM bom WHERE " + field + " LIKE ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_bom"),
                    rs.getString("kode_produk"),
                    rs.getString("ukuran"),
                    rs.getString("kombinasi_warna"),
                    rs.getString("keterangan")
                });
            }

            table.setModel(model);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
