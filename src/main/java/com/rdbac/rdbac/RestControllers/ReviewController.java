package com.rdbac.rdbac.RestControllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.Dto.ReviewDto;
import com.rdbac.rdbac.Pojos.Review;
import com.rdbac.rdbac.ServiceImplementation.ReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String>  saveReview(@RequestBody ReviewDto entity) {
       reviewService.save(entity);
        return ResponseEntity.ok("Review Saved");
    }
    

}
