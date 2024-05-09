/*######################################################
 *#Configuration class to instantiate objects in memory#
 *######################################################*/
package com.java.marketplace.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.java.marketplace.entities.Address;
import com.java.marketplace.entities.Customer;
import com.java.marketplace.entities.Order;
import com.java.marketplace.entities.OrderItem;
import com.java.marketplace.entities.ProductOffering;
import com.java.marketplace.entities.Role;
import com.java.marketplace.entities.User;
import com.java.marketplace.entities.enums.AddressType;
import com.java.marketplace.entities.enums.Authorities;
import com.java.marketplace.entities.enums.CustomerType;
import com.java.marketplace.entities.enums.POState;
import com.java.marketplace.repositories.AddressRepository;
import com.java.marketplace.repositories.CustomerRepository;
import com.java.marketplace.repositories.OrderItemRepository;
import com.java.marketplace.repositories.OrderRepository;
import com.java.marketplace.repositories.ProductOfferingRepository;
import com.java.marketplace.repositories.RoleRepository;
import com.java.marketplace.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductOfferingRepository productOfferingRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public void run(String... args) throws Exception {
		
		ProductOffering p1 = new ProductOffering(null, "Sneaker", 90.5, true, 5, POState.Active);
		ProductOffering p2 = new ProductOffering(null, "Smart TV", 2190.0, true, 5, POState.Technical);
		ProductOffering p3 = new ProductOffering(null, "Macbook Pro", 1250.0, true, 0, POState.Active);
		ProductOffering p4 = new ProductOffering(null, "PC Gamer", 1200.0, true, 1, POState.Definition);
		ProductOffering p5 = new ProductOffering(null, "Boot", 100.99, false, 5, POState.Technical);
		ProductOffering p6 = new ProductOffering(null, "Sandal", 49.99, true, 5, POState.Active);
		ProductOffering p7 = new ProductOffering(null, "Slipper", 100.99, false, 5, POState.Technical);
		ProductOffering p8 = new ProductOffering(null, "Watch", 100.99, true, 5, POState.Active);
		ProductOffering p9 = new ProductOffering(null, "Sunglasses", 100.99, true, 5, POState.Technical);
		ProductOffering p10 = new ProductOffering(null, "Handbag", 100.99, false, 0, POState.Active);
		ProductOffering p11 = new ProductOffering(null, "Smartphone", 100.99, true, 5, POState.Definition);
		ProductOffering p12 = new ProductOffering(null, "Headphone", 100.99, true, 5, POState.Definition);
		ProductOffering p13 = new ProductOffering(null, "Speaker", 100.99, true, 0, POState.Active);
		ProductOffering p14 = new ProductOffering(null, "Puzzle", 100.99, true, 1, POState.Technical);
		ProductOffering p15 = new ProductOffering(null, "Charger", 100.99, false, 10, POState.Technical);

		productOfferingRepository.saveAll(Arrays.asList(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15));
		
		User u1 = new User(null, "admin@main.com", "12345678");
		User u2 = new User(null, "operator@mail.com", "12345678");
		User u3 = new User(null, "user_sem_customer@mail.com", "12345678");
		User u4 = new User(null, "user_sem_customer2@mail.com", "12345678");
		User u5 = new User(null, "maria@gmail.com", "12345678");
		User u6 = new User(null, "alex@gmail.com", "12345678");
		User u7 = new User(null, "caio@gmail.com", "12345678");
		User u8 = new User(null, "ana@gmail.com", "12345678");
		User u9 = new User(null, "taina@gmail.com", "12345678");
		User u10 = new User(null, "cristina@gmail.com", "12345678");
		User u11 = new User(null, "david@gmail.com", "12345678");
		User u12 = new User(null, "victor@gmail.com", "12345678");
		User u13 = new User(null, "admin@mail.com", "12345678");
		User u14 = new User(null, "admin2@mail.com", "12345678");
		User u15 = new User(null, "dalila@gmail.com", "12345678");
		User u16 = new User(null, "ale@gmail.com", "12345678");
		
		Customer c1 = new Customer(null,"Operator",112233L,"active",CustomerType.LegalPerson,"12000","12345678",u2);
		Customer c2 = new Customer(null,"Maria",223344L,"active",CustomerType.NaturalPerson,"12000","123456",u5);
		Customer c3 = new Customer(null,"Alex",334455L,"active",CustomerType.LegalPerson,"12000","123456",u6);
		Customer c4 = new Customer(null,"Caio",445566L,"active",CustomerType.LegalPerson,"12000","123456",u7);
		Customer c5 = new Customer(null,"Ana",556677L,"active",CustomerType.LegalPerson,"12000","123456",u8);
		Customer c6 = new Customer(null,"Taina",778899L,"active",CustomerType.NaturalPerson,"12000","123456",u9);
		Customer c7 = new Customer(null,"Cristina",889911L,"active",CustomerType.NaturalPerson,"12000","123456",u10);
		Customer c8 = new Customer(null,"David",991122L,"active",CustomerType.Technical,"12000","123456",u11);
		Customer c9 = new Customer(null,"Victor",113344L,"active",CustomerType.Technical,"12000","123456",u12);
		Customer c10 = new Customer(null,"Dalila",224455L,"active",CustomerType.Technical,"12000","123456",u15);
		Customer c11 = new Customer(null,"Ale",335566L,"active",CustomerType.NaturalPerson,"12000","123456",u16);

		
		Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), c1);
		Order o2 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c2);
		Order o3 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), c1);
		Order o4 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c2);
		Order o5 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), c3);
		Order o6 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c4);
		Order o7 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), c5);
		Order o8 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c6);
		Order o9 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), c7);
		Order o10 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c8);
		Order o11 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), c9);
		Order o12 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c10);
		Order o13 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), c11);
		Order o14 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c2);
		Order o15 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), c1);
		Order o16 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), c3);

		
		userRepository.saveAll(Arrays.asList(u1,u2,u3,u4,u5,u6,u7,u8,u9,u10,u11,u12,u13,u14,u15,u16));
		customerRepository.saveAll(Arrays.asList(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11));
		orderRepository.saveAll(Arrays.asList(o1,o2,o3,o4,o5,o6,o7,o8,o9,o10,o11,o12,o13,o14,o15,o16));
		
		OrderItem oi1 = new OrderItem(o1, p1, 0.0, 1);
		OrderItem oi2 = new OrderItem(o1, p2, 0.0, 1);
		OrderItem oi3 = new OrderItem(o2, p3, 0.0, 1);
		OrderItem oi4 = new OrderItem(o3, p3, 0.0, 1);
		OrderItem oi5 = new OrderItem(o4, p4, 0.0, 1); 

		
//		o1.setTotal();
//		o2.setTotal();
//		o3.setTotal();
		
		orderItemRepository.saveAll(Arrays.asList(oi1,oi2,oi3,oi4,oi5));
		
		u2.setCustomer(c1);
		u5.setCustomer(c2);
		u6.setCustomer(c3);
		u7.setCustomer(c4);
		u8.setCustomer(c5);
		u9.setCustomer(c6);
		u10.setCustomer(c7);
		u11.setCustomer(c8);
		u12.setCustomer(c9);
		u15.setCustomer(c10);
		u16.setCustomer(c11);
		
		userRepository.saveAll(Arrays.asList(u1,u2,u3,u4,u5,u6,u7,u8,u9,u10,u11,u12,u13,u14,u15,u16));
		
		Role r1 = new Role(null, Authorities.ADMIN);
		Role r2 = new Role(null,Authorities.OPERATOR);
		
		roleRepository.saveAll(Arrays.asList(r1,r2));
		
//		u1.getRoles().add(r1);
//		u1.getRoles().add(r2);
//		u2.getRoles().add(r1);
		
		u1.setRole(r1);
		u2.setRole(r2);
		u3.setRole(r2);
		u4.setRole(r2);
		u5.setRole(r2);
		u6.setRole(r2);
		u7.setRole(r2);
		u8.setRole(r2);
		u9.setRole(r2);
		u10.setRole(r2);
		u11.setRole(r2);
		u12.setRole(r2);
		u13.setRole(r1);
		u14.setRole(r1);
		u15.setRole(r2);
		u16.setRole(r2);
		
		
		userRepository.saveAll(Arrays.asList(u1,u2,u3,u4,u5,u6,u7,u8,u9,u10,u11,u12,u13,u14,u15,u16));
		
		Address ad1 = new Address(null,"7 setembro",1458,"centro","13560180","brasil",AddressType.HomeAddress,c1);
		Address ad2 = new Address(null,"dona alexandrina",1259,"centro","13560240","brasil",AddressType.HomeAddress,c2);
		Address ad3 = new Address(null,"dona alexandrina",1259,"centro","13560240","brasil",AddressType.BusinessAddress,c1);
		
		addressRepository.saveAll(Arrays.asList(ad1,ad2,ad3));
		
		c1.getAddresses().add(ad1);
		c2.getAddresses().add(ad2);
		c1.getAddresses().add(ad3);
		
		customerRepository.saveAll(Arrays.asList(c1,c2));
		
		o1.setDeliveryAddress(ad1);
		o2.setDeliveryAddress(ad2);
		o3.setDeliveryAddress(ad3);
		
		orderRepository.saveAll(Arrays.asList(o1,o2,o3));
		
	}
	
	
}
