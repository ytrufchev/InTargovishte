package eu.trufchev.intargovishte.information.events.dramaTheatre.service;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Event;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Production;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.UpcomingEvent;

import java.util.*;
import java.util.stream.Collectors;

public class ResponseToPlayList {

    public List<Play> playsResponseToPlays(List<Production> productions, List<Event> events) {
        // Map events by production ID for faster lookup
        Map<String, List<Event>> eventsByProductionId = new HashMap<>();
        System.out.println(events);
        for (Event event : events) {
            eventsByProductionId
                    .computeIfAbsent(event.getProduction().getId(), k -> new ArrayList<>())
                    .add(event);
        }

        List<Play> playList = new ArrayList<>();

        // Filter each production's upcomingEvents to only keep those in "Търговище"
        productions.forEach(production -> {
            if (production.getUpcomingEvents() != null) {
                List<UpcomingEvent> targovishteEvents = production.getUpcomingEvents().stream()
                        .filter(event -> event.getLocation() != null &&
                                "Търговище".equals(event.getLocation().getCityName()))
                        .collect(Collectors.toList());

                // Set to null if no events remain
                if (targovishteEvents.isEmpty()) {
                    production.setUpcomingEvents(null);
                } else {
                    production.setUpcomingEvents(targovishteEvents);
                }
            }
        });

        // Iterate over filtered productions
        for (Production production : productions) {
            if (production.getUpcomingEvents() == null || production.getUpcomingEvents().isEmpty()) {
                continue;
            }

            List<Event> matchingEvents = eventsByProductionId.getOrDefault(production.getId(), Collections.emptyList());

            List<String> startDates = matchingEvents.stream()
                    .map(Event::getDateStart)
                    .collect(Collectors.toList());

            String largePoster = (production.getPhotos() != null && production.getPhotos().getPoster() != null)
                    ? production.getPhotos().getPoster().getLarge() : null;
            String mediumPoster = (production.getPhotos() != null && production.getPhotos().getPoster() != null)
                    ? production.getPhotos().getPoster().getMedium() : null;
            String smallPoster = (production.getPhotos() != null && production.getPhotos().getPoster() != null)
                    ? production.getPhotos().getPoster().getSmall() : null;

            String location = (production.getUpcomingEvents() != null && !production.getUpcomingEvents().isEmpty())
                    ? production.getUpcomingEvents().get(0).getLocation().getPlaceName()
                    : null;

            Play play = new Play();
            play.setId(production.getId());
            play.setLength(production.getLength());
            play.setMinAgeRestriction(production.getMinAgeRestriction());
            play.setTitle(production.getTitle());
            play.setLargePhoto(largePoster);
            play.setMediumPhoto(mediumPoster);
            play.setSmallPhoto(smallPoster);
            play.setPlaceName(location);
            play.setStartDates(startDates);

            playList.add(play);
        }

        return playList;
    }
}
