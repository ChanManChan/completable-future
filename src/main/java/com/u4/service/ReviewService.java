package com.u4.service;

import com.u4.domain.Review;

import static com.u4.util.CommonUtil.delay;

public class ReviewService {

    public Review retrieveReviews(String productId) {
        delay(1000);
        return new Review(200, 4.5);
    }
}
