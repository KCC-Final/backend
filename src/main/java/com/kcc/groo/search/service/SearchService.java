package com.kcc.groo.search.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.groo.search.dao.ISearchRepository;
import com.kcc.groo.search.data.dto.SearchResult;

@Service
public class SearchService implements ISearchService{
	
	@Autowired
	ISearchRepository searchRepository;
	
	public List<SearchResult> searchAll(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("insert keyword");
        }
        return searchRepository.searchAll(keyword);
    }

}
