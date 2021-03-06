package ch.ehi.ili2db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.ili2db.base.DbUrlConverter;
import ch.ehi.ili2db.base.Ili2db;
import ch.ehi.ili2db.base.Ili2dbException;
import ch.ehi.ili2db.gui.Config;
import ch.ehi.ili2db.mapping.NameMapping;

/*
 * jdbc:postgresql:database
 * jdbc:postgresql://host/database
 * jdbc:postgresql://host:port/database
 */
// -Ddburl=jdbc:postgresql:dbname -Ddbusr=usrname -Ddbpwd=1234
public class InheritanceNoSmartTest {
	private static final String DBSCHEMA = "InheritanceNoSmart";
	private static final String DATASETNAME = "Testset1";
	
	String dburl=System.getProperty("dburl"); 
	String dbuser=System.getProperty("dbusr");
	String dbpwd=System.getProperty("dbpwd"); 

	public Config initConfig(String xtfFilename,String dbschema,String logfile) {
		Config config=new Config();
		new ch.ehi.ili2pg.PgMain().initConfig(config);
		
		
		config.setDburl(dburl);
		config.setDbusr(dbuser);
		config.setDbpwd(dbpwd);
		if(dbschema!=null){
			config.setDbschema(dbschema);
		}
		if(logfile!=null){
			config.setLogfile(logfile);
		}


		config.setXtffile(xtfFilename);
		if(Ili2db.isItfFilename(xtfFilename)){
			config.setItfTransferfile(true);
		}
		return config;
		
	}

	@Test
	public void importNoSmart() throws Exception
	{
		File data=new File("test/data/InheritanceNoSmart/Inheritance1a.xtf");
		
		Connection jdbcConnection=null;
		try{
	        Class driverClass = Class.forName("org.postgresql.Driver");
	        jdbcConnection = DriverManager.getConnection(
	        		dburl, dbuser, dbpwd);
	        Statement stmt=jdbcConnection.createStatement();
	        stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE");
	        
			Config config=initConfig(data.getPath(),DBSCHEMA,data.getPath()+".log");
			config.setFunction(Config.FC_IMPORT);
			config.setCreateFk(Config.CREATE_FK_YES);
			config.setInheritanceTrafo(null);
			config.setDatasetName(DATASETNAME);
			config.setTidHandling(Config.TID_HANDLING_PROPERTY);
			config.setBasketHandling(Config.BASKET_HANDLING_READWRITE);
			config.setCreatescript(data.getPath()+".sql");
			Ili2db.readSettingsFromDb(config);
			Ili2db.run(config,null);
	        
			Assert.assertTrue(stmt.execute("SELECT a3.attra3,a3b.attra3b FROM "+DBSCHEMA+".classa3 a3,"+DBSCHEMA+".classa3b a3b WHERE a3.t_ili_tid='7'"));
			ResultSet rs=stmt.getResultSet();
			Assert.assertTrue(rs.next());
			Assert.assertEquals("a3",rs.getString(1));
			Assert.assertEquals("a3b",rs.getString(2));
	        
		}finally{
			if(jdbcConnection!=null){
				jdbcConnection.close();
			}
		}
		exportNoSmart();
	}
	//@Test
	public void exportNoSmart() throws Ili2dbException
	{
		File data=new File("test/data/InheritanceNoSmart/Inheritance1a-out.xtf");
		Config config=initConfig(data.getPath(),DBSCHEMA,data.getPath()+".log");
		config.setModels("Inheritance1");
		config.setFunction(Config.FC_EXPORT);
		config.setDatasetName(DATASETNAME);
		Ili2db.readSettingsFromDb(config);
		Ili2db.run(config,null);
	}
	@Test
	public void importIliStructAttrFK() throws Exception
	{
		Connection jdbcConnection=null;
		try{
	        Class driverClass = Class.forName("org.postgresql.Driver");
	        jdbcConnection = DriverManager.getConnection(
	        		dburl, dbuser, dbpwd);
	        Statement stmt=jdbcConnection.createStatement();
	        stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE");

			File data=new File("test/data/InheritanceNoSmart/StructAttr1.ili");
			Config config=initConfig(data.getPath(),DBSCHEMA,data.getPath()+".log");
			config.setFunction(Config.FC_SCHEMAIMPORT);
			config.setCreateFk(Config.CREATE_FK_YES);
			config.setInheritanceTrafo(null);
			config.setDatasetName(DATASETNAME);
			config.setTidHandling(Config.TID_HANDLING_PROPERTY);
			config.setBasketHandling(Config.BASKET_HANDLING_READWRITE);
			config.setNameOptimization(Config.NAME_OPTIMIZATION_TOPIC);
			config.setCreatescript(data.getPath()+".sql");
			Ili2db.readSettingsFromDb(config);
			Ili2db.run(config,null);
		}finally{
			if(jdbcConnection!=null){
				jdbcConnection.close();
			}
		}    
	}
	@Test
	public void importXtfStructAttrFK() throws Exception
	{
		Connection jdbcConnection=null;
		try{
	        Class driverClass = Class.forName("org.postgresql.Driver");
	        jdbcConnection = DriverManager.getConnection(
	        		dburl, dbuser, dbpwd);
	        Statement stmt=jdbcConnection.createStatement();
	        stmt.execute("DROP SCHEMA IF EXISTS "+DBSCHEMA+" CASCADE");

			File data=new File("test/data/InheritanceNoSmart/StructAttr1a.xtf");
			Config config=initConfig(data.getPath(),DBSCHEMA,data.getPath()+".log");
			config.setFunction(Config.FC_IMPORT);
			config.setCreateFk(Config.CREATE_FK_YES);
			config.setInheritanceTrafo(null);
			config.setDatasetName(DATASETNAME);
			config.setTidHandling(Config.TID_HANDLING_PROPERTY);
			config.setBasketHandling(Config.BASKET_HANDLING_READWRITE);
			config.setNameOptimization(Config.NAME_OPTIMIZATION_TOPIC);
			config.setCreatescript(data.getPath()+".sql");
			Ili2db.readSettingsFromDb(config);
			Ili2db.run(config,null);
		}finally{
			if(jdbcConnection!=null){
				jdbcConnection.close();
			}
		}
		exportXtfStructAttrFK();
	}
	//@Test
	public void exportXtfStructAttrFK() throws Exception
	{
		Connection jdbcConnection=null;
		try{
	        Class driverClass = Class.forName("org.postgresql.Driver");
	        jdbcConnection = DriverManager.getConnection(
	        		dburl, dbuser, dbpwd);

			File data=new File("test/data/InheritanceNoSmart/StructAttr1a-out.xtf");
			Config config=initConfig(data.getPath(),DBSCHEMA,data.getPath()+".log");
			config.setFunction(Config.FC_EXPORT);
			config.setDatasetName(DATASETNAME);
			Ili2db.readSettingsFromDb(config);
			Ili2db.run(config,null);
		}finally{
			if(jdbcConnection!=null){
				jdbcConnection.close();
			}
		}    
	}

}
