/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.controller;

import com.mycompany.aplikasihppkonveksi.config.koneksi;
import com.mycompany.aplikasihppkonveksi.model.ProdukModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
/**
 *
 * @author Shofa
 */
public class ProdukController {
    public void tampilData(JTable table){
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("KODE PRODUK");
        model.addColumn("NAMA PRODUK");
        model.addColumn("KATEGORI");
        model.addColumn("DESKRIPSI");
        
        try{
            Connection conn = koneksi.getKoneksi();
            
            String sql = "SELECT * FROM produk ";
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
                
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_produk"),
                    rs.getString("nama_produk"),
                    rs.getString("kategori"),
                    rs.getString("deskripsi"),
                });
            }
            table.setModel(model);
        }catch(Exception e){
            System.out.println("Gagal Menampilkan Data :" + e.getMessage());
            
        }
    }
    
    public void simpanData(ProdukModel model){
        try{
            // Validasi field kosong
            if(model.getKode().trim().isEmpty() ||
               model.getNama().trim().isEmpty() ||
               model.getKategori().trim().isEmpty()||
               model.getDeskripsi().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            //validasi kode unik
            String cek = "SELECT kode_produk FROM produk WHERE kode_produk= ?";

            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, model.getKode());

            ResultSet rs = psCek.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(null,
                        "Kode Produk sudah digunakan!");
                return;
            }
            
            //simpan data
            String sql="INSERT INTO produk (kode_produk,nama_produk,kategori,deskripsi) VALUES (?,?,?,?)";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKode());
            ps.setString(2, model.getNama());
            ps.setString(3, model.getKategori());
            ps.setString(4, model.getDeskripsi());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data" +e.getMessage());
        }
    }
    
    public void ubahData(ProdukModel model){
        try{
            // Validasi field kosong
            if(model.getKode().trim().isEmpty() ||
               model.getNama().trim().isEmpty() ||
               model.getKategori().trim().isEmpty()||
               model.getDeskripsi().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="UPDATE produk SET nama_produk=?,kategori=?,deskripsi=? where kode_produk=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getNama());
            ps.setString(2, model.getKategori());
            ps.setString(3, model.getDeskripsi());
            ps.setString(4, model.getKode());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengubah data" +e.getMessage());
        }
    }
    
    public void hapusData(ProdukModel model){
        try{
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="DELETE from produk where kode_produk=?";
            
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

        model.addColumn("KODE PRODUK");
        model.addColumn("NAMA PRODUK");
        model.addColumn("KATEGORI");
        model.addColumn("DESKRIPSI");

        try{
            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT * FROM produk WHERE kode_produk LIKE ? OR nama_produk LIKE ? OR kategori LIKE ? OR deskripsi LIKE ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_produk"),
                    rs.getString("nama_produk"),
                    rs.getString("kategori"),
                    rs.getString("deskripsi")
                });
            }

            table.setModel(model);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
