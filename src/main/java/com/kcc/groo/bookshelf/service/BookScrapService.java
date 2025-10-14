package com.kcc.groo.bookshelf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.groo.bookshelf.dao.IBookScrapRepository;

@Service
public class BookScrapService implements IBookScrapService {
	
	@Autowired
	IBookScrapRepository bookScrapRepository;

}
