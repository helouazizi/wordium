package com.wordium.posts.dto;

import java.util.List;

public record BatchUsersRequest(List<Long> usersIds) {

}
