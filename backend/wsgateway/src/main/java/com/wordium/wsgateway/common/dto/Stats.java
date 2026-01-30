package com.wordium.wsgateway.common.dto;

public record Stats(
        int followers,
        int following,
        int posts,
        int bookmarks) {
}
