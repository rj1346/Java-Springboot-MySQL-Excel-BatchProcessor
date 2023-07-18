package batch.tasklet;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import batch.common.AppConstants;
import batch.common.DatabaseConfig;
import batch.job.ListOfFilmsJob;
import batch.util.BatchListOfFilmsRtm;

@Slf4j
@Component

public class ListOfFilmsTasklet  implements Tasklet, InitializingBean {
	
	private static final Log log = LogFactory.getLog(ListOfFilmsTasklet.class);
	
	@Autowired
	private AppConstants appConstatns;
	
	private JdbcTemplate jdbcTemplate;
	
	@Value("$prodUpdateUserId")
	private String prodUpdateUserId;
	
	@Autowired
    @Qualifier(DatabaseConfig.MYSQL_DATASOURCE)
	public void setDataSource(DataSource dataSource) {
		log.info("In setDataSource");
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.info("In tasklet execute");
		BatchListOfFilmsRtm testBatch = new BatchListOfFilmsRtm(appConstatns, jdbcTemplate);
		testBatch.DoIt();
		return RepeatStatus.FINISHED;
	}
}
