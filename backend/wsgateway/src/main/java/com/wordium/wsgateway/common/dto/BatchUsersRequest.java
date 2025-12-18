package com.wordium.wsgateway.common.dto;

import java.util.List;

public record BatchUsersRequest(List<Long> usersIds) {

}
