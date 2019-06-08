/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class main {

    public static void main(String[] args)throws IOException, SQLException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int wahl, lnr;

        do {
            System.out.println("------------------------------------------------");
            System.out.println("1. Insert ARTIKEL1.XML auf die Tabelle Artikel");
            System.out.println("2. Insert LagerA.XML auf die Tabelle LagerA");
            System.out.println("3. Select auf LagerA");
            System.out.println("0. Ende");
            wahl = Integer.parseInt(in.readLine());

            switch (wahl) {
                case 1:
                    Parser p1 = new Parser("ARTIKEL1.xml");
                    p1.start(true,"ARTIKEL.XSD");
                    System.out.println("*******Parsen ENDE************");
                    break;


        } }while (wahl != 0);

    }
}
