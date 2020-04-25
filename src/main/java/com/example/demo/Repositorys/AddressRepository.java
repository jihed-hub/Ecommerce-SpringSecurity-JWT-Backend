package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Address;
import com.example.demo.Models.User;
@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{

	Address findByUser(User user);
}
