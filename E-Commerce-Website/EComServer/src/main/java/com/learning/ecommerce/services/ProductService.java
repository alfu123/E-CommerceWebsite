package com.learning.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.learning.ecommerce.Configuration.JwtRequestFilter;
import com.learning.ecommerce.dao.CartDao;
import com.learning.ecommerce.dao.UserDao;
import com.learning.ecommerce.dto.ProductDto;
import com.learning.ecommerce.models.Cart;
import com.learning.ecommerce.models.Product;
import com.learning.ecommerce.models.Serviceability;
import com.learning.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learning.ecommerce.constants.Constants;
import com.learning.ecommerce.converters.DateConverter;
import com.learning.ecommerce.converters.ProductDtoConverter;
import com.learning.ecommerce.dao.ProductDao;

@Service
public class ProductService {
	ObjectMapper objMapper = new ObjectMapper();

	@Autowired
	ProductDao productDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private CartDao cartDao;
	@Autowired
	ProductDtoConverter pdc;

	// Method to return Product or a List of product
	public ResponseEntity<String> getProduct(Integer pid, String pname, String brand, Integer page) {
		JsonNode errorNode = objMapper.createObjectNode();
		JsonNode dataNode = objMapper.createObjectNode();
		// Create Pageable object for pagination
		Pageable pageable = PageRequest.of(page,12);
		// Initialize a Page object to store the paginated results
		Page<Product> productPage;

		// Initiate a new ArrayList of Products
//		List<Product> products = new ArrayList<>();

		// Check and Add products in ArrayList with given parameters
		if (pid != null && pname != null && brand != null) {

			productPage= productDao.findByPidOrPnameOrBrand(pid, pname, brand,pageable);
//			products.addAll(productDao.findByPidOrPnameOrBrand(pid, pname, brand,pageable));
		} else if (pid != null && pname != null) {
			productPage=productDao.findByPidOrPname(pid, pname,pageable);
//			products.addAll(productDao.findByPidOrPname(pid, pname,pageable));
		} else if (pid != null && brand != null) {
			productPage=productDao.findByPidOrBrand(pid, brand,pageable);
//			products.addAll(productDao.findByPidOrBrand(pid, brand,pageable));
		} else if (pname != null && brand != null) {
			productPage=productDao.findByPnameOrBrand(pname, brand,pageable);
//			products.addAll(productDao.findByPnameOrBrand(pname, brand,pageable));
		} else if (pid != null) {
			Product product = productDao.findById(pid).orElse(null);
			if (product != null) {
				productPage = productDao.findByPid(pid, pageable);
//				products.add(productDao.findById(pid,pageable));
			}
			else {
				productPage = Page.empty(); // Empty page if product not found
			}
		} else if (pname != null) {
			productPage=productDao.findByPname(pname,pageable);
//			products.add(productDao.findByPname(pname,pageable));
		} else if (brand != null) {
			productPage=productDao.findByBrand(brand,pageable);
//			products.addAll(productDao.findByBrand(brand,pageable));
		} else {
			productPage=productDao.findAll(pageable);
//			products.addAll(productDao.findAll(pageable));
		}

		// Check if products exists in ArrayList
		if (productPage.hasContent()) {
			// Map Product into JsonNode
			dataNode = objMapper
					.valueToTree(productPage.getContent().stream().map(x -> pdc.convertEntityToDto(x)).collect(Collectors.toList()));
			// Return Success Response
			return new ResponseEntity<String>(
					constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null), HttpStatus.OK);
		}

		// Send an error response if no product with corresponding fields exists
		((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.NO_PRODUCT_FOUND);
		// Return Error Response
		return new ResponseEntity<String>(
				constructResponse(Constants.STATUS_ERROR, HttpStatus.NOT_FOUND.value(), null, errorNode),
				HttpStatus.OK);
	}

	// Method to check availability of product with corresponding pincode
	public ResponseEntity<String> getAvailability(int pid, int pincode) {
		JsonNode errorNode = objMapper.createObjectNode();
		JsonNode dataNode = objMapper.createObjectNode();
		// Get product from pid
		Product product = productDao.findById(pid).orElse(null);

		// Check if product is exists
		if (product != null) {
			// Get from product pincode
			Serviceability serviceability = product.getServiceability().stream()
					.filter(x -> (x.getPincode() == pincode)).findAny().orElse(null);

			// check if serviceability exists
			if (serviceability != null) {
				// Construct Delivery Date Response
				((ObjectNode) dataNode).put(Constants.PID_FIELD, pid);
				((ObjectNode) dataNode).put(Constants.PINCODE_FIELD, pincode);
				((ObjectNode) dataNode).put(Constants.AVAILABILITY_FIELD, true);
				((ObjectNode) dataNode).put(Constants.DELIVERY_DATE_FIELD,
						DateConverter.covertDaysIntoDate(serviceability.getExpectedDelivery()));

				// return Success Response
				return new ResponseEntity<String>(
						constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null),
						HttpStatus.OK);
			}

			// Not Available in given pincode
			((ObjectNode) dataNode).put(Constants.PID_FIELD, pid);
			((ObjectNode) dataNode).put(Constants.PINCODE_FIELD, pincode);
			((ObjectNode) dataNode).put(Constants.AVAILABILITY_FIELD, false);

			// return success response with availability false
			return new ResponseEntity<String>(
					constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null), HttpStatus.OK);
		}
		// Product with pid not found
		((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_FIELD, Constants.PID_FIELD);
		((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.NO_PRODUCT_FOUND);
		// Return Error Response with Product Not Found
		return new ResponseEntity<String>(
				constructResponse(Constants.STATUS_SUCCESS, HttpStatus.NOT_FOUND.value(), null, errorNode),
				HttpStatus.OK);
	}

	// Method to add Product in database
	public String addProduct(ProductDto productDto) {
		Product product = productDao.findById(productDto.getPid()).orElse(null);

		// Product ID Does not Exists
		if (product == null) {
			try {
				return objMapper.writeValueAsString(productDao.save(pdc.convertDtoToEntity(productDto)));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return "Some Error Occurred";
			}
		}

		// Return Product ID Already Exists
		return "Product Already Exists";
	}



	public ResponseEntity<String> getProductDetails(boolean isSingleProductCheckout, Integer productId) {

//		if(isSingleProductCheckout && productId != 0) {
//			// we are going to buy a single product
//
//			List<Product> list = new ArrayList<>();
//			Product product = productDao.findById(productId).get();
//			list.add(product);
//			return list;
//		}
//		return new ArrayList<>();
		JsonNode errorNode = objMapper.createObjectNode();
		JsonNode dataNode = objMapper.createObjectNode();
		List<Product> list = new ArrayList<>();
		if(isSingleProductCheckout && productId!=null){

			Product product = productDao.findById(productId).get();
			list.add(product);
			if(list.size()>0){
				dataNode = objMapper
						.valueToTree(list);
				// Return Success Response
				return new ResponseEntity<String>(
						constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null), HttpStatus.OK);
			}
			((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.NO_PRODUCT_FOUND);
			// Return Error Response
			return new ResponseEntity<String>(
					constructResponse(Constants.STATUS_ERROR, HttpStatus.NOT_FOUND.value(), null, errorNode),
					HttpStatus.OK);
		}
		else{
			String username= JwtRequestFilter.CURRENT_USER;
			User user=userDao.findByUserName(username);
			List<Cart> carts=cartDao.findByUser(user);

			if(carts.size()>0){
				dataNode=objMapper.valueToTree(carts.stream().map(Cart::getProduct).collect(Collectors.toList()));

				return new ResponseEntity<String>(constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null)
						, HttpStatus.OK);
			}
			((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.NO_PRODUCT_FOUND);
			// Return Error Response
			return new ResponseEntity<String>(
					constructResponse(Constants.STATUS_ERROR, HttpStatus.NOT_FOUND.value(), null, errorNode),
					HttpStatus.OK);
		}

	}

	// Method to construct response
	public String constructResponse(String status, int statusCode, JsonNode dataNode, JsonNode errorNode) {
		String response = "";

		// Create a new Empty Object Node
		ObjectNode objNode = objMapper.createObjectNode();
		objNode.put(Constants.RESPONSE_STATUS, status);
		objNode.put(Constants.RESPONSE_STATUS_CODE, statusCode);
		objNode.set(Constants.RESPONSE_DATA_STRING, dataNode);
		objNode.set(Constants.RESPONSE_ERRORS, errorNode);

		try {
			response = objMapper.writeValueAsString(objNode);
		} catch (JsonProcessingException e) {
			response = null;
		}

		return response;
	}
}
