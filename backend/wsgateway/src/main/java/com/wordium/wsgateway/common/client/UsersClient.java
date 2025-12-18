package com.wordium.wsgateway.common.client;

import java.util.Collection;
import java.util.List;

import com.wordium.wsgateway.common.dto.UserProfile;

public interface UsersClient {
    List<UserProfile> getUsersByIds(Collection<Long> userIds);
}
