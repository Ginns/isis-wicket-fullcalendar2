/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.wicket.fullcalendar2.cpt.ui;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import net.ftlines.wicket.fullcalendar.Event;
import net.ftlines.wicket.fullcalendar.EventNotFoundException;
import net.ftlines.wicket.fullcalendar.EventProvider;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.*;

public abstract class EventProviderAbstract implements EventProvider {

    private static final long serialVersionUID = 1L;

    private final ListMultimap<String, Event> eventById = ArrayListMultimap.create();

    // //////////////////////////////////////

    public EventProviderAbstract(final EntityCollectionModel collectionModel, final String calendarName) {
        createEvents(collectionModel, calendarName);
    }

    private void createEvents(final EntityCollectionModel model, final String calendarName) {
        final Collection<ObjectAdapter> entityList = model.getObject();
        final Iterable<List<Event>> events = Iterables.filter(
                Iterables.transform(entityList, newEvent(calendarName)), NOT_NULL);
        for (final List<Event> eventList : events) {
            for (final Event event : eventList) {
                eventById.put(event.getId(), event);
            }
        }
    }

    protected abstract List<CalendarEvent> calendarEventFor(final Object domainObject, final String calendarName);

    private Function<ObjectAdapter, List<Event>> newEvent(final String calendarName) {
        return new Function<ObjectAdapter, List<Event>>() {

            public List<Event> apply(final ObjectAdapter input) {

                final Object domainObject = input.getObject();
                final List<CalendarEvent> calendarEvents = calendarEventFor(domainObject, calendarName);
                if (calendarEvents == null) {
                    return null;
                }

                List<CalendarEvent> calendarEventsForThisName = new ArrayList<CalendarEvent>();
                for (CalendarEvent calendarEvent : calendarEvents) {
                    if (calendarEvent.getCalendarName().equals(calendarName)) {
                        calendarEventsForThisName.add(calendarEvent);
                    }
                }

                List<Event> events = new ArrayList<Event>();
                for (CalendarEvent calendarEvent : calendarEventsForThisName) {
                    final Event event = new Event();

                    final DateTime start = calendarEvent.getDateTime();
                    final DateTime end = calendarEvent.getDateTime();

                    event.setStart(start);
                    event.setEnd(end);

                    final String oidStr = input.getOid().enString(IsisContext.getOidMarshaller());
                    event.setId(oidStr + "-" + calendarName);

                    event.setClassName("fullCalendar2-event-" + calendarName);
                    event.setEditable(false);
                    event.setPayload(oidStr);
                    event.setTitle(calendarEvent.getTitle());

                    if (calendarEvent.getEmptyPlaylist()) {
                        event.setColor("#BA6863");
                    }


                    events.add(event);
                    //event.setBackgroundColor(backgroundColor)
                    //event.setBorderColor(borderColor)
                    //event.setColor(color)
                    //event.setTextColor(textColor)
                    //event.setUrl(url)

                }

                return events;
            }

        };
    }

    final static Predicate<List<Event>> NOT_NULL = new Predicate<List<Event>>() {
        public boolean apply(List<Event> input) {
            return input != null;
        }
    };

    // //////////////////////////////////////

    class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event a, Event b) {
            String titleA = a.getTitle().substring(0, a.getTitle().length() - 18);
            String titleB = b.getTitle().substring(0, b.getTitle().length() - 18);

            if (titleA.equals(titleB)) {
                return a.getStart().compareTo(b.getStart());
            } else {
                return titleA.compareTo(titleB);
            }

        }
    }

    public Collection<Event> getEvents(final DateTime start, final DateTime end) {
        final Interval interval = new Interval(start, end);
        final Predicate<Event> withinInterval = new Predicate<Event>() {
            public boolean apply(Event input) {
                return interval.contains(input.getStart());
            }
        };
        final ArrayList<Event> values = new ArrayList<Event>(eventById.values());

        Collections.sort(values, new EventComparator());

        for (int i = 0; i < values.size(); i++) {
            Event event = values.get(i);
            if (i < (values.size() - 1)) {
                String thisTitle = event.getTitle().substring(0, event.getTitle().length() - 18);
                String nextTitle = values.get(i + 1).getTitle().substring(0, values.get(i + 1).getTitle().length() - 18);

                if (thisTitle.equals(nextTitle) && (values.get(i + 1).getStart().getMillis() - event.getStart().getMillis() < 86400000)) {
                    event.setEnd(values.get(i + 1).getStart().minusMillis(1));
                } else {
                    DateTime newEnd = event.getStart().plusDays(1).minusMillis(1);
                    event.setEnd(newEnd);
                }
            } else {
                DateTime newEnd = event.getStart().plusDays(1).minusMillis(1);
                event.setEnd(newEnd);
            }
        }

        return Collections2.filter(values, withinInterval);
    }

    public Event getEventForId(String id) throws EventNotFoundException {
        return eventById.get(id).get(0);
    }
}
