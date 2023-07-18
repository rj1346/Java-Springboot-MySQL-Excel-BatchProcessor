package batch.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import batch.common.DatabaseConfig;

@Configuration
public class DataSourceConfiguration {
	
	private static final Log log = LogFactory.getLog(DataSourceConfiguration.class);
	
    private ResourceLoader resourceLoader;
    
    @Bean(name = DatabaseConfig.MYSQL_DATASOURCE, destroyMethod = "")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    @Primary
    public DataSource dataSource() {
        log.info("DataSourceConfiguration creation..");
        return DataSourceBuilder.create().build();
    }
    
    @Autowired
    public DataSourceConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean(name = DatabaseConfig.TX_MANAGER_MYSQL)
    @Autowired
    DataSourceTransactionManager transactionManagerMysql(
            @Qualifier(DatabaseConfig.MYSQL_DATASOURCE) DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    @Bean(name = DatabaseConfig.MYSQL_NAMED_PARAMETER_JDBCTEMPLATE)
    @DependsOn(DatabaseConfig.MYSQL_DATASOURCE)
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
            @Qualifier(DatabaseConfig.MYSQL_DATASOURCE) DataSource mysqlDataSource) {
        return new NamedParameterJdbcTemplate(mysqlDataSource);
    }

    @Bean(name = DatabaseConfig.MYSQL_JDBCTEMPLATE)
    @DependsOn(DatabaseConfig.MYSQL_DATASOURCE)
    public JdbcTemplate jdbcTemplate(@Qualifier(DatabaseConfig.MYSQL_DATASOURCE) DataSource mysqlDataSource) {
        return new JdbcTemplate(mysqlDataSource);
    }

}
