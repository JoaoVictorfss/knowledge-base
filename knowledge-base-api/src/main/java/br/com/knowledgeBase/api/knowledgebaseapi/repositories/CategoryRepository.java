package br.com.knowledgeBase.api.knowledgebaseapi.repositories;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CategoryRepository extends JpaRepository<Category, Long> { }
