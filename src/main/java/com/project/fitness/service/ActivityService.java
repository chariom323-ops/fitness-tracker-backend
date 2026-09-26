package com.project.fitness.service;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    public ActivityResponse trackActivity(ActivityRequest request)
    {
        User user=userRepository.findById(request.getUserId())
                .orElseThrow(()->new RuntimeException("invalid user :"+request.getUserId()));
        Activity activity=Activity.builder()
                .user(user)
                .type(request.getType())
                .duration(request.getDuration())
                .calloriesBurned(request.getCalloriesBurned())
                .startTime(request.getStartTime())
                .additionalMatrics(request.getAdditionalMatrics())
                .build();

        Activity savedActivity =activityRepository.save(activity);
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response=new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUser().getId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setAdditionalMatrics(activity.getAdditionalMatrics());
        response.setCalloriesBurned(activity.getCalloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

     return response;
    }

    public List<ActivityResponse> getUserActivites(String userId) {
  List<Activity> activityList=activityRepository.findByUserId(userId);
  return activityList.stream()
          .map(this::mapToResponse)
          .collect(Collectors.toList());
    }
}
