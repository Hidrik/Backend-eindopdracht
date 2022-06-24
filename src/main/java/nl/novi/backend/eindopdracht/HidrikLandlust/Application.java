package nl.novi.backend.eindopdracht.HidrikLandlust;

import nl.novi.backend.eindopdracht.HidrikLandlust.services.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Resource
	FileStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	//At startup => Initialize root map and delete all existing files
	@Override
	public void run(String... arg) {
		storageService.deleteAll();
		storageService.init();
	}

}
