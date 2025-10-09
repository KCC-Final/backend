package com.kcc.groo.quote.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kcc.groo.quote.data.dto.BookInfoDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AladinBookService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    @Value("${TTB_KEY}") //알라딘 API 키
    private final String TTB_KEY;

    public BookInfoDto getBookByIsbn(String isbn) {
        String url = BASE_URL + "?ttbkey=" + TTB_KEY
                + "&itemIdType=ISBN"
                + "&ItemId=" + isbn
                + "&output=js"
                + "&Version=20131101";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map body = response.getBody();
        if (body == null || body.get("item") == null) return null;

        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("item");
        if (items.isEmpty()) return null;

        Map<String, Object> item = items.get(0);

        return new BookInfoDto(
                (String) item.get("title"),
                (String) item.get("author")
        );
    }
}