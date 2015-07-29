package org.isisaddons.wicket.fullcalendar2.fixture.dom;

import com.google.common.collect.ImmutableMap;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.Calendarable;

import java.util.List;
import java.util.Set;

@ViewModel
public class FullCalendar2WicketAggregatedToDoItem implements Calendarable {

    private Set<String> calendarNames;

    private ImmutableMap<String, CalendarEventable> events;

    public FullCalendar2WicketAggregatedToDoItem() {
    }

//    public FullCalendar2WicketAggregatedToDoItem(Set<String> calendarNames, ImmutableMap<String, CalendarEventable> events) {
//        this.calendarNames = calendarNames;
//        this.events = events;
//    }

    @Override
    public Set<String> getCalendarNames() {
        return null;
    }

    @Override
    public ImmutableMap<String, List<? extends CalendarEventable>> getCalendarEvents() {
        return null;
    }
}
