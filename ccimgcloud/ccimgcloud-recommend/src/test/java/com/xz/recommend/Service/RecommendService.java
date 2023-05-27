package com.xz.recommend.Service;



import com.xz.recommend.Dto.UserSet;

import java.util.List;
import java.util.Map;

public interface RecommendService {

    Map<Double, String> computeNearestNeighbor(String username, UserSet set);

    double pearson_dis(List<UserSet.Set> rating1, List<UserSet.Set> rating2);

    public List<UserSet.Set> recommend(String username, UserSet set);

}
