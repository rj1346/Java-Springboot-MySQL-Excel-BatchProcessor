package batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j

public class ListOfFilmsConfig {
	
	private static final Log log = LogFactory.getLog(ListOfFilmsConfig.class);

    @Autowired
    public JobRepository jobRepository;

    @Autowired
    public JobLauncher jobLauncher;

    @Autowired
    public Job listOfFilmsJob;
    
    public JobLauncher simpleJobLauncher() {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }
    
    @Scheduled(cron = "${job.cron}")
    public void runBatchJob() {
        log.info("START Run Batch Method");
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("time", System.currentTimeMillis());
        try {
            jobLauncher.run(listOfFilmsJob, jobParametersBuilder.toJobParameters());
            log.info("END In Run Batch Method");
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
