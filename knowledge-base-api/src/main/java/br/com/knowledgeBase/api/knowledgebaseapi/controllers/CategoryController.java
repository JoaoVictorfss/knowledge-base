package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/knowledgeBase-api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(TagController.class);


}
