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
import org.springframework.context.annotation.Primary;
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
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "applicationEntityManagerFactory", transactionManagerRef = "applicationTransactionManager", basePackages = {"de.michlb.sample.repositories"})
public class ApplicationDBConfig {

    //@Value("${datasource.stravovani.maxPoolSize:10}")
    private int maxPoolSize = 3;

    @Bean(name = "applicationDataSource")
    @ConfigurationProperties(prefix = "datasource.secondary")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSource() {
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

    @Primary
    @Bean(name = "applicationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean applicationEntityManagerFactory()
            throws NamingException {
        LocalContainerEntityManagerFactoryBean factoryBean = new
                LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("de.michlb.sample.domain");  //("com.stravovani.DB.batch.Model");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());

        Properties properties = new Properties();
        //properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new
                HibernateJpaVendorAdapter();
        return hibernateJpaVendorAdapter;
    }

    @Bean(name = "applicationTransactionManager")
    @Autowired
    public PlatformTransactionManager
    transactionManager(@Qualifier("applicationEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

}
