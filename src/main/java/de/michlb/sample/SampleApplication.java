package de.michlb.sample;

import de.michlb.sample.domain.Person;
import de.michlb.sample.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class SampleApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(SampleApplication.class, args);

    PersonRepository personRepository = context.getBean(PersonRepository.class);
    personRepository.save(new Person("Hanz2235", "Meiser"));
    personRepository.save(new Person("Peter", "Lustig"));
  }

}
