package org.isisaddons.wicket.fullcalendar2.fixture.dom;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.isis.applib.annotation.DomainService;

import javax.inject.Inject;
import java.util.Set;

@DomainService
public class FullCalendar2WicketAggregatedToDoItems {

    public FullCalendar2WicketAggregatedToDoItem aggregatedToDoItem() {

//        final Set<String> calendarNames =
//                FluentIterable
//                        .from(items.allToDos())
//                        .transform(FullCalendar2WicketToDoItem.Functions.getCalendarName())
//                        .toSet();
//        final ImmutableMap eventsByCalendarName =
//                Maps.uniqueIndex(
//                        items.allToDos(), FullCalendar2WicketToDoItem.Functions.getCalendarName());

        return null; //new FullCalendar2WicketAggregatedToDoItem(calendarNames, eventsByCalendarName);

    }

    @Inject
    private FullCalendar2WicketToDoItems items;

}
