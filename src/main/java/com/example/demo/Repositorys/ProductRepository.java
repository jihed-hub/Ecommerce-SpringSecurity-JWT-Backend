package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	void deleteByProductid(int productid);
	
	Product findByProductid(int productid);

}
