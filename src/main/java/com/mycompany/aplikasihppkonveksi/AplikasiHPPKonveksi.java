/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aplikasihppkonveksi;

import com.mycompany.aplikasihppkonveksi.views.LoginView;
import javax.swing.SwingUtilities;

/**
 *
 * @author ZHILLAN
 */
//Zhillan belum mandi
public class AplikasiHPPKonveksi {

    public static void main(String[] args) {
        //Buat jalanin UI di dalam Event
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true); //Buat UI nya muncul(tampil)
        });
    }
}
