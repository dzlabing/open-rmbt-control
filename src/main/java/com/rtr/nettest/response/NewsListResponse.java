package com.rtr.nettest.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class NewsListResponse {
    private final List<NewsResponse> news;
}
