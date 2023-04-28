/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.*;

import com.stripe.Stripe;
import com.stripe.net.*;
import com.stripe.model.*;
import com.stripe.param.*;

public class DemoRequest {
  public static void main(String[] args) {
    Stripe.apiKey = "sk_test_51N0lANCneaseIC3dkmUlLaXjDw9ra1M1t4cTWJ2yakXeI8oom6LtKh7IZD9JUTsrIhcvdXHRMcPNFigfwXEwtem3008XD687LZ";
    System.out.println("Make requests!");
    
    
    
         PaymentIntentCreateParams params =
       PaymentIntentCreateParams.builder()
         .setAmount(1000L)
         .setCurrency("USD")
         .build();
    
     try {
       PaymentIntent intent =
         PaymentIntent.create(params);
       System.out.println(intent.getId());
       System.out.println(intent.getStatus());
     } catch(Exception e) {
       System.out.println(e);
     }
    
     PaymentIntentConfirmParams para =
       PaymentIntentConfirmParams.builder()
         .setPaymentMethod("pm_card_visa")
         .build();
    
     try {
       PaymentIntent intent =
         PaymentIntent.retrieve(
             "pi_1HuUlQCZ6qsJgndJUduLpPGm"
         );
       PaymentIntent confirmedIntent =
         intent.confirm(para);
       System.out.println(confirmedIntent.getId());
       System.out.println(confirmedIntent.getStatus());
     } catch(Exception e) {
       System.out.println(e);
     }
    
     CustomerCreateParams param =
      CustomerCreateParams.builder()
        .setEmail("ayoub.hammoudi@esprit.tn")
        .build();

    RequestOptions requestOptions =
      RequestOptions.builder()
        .setStripeAccount("acct_1N0lANCneaseIC3d")
        .build();

    try {
      Customer customer =
        Customer.create(param, requestOptions);
      System.out.println(customer);
    } catch(Exception e) {
      System.out.println(e);
    }
  }
}

/**
 *
 * @author HP
 */

