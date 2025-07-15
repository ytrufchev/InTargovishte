package eu.trufchev.intargovishte.information.inAppInformation.services;

import eu.trufchev.intargovishte.information.events.appEvents.entities.AppEventLike;
import eu.trufchev.intargovishte.information.events.appEvents.entities.EventEntity;
import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.inAppInformation.DTO.InfoDTO;
import eu.trufchev.intargovishte.information.inAppInformation.entities.Information;
import eu.trufchev.intargovishte.information.inAppInformation.repositories.InformationRepository;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationService {
    @Autowired
    InformationRepository informationRepository;
    @Autowired
    UserRepository userRepository;


    public Information addInformation(InfoDTO infoDTO) {
        Information info = new Information();
        info.setTitle(infoDTO.getTitle());
                if(infoDTO.getDescription() != null){
                   info.setDescription(infoDTO.getDescription());
                }
                else{
                    info.setDescription("");
                }
        info.setEndDate(infoDTO.getEndDate());
                if(infoDTO.getImage() != null){
                    info.setImage(infoDTO.getImage());
                }
                else{
                    info.setImage("");
                }
        info.setUserId(infoDTO.getUserId());
        info.setStatus(StatusENUMS.PENDING);
        // Save and return the event
        return informationRepository.save(info);
    }
    public List<InfoDTO> getApprovedInfo(){
        List<InfoDTO> info = new ArrayList<>();
        List<Information> information = new ArrayList<>();
                informationRepository.findByStatus(StatusENUMS.APPROVED).forEach(information::add);
        for(Information infoResponse : information){
            InfoDTO infoDto = new InfoDTO();
            infoDto.setTitle(infoResponse.getTitle());
            infoDto.setDescription(infoResponse.getDescription());
            infoDto.setImage(infoResponse.getImage());
            infoDto.setEndDate(infoResponse.getEndDate());
            infoDto.setUserId(infoResponse.getUserId());
            info.add(infoDto);
        }
        return info;
    }
    @Transactional
    public void deleteInformation(Long infoId, String requestingUsername) {
        User requestingUser = userRepository.findByUsername(requestingUsername);
        if (requestingUser == null) {
            throw new AccessDeniedException("Authenticated user not found in database.");
        }

        Information infoForDeletion = informationRepository.findById(infoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with ID: " + infoId));

        if (!infoForDeletion.getUserId().equals(requestingUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this event.");
        }
        informationRepository.delete(infoForDeletion);
    }
    public List<InfoDTO> getPendingInfo() {
        List<InfoDTO> info = new ArrayList<>();
        informationRepository.findByStatus(StatusENUMS.PENDING).forEach(entity -> {
            InfoDTO dto = new InfoDTO();
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setImage(entity.getImage());
            dto.setEndDate(entity.getEndDate());
            dto.setUserId(entity.getUserId()); // Include userId for admin context
            info.add(dto);
        });
        return info;
    }

}
