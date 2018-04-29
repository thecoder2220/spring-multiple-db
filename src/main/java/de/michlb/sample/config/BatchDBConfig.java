package de.michlb.sample.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class BatchDBConfig {

    //@Value("${datasource.stravovani.maxPoolSize:10}")
    private int maxPoolSize = 10;

    @Bean
    @ConfigurationProperties(prefix = "batch.datasource")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean(name = "batchDataSource")
    public DataSource batchDataSource() {
        DataSourceProperties dataSourceProperties = dataSourceProperties();
        HikariDataSource dataSource = (HikariDataSource)
                DataSourceBuilder
                        .create(dataSourceProperties.getClassLoader())
                        .driverClassName(dataSourceProperties.getDriverClassName())
                        .url(dataSourceProperties.getUrl())
                        .username(dataSourceProperties.getUsername())
                        .password(dataSourceProperties.getPassword())
                        .type(HikariDataSource.class)
                        .build();
        dataSource.setMaximumPoolSize(maxPoolSize);
        return dataSource;
    }

    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory()
            throws NamingException {
        LocalContainerEntityManagerFactoryBean factoryBean = new
                LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(batchDataSource());
        factoryBean.setPackagesToScan ("de.michlb.sample.domain");  //("com.stravovani.DB.batch.Model");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        return factoryBean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new
                HibernateJpaVendorAdapter();
        return hibernateJpaVendorAdapter;
    }

    @Bean(name = "batchTransactionManager")
    @Autowired
    public PlatformTransactionManager
    transactionManager(@Qualifier("batchEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

}
