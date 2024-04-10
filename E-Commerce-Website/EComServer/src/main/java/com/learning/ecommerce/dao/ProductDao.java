package com.learning.ecommerce.dao;

import java.util.List;
import java.util.Optional;

import com.learning.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
	// Find By Product Name
	Page findByPname(String pname, Pageable pageable);

	// Find By Product Brand
	Page<Product> findByBrand(String brand, Pageable pageable);

	// Find By Product id & Name
	Page<Product> findByPidOrPname(int pid, String pname,Pageable pageable);

	// Find By Product id & brand
	Page<Product> findByPidOrBrand(int pid, String brand, Pageable pageable);

	// Find By Product Name & Brand
	Page<Product> findByPnameOrBrand(String pname, String brand, Pageable pageable);

	// Find By Product id, Name & Brand
	Page<Product> findByPidOrPnameOrBrand(int pid, String pname, String brand, Pageable pageable);

	Page<Product> findByPid(Integer pid, Pageable pageable);

//	Page findById(Integer pid, Pageable pageable);


}
