/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.controller;

import com.mycompany.aplikasihppkonveksi.config.koneksi;
import com.mycompany.aplikasihppkonveksi.model.TkModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author LOQ
 */
public class TkController {
     public void tampildata (JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("KODE TK");
        model.addColumn("NAMA PROSES");
        model.addColumn("BIAYA HARIAN");
        
        try{
            Connection conn = koneksi.getKoneksi();
            
            String sql = "SELECT * FROM tenaga_kerja ";
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
                
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_tk"),
                    rs.getString("nama_proses"),
                    rs.getDouble("biaya_harian"),
                });
            }
            table.setModel(model);
        }catch(Exception e){
            System.out.println("Gagal Menampilkan Data :" + e.getMessage());
        }
    }
     
     public void simpanData(TkModel model){
        try{
            if (!model.getKode().matches("^TK\\d{3}$")) {
                JOptionPane.showMessageDialog(null,
                        "Kode overhead harus berformat TK001, TK002, TK003, dst.");
                return;
                
            }
            // Validasi field kosong
            if(model.getKode().trim().isEmpty() ||
               model.getNama().trim().isEmpty() ||
               model.getBiaya() == 0 )
            {

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            //validasi kode unik
            String cek = "SELECT kode_tk FROM tenaga_kerja WHERE kode_tk = ?";

            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, model.getKode());

            ResultSet rs = psCek.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(null,
                        "Kode Tenaga Kerjasudah digunakan!");
                return;
            }
            
            //simpan data
            String sql="INSERT INTO tenaga_kerja (kode_tk,nama_proses,biaya_harian) VALUES (?,?,?)";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKode());
            ps.setString(2, model.getNama());
            ps.setDouble (3, model.getBiaya());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data" +e.getMessage());
        }
    }
     
      public void ubahData(TkModel model){
        try{
            // Validasi field kosong
            if(model.getKode().trim().isEmpty() ||
               model.getNama().trim().isEmpty() ||
               model.getBiaya() == 0 ){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="UPDATE tenaga_kerja SET nama_proses=?,biaya_harian=? where kode_tk=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getNama());
            ps.setDouble (2, model.getBiaya());
            ps.setString(3, model.getKode());
            
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengubah data" +e.getMessage());
        }
    }
      
    public void hapusData(TkModel model){
        try{
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="DELETE from tenaga_kerja where kode_tk=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
       
            ps.setString(1, model.getKode());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menghapus data" +e.getMessage());
        }
    }
    
    public void cariData(JTable table , String keyword){

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("KODE TENAGA KERJA");
        model.addColumn("NAMA PROSES");
        model.addColumn("BIAYA HARIAN");

        try{
            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT * FROM tenaga_kerja WHERE kode_tk LIKE ? OR nama_proses LIKE ? OR biaya_harian LIKE ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_tk"),
                    rs.getString("nama_proses"),
                    rs.getDouble("biaya_harian")
                });
            }

            table.setModel(model);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
     
