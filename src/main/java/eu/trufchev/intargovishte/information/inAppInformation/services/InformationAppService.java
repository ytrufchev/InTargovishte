package eu.trufchev.intargovishte.information.inAppInformation.services;

import eu.trufchev.intargovishte.information.events.appEvents.enums.StatusENUMS;
import eu.trufchev.intargovishte.information.inAppInformation.entity.Information;
import eu.trufchev.intargovishte.information.inAppInformation.repositories.InformationEntityRepository;
import eu.trufchev.intargovishte.user.entity.User;
import eu.trufchev.intargovishte.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationAppService {
    @Autowired
    private InformationEntityRepository informationEntityRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Information> getInformation() {
        List<Information> infos = new ArrayList<>();
        informationEntityRepository.findAll().forEach(infos::add);
        return infos;
    }

    public Information addInformation(String title, String validTo, String description,  Long user, StatusENUMS status) {
        Information info = new Information();
        info.setTitle(title);
        info.setValidTo(validTo);
        info.setDescription(description);
        info.setUser(user);
        info.setStatus(status);

        // Save and return the event
        return informationEntityRepository.save(info);
    }
    public List<Information> findNextTenApprovedInformation() {
            // Get the current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = null;
            if (authentication != null) {
                currentUser = userRepository.findByUsernameOrEmail(authentication.getName(), authentication.getName())
                        .orElse(null);
            }

            Long today = System.currentTimeMillis();
            List<Information> infos = new ArrayList<>();
            informationEntityRepository.findNextTenApprovedInformation(today, StatusENUMS.APPROVED).forEach(infos::add);

            return infos;
    }
}
