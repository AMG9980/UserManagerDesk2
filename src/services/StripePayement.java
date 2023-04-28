/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;

public class StripePayement {

    public static void main(String[] args) {
        Stripe.apiKey = "sk_test_51N0lANCneaseIC3dkmUlLaXjDw9ra1M1t4cTWJ2yakXeI8oom6LtKh7IZD9JUTsrIhcvdXHRMcPNFigfwXEwtem3008XD687LZ";
        RequestOptions requestOptions = RequestOptions.builder()
          .setStripeAccount("acct_1N0lANCneaseIC3d")
          .build();

        CustomerCreateParams params =
            CustomerCreateParams
                .builder()
                .setDescription("lien pour réaliser vos payement")
                .setEmail("ayoub.hammoudi@esprit.tn")
                .setPaymentMethod("pm_card_visa")  
                .build();

        try {
            Customer customer = Customer.create(params);
            System.out.println(customer);
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}