/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.aplikasihppkonveksi.views;
//buat ngegerakin jamnya (realtime)
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.aplikasihppkonveksi.controller.BahanBakuController;
import com.mycompany.aplikasihppkonveksi.controller.BomController;
import com.mycompany.aplikasihppkonveksi.controller.BomDetailController;
import com.mycompany.aplikasihppkonveksi.controller.ProdukController;
import com.mycompany.aplikasihppkonveksi.model.BahanBakuModel;
import com.mycompany.aplikasihppkonveksi.model.BomModel;
import com.mycompany.aplikasihppkonveksi.model.DetailBomModel;
import com.mycompany.aplikasihppkonveksi.model.ItemProduk;
import com.mycompany.aplikasihppkonveksi.model.ItemBahan;
import com.mycompany.aplikasihppkonveksi.model.ItemBom;
import com.mycompany.aplikasihppkonveksi.model.ProdukModel;
import java.text.DecimalFormat;
/**
 *
 * @author ZHILLAN
 */
public class MainMenuView extends javax.swing.JFrame {
    private BahanBakuController controller = new BahanBakuController();
    private ProdukController produkcontroller = new ProdukController();
    private BomController bomcontroller = new BomController();
    private BomDetailController dtlbomcontroller = new BomDetailController();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainMenuView.class.getName());

    /**
     * Creates new form MainMenuView
     */
    public MainMenuView() {
    initComponents();
        this.setLocationRelativeTo(null); // biar center(tampil ditengah)
        panelSubMaster.setVisible(false);
        panelSubTransaksi.setVisible(false);

        //semyembunyikan tab atas
        jTabbedPane1.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int runCount, int maxTabHeight) {
                return 0;
            }
        });

        //BUAT JAMNYA JALAN JANGAN DIHAPUS
        javax.swing.Timer timer = new javax.swing.Timer(1000, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMM yyyy | HH:mm:ss", new java.util.Locale("id", "ID"));
                lblwaktu.setText(sdf.format(new java.util.Date()));
            }
        });
        timer.start();

        //Panggil data
        initDashboard();
        //initMasterProduk();
        initMasterBahanBaku();
        initMasterBOM();
        initDetailBOM();
        initMasterTenagaKerja();
        initMasterOverhead();
        initTransaksiProduksi();
        initPerhitunganHPP();
        
        
        //memanggil controller
        controller.tampilData(tblBahanBaku);
        produkcontroller.tampilData(tblProduk);
        bomcontroller.tampilData(tblBOM);
        dtlbomcontroller.tampilData(tblDetailBOM);
        
        //menampilkan jumlah data
        lblTotalBahan.setText("Total Data : " + tblBahanBaku.getRowCount());
        lblTotalDataProduk.setText("Total Data : " + tblProduk.getRowCount());
        lblTotalBOM.setText("Total Data : " + tblBOM.getRowCount());
        lblTotalDtlBom.setText("Total Data : " + tblDetailBOM.getRowCount());
        
        //inisialisasi untuk combo box cari di bahan baku
        cbBahanBaku.removeAllItems();
        cbBahanBaku.addItem("Kode Bahan");
        cbBahanBaku.addItem("Nama Bahan");
        cbBahanBaku.addItem("Satuan");
        cbBahanBaku.addItem("Harga Beli");
        
        //combobox 
        bomcontroller.loadProduk(cbProdukBOM);
        dtlbomcontroller.loadBom(cbKodeBOM);
        dtlbomcontroller.loadBahan(cbKodeBahan);
        
        
    }
    
    //method untuk atur form kode 
    private void aturForm(boolean status){
        txtKodeBahan.setEditable(status);
        txtKodeProduk.setEditable(status);
        txtKodeBOM.setEditable(status);
    }
    
    

    private void hitungHargaSatuanOtomatis() {
        try {
            String hargaStr = txtHargaBeli.getText().trim();
            String qtyStr = txtQtyBeli.getText().trim();

            // Cek apakah kedua field sudah diisi dan tidak kosong
            if (!hargaStr.isEmpty() && !qtyStr.isEmpty()) {
                double hargaBeli = Double.parseDouble(hargaStr);
                double qtyBeli = Double.parseDouble(qtyStr);

                // Validasi agar tidak terjadi eror pembagian dengan nol (0)
                if (qtyBeli != 0) {
                    double hargaSatuan = controller.hitungHargaSatuan(hargaBeli, qtyBeli);
                    //ini untuk mengunci data & tidak berbentuk desimal
                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(2);
                    txtHargaSatuan.setText(String.valueOf(hargaSatuan));
                } else {
                    txtHargaSatuan.setText("0");
                }
            } else {
                txtHargaSatuan.setText(""); // Kosongkan jika salah satu form belum diisi
            }
        } catch (NumberFormatException e) {
            // Jika user mengetik huruf, abaikan saja atau set 0 agar tidak crash
            txtHargaSatuan.setText("0");
        }
    }
    
    //METHOD INIT DASHBOARD
    private void initDashboard() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode Produksi");
        model.addColumn("Produk");
        model.addColumn("Jumlah");
        model.addColumn("Tanggal");
        
        tblProduksiTerakhir.setModel(model);
        
        model.addRow(new Object[]{"PRD-0005", "POH", "100", "08/07/2026"});
        model.addRow(new Object[]{"PRD-0004", "Kaos", "200", "07/07/2026"});
        model.addRow(new Object[]{"PRD-0003", "Hoodie", "50", "06/07/2026"});
        model.addRow(new Object[]{"PRD-0002", "Jaket", "30", "05/07/2026"});
        model.addRow(new Object[]{"PRD-0001", "Vest", "40", "04/07/2026"});
    }

    //METHOD INIT MASTER PRODUK
    private void initMasterProduk() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode Produk");
        model.addColumn("Nama Produk");
        model.addColumn("Kategori");
        model.addColumn("Deskripsi");
        
        tblProduk.setModel(model);
        
        //model.addRow(new Object[]{"PRD001", "PDH", "Pakaian", "Pakaian Dinas Harian"});
        //model.addRow(new Object[]{"PRD002", "Kaos", "Pakaian", "Kaos Combed 24s"});
        //model.addRow(new Object[]{"PRD003", "Jaket", "Pakaian", "Jaket Drill"});
        //model.addRow(new Object[]{"PRD004", "Hoodie", "Pakaian", "Hoodie Fleece"});
        //model.addRow(new Object[]{"PRD005", "Vest", "Pakaian", "Vest Lapangan"});
        
        //lblTotalDataProduk.setText("Total Data : " + model.getRowCount());
    }
    
    //METHOD INIT MASTER BAHAN BAKU
    private void initMasterBahanBaku() {
        // Membuat judul kolom tabel
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode Bahan");
        model.addColumn("Nama Bahan");
        model.addColumn("Satuan");
        model.addColumn("Qty Beli");
        model.addColumn("Harga Beli");
        model.addColumn("Harga/Satuan");
        
        // Memasang kolom ke tabel di layar
        tblBahanBaku.setModel(model);
    }
    
    //METHOD INIT  MASTER BOM
    private void initMasterBOM() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode BOM");
        model.addColumn("Produk");
        model.addColumn("Ukuran");
        model.addColumn("Kombinasi Warna");
        model.addColumn("Keterangan");
        
        tblBOM.setModel(model);
    }
    
    //METHOD INIT DETAIL BOM
    private void initDetailBOM() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode Detail");
        model.addColumn("Kode BOM");
        model.addColumn("Nama Bahan");
        model.addColumn("Komponen");
        model.addColumn("Qty");
        model.addColumn("Satuan");
        model.addColumn("Harga");
        model.addColumn("Subtotal");
        
        tblDetailBOM.setModel(model);
    }
    
    //METHOD INIT MASTER TENAGA KERJA
    private void initMasterTenagaKerja() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode Pekerja");
        model.addColumn("Nama Pekerja");
        model.addColumn("Posisi");
        model.addColumn("Gaji Pokok");
        model.addColumn("Tunjangan");
        
        tblTenagaKerja.setModel(model);
    }
    
    //METHOD INIT MASTER OVERHEAD
    private void initMasterOverhead() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        model.addColumn("Kode Overhead");
        model.addColumn("Nama Biaya");
        model.addColumn("Kategori");
        model.addColumn("Nominal Biaya");
        model.addColumn("Keterangan");
        
        tblOverhead.setModel(model);
    }
    
    //LOGIKA TOMBOL MASTER (EFEK ACCORDION)
    private void btnMasterActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // 1. PINDAH HALAMAN: Langsung buka tab Master Produk (Index ke-1)
        jTabbedPane1.setSelectedIndex(0);
        
        // 2. LOGIKA ANIMASI: Buka-tutup panel sub-menu dan dorong panel bawah
        if (!panelSubMaster.isVisible()) {
            panelSubMaster.setVisible(true);
            
            int x = panelSisaMenu.getX();
            int y = panelSisaMenu.getY();
            panelSisaMenu.setLocation(x, y + panelSubMaster.getHeight());
        } 
        else {
            panelSubMaster.setVisible(false);
            
            int x = panelSisaMenu.getX();
            int y = panelSisaMenu.getY();
            panelSisaMenu.setLocation(x, y - panelSubMaster.getHeight());
        }
    }
    
    private void initTransaksiProduksi() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        
        model.addColumn("Kode Produksi");
        model.addColumn("Tanggal");
        model.addColumn("Produk");
        model.addColumn("Ukuran");
        model.addColumn("Jumlah");
        model.addColumn("Customer");
        
        tblTransaksiProduksi.setModel(model);
        
        if (lblTotalProduksi != null) {
            lblTotalProduksi.setText("Total Data : " + model.getRowCount());
        }
    }
    
    private void initPerhitunganHPP() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        
        model.addColumn("Kode HPP");
        model.addColumn("Kode Produksi");
        model.addColumn("Produk");
        model.addColumn("Jumlah");
        model.addColumn("Total HPP");
        model.addColumn("HPP / Pcs");
        model.addColumn("Tanggal");
        
        tblHPP.setModel(model);
        
        if (lblTotalDataHPP != null) {
            lblTotalDataHPP.setText("Total Data : " + model.getRowCount());
        }
    }
    
    @SuppressWarnings("unchecked")
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnDashboard1 = new javax.swing.JButton();
        btnTransaksi = new javax.swing.JButton();
        panelSubMaster = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        panelSubTransaksi = new javax.swing.JPanel();
        btnTransaksiProduksi = new javax.swing.JButton();
        panelSisaMenu = new javax.swing.JPanel();
        btnMasterProduk2 = new javax.swing.JButton();
        btnMasterProduk3 = new javax.swing.JButton();
        btnMasterProduk4 = new javax.swing.JButton();
        btnMasterProduk1 = new javax.swing.JButton();
        btnMasterProduk = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblwaktu = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lblTotalProduk = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lblProduksi = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lblHPP = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        lblTotalBahanBaku = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduksiTerakhir = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtKodeProduk = new javax.swing.JTextField();
        txtNamaProduk = new javax.swing.JTextField();
        cbKategori = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        txtDeskripsi = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        cbCariProduk = new javax.swing.JComboBox<>();
        txtCariProduk = new javax.swing.JTextField();
        btnCariProduk = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProduk = new javax.swing.JTable();
        lblTotalDataProduk = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtKodeBahan = new javax.swing.JTextField();
        txtNamaBahan = new javax.swing.JTextField();
        txtQtyBeli = new javax.swing.JTextField();
        txtHargaBeli = new javax.swing.JTextField();
        txtHargaSatuan = new javax.swing.JTextField();
        cbSatuan = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        btnTambahBahan = new javax.swing.JButton();
        btnSimpanBahan = new javax.swing.JButton();
        btnUbahBahan = new javax.swing.JButton();
        btnHapusBahan = new javax.swing.JButton();
        btnResetBahan = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        cbBahanBaku = new javax.swing.JComboBox<>();
        txtCariBB = new javax.swing.JTextField();
        btnCariBB = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBahanBaku = new javax.swing.JTable();
        lblTotalBahan = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        cbUkuranBOM = new javax.swing.JComboBox<>();
        txtKodeBOM = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtKetaranganBOM = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        cbProdukBOM = new javax.swing.JComboBox<>();
        txtKombinasiWarna = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        btnTambahBOM = new javax.swing.JButton();
        btnSimpanBOM = new javax.swing.JButton();
        btnUbahBOM = new javax.swing.JButton();
        btnHapusBOM = new javax.swing.JButton();
        btnResetBOM = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        cbCariBom = new javax.swing.JComboBox<>();
        txtCariBom = new javax.swing.JTextField();
        btnCariBom = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBOM = new javax.swing.JTable();
        lblTotalBOM = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        cbKodeBahan = new javax.swing.JComboBox<>();
        cbKodeBOM = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtKodeDetail = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtKomponen = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtSatuanDetail = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtHargaDetail = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        btnTambahDtlBOM = new javax.swing.JButton();
        btnSimpanDtlBOM = new javax.swing.JButton();
        btnUbahDtlBOM = new javax.swing.JButton();
        btnHapusDtlBOM = new javax.swing.JButton();
        btnResetDtlBOM = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        cbCariDetailBom = new javax.swing.JComboBox<>();
        txtCariDtlBom = new javax.swing.JTextField();
        btnCariDtlBom = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDetailBOM = new javax.swing.JTable();
        lblTotalDtlBom = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtKodePekerja = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txtBiayaHari = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        btnTambahTK = new javax.swing.JButton();
        btnSimpanTK = new javax.swing.JButton();
        btnUbahTK = new javax.swing.JButton();
        btnHapusTK = new javax.swing.JButton();
        btnResetTK = new javax.swing.JButton();
        cbPosisi = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jTextField10 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblTenagaKerja = new javax.swing.JTable();
        lblTotalPekerja = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtKodeOverhead = new javax.swing.JTextField();
        txtNamaBiaya = new javax.swing.JTextField();
        cbKategoriOverhead = new javax.swing.JComboBox<>();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtKeteranganOverhead = new javax.swing.JTextArea();
        txtBiayaOverhead = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        btnTambahOverhead = new javax.swing.JButton();
        btnSimpanOverhead = new javax.swing.JButton();
        btnUbahOverhead = new javax.swing.JButton();
        btnHapusOverhead = new javax.swing.JButton();
        btnResetOverhead = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jTextField11 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblOverhead = new javax.swing.JTable();
        lblTotalOverhead = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtJumlahProduksi = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        txtTanggalProduksi = new javax.swing.JTextField();
        txtNoProduksi = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        txtUkuran = new javax.swing.JTextField();
        txtProdukTransaksi = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        cbBOMProduksi = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        chkPotong = new javax.swing.JCheckBox();
        chkJahit = new javax.swing.JCheckBox();
        chkBordir = new javax.swing.JCheckBox();
        chkAksesoris = new javax.swing.JCheckBox();
        chkQc = new javax.swing.JCheckBox();
        chkFinishing = new javax.swing.JCheckBox();
        btnHitungEstimasi = new javax.swing.JButton();
        btnSimpanProduksi = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblTransaksiProduksi = new javax.swing.JTable();
        lblTotalProduksi = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        txtUkuranHPP = new javax.swing.JTextField();
        txtTanggalHPP = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtJumlahHPP = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        cbKodeProduksiHPP = new javax.swing.JComboBox<>();
        txtProdukHPP = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        txtTotalTenagaKerja = new javax.swing.JTextField();
        txtTotalOverhead = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        txtTotalBahanBaku = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        lblTotalHPP = new javax.swing.JLabel();
        lblHPPPcs = new javax.swing.JLabel();
        btnSimpanHPP = new javax.swing.JButton();
        btnResetHPP = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblHPP = new javax.swing.JTable();
        lblTotalDataHPP = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(933, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(20, 20, 20));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Microsoft New Tai Lue", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("UNIKOM");

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("HPP Konveksi");

        btnDashboard1.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard1.setText("Dashboard");
        btnDashboard1.setBorderPainted(false);
        btnDashboard1.setContentAreaFilled(false);
        btnDashboard1.setFocusPainted(false);
        btnDashboard1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDashboard1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDashboard1MouseExited(evt);
            }
        });
        btnDashboard1.addActionListener(this::btnDashboard1ActionPerformed);

        btnTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        btnTransaksi.setText("Transaksi");
        btnTransaksi.setBorderPainted(false);
        btnTransaksi.setContentAreaFilled(false);
        btnTransaksi.setFocusPainted(false);
        btnTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTransaksiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTransaksiMouseExited(evt);
            }
        });
        btnTransaksi.addActionListener(this::btnTransaksiActionPerformed);

        panelSubMaster.setOpaque(false);

        jButton1.setText("Produk");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Bahan Baku");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("BOM");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("Detail Bom");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setText("Tenaga Kerja");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setText("Overhead");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        javax.swing.GroupLayout panelSubMasterLayout = new javax.swing.GroupLayout(panelSubMaster);
        panelSubMaster.setLayout(panelSubMasterLayout);
        panelSubMasterLayout.setHorizontalGroup(
            panelSubMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSubMasterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelSubMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelSubMasterLayout.setVerticalGroup(
            panelSubMasterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSubMasterLayout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6))
        );

        panelSubTransaksi.setOpaque(false);

        btnTransaksiProduksi.setText("Produksi");
        btnTransaksiProduksi.addActionListener(this::btnTransaksiProduksiActionPerformed);

        javax.swing.GroupLayout panelSubTransaksiLayout = new javax.swing.GroupLayout(panelSubTransaksi);
        panelSubTransaksi.setLayout(panelSubTransaksiLayout);
        panelSubTransaksiLayout.setHorizontalGroup(
            panelSubTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSubTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTransaksiProduksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSubTransaksiLayout.setVerticalGroup(
            panelSubTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTransaksiProduksi, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        panelSisaMenu.setOpaque(false);

        btnMasterProduk2.setForeground(new java.awt.Color(255, 255, 255));
        btnMasterProduk2.setText("Laporan");
        btnMasterProduk2.setBorderPainted(false);
        btnMasterProduk2.setContentAreaFilled(false);
        btnMasterProduk2.setFocusPainted(false);
        btnMasterProduk2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMasterProduk2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMasterProduk2MouseExited(evt);
            }
        });
        btnMasterProduk2.addActionListener(this::btnMasterProduk2ActionPerformed);

        btnMasterProduk3.setForeground(new java.awt.Color(255, 255, 255));
        btnMasterProduk3.setText("Pengaturan");
        btnMasterProduk3.setBorderPainted(false);
        btnMasterProduk3.setContentAreaFilled(false);
        btnMasterProduk3.setFocusPainted(false);
        btnMasterProduk3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMasterProduk3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMasterProduk3MouseExited(evt);
            }
        });
        btnMasterProduk3.addActionListener(this::btnMasterProduk3ActionPerformed);

        btnMasterProduk4.setForeground(new java.awt.Color(255, 255, 255));
        btnMasterProduk4.setText("Logout");
        btnMasterProduk4.setBorderPainted(false);
        btnMasterProduk4.setContentAreaFilled(false);
        btnMasterProduk4.setFocusPainted(false);
        btnMasterProduk4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMasterProduk4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMasterProduk4MouseExited(evt);
            }
        });
        btnMasterProduk4.addActionListener(this::btnMasterProduk4ActionPerformed);

        btnMasterProduk1.setForeground(new java.awt.Color(255, 255, 255));
        btnMasterProduk1.setText("HPP");
        btnMasterProduk1.setBorderPainted(false);
        btnMasterProduk1.setContentAreaFilled(false);
        btnMasterProduk1.setFocusPainted(false);
        btnMasterProduk1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMasterProduk1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMasterProduk1MouseExited(evt);
            }
        });
        btnMasterProduk1.addActionListener(this::btnMasterProduk1ActionPerformed);

        javax.swing.GroupLayout panelSisaMenuLayout = new javax.swing.GroupLayout(panelSisaMenu);
        panelSisaMenu.setLayout(panelSisaMenuLayout);
        panelSisaMenuLayout.setHorizontalGroup(
            panelSisaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnMasterProduk2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnMasterProduk3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnMasterProduk4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnMasterProduk1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelSisaMenuLayout.setVerticalGroup(
            panelSisaMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSisaMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMasterProduk1)
                .addGap(12, 12, 12)
                .addComponent(btnMasterProduk2)
                .addGap(18, 18, 18)
                .addComponent(btnMasterProduk3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btnMasterProduk4)
                .addGap(12, 12, 12))
        );

        btnMasterProduk.setForeground(new java.awt.Color(255, 255, 255));
        btnMasterProduk.setText("Master");
        btnMasterProduk.setBorderPainted(false);
        btnMasterProduk.setContentAreaFilled(false);
        btnMasterProduk.setFocusPainted(false);
        btnMasterProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMasterProdukMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMasterProdukMouseExited(evt);
            }
        });
        btnMasterProduk.addActionListener(this::btnMasterProdukActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDashboard1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSubMaster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSubTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSisaMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMasterProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(btnDashboard1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMasterProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSubMaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSubTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSisaMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 600));

        jPanel2.setBackground(new java.awt.Color(34, 34, 34));

        jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(36, 53, 71));
        jPanel3.setPreferredSize(new java.awt.Dimension(760, 565));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Selamat Datang, ADMIN");

        lblwaktu.setForeground(new java.awt.Color(153, 153, 153));
        lblwaktu.setText("Selasa, 09 Juli 2026  | 01.22.09");

        jPanel11.setBackground(new java.awt.Color(36, 53, 71));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel11.setForeground(new java.awt.Color(255, 255, 255));

        lblTotalProduk.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTotalProduk.setForeground(new java.awt.Color(225, 225, 0));
        lblTotalProduk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalProduk.setText("11");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Produk");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblTotalProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalProduk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(36, 53, 71));
        jPanel10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel10.setForeground(new java.awt.Color(255, 255, 255));

        lblProduksi.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblProduksi.setForeground(new java.awt.Color(225, 225, 0));
        lblProduksi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProduksi.setText("5");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Produksi");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProduksi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(36, 53, 71));
        jPanel12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel12.setForeground(new java.awt.Color(255, 255, 255));

        lblHPP.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblHPP.setForeground(new java.awt.Color(225, 225, 0));
        lblHPP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHPP.setText("5");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("HPP");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHPP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(36, 53, 71));
        jPanel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel13.setForeground(new java.awt.Color(255, 255, 255));

        lblTotalBahanBaku.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTotalBahanBaku.setForeground(new java.awt.Color(225, 225, 0));
        lblTotalBahanBaku.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalBahanBaku.setText("31");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Bahan Baku");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblTotalBahanBaku, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalBahanBaku)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Dashboard");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Produksi Terakhir");

        tblProduksiTerakhir.setBackground(new java.awt.Color(36, 53, 71));
        tblProduksiTerakhir.setForeground(new java.awt.Color(255, 255, 255));
        tblProduksiTerakhir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Produksi", "Produk", "Jumlah", "Tanggal"
            }
        ));
        tblProduksiTerakhir.setSelectionBackground(new java.awt.Color(102, 102, 102));
        tblProduksiTerakhir.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tblProduksiTerakhir);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("X");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblwaktu, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(34, 34, 34))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblwaktu, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(34, 34, 34)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(152, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("TabDashboard", jPanel3);

        jPanel4.setBackground(new java.awt.Color(36, 53, 71));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Master Produk");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("X");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Kode Produk");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Nama Produk");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Kategori");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Deksripsi");

        cbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seragam", "Pakaian", "Outer", "Formal", "Aksesoris", "Merchandise" }));
        cbKategori.addActionListener(this::cbKategoriActionPerformed);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDeskripsi)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDeskripsi, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
        );

        jPanel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel15.setOpaque(false);

        btnTambah.setBackground(new java.awt.Color(255, 204, 0));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambah.setText("+ Tambah");
        btnTambah.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnTambah.setFocusPainted(false);
        btnTambah.addActionListener(this::btnTambahActionPerformed);

        btnSimpan.setBackground(new java.awt.Color(255, 204, 0));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setFocusPainted(false);
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);

        btnUbah.setBackground(new java.awt.Color(102, 102, 102));
        btnUbah.setForeground(new java.awt.Color(255, 255, 255));
        btnUbah.setText("Ubah");
        btnUbah.setFocusPainted(false);
        btnUbah.addActionListener(this::btnUbahActionPerformed);

        btnHapus.setBackground(new java.awt.Color(204, 0, 51));
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("- Hapus");
        btnHapus.setFocusPainted(false);
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        btnReset.setBackground(new java.awt.Color(102, 102, 102));
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setText("Reset");
        btnReset.setFocusPainted(false);
        btnReset.addActionListener(this::btnResetActionPerformed);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUbah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Pencarian");

        cbCariProduk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Produk", "Nama Produk", "Kategori" }));

        txtCariProduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariProdukKeyReleased(evt);
            }
        });

        btnCariProduk.setBackground(new java.awt.Color(255, 204, 0));
        btnCariProduk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCariProduk.setText("Cari");
        btnCariProduk.addActionListener(this::btnCariProdukActionPerformed);

        tblProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Produk", "Nama Produk", "Kategori", "Deskripsi"
            }
        ));
        tblProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdukMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblProduk);
        if (tblProduk.getColumnModel().getColumnCount() > 0) {
            tblProduk.getColumnModel().getColumn(0).setResizable(false);
            tblProduk.getColumnModel().getColumn(1).setResizable(false);
            tblProduk.getColumnModel().getColumn(2).setResizable(false);
            tblProduk.getColumnModel().getColumn(3).setResizable(false);
        }

        lblTotalDataProduk.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalDataProduk.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalDataProduk.setText("Total Data : 0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblTotalDataProduk)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtKodeProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel15)
                                                    .addComponent(jLabel16)
                                                    .addComponent(jLabel17))
                                                .addGap(31, 31, 31))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel14)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtNamaProduk)
                                                .addComponent(cbKategori, 0, 304, Short.MAX_VALUE)
                                                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(cbCariProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCariProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCariProduk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(55, 55, 55))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addComponent(jLabel13))
                            .addComponent(txtKodeProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtNamaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cbCariProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCariProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariProduk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalDataProduk)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("TabProduk", jPanel4);

        jPanel5.setBackground(new java.awt.Color(36, 53, 71));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Master Bahan Baku");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Kode Bahan");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Nama Bahan");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Satuan");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Qty Beli");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Harga Beli");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Harga/Satuan");

        txtKodeBahan.addActionListener(this::txtKodeBahanActionPerformed);

        txtQtyBeli.addCaretListener(this::txtQtyBeliCaretUpdate);

        txtHargaBeli.addCaretListener(this::txtHargaBeliCaretUpdate);

        txtHargaSatuan.addActionListener(this::txtHargaSatuanActionPerformed);

        cbSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Meter", "Kg", "Pcs", "Roll", "Lusin" }));

        jPanel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel16.setOpaque(false);

        btnTambahBahan.setBackground(new java.awt.Color(255, 204, 0));
        btnTambahBahan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahBahan.setText("+ Tambah");
        btnTambahBahan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnTambahBahan.setFocusPainted(false);
        btnTambahBahan.addActionListener(this::btnTambahBahanActionPerformed);

        btnSimpanBahan.setBackground(new java.awt.Color(255, 204, 0));
        btnSimpanBahan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanBahan.setText("Simpan");
        btnSimpanBahan.setFocusPainted(false);
        btnSimpanBahan.addActionListener(this::btnSimpanBahanActionPerformed);

        btnUbahBahan.setBackground(new java.awt.Color(102, 102, 102));
        btnUbahBahan.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahBahan.setText("Ubah");
        btnUbahBahan.setFocusPainted(false);
        btnUbahBahan.addActionListener(this::btnUbahBahanActionPerformed);

        btnHapusBahan.setBackground(new java.awt.Color(204, 0, 51));
        btnHapusBahan.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusBahan.setText("- Hapus");
        btnHapusBahan.setFocusPainted(false);
        btnHapusBahan.addActionListener(this::btnHapusBahanActionPerformed);

        btnResetBahan.setBackground(new java.awt.Color(102, 102, 102));
        btnResetBahan.setForeground(new java.awt.Color(255, 255, 255));
        btnResetBahan.setText("Reset");
        btnResetBahan.setFocusPainted(false);
        btnResetBahan.addActionListener(this::btnResetBahanActionPerformed);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpanBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbahBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapusBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetBahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambahBahan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpanBahan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUbahBahan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusBahan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetBahan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Pencarian");

        cbBahanBaku.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Bahan", "Nama Bahan", "Satuan", "Harga Beli" }));

        txtCariBB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariBBKeyReleased(evt);
            }
        });

        btnCariBB.setBackground(new java.awt.Color(255, 204, 0));
        btnCariBB.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCariBB.setText("Cari");
        btnCariBB.addActionListener(this::btnCariBBActionPerformed);

        tblBahanBaku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Bahan", "Nama Bahan", "Satuan", "Qty Beli", "Harga Beli", "Harga/Satuan"
            }
        ));
        tblBahanBaku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBahanBakuMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBahanBaku);

        lblTotalBahan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalBahan.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalBahan.setText("Total Data : 0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtKodeBahan, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel24)
                                            .addComponent(jLabel25))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNamaBahan, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                            .addComponent(cbSatuan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel27)
                                            .addComponent(jLabel26))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtHargaBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtQtyBeli, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(41, 41, 41)
                                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTotalBahan)
                                    .addComponent(jLabel29))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane3))))
                .addGap(55, 55, 55))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(150, 150, 150)
                    .addComponent(cbBahanBaku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtCariBB, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btnCariBB, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(80, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel26)
                                    .addComponent(txtKodeBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel27)
                                    .addComponent(txtNamaBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(txtQtyBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel28)
                            .addComponent(txtHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalBahan)
                .addGap(60, 60, 60))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(271, 271, 271)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbBahanBaku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCariBB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCariBB))
                    .addContainerGap(268, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("TabBahanBaku", jPanel5);

        jPanel6.setBackground(new java.awt.Color(36, 53, 71));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Master BOM");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Kode BOM");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Produk");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Ukuran");

        cbUkuranBOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S", "M", "L", "XL", "ALL" }));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Keterangan");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Kombinasi Warna");

        cbProdukBOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel17.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel17.setOpaque(false);

        btnTambahBOM.setBackground(new java.awt.Color(255, 204, 0));
        btnTambahBOM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahBOM.setText("+ Tambah");
        btnTambahBOM.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnTambahBOM.setFocusPainted(false);
        btnTambahBOM.addActionListener(this::btnTambahBOMActionPerformed);

        btnSimpanBOM.setBackground(new java.awt.Color(255, 204, 0));
        btnSimpanBOM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanBOM.setText("Simpan");
        btnSimpanBOM.setFocusPainted(false);
        btnSimpanBOM.addActionListener(this::btnSimpanBOMActionPerformed);

        btnUbahBOM.setBackground(new java.awt.Color(102, 102, 102));
        btnUbahBOM.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahBOM.setText("Ubah");
        btnUbahBOM.setFocusPainted(false);
        btnUbahBOM.addActionListener(this::btnUbahBOMActionPerformed);

        btnHapusBOM.setBackground(new java.awt.Color(204, 0, 51));
        btnHapusBOM.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusBOM.setText("- Hapus");
        btnHapusBOM.setFocusPainted(false);
        btnHapusBOM.addActionListener(this::btnHapusBOMActionPerformed);

        btnResetBOM.setBackground(new java.awt.Color(102, 102, 102));
        btnResetBOM.setForeground(new java.awt.Color(255, 255, 255));
        btnResetBOM.setText("Reset");
        btnResetBOM.setFocusPainted(false);
        btnResetBOM.addActionListener(this::btnResetBOMActionPerformed);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpanBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbahBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapusBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambahBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpanBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUbahBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetBOM)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Pencarian");

        cbCariBom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode BOM", "Kode Produk", "Ukuran", "Kombinasi Warna" }));

        txtCariBom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyRealesed(evt);
            }
        });

        btnCariBom.setBackground(new java.awt.Color(255, 204, 0));
        btnCariBom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCariBom.setText("Cari");
        btnCariBom.addActionListener(this::btnCariBomActionPerformed);

        tblBOM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode BOM", "Produk", "Ukuran", "Kombinasi Warna", "Keterangan"
            }
        ));
        tblBOM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBOMMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblBOM);

        lblTotalBOM.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalBOM.setText("Total Data : 0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblTotalBOM)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel35))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtKodeBOM, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbProdukBOM, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbUkuranBOM, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel33)
                                            .addComponent(jLabel34))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtKetaranganBOM)
                                            .addComponent(txtKombinasiWarna, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(cbCariBom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCariBom, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCariBom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(54, 54, 54))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel30)
                                    .addComponent(txtKodeBOM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel33)
                                    .addComponent(cbProdukBOM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel34)
                                    .addComponent(txtKombinasiWarna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(txtKetaranganBOM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(cbUkuranBOM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCariBom)
                    .addComponent(jLabel35)
                    .addComponent(cbCariBom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCariBom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalBOM)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("TabBOM", jPanel6);

        jPanel8.setBackground(new java.awt.Color(36, 53, 71));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Detail BOM");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Kode Bahan");

        cbKodeBOM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Kode BOM");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Kode Detail");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Komponen");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Quantity");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Qty");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Harga/Satuan");

        jPanel19.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel19.setOpaque(false);

        btnTambahDtlBOM.setBackground(new java.awt.Color(255, 204, 0));
        btnTambahDtlBOM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahDtlBOM.setText("+ Tambah");
        btnTambahDtlBOM.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnTambahDtlBOM.setFocusPainted(false);
        btnTambahDtlBOM.addActionListener(this::btnTambahDtlBOMActionPerformed);

        btnSimpanDtlBOM.setBackground(new java.awt.Color(255, 204, 0));
        btnSimpanDtlBOM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanDtlBOM.setText("Simpan");
        btnSimpanDtlBOM.setFocusPainted(false);
        btnSimpanDtlBOM.addActionListener(this::btnSimpanDtlBOMActionPerformed);

        btnUbahDtlBOM.setBackground(new java.awt.Color(102, 102, 102));
        btnUbahDtlBOM.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahDtlBOM.setText("Ubah");
        btnUbahDtlBOM.setFocusPainted(false);
        btnUbahDtlBOM.addActionListener(this::btnUbahDtlBOMActionPerformed);

        btnHapusDtlBOM.setBackground(new java.awt.Color(204, 0, 51));
        btnHapusDtlBOM.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusDtlBOM.setText("- Hapus");
        btnHapusDtlBOM.setFocusPainted(false);
        btnHapusDtlBOM.addActionListener(this::btnHapusDtlBOMActionPerformed);

        btnResetDtlBOM.setBackground(new java.awt.Color(102, 102, 102));
        btnResetDtlBOM.setForeground(new java.awt.Color(255, 255, 255));
        btnResetDtlBOM.setText("Reset");
        btnResetDtlBOM.setFocusPainted(false);
        btnResetDtlBOM.addActionListener(this::btnResetDtlBOMActionPerformed);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpanDtlBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahDtlBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbahDtlBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapusDtlBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetDtlBOM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTambahDtlBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpanDtlBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUbahDtlBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusDtlBOM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetDtlBOM)
                .addContainerGap())
        );

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Pencarian");

        cbCariDetailBom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Detail BOM", "BOM", "Nama Bahan", "Komponen", "Quantity" }));

        txtCariDtlBom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TxtCariDtlBomKeyReleased(evt);
            }
        });

        btnCariDtlBom.setBackground(new java.awt.Color(255, 204, 0));
        btnCariDtlBom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCariDtlBom.setText("Cari");
        btnCariDtlBom.addActionListener(this::btnCariDtlBomActionPerformed);
        btnCariDtlBom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnCariDtlBomKeyReleased(evt);
            }
        });

        tblDetailBOM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode DetailBOM", "BOM", "Nama Bahan", "Komponen", "Quantity"
            }
        ));
        tblDetailBOM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetailBOMMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblDetailBOM);

        lblTotalDtlBom.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalDtlBom.setText("Total Data : 0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(lblTotalDtlBom)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane5)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel38))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtKodeDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbKodeBOM, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbKodeBahan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addComponent(jLabel39)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel42))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtHargaDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtSatuanDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtKomponen, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                        .addComponent(txtQty)))
                                .addGap(34, 34, 34)
                                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(62, 62, 62))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(59, 59, 59)
                    .addComponent(jLabel43)
                    .addGap(4, 4, 4)
                    .addComponent(cbCariDetailBom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtCariDtlBom, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnCariDtlBom, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(60, 60, 60)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel40)))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel39)
                                    .addComponent(txtKomponen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel41)
                            .addComponent(txtSatuanDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(txtHargaDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtKodeDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbKodeBOM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbKodeBahan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalDtlBom)
                .addContainerGap(84, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(271, 271, 271)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCariDtlBom)
                        .addComponent(jLabel43)
                        .addComponent(cbCariDetailBom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCariDtlBom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(268, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("TabDetailBOM", jPanel8);

        jPanel9.setBackground(new java.awt.Color(36, 53, 71));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Master Tenaga Kerja");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Biaya/Hari");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Posisi Pekerja");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Kode Pekerja");

        jPanel20.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel20.setOpaque(false);

        btnTambahTK.setBackground(new java.awt.Color(255, 204, 0));
        btnTambahTK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahTK.setText("+ Tambah");
        btnTambahTK.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnTambahTK.setFocusPainted(false);

        btnSimpanTK.setBackground(new java.awt.Color(255, 204, 0));
        btnSimpanTK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanTK.setText("Simpan");
        btnSimpanTK.setFocusPainted(false);

        btnUbahTK.setBackground(new java.awt.Color(102, 102, 102));
        btnUbahTK.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahTK.setText("Ubah");
        btnUbahTK.setFocusPainted(false);

        btnHapusTK.setBackground(new java.awt.Color(204, 0, 51));
        btnHapusTK.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusTK.setText("- Hapus");
        btnHapusTK.setFocusPainted(false);
        btnHapusTK.addActionListener(this::btnHapusTKActionPerformed);

        btnResetTK.setBackground(new java.awt.Color(102, 102, 102));
        btnResetTK.setForeground(new java.awt.Color(255, 255, 255));
        btnResetTK.setText("Reset");
        btnResetTK.setFocusPainted(false);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpanTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbahTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapusTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetTK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambahTK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpanTK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUbahTK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusTK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetTK)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbPosisi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Pencarian");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Produk", "Nama Produk" }));

        jButton12.setBackground(new java.awt.Color(255, 204, 0));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setText("Cari");

        tblTenagaKerja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode Pekerja", "Posisi", "Biaya/Hari"
            }
        ));
        jScrollPane6.setViewportView(tblTenagaKerja);

        lblTotalPekerja.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalPekerja.setText("Total Pekerja : 0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGap(57, 57, 57)
                                        .addComponent(jLabel44)
                                        .addGap(31, 31, 31))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel45)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtBiayaHari, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                                    .addComponent(cbPosisi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel46)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKodePekerja, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(100, 100, 100)
                        .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTotalPekerja)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addGap(61, 61, 61))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(59, 59, 59)
                    .addComponent(jLabel47)
                    .addGap(4, 4, 4)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(77, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel46)
                            .addComponent(txtKodePekerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(cbPosisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(txtBiayaHari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalPekerja)
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(271, 271, 271)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton12)
                        .addComponent(jLabel47)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(268, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("TabTenagaKerja", jPanel9);

        jPanel7.setBackground(new java.awt.Color(36, 53, 71));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Master Overhead");

        cbKategoriOverhead.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Meter", "Kg", "Pcs", "Roll", "Lusin" }));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Kategori");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Nama Biaya");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Kode Overhead");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Nominal Biaya");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Keterangan");

        txtKeteranganOverhead.setColumns(20);
        txtKeteranganOverhead.setRows(5);
        jScrollPane7.setViewportView(txtKeteranganOverhead);

        jPanel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel21.setOpaque(false);

        btnTambahOverhead.setBackground(new java.awt.Color(255, 204, 0));
        btnTambahOverhead.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambahOverhead.setText("+ Tambah");
        btnTambahOverhead.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnTambahOverhead.setFocusPainted(false);

        btnSimpanOverhead.setBackground(new java.awt.Color(255, 204, 0));
        btnSimpanOverhead.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanOverhead.setText("Simpan");
        btnSimpanOverhead.setFocusPainted(false);

        btnUbahOverhead.setBackground(new java.awt.Color(102, 102, 102));
        btnUbahOverhead.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahOverhead.setText("Ubah");
        btnUbahOverhead.setFocusPainted(false);

        btnHapusOverhead.setBackground(new java.awt.Color(204, 0, 51));
        btnHapusOverhead.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusOverhead.setText("- Hapus");
        btnHapusOverhead.setFocusPainted(false);
        btnHapusOverhead.addActionListener(this::btnHapusOverheadActionPerformed);

        btnResetOverhead.setBackground(new java.awt.Color(102, 102, 102));
        btnResetOverhead.setForeground(new java.awt.Color(255, 255, 255));
        btnResetOverhead.setText("Reset");
        btnResetOverhead.setFocusPainted(false);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnSimpanOverhead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTambahOverhead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUbahOverhead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapusOverhead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetOverhead, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTambahOverhead)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSimpanOverhead)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnUbahOverhead)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapusOverhead)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnResetOverhead)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Pencarian");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode Produk", "Nama Produk" }));

        jButton13.setBackground(new java.awt.Color(255, 204, 0));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton13.setText("Cari");

        tblOverhead.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Overhead", "Nama Biaya", "Kategori", "Nominal Biaya", "Keterangan"
            }
        ));
        jScrollPane8.setViewportView(tblOverhead);

        lblTotalOverhead.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalOverhead.setText("Total Data : 0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblTotalOverhead)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabel50)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtKodeOverhead, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel49)
                                            .addComponent(jLabel48))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNamaBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbKategoriOverhead, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel51)
                                            .addComponent(jLabel52))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtBiayaOverhead))
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(49, 49, 49))))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(210, 210, 210)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(69, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(jLabel51)
                            .addComponent(txtKodeOverhead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBiayaOverhead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(txtNamaBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbKategoriOverhead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel48))
                            .addComponent(jScrollPane7)))
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalOverhead)
                .addContainerGap(68, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(271, 271, 271)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton13)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(268, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("TabOverhead", jPanel7);

        jPanel18.setBackground(new java.awt.Color(36, 53, 71));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Transaksi Produksi");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Customer");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("BOM");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Jumlah Produksi");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Tanggal");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("No. Produksi");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Ukuran");

        txtProdukTransaksi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kaos", "PDH", "Rompi", "Hoodie", "Lanyard" }));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Produk");

        cbBOMProduksi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Sedang Diproses", "Selesai", "Batal" }));

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Tenaga Kerja");

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("(Proses yang Digunakan)");

        chkPotong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkPotong.setForeground(new java.awt.Color(255, 255, 255));
        chkPotong.setText("Pemotongan Kain");
        chkPotong.addActionListener(this::chkPotongActionPerformed);

        chkJahit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkJahit.setForeground(new java.awt.Color(255, 255, 255));
        chkJahit.setText("Penjahitan");
        chkJahit.addActionListener(this::chkJahitActionPerformed);

        chkBordir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkBordir.setForeground(new java.awt.Color(255, 255, 255));
        chkBordir.setText("Bordir/Sablon");
        chkBordir.addActionListener(this::chkBordirActionPerformed);

        chkAksesoris.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkAksesoris.setForeground(new java.awt.Color(255, 255, 255));
        chkAksesoris.setText("Pemasangan Aksesoris");
        chkAksesoris.addActionListener(this::chkAksesorisActionPerformed);

        chkQc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkQc.setForeground(new java.awt.Color(255, 255, 255));
        chkQc.setText("Steam & Quality Control");
        chkQc.addActionListener(this::chkQcActionPerformed);

        chkFinishing.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkFinishing.setForeground(new java.awt.Color(255, 255, 255));
        chkFinishing.setText("Finishing");
        chkFinishing.addActionListener(this::chkFinishingActionPerformed);

        btnHitungEstimasi.setBackground(new java.awt.Color(255, 255, 0));
        btnHitungEstimasi.setText("Hitung Estimasi");

        btnSimpanProduksi.setBackground(new java.awt.Color(255, 255, 0));
        btnSimpanProduksi.setText("Simpan Produksi");

        tblTransaksiProduksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Produksi", "Tanggal", "Produk", "Ukuran", "Jumlah", "Customer"
            }
        ));
        jScrollPane9.setViewportView(tblTransaksiProduksi);

        lblTotalProduksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalProduksi.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalProduksi.setText("Total Produksi :");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(lblTotalProduksi)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(jLabel62)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel63))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jLabel59)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtNoProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel58)
                                            .addComponent(jLabel55))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtTanggalProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtProdukTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txtCustomer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel57)
                                    .addComponent(jLabel60)
                                    .addComponent(jLabel56))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtUkuran, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                    .addComponent(txtJumlahProduksi, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                    .addComponent(cbBOMProduksi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel61))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkJahit)
                                    .addComponent(chkPotong))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkAksesoris)
                                    .addComponent(chkBordir))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkFinishing)
                                    .addComponent(chkQc))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnHitungEstimasi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnSimpanProduksi, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))))
                        .addGap(47, 47, 47))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60)
                    .addComponent(txtNoProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUkuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(jLabel57)
                    .addComponent(txtTanggalProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJumlahProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jLabel56)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbBOMProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(txtProdukTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(chkPotong)
                                    .addComponent(chkBordir))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkJahit))
                            .addComponent(chkAksesoris)))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkFinishing)
                            .addComponent(btnHitungEstimasi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkQc)
                            .addComponent(btnSimpanProduksi))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalProduksi)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Transaksi Produksi", jPanel18);

        jPanel22.setBackground(new java.awt.Color(36, 53, 71));

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Kode Produksi");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Tanggal");

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("Produk");

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Ukuran");

        jLabel68.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Perhitungan HPP");

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Produk");

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Pcs");

        cbKodeProduksiHPP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kaos", "PDH", "Rompi", "Hoodie", "Lanyard" }));

        jPanel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        jPanel23.setOpaque(false);

        jLabel73.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setText("Total Overhead");

        txtTotalTenagaKerja.setBorder(null);

        txtTotalOverhead.setBorder(null);

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setText("Total Bahan Baku");

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Total Tenaga Kerja");

        txtTotalBahanBaku.setBorder(null);

        jLabel74.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 0));
        jLabel74.setText("TOTAL HPP");

        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 0));
        jLabel75.setText("HPP / Pcs");

        lblTotalHPP.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalHPP.setForeground(new java.awt.Color(255, 255, 0));
        lblTotalHPP.setText("Rp. 0");

        lblHPPPcs.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHPPPcs.setForeground(new java.awt.Color(255, 255, 0));
        lblHPPPcs.setText("Rp. 0");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblHPPPcs, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTotalHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel72)
                            .addComponent(jLabel73)
                            .addComponent(jLabel71))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalBahanBaku, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalTenagaKerja)
                            .addComponent(txtTotalOverhead))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(txtTotalBahanBaku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(txtTotalTenagaKerja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(txtTotalOverhead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHPPPcs)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        btnSimpanHPP.setBackground(new java.awt.Color(255, 255, 0));
        btnSimpanHPP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSimpanHPP.setText("Simpan HPP");
        btnSimpanHPP.addActionListener(this::btnSimpanHPPActionPerformed);

        btnResetHPP.setBackground(new java.awt.Color(102, 102, 102));
        btnResetHPP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnResetHPP.setForeground(new java.awt.Color(255, 255, 255));
        btnResetHPP.setText("Reset");

        tblHPP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode HPP", "Kode Produksi", "Produk", "Jumlah", "Total HPP", "HPP/Pcs", "Tanggal"
            }
        ));
        jScrollPane10.setViewportView(tblHPP);

        lblTotalDataHPP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalDataHPP.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalDataHPP.setText("Total Data : 0");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addGap(39, 39, 39)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel22Layout.createSequentialGroup()
                                        .addComponent(jLabel64)
                                        .addGap(18, 18, 18)
                                        .addComponent(cbKodeProduksiHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel65)
                                            .addComponent(jLabel66))
                                        .addGap(58, 58, 58)
                                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtTanggalHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel22Layout.createSequentialGroup()
                                                .addComponent(txtJumlahHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel70))
                                            .addComponent(txtUkuranHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtProdukHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel69)
                                    .addComponent(jLabel67)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel22Layout.createSequentialGroup()
                                    .addComponent(btnSimpanHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnResetHPP, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblTotalDataHPP)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel64)
                            .addComponent(cbKodeProduksiHPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel65)
                            .addComponent(txtTanggalHPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel66)
                            .addComponent(txtProdukHPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel67)
                            .addComponent(txtUkuranHPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtJumlahHPP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel69)
                            .addComponent(jLabel70))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanHPP)
                    .addComponent(btnResetHPP))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalDataHPP)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("HPP", jPanel22);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 740, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMasterProdukMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProdukMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProdukMouseEntered

    private void btnMasterProdukMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProdukMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProdukMouseExited

    private void btnDashboard1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboard1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard1MouseEntered

    private void btnDashboard1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboard1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboard1MouseExited

    private void btnTransaksiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTransaksiMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTransaksiMouseEntered

    private void btnTransaksiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTransaksiMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTransaksiMouseExited

    private void btnTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransaksiActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
        // Jika sub-menu transaksi tersembunyi, TAMPILKAN dan DORONG panel sisa menu ke bawah
        if (!panelSubTransaksi.isVisible()) {
            panelSubTransaksi.setVisible(true);

            int x = panelSisaMenu.getX();
            int y = panelSisaMenu.getY();
            panelSisaMenu.setLocation(x, y + panelSubTransaksi.getHeight());
        } 
        // Jika sub-menu transaksi tampil, SEMBUNYIKAN dan TARIK panel sisa menu ke atas
        else {
            panelSubTransaksi.setVisible(false);

            int x = panelSisaMenu.getX();
            int y = panelSisaMenu.getY();
            panelSisaMenu.setLocation(x, y - panelSubTransaksi.getHeight());
        }
    }//GEN-LAST:event_btnTransaksiActionPerformed

    private void btnDashboard1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboard1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnDashboard1ActionPerformed

    private void btnMasterProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasterProdukActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
        // Jika panel sub-menu sedang tersembunyi, maka TAMPILKAN dan DORONG menu bawah
        if (!panelSubMaster.isVisible()) {
            panelSubMaster.setVisible(true);

            // Menggeser panelMenuBawah ke BAWAH.
            // Rumus: Titik Y saat ini + Tinggi panelSubMaster
            int x = panelSubTransaksi.getX();
            int y = panelSubTransaksi.getY();
            panelSubTransaksi.setLocation(x, y + panelSubMaster.getHeight());
        } 
        // Jika panel sub-menu sedang tampil, maka SEMBUNYIKAN dan TARIK menu bawah ke atas
        else {
            panelSubMaster.setVisible(false);

            // Menggeser panelMenuBawah kembali ke ATAS
            // Rumus: Titik Y saat ini - Tinggi panelSubMaster
            int x = panelSubTransaksi.getX();
            int y = panelSubTransaksi.getY();
            panelSubTransaksi.setLocation(x, y - panelSubMaster.getHeight());
        }
    }//GEN-LAST:event_btnMasterProdukActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(6);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnMasterProduk1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk1MouseEntered

    private void btnMasterProduk1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk1MouseExited

    private void btnMasterProduk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasterProduk1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedComponent(jPanel22);
    }//GEN-LAST:event_btnMasterProduk1ActionPerformed

    private void btnMasterProduk2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk2MouseEntered

    private void btnMasterProduk2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk2MouseExited

    private void btnMasterProduk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasterProduk2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk2ActionPerformed

    private void btnMasterProduk3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk3MouseEntered

    private void btnMasterProduk3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk3MouseExited

    private void btnMasterProduk3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasterProduk3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk3ActionPerformed

    private void btnMasterProduk4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk4MouseEntered

    private void btnMasterProduk4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMasterProduk4MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk4MouseExited

    private void btnMasterProduk4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasterProduk4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMasterProduk4ActionPerformed

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeProduk.getText().trim();
    
        // Validasi apakah ada kode produk yang dipilih/akan dihapus
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih produk yang ingin dihapus dari tabel terlebih dahulu!");
            return;
        }

        // Konfirmasi hapus data demi keamanan data konveksi
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus produk dengan kode: " + kode + "?", 
                "Konfirmasi Hapus", javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            ProdukModel model = new ProdukModel();
            model.setKode(kode);

            produkcontroller.hapusData(model);

            produkcontroller.tampilData(tblProduk);
            btnResetActionPerformed(null);
            lblTotalDataProduk.setText("Total Data : " + tblProduk.getRowCount());
        
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnHapusBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBahanActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeBahan.getText().trim();
    
        // Validasi apakah  dipilih/akan dihapus
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih bahan baku yang ingin dihapus dari tabel terlebih dahulu!");
            return;
        }

        // Konfirmasi hapus data demi keamanan data konveksi
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus bahan baku dengan kode: " + kode + "?", 
                "Konfirmasi Hapus", javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            BahanBakuModel model = new BahanBakuModel();
            model.setKode(txtKodeBahan.getText());

            controller.hapusData(model);

            controller.tampilData(tblBahanBaku);
            btnResetBahanActionPerformed(null);
            lblTotalBahan.setText("Total Data : " + tblBahanBaku.getRowCount());
        }
    }//GEN-LAST:event_btnHapusBahanActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnHapusBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusBOMActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeBOM.getText().trim();
    
        // Validasi apakah  dipilih/akan dihapus
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih kode bom yang ingin dihapus dari tabel terlebih dahulu!");
            return;
        }

        // Konfirmasi hapus data demi keamanan data konveksi
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus bom dengan kode: " + kode + "?", 
                "Konfirmasi Hapus", javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            BomModel model = new BomModel();
            model.setKodeBom(txtKodeBOM.getText());

            bomcontroller.hapusData(model);

            bomcontroller.tampilData(tblBOM);
            btnResetBOMActionPerformed(null);
            lblTotalBOM.setText("Total Data : " + tblBOM.getRowCount());
        }
    }//GEN-LAST:event_btnHapusBOMActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnHapusDtlBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusDtlBOMActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeDetail.getText().trim();
    
        // Validasi apakah  dipilih/akan dihapus
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih kode detail bom yang ingin dihapus dari tabel terlebih dahulu!");
            return;
        }

        // Konfirmasi hapus data demi keamanan data konveksi
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus detail bom dengan kode: " + kode + "?", 
                "Konfirmasi Hapus", javax.swing.JOptionPane.YES_NO_OPTION);

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            DetailBomModel model = new DetailBomModel();
            model.setKodeDtlBom(kode);

            dtlbomcontroller.hapusData(model);

            dtlbomcontroller.tampilData(tblDetailBOM);
            btnTambahDtlBOMActionPerformed(null);
            lblTotalDtlBom.setText("Total Data : " + tblDetailBOM.getRowCount());
        }
    }//GEN-LAST:event_btnHapusDtlBOMActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnHapusTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusTKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusTKActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnHapusOverheadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusOverheadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapusOverheadActionPerformed

    private void chkPotongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPotongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkPotongActionPerformed

    private void chkJahitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkJahitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkJahitActionPerformed

    private void chkBordirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBordirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkBordirActionPerformed

    private void chkAksesorisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAksesorisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkAksesorisActionPerformed

    private void chkQcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkQcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkQcActionPerformed

    private void chkFinishingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFinishingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkFinishingActionPerformed

    private void btnTransaksiProduksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransaksiProduksiActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(7);
    }//GEN-LAST:event_btnTransaksiProduksiActionPerformed

    private void btnSimpanHPPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanHPPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSimpanHPPActionPerformed

    private void btnSimpanBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanBahanActionPerformed
        // TODO add your handling code here:
        try {
            // View hanya fokus mengambil data dan mengonversi angka secara aman
            BahanBakuModel model = new BahanBakuModel();
            model.setKode(txtKodeBahan.getText());
            model.setNama(txtNamaBahan.getText());
            model.setSatuan(cbSatuan.getSelectedItem().toString());

            // Jika form kosong/berisi huruf, baris ini akan langsung melempar eror ke blok 'catch' di bawah
            double qtyBeli = Double.parseDouble(txtQtyBeli.getText());
            double hargaBeli = Double.parseDouble(txtHargaBeli.getText());

            model.setQtyBeli(qtyBeli);
            model.setHargaBeli(hargaBeli);

            // Kirim ke controller (validasi kosong & simpan database diproses di sini)
            controller.simpanData(model);

            // Refresh tabel
            controller.tampilData(tblBahanBaku);
            
            btnTambahBahanActionPerformed(null);
            
            lblTotalBahan.setText("Total Data : " + tblBahanBaku.getRowCount());

        } catch (NumberFormatException e) {
            // Menangkap eror jika Quantity atau Harga kosong / diisi huruf
            javax.swing.JOptionPane.showMessageDialog(this, "Field tidak boleh kosong!");
        }
    }//GEN-LAST:event_btnSimpanBahanActionPerformed

    private void btnUbahBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahBahanActionPerformed
        // TODO add your handling code here:
        try {
            BahanBakuModel model = new BahanBakuModel();
            model.setKode(txtKodeBahan.getText());
            model.setNama(txtNamaBahan.getText());
            model.setSatuan(cbSatuan.getSelectedItem().toString());

            // Ambil data angka dari form 
            double qtyBeli = Double.parseDouble(txtQtyBeli.getText());
            double hargaBeli = Double.parseDouble(txtHargaBeli.getText());

            model.setQtyBeli(qtyBeli);
            model.setHargaBeli(hargaBeli);

            // menghitung ulang harga_satuan di dalamnya
            controller.ubahData(model);

            //menampilkan ulang dengan rapi
            double hargaSatuan = controller.hitungHargaSatuan(hargaBeli, qtyBeli);

            controller.tampilData(tblBahanBaku);

            btnTambahBahanActionPerformed(null);
            

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Pilih dulu data yang akan dihapus !");
        }
    }//GEN-LAST:event_btnUbahBahanActionPerformed

    private void btnResetBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBahanActionPerformed
        // TODO add your handling code here:
        txtKodeBahan.setText("");
        txtNamaBahan.setText("");
        txtQtyBeli.setText("");
        txtHargaBeli.setText("");
        txtHargaSatuan.setText("");

        cbSatuan.setSelectedIndex(0);

        txtKodeBahan.requestFocus();
        aturForm(true);
    }//GEN-LAST:event_btnResetBahanActionPerformed

    private void btnTambahBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBahanActionPerformed
        // TODO add your handling code here:
        txtKodeBahan.setText("");
        txtNamaBahan.setText("");
        txtQtyBeli.setText("");
        txtHargaBeli.setText("");
        txtHargaSatuan.setText("");

        cbSatuan.setSelectedIndex(0);
        
        //mrmberikan status true agar kode bisa dibuka
        aturForm(true);

        txtKodeBahan.requestFocus();
    }//GEN-LAST:event_btnTambahBahanActionPerformed

    private void tblBahanBakuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBahanBakuMouseClicked
        // TODO add your handling code here:
        int baris = tblBahanBaku.getSelectedRow();
        
        
        txtKodeBahan.setText(tblBahanBaku.getValueAt(baris,0).toString());
        txtNamaBahan.setText(tblBahanBaku.getValueAt(baris,1).toString());
        cbSatuan.setSelectedItem(tblBahanBaku.getValueAt(baris,2).toString());
        txtQtyBeli.setText(tblBahanBaku.getValueAt(baris,3).toString());
        txtHargaBeli.setText(tblBahanBaku.getValueAt(baris,4).toString());
        txtHargaSatuan.setText(tblBahanBaku.getValueAt(baris,5).toString());
        
        //mengunci kode bahan baku
        aturForm(false);
    }//GEN-LAST:event_tblBahanBakuMouseClicked

    private void btnCariBBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariBBActionPerformed
        // TODO add your handling code here:
        String field = "";
        switch (cbBahanBaku.getSelectedItem().toString()) {

        case "Kode Bahan":
            field = "kode_bahan";
            break;

        case "Nama Bahan":
            field = "nama_bahan";
            break;
            
         case "Satuan":
            field = "satuan";
            break;
        
        case "Harga Beli":
            field = "harga_beli";
            break;
        }

        controller.cariData(tblBahanBaku,  txtCariBB.getText());
        
        if(txtCariBB.getText().trim().isEmpty()){
            controller.tampilData(tblBahanBaku);
        }else{
            controller.cariData(tblBahanBaku, txtCariBB.getText());
        }
    }//GEN-LAST:event_btnCariBBActionPerformed

    private void txtHargaSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaSatuanActionPerformed
        // TODO add your handling code here:
        txtHargaSatuan.setEditable(false);
        txtHargaSatuan.setFocusable(false);
    }//GEN-LAST:event_txtHargaSatuanActionPerformed

    private void txtKodeBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeBahanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeBahanActionPerformed

    private void txtHargaBeliCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtHargaBeliCaretUpdate
        // TODO add your handling code here:
        hitungHargaSatuanOtomatis();
    }//GEN-LAST:event_txtHargaBeliCaretUpdate

    private void txtQtyBeliCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtQtyBeliCaretUpdate
        // TODO add your handling code here:
        hitungHargaSatuanOtomatis();
    }//GEN-LAST:event_txtQtyBeliCaretUpdate

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        ProdukModel model = new ProdukModel();
        model.setKode(txtKodeProduk.getText());
        model.setNama(txtNamaProduk.getText());
        model.setKategori(cbKategori.getSelectedItem().toString()); 
        model.setDeskripsi(txtDeskripsi.getText());

        produkcontroller.simpanData(model);
        produkcontroller.tampilData(tblProduk);
        
        btnTambahActionPerformed(null);
        
        lblTotalDataProduk.setText("Total Data : " + tblProduk.getRowCount());
        
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void tblProdukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdukMouseClicked
        // TODO add your handling code here:
        int baris = tblProduk.getSelectedRow();
    
        txtKodeProduk.setText(tblProduk.getValueAt(baris, 0).toString());
        txtNamaProduk.setText(tblProduk.getValueAt(baris, 1).toString());
        cbKategori.setSelectedItem(tblProduk.getValueAt(baris, 2).toString());
        txtDeskripsi.setText(tblProduk.getValueAt(baris, 3).toString());
        
        aturForm(false);
    }//GEN-LAST:event_tblProdukMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        txtKodeProduk.setText("");
        txtNamaProduk.setText("");
        cbKategori.setSelectedIndex(0); 
        txtDeskripsi.setText("");

        aturForm(true);
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        txtKodeProduk.setText("");
        txtNamaProduk.setText("");
        cbKategori.setSelectedIndex(0); 
        txtDeskripsi.setText("");

        aturForm(true);
        
    }//GEN-LAST:event_btnTambahActionPerformed

    private void cbKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbKategoriActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeProduk.getText().trim();
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih data produk yang ingin diubah dari tabel terlebih dahulu!");
            return;
        }

        ProdukModel model = new ProdukModel();
        model.setKode(kode);
        model.setNama(txtNamaProduk.getText());
        model.setKategori(cbKategori.getSelectedItem().toString()); 
        model.setDeskripsi(txtDeskripsi.getText());

        produkcontroller.ubahData(model);
        produkcontroller.tampilData(tblProduk);
        
        btnTambahActionPerformed(null);
        aturForm(false);
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnCariProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariProdukActionPerformed
        // TODO add your handling code here:
        String field = "";
        switch (cbCariProduk.getSelectedItem().toString()) {

        case "Kode Produk":
            field = "kode_produk";
            break;

        case "Nama Produk":
            field = "nama_produk";
            break;
            
        case "Kategori":
            field = "kategori";
            break;
        }

        produkcontroller.cariData(tblProduk, txtCariProduk.getText());
        
        if(txtCariProduk.getText().trim().isEmpty()){
            produkcontroller.tampilData(tblProduk);
        }else{
            produkcontroller.cariData(tblProduk, txtCariProduk.getText());
        }
    }//GEN-LAST:event_btnCariProdukActionPerformed

    private void btnSimpanBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanBOMActionPerformed
        // TODO add your handling code here:
        BomModel model = new BomModel();
        ItemProduk item = (ItemProduk) cbProdukBOM.getSelectedItem();
        model.setKodeBom(txtKodeBOM.getText());
        model.setKodeProduk(item.getKodeProduk());
        model.setUkuran(cbUkuranBOM.getSelectedItem().toString()); 
        model.setKombinasi(txtKombinasiWarna.getText());
        model.setKet(txtKetaranganBOM.getText());

        bomcontroller.simpanData(model);
        bomcontroller.tampilData(tblBOM);
        
        btnTambahBOMActionPerformed(null);
        
        lblTotalBOM.setText("Total Data : " + tblBOM.getRowCount());
    }//GEN-LAST:event_btnSimpanBOMActionPerformed

    private void btnTambahBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBOMActionPerformed
        // TODO add your handling code here:
        txtKodeBOM.setText("");
        cbProdukBOM.setSelectedIndex(0); 
        cbUkuranBOM.setSelectedIndex(0); 
        txtKombinasiWarna.setText("");
        txtKetaranganBOM.setText("");

        aturForm(true);
    }//GEN-LAST:event_btnTambahBOMActionPerformed

    private void btnUbahBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahBOMActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeBOM.getText().trim();
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih data BOM yang ingin diubah dari tabel terlebih dahulu!");
            return;
        }

        BomModel model = new BomModel();
        ItemProduk item = (ItemProduk) cbProdukBOM.getSelectedItem();
        model.setKodeBom(kode);
        model.setKodeProduk(item.getKodeProduk()); 
        model.setUkuran(cbUkuranBOM.getSelectedItem().toString()); 
        model.setKombinasi(txtKombinasiWarna.getText());
        model.setKet(txtKetaranganBOM.getText());

        bomcontroller.ubahData(model);
        bomcontroller.tampilData(tblBOM);
        
        btnTambahBOMActionPerformed(null);
        aturForm(false);
    }//GEN-LAST:event_btnUbahBOMActionPerformed

    private void btnResetBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetBOMActionPerformed
        // TODO add your handling code here:
        txtKodeBOM.setText("");
        cbProdukBOM.setSelectedIndex(0); 
        cbUkuranBOM.setSelectedIndex(0); 
        txtKombinasiWarna.setText("");
        txtKetaranganBOM.setText("");

        aturForm(true);
    }//GEN-LAST:event_btnResetBOMActionPerformed

    private void btnCariBomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariBomActionPerformed
        // TODO add your handling code here:
        String field = "";
        switch (cbCariBom.getSelectedItem().toString()) {

        case "Kode Bom":
            field = "kode_bom";
            break;
            
        case "Kode Produk":
            field = "kode_produk";
            break;

        case "Ukuran":
            field = "ukuran";
            break;
            
        case "Kombinasi Warna":
            field = "kombinasi_warna";
            break;
        }

        bomcontroller.cariData(tblBOM,  txtCariBom.getText());
        
        if(txtCariBom.getText().trim().isEmpty()){
            bomcontroller.tampilData(tblBOM);
        }else{
            bomcontroller.cariData(tblBOM, txtCariBom.getText());
        }
    }//GEN-LAST:event_btnCariBomActionPerformed

    private void tblBOMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBOMMouseClicked
        // TODO add your handling code here:
        int baris = tblBOM.getSelectedRow();
    
        txtKodeBOM.setText(tblBOM.getValueAt(baris, 0).toString());
        bomcontroller.pilihProduk(cbProdukBOM,tblBOM.getValueAt(baris, 1).toString());
        cbUkuranBOM.setSelectedItem(tblBOM.getValueAt(baris, 2).toString());
        txtKombinasiWarna.setText(tblBOM.getValueAt(baris, 3).toString());
        txtKetaranganBOM.setText(tblBOM.getValueAt(baris, 4).toString());
        
        aturForm(false);
    }//GEN-LAST:event_tblBOMMouseClicked

    private void btnSimpanDtlBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanDtlBOMActionPerformed
        // TODO add your handling code here:
        DetailBomModel model = new DetailBomModel();
        ItemBahan bahan = (ItemBahan) cbKodeBahan.getSelectedItem();
        ItemBom bom = (ItemBom) cbKodeBOM.getSelectedItem();
        model.setKodeDtlBom(txtKodeDetail.getText());
        model.setKodeBom(bom.getKodeBom());
        model.setKodeBahan(bahan.getKodeBahan());
        model.setKomponen(txtKomponen.getText());  
        model.setQty(Double.parseDouble(txtQty.getText()));

        dtlbomcontroller.simpanData(model);
        dtlbomcontroller.tampilData(tblDetailBOM);
        
        btnTambahBOMActionPerformed(null);
        
        lblTotalDtlBom.setText("Total Data : " + tblDetailBOM.getRowCount());
    }//GEN-LAST:event_btnSimpanDtlBOMActionPerformed

    private void txtCariKeyRealesed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyRealesed
        // TODO add your handling code here:
        String keyword = txtCariBom.getText().trim();
        
        if (keyword.isEmpty()) {
            bomcontroller.tampilData(tblBOM);
        } else {
            bomcontroller.cariData(tblBOM, keyword);
        }
        
        lblTotalBOM.setText("Total Data : " + tblBOM.getRowCount());
           
    }//GEN-LAST:event_txtCariKeyRealesed

    private void btnTambahDtlBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDtlBOMActionPerformed
        // TODO add your handling code here:
        txtKodeDetail.setText("");
        cbKodeBOM.setSelectedIndex(0); 
        cbKodeBahan.setSelectedIndex(0); 
        txtKomponen.setText("");
        txtQty.setText("");

        aturForm(true);
    }//GEN-LAST:event_btnTambahDtlBOMActionPerformed

    private void btnResetDtlBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetDtlBOMActionPerformed
        // TODO add your handling code here:
        txtKodeDetail.setText("");
        cbKodeBOM.setSelectedIndex(0); 
        cbKodeBahan.setSelectedIndex(0); 
        txtKomponen.setText("");
        txtQty.setText("");

        aturForm(true);
    }//GEN-LAST:event_btnResetDtlBOMActionPerformed

    private void btnUbahDtlBOMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahDtlBOMActionPerformed
        // TODO add your handling code here:
        String kode = txtKodeDetail.getText().trim();
        if (kode.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih data detail BOM yang ingin diubah dari tabel terlebih dahulu!");
            return;
        }

        DetailBomModel model = new DetailBomModel();
        ItemBahan bahan = (ItemBahan) cbKodeBahan.getSelectedItem();
        ItemBom bom = (ItemBom) cbKodeBOM.getSelectedItem();
        model.setKodeDtlBom(txtKodeDetail.getText());
        model.setKodeBom(bom.getKodeBom());
        model.setKodeBahan(bahan.getKodeBahan());
        model.setKomponen(txtKomponen.getText()); 
        model.setQty(Double.parseDouble(txtQty.getText()));

        dtlbomcontroller.ubahData(model);
        dtlbomcontroller.tampilData(tblDetailBOM);
        
        btnTambahBOMActionPerformed(null);
        aturForm(false);
    }//GEN-LAST:event_btnUbahDtlBOMActionPerformed

    private void tblDetailBOMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailBOMMouseClicked
        // TODO add your handling code here:
        int baris = tblDetailBOM.getSelectedRow();
    
        txtKodeDetail.setText(tblDetailBOM.getValueAt(baris, 0).toString());
        dtlbomcontroller.pilihBom(cbKodeBOM,tblDetailBOM.getValueAt(baris, 1).toString());
        dtlbomcontroller.pilihBahan(cbKodeBahan,tblDetailBOM.getValueAt(baris, 2).toString());
        txtKomponen.setText(tblDetailBOM.getValueAt(baris, 3).toString());
        txtQty.setText(tblDetailBOM.getValueAt(baris, 4).toString());
        
        aturForm(false);
    }//GEN-LAST:event_tblDetailBOMMouseClicked

    private void btnCariDtlBomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariDtlBomActionPerformed
        // TODO add your handling code here:
        String field = "";
        switch (cbCariDetailBom.getSelectedItem().toString()) {

        case "Kode Detail Bom":
            field = "kode_detail_bom";
            break;
            
        case "BOM":
            field = "keterangan";
            break;
            
        case "Nama Bahan":
            field = "nama_bahan";
            break;

        case "Komponen":
            field = "komponen";
            break;
            
        case "Qty":
            field = "qty";
            break;
        } 

        dtlbomcontroller.cariData(tblDetailBOM,  txtCariDtlBom.getText());
        
        if(txtCariDtlBom.getText().trim().isEmpty()){
            dtlbomcontroller.tampilData(tblDetailBOM);
        }else{
            dtlbomcontroller.cariData(tblDetailBOM, txtCariDtlBom.getText());
        }
    }//GEN-LAST:event_btnCariDtlBomActionPerformed

    private void TxtCariDtlBomKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCariDtlBomKeyReleased
        // TODO add your handling code here:
       
        String keyword = txtCariDtlBom.getText().trim();
        

        // Terjemahkan kriteria dari ComboBox ke nama kolom asli di MySQL
        if (keyword.isEmpty()) {
            dtlbomcontroller.tampilData(tblDetailBOM);
        } else {
            dtlbomcontroller.cariData(tblDetailBOM, keyword);
        }
 
        // Panggil controller menggunakan nama kolom database yang benar
        dtlbomcontroller.cariData(tblDetailBOM, keyword);
        // Update total data
        lblTotalDtlBom.setText("Total Data : " + tblDetailBOM.getRowCount());
    }//GEN-LAST:event_TxtCariDtlBomKeyReleased

    private void btnCariDtlBomKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCariDtlBomKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariDtlBomKeyReleased

    private void txtCariProdukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariProdukKeyReleased
        // TODO add your handling code here:
        String keyword = txtCariProduk.getText().trim();
        
        if (keyword.isEmpty()) {
            produkcontroller.tampilData(tblProduk);
        } else {
            produkcontroller.cariData(tblProduk, keyword);
        }
        
        lblTotalDataProduk.setText("Total Data : " + tblProduk.getRowCount());
    }//GEN-LAST:event_txtCariProdukKeyReleased

    private void txtCariBBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariBBKeyReleased
        // TODO add your handling code here:
        String keyword = txtCariBB.getText().trim();
        
        if (keyword.isEmpty()) {
            controller.tampilData(tblBahanBaku);
        } else {
            controller.cariData(tblBahanBaku, keyword);
        }
        
        lblTotalBahan.setText("Total Data : " + tblBahanBaku.getRowCount());
    }//GEN-LAST:event_txtCariBBKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainMenuView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariBB;
    private javax.swing.JButton btnCariBom;
    private javax.swing.JButton btnCariDtlBom;
    private javax.swing.JButton btnCariProduk;
    private javax.swing.JButton btnDashboard1;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapusBOM;
    private javax.swing.JButton btnHapusBahan;
    private javax.swing.JButton btnHapusDtlBOM;
    private javax.swing.JButton btnHapusOverhead;
    private javax.swing.JButton btnHapusTK;
    private javax.swing.JButton btnHitungEstimasi;
    private javax.swing.JButton btnMasterProduk;
    private javax.swing.JButton btnMasterProduk1;
    private javax.swing.JButton btnMasterProduk2;
    private javax.swing.JButton btnMasterProduk3;
    private javax.swing.JButton btnMasterProduk4;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnResetBOM;
    private javax.swing.JButton btnResetBahan;
    private javax.swing.JButton btnResetDtlBOM;
    private javax.swing.JButton btnResetHPP;
    private javax.swing.JButton btnResetOverhead;
    private javax.swing.JButton btnResetTK;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanBOM;
    private javax.swing.JButton btnSimpanBahan;
    private javax.swing.JButton btnSimpanDtlBOM;
    private javax.swing.JButton btnSimpanHPP;
    private javax.swing.JButton btnSimpanOverhead;
    private javax.swing.JButton btnSimpanProduksi;
    private javax.swing.JButton btnSimpanTK;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambahBOM;
    private javax.swing.JButton btnTambahBahan;
    private javax.swing.JButton btnTambahDtlBOM;
    private javax.swing.JButton btnTambahOverhead;
    private javax.swing.JButton btnTambahTK;
    private javax.swing.JButton btnTransaksi;
    private javax.swing.JButton btnTransaksiProduksi;
    private javax.swing.JButton btnUbah;
    private javax.swing.JButton btnUbahBOM;
    private javax.swing.JButton btnUbahBahan;
    private javax.swing.JButton btnUbahDtlBOM;
    private javax.swing.JButton btnUbahOverhead;
    private javax.swing.JButton btnUbahTK;
    private javax.swing.JComboBox<String> cbBOMProduksi;
    private javax.swing.JComboBox<String> cbBahanBaku;
    private javax.swing.JComboBox<String> cbCariBom;
    private javax.swing.JComboBox<String> cbCariDetailBom;
    private javax.swing.JComboBox<String> cbCariProduk;
    private javax.swing.JComboBox<String> cbKategori;
    private javax.swing.JComboBox<String> cbKategoriOverhead;
    private javax.swing.JComboBox<String> cbKodeBOM;
    private javax.swing.JComboBox<String> cbKodeBahan;
    private javax.swing.JComboBox<String> cbKodeProduksiHPP;
    private javax.swing.JComboBox<String> cbPosisi;
    private javax.swing.JComboBox<String> cbProdukBOM;
    private javax.swing.JComboBox<String> cbSatuan;
    private javax.swing.JComboBox<String> cbUkuranBOM;
    private javax.swing.JCheckBox chkAksesoris;
    private javax.swing.JCheckBox chkBordir;
    private javax.swing.JCheckBox chkFinishing;
    private javax.swing.JCheckBox chkJahit;
    private javax.swing.JCheckBox chkPotong;
    private javax.swing.JCheckBox chkQc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JLabel lblHPP;
    private javax.swing.JLabel lblHPPPcs;
    private javax.swing.JLabel lblProduksi;
    private javax.swing.JLabel lblTotalBOM;
    private javax.swing.JLabel lblTotalBahan;
    private javax.swing.JLabel lblTotalBahanBaku;
    private javax.swing.JLabel lblTotalDataHPP;
    private javax.swing.JLabel lblTotalDataProduk;
    private javax.swing.JLabel lblTotalDtlBom;
    private javax.swing.JLabel lblTotalHPP;
    private javax.swing.JLabel lblTotalOverhead;
    private javax.swing.JLabel lblTotalPekerja;
    private javax.swing.JLabel lblTotalProduk;
    private javax.swing.JLabel lblTotalProduksi;
    private javax.swing.JLabel lblwaktu;
    private javax.swing.JPanel panelSisaMenu;
    private javax.swing.JPanel panelSubMaster;
    private javax.swing.JPanel panelSubTransaksi;
    private javax.swing.JTable tblBOM;
    private javax.swing.JTable tblBahanBaku;
    private javax.swing.JTable tblDetailBOM;
    private javax.swing.JTable tblHPP;
    private javax.swing.JTable tblOverhead;
    private javax.swing.JTable tblProduk;
    private javax.swing.JTable tblProduksiTerakhir;
    private javax.swing.JTable tblTenagaKerja;
    private javax.swing.JTable tblTransaksiProduksi;
    private javax.swing.JTextField txtBiayaHari;
    private javax.swing.JTextField txtBiayaOverhead;
    private javax.swing.JTextField txtCariBB;
    private javax.swing.JTextField txtCariBom;
    private javax.swing.JTextField txtCariDtlBom;
    private javax.swing.JTextField txtCariProduk;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtDeskripsi;
    private javax.swing.JTextField txtHargaBeli;
    private javax.swing.JTextField txtHargaDetail;
    private javax.swing.JTextField txtHargaSatuan;
    private javax.swing.JTextField txtJumlahHPP;
    private javax.swing.JTextField txtJumlahProduksi;
    private javax.swing.JTextField txtKetaranganBOM;
    private javax.swing.JTextArea txtKeteranganOverhead;
    private javax.swing.JTextField txtKodeBOM;
    private javax.swing.JTextField txtKodeBahan;
    private javax.swing.JTextField txtKodeDetail;
    private javax.swing.JTextField txtKodeOverhead;
    private javax.swing.JTextField txtKodePekerja;
    private javax.swing.JTextField txtKodeProduk;
    private javax.swing.JTextField txtKombinasiWarna;
    private javax.swing.JTextField txtKomponen;
    private javax.swing.JTextField txtNamaBahan;
    private javax.swing.JTextField txtNamaBiaya;
    private javax.swing.JTextField txtNamaProduk;
    private javax.swing.JTextField txtNoProduksi;
    private javax.swing.JTextField txtProdukHPP;
    private javax.swing.JComboBox<String> txtProdukTransaksi;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtQtyBeli;
    private javax.swing.JTextField txtSatuanDetail;
    private javax.swing.JTextField txtTanggalHPP;
    private javax.swing.JTextField txtTanggalProduksi;
    private javax.swing.JTextField txtTotalBahanBaku;
    private javax.swing.JTextField txtTotalOverhead;
    private javax.swing.JTextField txtTotalTenagaKerja;
    private javax.swing.JTextField txtUkuran;
    private javax.swing.JTextField txtUkuranHPP;
    // End of variables declaration//GEN-END:variables
}
