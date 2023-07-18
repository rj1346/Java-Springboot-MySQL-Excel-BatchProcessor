package batch.common;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AppConstants {
	
	@Value("${report.one.users.query}")
	private String reportOneUsers;
	
	@Value("${prodUpdateUserId}") 
	private String prodUpdateUserId;
	 
	public String getReportOneUsers() {
		return reportOneUsers;
	}

	public void setReportOneUsers(String reportOneUsers) {
		this.reportOneUsers = reportOneUsers;
	}
	
	public String getProdUpdateUserId() { 
		 return prodUpdateUserId; 
	}
	 
	 public void setProdUpdateUserId(String prodUpdateUserId) {
		 this.prodUpdateUserId = prodUpdateUserId; 
	}

}
