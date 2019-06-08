package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.pool.OracleDataSource;


public class DatabaseConnection {
        private Statement stm;
    private Connection con;

    public DatabaseConnection() {
        try {
            this.con = connect();
            this.stm = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public Statement getStm() {
        return stm;
    }

    public Connection getCon() {
        return con;
    }
    public void closeCon() throws SQLException {

        con.close();
    }
	public static Connection connect() throws SQLException {
		String treiber;
	       OracleDataSource odc = new OracleDataSource();
	       treiber ="oracle.jdbc.driver.OracleDriver";
	        Connection dbConnect= null;
	       //Treiber laden
	       try{
	           Class.forName(treiber);
	           }catch(Exception e){
	           System.out.println("fehler beim Laden des Treibers!");      
	           }
	       try{
			odc.setURL("jdbc:oracle:thin:dbprak26/paeffgen@schelling.nt.fh-koeln.de:1521:xe");
			dbConnect = odc.getConnection();
           }catch(Exception e){
               System.out.println("Fehler beim Verbindungsaufbau!");
               System.out.println(e.getMessage());
           }
       return dbConnect;
   }
}
