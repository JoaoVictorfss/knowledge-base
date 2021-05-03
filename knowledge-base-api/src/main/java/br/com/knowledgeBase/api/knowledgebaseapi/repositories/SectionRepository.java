package br.com.knowledgeBase.api.knowledgebaseapi.repositories;

import br.com.knowledgeBase.api.knowledgebaseapi.data.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SectionRepository extends JpaRepository<Section, Long> {
    @Query("SELECT s FROM Section s "
            + "JOIN s.categories c "
            + "WHERE c.id = :categoryId")
    Page<Section> findAllByCategoryId(Long categoryId, Pageable pageable);
}
