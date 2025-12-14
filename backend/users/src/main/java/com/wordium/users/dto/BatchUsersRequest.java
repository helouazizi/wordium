package com.wordium.users.dto;

import java.util.List;

public record BatchUsersRequest(List<Long> usersIds) {

}
