package com.mycompany.aplikasihppkonveksi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private static Connection konek;
    
    public static Connection getKoneksi(){
        if(konek == null){
            try{
                //Alamat database (hati-hati dengan port dan password--> cek sql yog)
                String url = "jdbc:mysql://localhost:3306/db_hpp";
                String user = "root";
                String password = "";
                
                //mendaftarkn driver mysql versi 5
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                
                konek = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi Database Berhasil");
                
            }catch (SQLException e){
                System.out.print("Koneksi Database Gagal : " + e.getMessage());             
            }
        }
        return konek;
    }
}
