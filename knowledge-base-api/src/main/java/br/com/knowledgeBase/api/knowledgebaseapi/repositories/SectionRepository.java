package br.com.knowledgeBase.api.knowledgebaseapi.repositories;

import br.com.knowledgeBase.api.knowledgebaseapi.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SectionRepository extends JpaRepository<Section, Long> {
}
