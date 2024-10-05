package eu.trufchev.intargovishte.information.events.cinemagic.services;

import eu.trufchev.intargovishte.information.events.cinemagic.dto.ProjectionsDTO;
import eu.trufchev.intargovishte.information.events.cinemagic.entities.Projections;

import java.util.ArrayList;
import java.util.List;

public class ProjectionsDTOToProjections {
    public List<Projections> toProjections(List<ProjectionsDTO> projectionsDTO){
        List<Projections> projections = new ArrayList<>(List.of());
        for (int i = 0; i < projectionsDTO.size(); i++) {
            Projections projection = new Projections(
                    projectionsDTO.get(i).getId(),
                    projectionsDTO.get(i).getScreenId(),
                    projectionsDTO.get(i).getScreeningTimeFrom(),
                    projectionsDTO.get(i).getScreeningTimeTo(),
                    projectionsDTO.get(i).getAudience(),
                    projectionsDTO.get(i).getMaxOccupancy(),
                    projectionsDTO.get(i).getMovieId(),
                    projectionsDTO.get(i).getPrintType(),
                    projectionsDTO.get(i).getLanguage(),
                    projectionsDTO.get(i).getSubtitles()
            );
            projections.add(projection);
        }
        return projections;
    }
}
