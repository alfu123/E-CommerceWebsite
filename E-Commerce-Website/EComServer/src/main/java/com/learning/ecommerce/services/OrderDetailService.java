package com.learning.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learning.ecommerce.Configuration.JwtRequestFilter;
import com.learning.ecommerce.constants.Constants;
import com.learning.ecommerce.converters.DateConverter;
import com.learning.ecommerce.dao.CartDao;
import com.learning.ecommerce.dao.OrderDetailDao;
import com.learning.ecommerce.dao.ProductDao;
import com.learning.ecommerce.dao.UserDao;
import com.learning.ecommerce.dto.OrderDetailDto;
import com.learning.ecommerce.dto.OrderInputDto;
import com.learning.ecommerce.dto.OrderProductQuantityDto;
import com.learning.ecommerce.dto.TransactionDetailsDto;
import com.learning.ecommerce.models.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";

    private static final String KEY = "rzp_test_mPern6tQSPXbU5";
    private static final String KEY_SECRET = "NaFHu410gHHOnMoWPJ053eC9";
    private static final String CURRENCY = "INR";

    ObjectMapper objMapper=new ObjectMapper();
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CartDao cartDao;

    public ResponseEntity<String> placedOrder(OrderInputDto orderInputDto, boolean isCartCheckout) throws JsonProcessingException {
        JsonNode errorNode = objMapper.createObjectNode();
        JsonNode dataNode = objMapper.createObjectNode();

        List<OrderDetail> orderDetails=new ArrayList<>();
        List<Product> productNotDeleiver=new ArrayList<>();
        int expectedDel=0;
        List<OrderProductQuantityDto> productQuantityList=orderInputDto.getOrderProductQuantityList();
        for(OrderProductQuantityDto pr: productQuantityList){
            Product product=productDao.findById(pr.getProductId()).get();
            String currentUser= JwtRequestFilter.CURRENT_USER;
            User user=userDao.findByUserName(currentUser);
            orderInputDto.getPincode();
//            Serviceability serviceability = product.getServiceability().stream()
//                    .filter(x -> (x.getPincode() == orderInputDto.getPincode())).findAny().orElse(null);

            List<Serviceability>  serviceabilityList=product.getServiceability();
            Serviceability serviceability=null;
            for(Serviceability ser:serviceabilityList){
                if(ser.getPincode()==orderInputDto.getPincode()){
                    expectedDel=Math.max(expectedDel,ser.getExpectedDelivery());
                    serviceability=ser;
                }
            }
            if(serviceability!=null) {
                OrderDetail orderDetail = new OrderDetail(
                        orderInputDto.getFullName(),
                        orderInputDto.getFullAddress(),
                        orderInputDto.getContactNumber(),
                        orderInputDto.getAlternateContactNumber(),
                        ORDER_PLACED,
                        product.getPrice() * pr.getQuantity(),
                        orderInputDto.getPincode(),
                        product, user
                );
                orderDetails.add(orderDetail);
                if(!isCartCheckout){
                    List<Cart> carts=cartDao.findByUser(user);
                    for(Cart cart:carts){
                        if(cart.getProduct().getPid()==product.getPid()){
                            cartDao.deleteById(cart.getCartId());
                        }
                    }
//                    carts.stream().forEach(x->cartDao.deleteById(x.getCartId()));
                }
                orderDetailDao.save(orderDetail);
            }
            else{
                productNotDeleiver.add(product);
            }


        }
        if(orderDetails.size()>0){
            dataNode= objMapper.valueToTree(orderDetails);
            ArrayNode dataNodeArray = (ArrayNode) dataNode; // Cast to ArrayNode

            // Iterate over each element of the array
            for (JsonNode orderDetail : dataNodeArray) {
                // Add additional fields to each element of the array
                ((ObjectNode) orderDetail).put(Constants.AVAILABILITY_FIELD, true);
                ((ObjectNode) orderDetail).put(Constants.DELIVERY_DATE_FIELD, DateConverter.covertDaysIntoDate(expectedDel));
            }

//            dataNode= objMapper.valueToTree(orderDetails);
//            ((ObjectNode) dataNode).put(Constants.AVAILABILITY_FIELD, true);
//            ((ObjectNode) dataNode).put(Constants.DELIVERY_DATE_FIELD,
//                    DateConverter.covertDaysIntoDate(expectedDel));

            return new ResponseEntity<String>(
                    constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNodeArray, null),
                    HttpStatus.OK);
        }
        else{
            ((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_FIELD, Constants.PID_FIELD);
            ((ObjectNode) errorNode).put(Constants.RESPONSE_ERROR_MESSAGE, Constants.NO_PRODUCT_FOUND);
        // Return Error Response with Product Not Found
            return new ResponseEntity<String>(
                constructResponse(Constants.STATUS_SUCCESS, HttpStatus.NOT_FOUND.value(), null, errorNode),
                HttpStatus.OK);


//            ((ObjectNode) dataNode).put(Constants.PINCODE_FIELD, orderInputDto.getPincode());
//            ((ObjectNode) dataNode).put(Constants.AVAILABILITY_FIELD, false);
//
//            // return success response with availability false
//            return new ResponseEntity<String>(
//                    constructResponse(Constants.STATUS_SUCCESS, HttpStatus.OK.value(), dataNode, null), HttpStatus.OK);
        }
    }

    public List<OrderDetail> getAllOrderDetails(String status) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        if(status.equals("All")) {
            orderDetailDao.findAll().forEach(
                    x -> orderDetails.add(x)
            );
        } else {
            orderDetailDao.findByOrderStatus(status).forEach(
                    x -> orderDetails.add(x)
            );
        }

        return orderDetails;
    }


    public TransactionDetailsDto createTransaction(Double amount) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", (amount * 100) );
            jsonObject.put("currency", CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);

            Order order = razorpayClient.orders.create(jsonObject);

            TransactionDetailsDto transactionDetailsDto =  prepareTransactionDetails(order);
            return transactionDetailsDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private TransactionDetailsDto prepareTransactionDetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        TransactionDetailsDto transactionDetailsDto = new TransactionDetailsDto(orderId, currency, amount, KEY);
        return transactionDetailsDto;
    }

    public String constructResponse(String status, int statusCode, ArrayNode dataNode, JsonNode errorNode) {
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
