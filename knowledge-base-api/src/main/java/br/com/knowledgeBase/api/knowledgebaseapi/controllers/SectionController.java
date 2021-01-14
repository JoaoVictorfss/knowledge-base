package br.com.knowledgeBase.api.knowledgebaseapi.controllers;

import br.com.knowledgeBase.api.knowledgebaseapi.dtos.SectionDto;
import br.com.knowledgeBase.api.knowledgebaseapi.entities.Section;
import br.com.knowledgeBase.api.knowledgebaseapi.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/knowledgeBase-api/sections")
@CrossOrigin(origins = "*")
public class SectionController {
    private static final Logger log = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    private SectionService sectionService;

    /**
     *Convert section to SectionDto
     *
     * @param section
     * @return SectionDto
     */
    private SectionDto convertSectionToSectionDto(Section section){
        SectionDto sectionDto = new SectionDto();

        sectionDto.setId(section.getId());
        sectionDto.setTitle(section.getTitle());
        sectionDto.setSubtitle(section.getSubtitle());
        sectionDto.setSlug(section.getSlug());
        //sectionDto.setCreated_at(Optional.of(section.getCreated_at()));
        //sectionDto.setUpdated_at(Optional.of(section.getUpdated_at()));
        sectionDto.setCreated_by(section.getCreated_by());
        sectionDto.setUpdated_by(section.getUpdated_by());

        return  sectionDto;
    }

    /**
     * Convert DTO data to Section
     *
     * @param sectionDto
     * @return Section
     */
    private Section convertDtoToSection(SectionDto sectionDto){
        Section section = new Section();
        section.setTitle(sectionDto.getTitle());
        section.setSubtitle(sectionDto.getSubtitle());
        section.setSlug(sectionDto.getSlug());
        section.setCreated_by(sectionDto.getCreated_by());
        section.setUpdated_by(sectionDto.getCreated_by());

        return section;
    }
}
