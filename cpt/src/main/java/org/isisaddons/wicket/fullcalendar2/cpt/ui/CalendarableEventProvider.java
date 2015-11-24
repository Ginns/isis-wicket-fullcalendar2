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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;

import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.Calendarable;

public class CalendarableEventProvider extends EventProviderAbstract {

    private static final long serialVersionUID = 1L;

    public CalendarableEventProvider(
            final EntityCollectionModel collectionModel,
            final String calendarName) {
        super(collectionModel, calendarName);
    }

    @Override
    protected List<CalendarEvent> calendarEventFor(final Object domainObject, final String calendarName) {
        final Calendarable calendarable = (Calendarable)domainObject;
        final Map<String, List<? extends CalendarEventable>> calendarEventsMap = calendarable.getCalendarEvents();
        List<? extends CalendarEventable> calendarEventables;
        calendarEventables = calendarEventsMap.get(calendarName);
        if (calendarEventables == null && calendarName.equals("Main")) {
            calendarEventables = calendarEventsMap.get("Fillers");
        } else if (calendarEventables == null && calendarName.equals("Fillers")) {
            calendarEventables = calendarEventsMap.get("Main");
        }
        try {
            List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
            for (CalendarEventable calendarEventable : calendarEventables) {
                calendarEvents.add(calendarEventable.toCalendarEvent());
            }
            return calendarEvents != null
                    ? calendarEvents
                    : null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

    }

    static final Function<ObjectAdapter, Iterable<String>> GET_CALENDAR_NAMES = new Function<ObjectAdapter, Iterable<String>>() {
        @Override
        public Iterable<String> apply(final ObjectAdapter input) {
            final Object domainObject = input.getObject();
            if(domainObject == null || !(domainObject instanceof Calendarable)) {
                return null;
            }
            final Calendarable calendarable = (Calendarable) domainObject;
            return calendarable.getCalendarNames();
        }
    };


}
