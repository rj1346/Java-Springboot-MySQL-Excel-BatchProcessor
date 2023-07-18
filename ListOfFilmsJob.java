package batch.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import batch.config.DataSourceConfiguration;
import batch.tasklet.ListOfFilmsTasklet;

@Configuration
public class ListOfFilmsJob {
	
	private static final Log log = LogFactory.getLog(ListOfFilmsJob.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ListOfFilmsTasklet listOfFilmsTasklet;
	
	@Qualifier("ListOfFilmsJob")
	@Bean
	public Job ListOfFilmsJob() {
		log.info("In Job ListOfFilmsJob ");
		return jobBuilderFactory.get("ListOfFilmsJob")
				.incrementer(new RunIdIncrementer())
				.start(ListOfFilmsTaskletStep())
				.build();
	}
	
	@Bean
	public Step ListOfFilmsTaskletStep() {
		log.info("In Task ListOfFilmsTaskletStep ");
		return stepBuilderFactory.get("ListOfFilmsTaskletStep")
				.tasklet(listOfFilmsTasklet)
				.build();
	}

}
