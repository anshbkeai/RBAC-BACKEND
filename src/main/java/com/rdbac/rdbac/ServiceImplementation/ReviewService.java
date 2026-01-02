
package com.rdbac.rdbac.ServiceImplementation;

import org.springframework.stereotype.Service;

import com.rdbac.rdbac.Dto.ReviewDto;
import com.rdbac.rdbac.Pojos.Review;
import com.rdbac.rdbac.Repositry.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review save(ReviewDto review) {
        
        return reviewRepository.save(Review.builder()
                                            .context(review.getContext())
                                            .learning(review.getLearning())
                                            .score(review.getScore())
                                            .text(review.getText())
                                            .understanding(review.getUnderstanding())
                                            .build()
        );
    }
}
