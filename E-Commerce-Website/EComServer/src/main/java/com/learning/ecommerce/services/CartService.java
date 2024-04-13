package com.learning.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.ecommerce.Configuration.JwtRequestFilter;
import com.learning.ecommerce.converters.CartDtoConverter;
import com.learning.ecommerce.converters.ProductDtoConverter;
import com.learning.ecommerce.converters.UserDtoConverter;
import com.learning.ecommerce.dao.CartDao;
import com.learning.ecommerce.dao.ProductDao;
import com.learning.ecommerce.dao.UserDao;
import com.learning.ecommerce.dto.CartDto;
import com.learning.ecommerce.dto.UserDto;
import com.learning.ecommerce.models.Cart;
import com.learning.ecommerce.models.Product;
import com.learning.ecommerce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CartDtoConverter cartDtoConverter;
    @Autowired
    private ProductDtoConverter productDtoConverter;
    @Autowired
    private UserDtoConverter userDtoConverter;

    ObjectMapper objectMapper=new ObjectMapper();


    public String addToCart(Integer productId) throws JsonProcessingException {
        Product product=productDao.findById(productId).orElse(null);
        CartDto cartDto=new CartDto();
        if(product!=null){
            cartDto.setProductDto(productDtoConverter.convertEntityToDto(product));
        }
        User user=null;
        String username = JwtRequestFilter.CURRENT_USER;
        user=userDao.findByUserName(username);
        cartDto.setUserDto(userDtoConverter.convertEntityToDto(user));
        return objectMapper.writeValueAsString(cartDao.save(cartDtoConverter.convertDtoToEntity(cartDto)));

    }
}
