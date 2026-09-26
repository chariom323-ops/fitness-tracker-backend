package com.project.fitness.service;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
  private final UserRepository userRepository;
  private final ActivityRepository activityRepository;
    public Recommendation generateRecommendation(RecommendationRequest request) {
        User user=userRepository.findById(request.getUserId())
        .orElseThrow(()->new RuntimeException(" user not found:"+request.getUserId()));

     Activity activity =activityRepository.findById(request.getActivityId())
                .orElseThrow(()->new RuntimeException(" activity not found:"+request.getActivityId()));

        Recommendation recommendation=Recommendation.builder()
                .user(user)
                .activity(activity)
                .improvements(request.getImprovements())
                .suggestions(request.getSuggestions())
                .safety(request.getSafety())
                .build();


        Recommendation savedRecommendation=recommendationRepository.save(recommendation);
                return savedRecommendation;
    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
