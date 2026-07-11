/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.controller;

import com.mycompany.aplikasihppkonveksi.config.koneksi;
import com.mycompany.aplikasihppkonveksi.model.DetailBomModel;
import com.mycompany.aplikasihppkonveksi.model.ItemBom;
import com.mycompany.aplikasihppkonveksi.model.ItemBahan;
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
 * @author Keysa
 */
public class BomDetailController {
    public void tampilData(JTable table){
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("KODE DETAIL BOM");
        model.addColumn("BOM (Bill of Materials)");
        model.addColumn("Nama Bahan");
        model.addColumn("KOMPONEN");
        model.addColumn("QUANTITY");
        
        try{
            Connection conn = koneksi.getKoneksi();
            
            String sql = "SELECT a.kode_detail_bom, b.kode_bom, b.keterangan, p.kode_bahan,p.nama_bahan, a.komponen, a.qty FROM detail_bom a JOIN bom b ON a.kode_bom = b.kode_bom JOIN bahan_baku p ON a.kode_bahan = p.kode_bahan";
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery(sql);
                
            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_detail_bom"),
                    rs.getString("keterangan"),
                    rs.getString("nama_bahan"),
                    rs.getString("komponen"),
                    rs.getString("qty"),
                });
            }
            table.setModel(model);
        }catch(Exception e){
            System.out.println("Gagal Menampilkan Data :" + e.getMessage());
            
        }
    }
    
    public void loadBahan(JComboBox cbCariDetailBom){
        try{

            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT kode_bahan,nama_bahan FROM bahan_baku ORDER BY nama_bahan";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            cbCariDetailBom.removeAllItems();

            while(rs.next()){

                cbCariDetailBom.addItem(
                    new ItemBahan(
                        rs.getString("kode_bahan"),
                        rs.getString("nama_bahan")
                    )
                );
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal Load Bahan"+e.getMessage());
        }
    }
    
    public void loadBom(JComboBox cbCariDetailBom){
        try{

            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT kode_bom,keterangan FROM bom ORDER BY keterangan";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            cbCariDetailBom.removeAllItems();

            while(rs.next()){

                cbCariDetailBom.addItem(
                    new ItemBom(
                        rs.getString("kode_bom"),
                        rs.getString("keterangan")
                    )
                );
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal Load BOM"+e.getMessage());
        }
    }
    
    public void simpanData(DetailBomModel model){
        try{
            // Validasi field kosong
            if(model.getKodeDtlBom().trim().isEmpty() ||
               model.getKodeBom().trim().isEmpty() ||
               model.getKodeBahan().trim().isEmpty()||
               model.getKomponen().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            //validasi qty
            if (model.getQty() <= 0) {
                JOptionPane.showMessageDialog(null, "Qty harus lebih dari 0!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            String cek = "SELECT kode_detail_bom FROM detail_bom WHERE kode_detail_bom= ?";

            PreparedStatement psCek = conn.prepareStatement(cek);
            psCek.setString(1, model.getKodeDtlBom());

            ResultSet rs = psCek.executeQuery();

            if(rs.next()){
                JOptionPane.showMessageDialog(null,
                        "Kode detail Bom sudah digunakan!");
                return;
            }
            
            //simpan data
            String sql="INSERT INTO detail_bom (kode_detail_bom,kode_bom,kode_bahan,komponen,qty) VALUES (?,?,?,?,?)";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKodeDtlBom());
            ps.setString(2, model.getKodeBom());
            ps.setString(3, model.getKodeBahan());
            ps.setString(4, model.getKomponen());
            ps.setDouble(5, model.getQty());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data" +e.getMessage());
        }
    }
    
    public void ubahData(DetailBomModel model){
        try{
            // Validasi field kosong
            if(model.getKodeDtlBom().trim().isEmpty() ||
               model.getKodeBom().trim().isEmpty() ||
               model.getKodeBahan().trim().isEmpty()||
               model.getKomponen().trim().isEmpty()){

                JOptionPane.showMessageDialog(null,
                        "Semua field harus diisi!");
                return;
            }
            
            //validasi qty
            if (model.getQty() <= 0) {
                JOptionPane.showMessageDialog(null, "Qty harus lebih dari 0!");
                return;
            }
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="UPDATE detail_bom SET kode_bom=?, kode_bahan=?, komponen=?, qty=? WHERE kode_detail_bom=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
            
            ps.setString(1, model.getKodeBom());      
            ps.setString(2, model.getKodeBahan());     
            ps.setString(3, model.getKomponen());     
            ps.setDouble(4, model.getQty());           
            ps.setString(5, model.getKodeDtlBom());  
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal mengubah data" +e.getMessage());
        }
    }
    
    public void hapusData(DetailBomModel model){
        try{
            
            Connection conn=koneksi.getKoneksi();
            
            String sql="DELETE from detail_bom where kode_detail_bom=?";
            
            PreparedStatement ps=conn.prepareStatement(sql);
       
            ps.setString(1, model.getKodeDtlBom());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Gagal menghapus data" +e.getMessage());
        }
    }
    
    public void cariData(JTable table, String keyword){

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("KODE DETAIL BOM");
        model.addColumn("BOM (Bill of Materials)");
        model.addColumn("NAMA BAHAN");
        model.addColumn("KOMPONEN");
        model.addColumn("QUANTITY");

        try{
            Connection conn = koneksi.getKoneksi();

            String sql = "SELECT a.kode_detail_bom, b.kode_bom, b.keterangan, p.kode_bahan, p.nama_bahan, a.komponen, a.qty " +
                     "FROM detail_bom a " + 
                     "JOIN bom b ON a.kode_bom = b.kode_bom " + 
                     "JOIN bahan_baku p ON a.kode_bahan = p.kode_bahan " + 
                     "WHERE a.kode_detail_bom LIKE ? " +
                     "OR b.keterangan LIKE ? " +
                     "OR p.nama_bahan LIKE ? " +
                     "OR a.komponen LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";
            
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                    rs.getString("kode_detail_bom"),
                    rs.getString("keterangan"),
                    rs.getString("nama_bahan"),
                    rs.getString("komponen"),
                    rs.getString("qty")
                });
            }

            table.setModel(model);

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void pilihBom(JComboBox cbKodeBOM, String namaBom) {

        for (int i = 0; i < cbKodeBOM.getItemCount(); i++) {

            ItemBom item = (ItemBom) cbKodeBOM.getItemAt(i);

            if (item.getKeterangan().equals(namaBom)) {
                cbKodeBOM.setSelectedIndex(i);
                break;
            }
        }
    }
    
    public void pilihBahan(JComboBox cbKodeBahan, String namaBahan) {

        for (int i = 0; i < cbKodeBahan.getItemCount(); i++) {

            ItemBahan item = (ItemBahan) cbKodeBahan.getItemAt(i);

            if (item.getNamaBahan().equals(namaBahan)) {
                cbKodeBahan.setSelectedIndex(i);
                break;
            }
        }
    }
}
