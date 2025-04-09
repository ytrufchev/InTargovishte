package eu.trufchev.intargovishte.information.events.dramaTheatre.service;

import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Event;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Play;
import eu.trufchev.intargovishte.information.events.dramaTheatre.entities.Production;

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

        for (Production production : productions) {
            if (production.getUpcomingEvents().get(0).getLocation().getCityName().equals("Търговище")) {
                // Get events for the current production
                List<Event> matchingEvents = eventsByProductionId.getOrDefault(production.getId(), Collections.emptyList());

                // Extract start dates
                List<String> startDates = matchingEvents.stream()
                        .map(Event::getDateStart)
                        .collect(Collectors.toList());

                // Null checks for nested objects
                String largePoster = (production.getPhotos() != null && production.getPhotos().getPoster() != null)
                        ? production.getPhotos().getPoster().getLarge() : null;
                String mediumPoster = (production.getPhotos() != null && production.getPhotos().getPoster() != null)
                        ? production.getPhotos().getPoster().getMedium() : null;
                String smallPoster = (production.getPhotos() != null && production.getPhotos().getPoster() != null)
                        ? production.getPhotos().getPoster().getSmall() : null;

                // Get location (ensure upcomingEvents exists and has elements)
                String location = (production.getUpcomingEvents() != null && !production.getUpcomingEvents().isEmpty())
                        ? production.getUpcomingEvents().get(0).getLocation().getPlaceName()
                        : null;

                // Create the Play object
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
        }
            return playList;
        }
}
