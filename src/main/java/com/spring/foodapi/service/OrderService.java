package com.spring.foodapi.service;

import com.razorpay.RazorpayException;
import com.spring.foodapi.io.OrderRequest;
import com.spring.foodapi.io.OrderResponse;

import java.util.List;
import java.util.Map;

public interface OrderService  {
 OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;


 void verifyPayment(Map<String, String> paymentData, String status);

 List<OrderResponse> getUserOrders();

 void removeOrder(String orderId);

 List<OrderResponse> getOrdersOfAllUsers();

 void updateOrderStatus(String orderId, String status);

}
