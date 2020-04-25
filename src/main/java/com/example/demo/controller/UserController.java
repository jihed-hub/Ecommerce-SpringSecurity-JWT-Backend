package com.example.demo.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Address;
import com.example.demo.Models.Cart;
import com.example.demo.Models.PlaceOrder;
import com.example.demo.Models.Product;
import com.example.demo.Models.User;
import com.example.demo.Repositorys.AddressRepository;
import com.example.demo.Repositorys.CartRepository;
import com.example.demo.Repositorys.PlaceOrderRepository;
import com.example.demo.Repositorys.ProductRepository;
import com.example.demo.Repositorys.UserRepository;
import com.example.demo.configs.JwtUtils;
import com.example.demo.payload.CartResponse;
import com.example.demo.payload.PlaceOrderResponse;
import com.example.demo.payload.ProductResponse;
import com.example.demo.payload.UserResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
	
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private PlaceOrderRepository placeOrderRepository;
	
	public User getCurrentUser(Principal p){
		String username = p.getName();
		User u =new User();
		if(u!=null){
			u=userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		}
		return u;
	}
	@PostMapping("/addAddress")
	public ResponseEntity<?> addAddress(@Valid @RequestBody Address address,Principal p){
		User user=getCurrentUser(p);
		user.setAddress(address);
		address.setUser(user);
		Address adr=addressRepository.saveAndFlush(address);
		return ResponseEntity.ok(new UserResponse("Address saved Succeffuly"));
	}
	@GetMapping("/getAddress")
	public ResponseEntity<?> getAddress(Principal p){
		User user=getCurrentUser(p);
		Address adr=addressRepository.findByUser(user);
		HashMap<String,String> map=new HashMap<>();
		map.put("address", adr.getAddress());
		map.put("city", adr.getCity());
		map.put("state", adr.getState());
		map.put("country", adr.getCountry());
		map.put("zipcode", String.valueOf(adr.getZipcode()));
		map.put("phonenumber", adr.getPhonenumber());
		return ResponseEntity.ok(new UserResponse("the address saved succeffuly",map));			
	}
	@GetMapping("/getProducts")
	public ResponseEntity<?> getProduct(){
		return ResponseEntity.ok(new ProductResponse("Your Products are in the List Below",productRepository.findAll()));
	}
	@PostMapping("/addProductToCart")
	public ResponseEntity<?> addProductToCart(@RequestParam("productid") int productid,Principal p){
		User user=getCurrentUser(p);
		Product product=productRepository.findByProductid(productid);
		if(cartRepository.existsByProductId(product.getProductid())){
			return ResponseEntity.ok(new CartResponse("the product you attempt to add to your cart exists already"));
		}
		Cart cart=new Cart();
		cart.setProductId(productid);
		cart.setProductname(product.getProductname());
		cart.setPrice(product.getPrice());
		cart.setQuantity(1);
		cart.setEmail(user.getEmail());
		Date date=new Date();
		cart.setDateAdded(date);
		cartRepository.save(cart);
		return ResponseEntity.ok(new CartResponse("the product has been saved in your cart",cartRepository.findByEmail(user.getEmail())));		
	}
	@GetMapping("/viewCart")
	public ResponseEntity<?> getCart(Principal p){
		User user=getCurrentUser(p);
		return ResponseEntity.ok(new CartResponse("cart view",cartRepository.findByEmail(user.getEmail())));		
	}
	@PutMapping("/updCart")
	public ResponseEntity<?> updateCart(@RequestParam("cartId") int cartId,@RequestParam("quantity")int quantity,Principal p){
		User user=getCurrentUser(p);
		Cart cart=cartRepository.findByCartIdAndEmail(cartId, user.getEmail());
		cart.setQuantity(quantity);
		cartRepository.save(cart);
		return ResponseEntity.ok(new CartResponse("the cart has been updated succeffuly",cartRepository.findByEmail(user.getEmail())));
	}
	@DeleteMapping("/delCart")
	@Transactional
	public ResponseEntity<?> deleteCart(@RequestParam("cartId") int cartId,Principal p){
		User user=getCurrentUser(p);
		cartRepository.deleteByCartIdAndEmail(cartId, user.getEmail());
		return ResponseEntity.ok(new CartResponse("the cart has been deleted succeffuly",cartRepository.findByEmail(user.getEmail())));

	}
	@PostMapping("/placeOrder")
	public ResponseEntity<?> placeOrder(Principal p){
		User user=getCurrentUser(p);
		PlaceOrder po=new PlaceOrder();
		po.setEmail(user.getEmail());
		po.setOrderStatus("pending");
		Date date=new Date();
		po.setOrderDate(date);
		double total=0;
		List<Cart> carts=cartRepository.findAllByEmail(user.getEmail());
		for(Cart cart: carts){
			total=cart.getQuantity()*cart.getPrice();
		}
		po.setTotalCost(total);
		PlaceOrder PO=placeOrderRepository.save(po);
		carts.forEach(c->{
			c.setOrderId(PO.getOrderId());
			cartRepository.save(c);
		});
		
		return ResponseEntity.ok(new PlaceOrderResponse("your order is sent"));
		
	}
}
