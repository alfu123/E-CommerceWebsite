package com.learning.ecommerce.converters;

import com.learning.ecommerce.dto.*;
import com.learning.ecommerce.models.*;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CartDtoConverter {

    public CartDto convertEntityToDto(Cart cart){
        CartDto cartDto=new CartDto();
        cartDto.setCartId(cart.getCartId());
        ServiceabilityDtoConverter serviceabilityDtoConverter = new ServiceabilityDtoConverter();

        // Convert Serviceability entities of the product to DTOs
        List<ServiceabilityDto> serviceabilityDtos = cart.getProduct().getServiceability().stream()
                .map(serviceabilityDtoConverter::convertEntityToDto)
                .collect(Collectors.toList());

        // Create a new ProductDto and set Serviceability DTOs
        ProductDto productDto = new ProductDto();
        productDto.setServiceability(serviceabilityDtos);
        cartDto.setProductDto(productDto);

        RoleDtoConverter roleDtoConverter=new RoleDtoConverter();
        Set<RoleDto> roleDtos=cart.getUser().getRole().stream().map(roleDtoConverter::convertEntityToDto).collect(Collectors.toSet());
        UserDto userDto=new UserDto();
        userDto.setRole(roleDtos);
        cartDto.setUserDto(userDto);
        return cartDto;
    }

    public Cart convertDtoToEntity(CartDto cartDto){
        Cart cart=new Cart();
        cart.setCartId(cartDto.getCartId());
        System.out.println(cartDto.getProductDto().getServiceability().get(0).getExpectedDelivery());

        ProductDtoConverter productDtoConverter=new ProductDtoConverter();
        cart.setProduct(productDtoConverter.convertDtoToEntity(cartDto.getProductDto()));

//        ServiceabilityDtoConverter serviceabilityDtoConverter = new ServiceabilityDtoConverter();
//        // Convert Serviceability DTOs to product Entity
//        List<Serviceability> serviceability = cartDto.getProductDto().getServiceability().stream()
//                .map(serviceabilityDto -> {
//                    try {
//                        return serviceabilityDtoConverter.convertDtoToEntity(serviceabilityDto);
//                    } catch (ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .collect(Collectors.toList());
//        Product product=new Product();
//        product.setServiceability(serviceability);
//        cart.setProduct(product);

//        RoleDtoConverter roleDtoConverter=new RoleDtoConverter();
//        Set<Role> role=cartDto.getUserDto().getRole().stream().map(roleDtoConverter::convertDtoToEntity).collect(Collectors.toSet());
//        User user=new User();
//        user.setRole(role);
//        cart.setUser(user);

        UserDtoConverter userDtoConverter=new UserDtoConverter();
        cart.setUser(userDtoConverter.convertDtoToEntity(cartDto.getUserDto()));
        return cart;

    }
}
