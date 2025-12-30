package com.wordium.users.dto.users;

import java.util.List;

public record BatchUsersRequest(List<Long> usersIds) {

}
