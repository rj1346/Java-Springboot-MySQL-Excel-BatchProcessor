package batch.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import batch.common.AppConstants;
import batch.model.User;

import com.mysql.cj.jdbc.Driver;



@Data
@Slf4j
public class BatchListOfFilmsRtm {
	private static final Log log = LogFactory.getLog(BatchListOfFilmsRtm.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private String prodUpdateUserId;
	
	private AppConstants appConstants;
	
	public BatchListOfFilmsRtm(AppConstants appConstants, JdbcTemplate jdbcTemplate) {
		this.appConstants = appConstants;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	Integer countPerRun = -1;
	ExcelCreator creator = null;
	String updateUserId= null;
	
	 public void DoIt() throws Exception {
		log.info("In DoIt Start");
		
		updateUserId = appConstants.getProdUpdateUserId();
		
		//int countPerRun = 10;
		if (countPerRun != 0) {					
			Connection connMySQL = null;	
			try {
				// Initialize Database 
				creator = new ExcelCreator();
				creator.initExcelExporter();
				log.info("tring to get jdbc connection..");
				log.info("tring to get jdbcTemplate connection.."+jdbcTemplate);
				connMySQL = jdbcTemplate.getDataSource().getConnection();
                log.info("connMySQL connection = " + connMySQL);
                createReport1(connMySQL);
                
                String filename = creator.exportToFile();
				saveFile(filename);
				
			} catch (Exception e) {
				e.printStackTrace();
				log.error("In DoIt error - " + e.getMessage());
			}
			finally {
				if (connMySQL != null)
                    connMySQL.close();
			}
		}
		log.info("In DoIt - end");
	}
	
	private void createReport1(Connection connMySQL) throws Exception {
		log.info("In createReport1");
		List<String> names = new ArrayList<String>();
		names.add("Title");
		names.add("Release_year");
		names.add("Actor_name");
		names.add("Genre");
		names.add("Rental_rate");
		try {
			//ExcelCreator creator = new ExcelCreator(); // Initialize the creator object
	        List<User> users = getReport1Users(connMySQL);
	        creator.writeHeaderLine("ListOfFilms", names);
			creator.writeDataLines1(users);
		} catch (Exception e) {
			log.error("BatchListOfFilms.createReport1 - " + e.getStackTrace());
			
			//see stack trace
			StackTraceElement[] stackTraceLines = e.getStackTrace();
			for(int i = 0; i < stackTraceLines.length; i++)
			{
				log.info(stackTraceLines[i].toString());
			}
		}
	}
	
	private List<User>  getReport1Users(Connection connMySQL) throws Exception {
		log.info("In getReport1Users");
		log.info("In getReport1Users Query = " + appConstants.getReportOneUsers());
		List<User> users = null;
		Statement statementDb = null;
		ResultSet resultSet = null;
		String query = null;
		try {
			users = new ArrayList<User>();
			statementDb = connMySQL.createStatement();
			
			query = this.appConstants.getReportOneUsers();
			
			resultSet = statementDb.executeQuery(query);
			//log.info("In resultset extraction");
			while (resultSet.next()) {
				User user = new User();
				user.setTitle(resultSet.getString("Title"));
				user.setRelease_year(resultSet.getInt("Release_year"));
				user.setActor_name(resultSet.getString("Actor_name"));
				user.setGenre(resultSet.getString("Genre"));
				user.setRental_rate(resultSet.getFloat("Rental_rate"));
				users.add(user);
			}
		} catch (Exception e) {
			log.error("BatchListOfFilms.getReport1Users - " + e.getMessage());
		}
		return users;	
		
	}
	
	public void saveFile(String filename) throws Exception {
		log.info("Saving file: " + filename);
        String destinationPath = "C:\\Users\\rjaip\\eclipse-workspace\\OutputFiles\\" + filename;  // Setting the desired destination path
        File sourceFile = new File(filename);
        File destinationFile = new File(destinationPath);

        // Use Files.copy to save the file to the destination path
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        log.info("File saved: " + destinationPath);
    }

}
