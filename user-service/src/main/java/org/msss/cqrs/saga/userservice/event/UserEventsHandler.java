package org.msss.cqrs.saga.userservice.event;

import org.axonframework.queryhandling.QueryHandler;
import org.msss.cqrs.saga.sharedcommon.payment.PaymentDetails;
import org.msss.cqrs.saga.sharedcommon.payment.UserPayment;
import org.msss.cqrs.saga.sharedcommon.query.FetchUserPaymentDetailsQuery;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {


    @QueryHandler
    public UserPayment queryPayment (FetchUserPaymentDetailsQuery fetchQuery){
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("James Vo")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        UserPayment userRest = UserPayment.builder()
                .firstName("James")
                .lastName("Vo")
                .userId(fetchQuery.getUserId())
                .paymentDetails(paymentDetails)
                .build();

        return userRest;
    }

}
