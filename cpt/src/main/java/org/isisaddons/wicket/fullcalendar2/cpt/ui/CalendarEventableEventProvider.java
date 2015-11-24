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

import com.google.common.base.Function;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;

import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;

public class CalendarEventableEventProvider extends EventProviderAbstract {

    private static final long serialVersionUID = 1L;

    public CalendarEventableEventProvider(
            final EntityCollectionModel collectionModel,
            final String calendarName) {
        super(collectionModel, calendarName);
    }

    @Override
    protected List<CalendarEvent> calendarEventFor(Object domainObject, String calendarName) {
        final List<CalendarEventable> calendarEventables = (List<CalendarEventable>) domainObject;
        List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
        for (CalendarEventable calendarEventable : calendarEventables) {
            if (calendarName.equals(calendarEventable.getCalendarName())) {
                calendarEvents.add(calendarEventable.toCalendarEvent());
            }
        }
        return calendarEvents;
    }

    static final Function<ObjectAdapter, String> GET_CALENDAR_NAME = new Function<ObjectAdapter, String>() {
        @Override
        public String apply(final ObjectAdapter input) {
            final Object domainObject = input.getObject();
            if (domainObject == null || !(domainObject instanceof CalendarEventable)) {
                return null;
            }
            final CalendarEventable calendarEventable = (CalendarEventable) domainObject;
            if (calendarEventable == null) {
                return null;
            }
            return calendarEventable.getCalendarName();
        }
    };

}
