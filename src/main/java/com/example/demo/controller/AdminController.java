package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Models.PlaceOrder;
import com.example.demo.Models.Product;
import com.example.demo.Models.ViewOrder;
import com.example.demo.Repositorys.CartRepository;
import com.example.demo.Repositorys.PlaceOrderRepository;
import com.example.demo.Repositorys.ProductRepository;
import com.example.demo.payload.PlaceOrderResponse;
import com.example.demo.payload.ProductResponse;
import com.example.demo.payload.ViewOrderResponse;
import com.example.demo.payload.dto.ProductDto;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private PlaceOrderRepository placeOrderRepository;
	@Autowired
	private CartRepository cartRepository;
	
	@PostMapping("/addProduct")
	public ResponseEntity<?> addProduct( 
			   @RequestParam(name="file") MultipartFile prodImage,
			   @RequestParam(name="productname") String productname,
			   @RequestParam(name="price") String price,
			   @RequestParam(name="description") String description,
			   @RequestParam(name="quantity") String quantity) throws IOException{
		Product product=new Product();
		product.setProductname(productname);
		product.setDescription(description);
		product.setPrice(Double.parseDouble(price));
		product.setQuantity(Integer.parseInt(quantity));
		product.setProductimage(prodImage.getBytes());
		productRepository.save(product);
		return ResponseEntity.ok(new ProductResponse("Product Saved Succeffuly",productRepository.findAll()));	
	}
	@GetMapping("/getProducts")
	public ResponseEntity<?> getProducts(){
		return ResponseEntity.ok(new ProductResponse("Your Products are in the List Below",productRepository.findAll()));
	}
	@DeleteMapping("/delProduct")
	@Transactional
	public ResponseEntity<?> delProduct(@RequestParam (name="productid") int productid){
		productRepository.deleteByProductid(productid);
		return ResponseEntity.ok(new ProductResponse("Your Product with ID"+ productid + " deleted succeffuly"
				,productRepository.findAll()));		
	}
	@PutMapping("/updProduct")
	public ResponseEntity<?> updProduct(@RequestParam(name="file",required = false) MultipartFile prodImage,
			   @RequestParam(name="productname") String productname,
			   @RequestParam(name="price") String price,
			   @RequestParam(name="description") String description,
			   @RequestParam(name="quantity") String quantity,
			   @RequestParam (name="productid") int productid) throws IOException{
		Product p1=productRepository.findByProductid(productid);
		Product p2=productRepository.findByProductid(productid);
		if(prodImage!=null){
			p1.setProductname(productname);
			p1.setDescription(description);
			p1.setPrice(Double.parseDouble(price));
			p1.setQuantity(Integer.parseInt(quantity));
			p1.setProductimage(prodImage.getBytes());}
		else{
			p1.setProductname(productname);
			p1.setDescription(description);
			p1.setPrice(Double.parseDouble(price));
			p1.setQuantity(Integer.parseInt(quantity));
			p1.setProductimage(p2.getProductimage());}
		productRepository.save(p1);
		return ResponseEntity.ok(new ProductResponse("Your Product with ID " + productid + " updated succeffuly",p1));
	}
	@PutMapping(path = { "/updOrder/{orderId}/{orderStatus}" })
	public ResponseEntity<?> updOrder(@PathVariable("orderId") int orderid, @PathVariable("orderStatus") String orderstatus){
		PlaceOrder po=placeOrderRepository.findByOrderId(orderid);
		po.setOrderStatus(orderstatus);
		placeOrderRepository.save(po);
		return ResponseEntity.ok(new PlaceOrderResponse("the Order with ID "+orderid+"updated succeffuly", po));	
	}
	@GetMapping("/getOrders")
	public ResponseEntity<?> getOrders(){
		List<ViewOrder> viewOrders=new ArrayList<>();
		List<PlaceOrder> placeOrders=placeOrderRepository.findAll();
		placeOrders.forEach((po) ->{
			ViewOrder vo=new ViewOrder();
			vo.setOrderId(po.getOrderId());
			vo.setOrderBy(po.getEmail());
			vo.setOrderStatus(po.getOrderStatus());
			vo.setCartList(cartRepository.findAllByOrderId(po.getOrderId()));
			viewOrders.add(vo);
		});
		return ResponseEntity.ok(new ViewOrderResponse("this is all the Orders", viewOrders));		
	}
}	
