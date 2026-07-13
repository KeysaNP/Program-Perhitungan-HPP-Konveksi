/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.controller;

import com.mycompany.aplikasihppkonveksi.config.koneksi;
import com.mycompany.aplikasihppkonveksi.model.BomModel;
import com.mycompany.aplikasihppkonveksi.model.ItemProduk;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.PreparedStatement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
/**
 *
 * @author User
 */
public class BomController {
    public void tampilData(JTable table){
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("KODE BOM");
        model.addColumn("NAMA PRODUK");
        model.addColumn("UKURAN");
        model.addColumn("KOMBINASI WARNA ");
        model.addColumn("KETERANGAN");
        
        try{
            Connection conn = koneksi.getKoneksi();
            
            String sql = "SELECT b.kode_bom, p.kode_produk,p.nama_produk, b.ukuran, b.kombinasi_warna, b.keterangan FROM bom b JOIN produk p ON b.kode_produk = p.kode_produk";
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
                
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_bom"),
                    rs.getString("nama_produk"),
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
    
    public void loadProduk(JComboBox cmbProduk){
        try{

            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT kode_produk,nama_produk FROM produk ORDER BY nama_produk";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            cmbProduk.removeAllItems();

            while(rs.next()){

                cmbProduk.addItem(
                    new ItemProduk(
                        rs.getString("kode_produk"),
                        rs.getString("nama_produk")
                    )
                );
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
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
    
    public void cariData(JTable table, String keyword){

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("KODE BOM");
        model.addColumn("KODE PRODUK");
        model.addColumn("UKURAN");
        model.addColumn("KOMBINASI WARNA");
        model.addColumn("KETERANGAN");

        try{
            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT * FROM bom WHERE kode_bom LIKE ? OR kode_produk LIKE ? OR ukuran LIKE ? OR kombinasi_warna LIKE ? OR keterangan LIKE ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);
            ps.setString(5, searchKey);

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
    
    public void pilihProduk(JComboBox cbProduk, String namaProduk) {

        for (int i = 0; i < cbProduk.getItemCount(); i++) {

            ItemProduk item = (ItemProduk) cbProduk.getItemAt(i);

            if (item.getNamaProduk().equals(namaProduk)) {
                cbProduk.setSelectedIndex(i);
                break;
            }
        }
    }
}
