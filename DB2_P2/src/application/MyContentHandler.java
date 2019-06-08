/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.validation.TypeInfoProvider;

public class MyContentHandler implements ContentHandler {

    int za = 0;
    /* Zeile aktiv <=> za=1               */
    int m = 0;
    /* lfd. Spaltenr.i.d. Zeile           */
    int cm = 0;
    /* DTYP(Spalte) ist SQL-char-aehnlich */
    String aktwert;
    /* Wert des aktuellen XML-Elements    */
    private final TypeInfoProvider typeInfoProvider;
    private List<Artikel> alleArtikel = new LinkedList<>();
    private String currentValue;
    private Artikel artikel;
    private String table;
    private boolean boolVal;
    private Connection connection;
    int elementAnz;
    private List<String> statements;

    public MyContentHandler(TypeInfoProvider typeInfoProvider) {
        this.typeInfoProvider = typeInfoProvider;

    }

    /**
     * Methode wird aufgerufen am Start des Dokuments
     *
     */
    public void startDocument() {
        System.out.println("Anfang des Parsens");
        this.statements = new LinkedList<>();
    }

    /**
     * Methode wird aufgerufen am Ende des Dokuments
     *
     */
    public void endDocument() {
        DecimalFormat df = new DecimalFormat("#####0.00");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(dfs);

        for (Artikel art : alleArtikel) {
            if ((art.getKuehl()==null) || (art.getArtikelbezeichnung()==null)){
                System.out.println("Fehler");
            }
            else{
 
            String sql = "INSERT INTO ARTIKEL (ARTBEZ, MGE, PREIS, KUEHL, EDAT) VALUES " + "('"
                    + art.getArtikelbezeichnung() + "','" + art.getMengeneinheit() + "','" + df.format(art.getPreis())
                    + "','" + art.getKuehl() + "', TO_DATE('" + art.getDatum()
                    + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS'))";

            statements.add(sql);}
        }
        if (statements.isEmpty()) {
            return;
        }

        try {
            connection = DatabaseConnection.connect();

            for (String statement : statements) {
                execute(statement);
            }
        } catch (SQLException ex) {
            System.out.println("Connection to database failed: " + ex.getMessage());
        }

    }

    private void execute(String statement) throws SQLException {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery(statement);
            System.out.println(statement);
            System.out.println("-----------------Inserted------------------");
        } catch (SQLException ex) {
            System.out.println("SQL statement error!" + ex.getMessage() + statement);
        }

    }

    /**
     * Methode liest Wert nach dem Begin Element aus
     *
     */
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        System.out.print(qName + "  Datentyp: " + typeInfoProvider.getElementTypeInfo().getTypeName());
        AttributesImpl attributesImpl = new AttributesImpl(attributes);
        if (qName == null || qName.trim().length() < 1) {
            return;
        }

        switch (qName) {
            case "tabelleMa":
                System.out.println("");
                break;
            case "tabname":
                System.out.println("");
                break;

            case "zeile":
                System.out.println("");
                this.boolVal = true;
                break;

            default:
                if (boolVal) {

                    if (attributes == null || attributes.getLength() < 1) {
                        return;
                    }
                    for (int i = 0; i < attributesImpl.getLength(); i++) {
                        if (!typeInfoProvider.getElementTypeInfo().getTypeName().equalsIgnoreCase(attributes.getQName(i))) {
                            throw new SAXException("Tabele hat keinen gueltigen Datentyp!");

                        }
                       
                    }
                }

        }
    }

    /**
     * Methode liest Zeichen fÃ¼r Zeichen in Variable aktwert
     *
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
this.currentValue = new String(ch, start, length);
        System.out.println("Wert: "+currentValue+" ");
    }

    public void skippedEntity(String name) throws SAXException {
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
    }

    /**
     * Methode liest Wert vor dem Ende Element aus
     *
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

switch(qName.toUpperCase()){
        case "ARTNR":
            artikel = new Artikel();
            artikel.setArtikelnummer(Integer.parseInt(currentValue));
            break;
        case "ARTBEZ":
            artikel.setArtikelbezeichnung(currentValue);
            break;
        case "MGE":
                        artikel.setMengeneinheit(currentValue);
            break;
        case "PREIS":            artikel.setPreis(Double.parseDouble(currentValue));

            break;
        case "KUEHL":
                        artikel.setKuehl(currentValue);
                        break;
        case "EDAT":
                        artikel.setDatum(currentValue);
            break;
        case "ZEILE":
                        alleArtikel.add(artikel);
            break;
        default:
            break;
                        
}


    }

    public void endPrefixMapping(String prefix) throws SAXException { //System.out.println("-Pr-> Praefix: "+prefix);
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException { //System.out.println("-PrS-> Praefix: "+prefix);
    }

    public void setDocumentLocator(Locator locator) { //System.out.println("-L-> Locator: "+locator.toString());
    }
}
