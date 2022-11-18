package app.eni;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private double price;

}

@RepositoryRestResource
interface ProductRepository extends JpaRepository<Product,Long>{

}
@Projection(name="fullProduct",types=Product.class)
interface ProductInterface
{
public Long getId();
public String getName();
public double getPrice();
}
@SpringBootApplication
public class InventoryServiceApplication {
	@Autowired
	ProductRepository productRepository;


	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(ProductRepository productRepository,
							RepositoryRestConfiguration restConfiguration) {


		return args -> {
			restConfiguration.exposeIdsFor(Product.class);

			productRepository.save(new Product(null, "PC LENOVO", 1500));
			productRepository.save(new Product(null, "IMP EPSON", 500.5));
			productRepository.save(new Product(null, "CBL HDMI", 3.5));
			productRepository.findAll().forEach(System.out::println);

		};
	}
}
