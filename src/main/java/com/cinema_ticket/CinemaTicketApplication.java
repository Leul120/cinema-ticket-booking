package com.cinema_ticket;

import com.cinema_ticket.entities.Role;
import com.cinema_ticket.entities.User;
import com.cinema_ticket.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = "com.cinema_ticket")
@EnableJpaRepositories(basePackages = "com.cinema_ticket.repositories")
public class CinemaTicketApplication implements CommandLineRunner {

	private final UserRepository userRepository;


	@Autowired
	public CinemaTicketApplication(UserRepository userRepository) {
		this.userRepository = userRepository;

	}
	public static void main(String[] args) {
		SpringApplication.run(CinemaTicketApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if (adminAccount == null) {
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstName("admin");
			user.setLastName("admin");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);
		}
	}
}
