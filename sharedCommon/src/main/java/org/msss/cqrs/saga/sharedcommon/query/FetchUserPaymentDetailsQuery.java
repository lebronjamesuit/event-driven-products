package org.msss.cqrs.saga.sharedcommon.query;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class FetchUserPaymentDetailsQuery {

    private String userId;

}
