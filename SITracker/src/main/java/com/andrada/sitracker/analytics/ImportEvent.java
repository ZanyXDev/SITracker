/*
 * Copyright 2016 Gleb Godonoga.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andrada.sitracker.analytics;

import com.andrada.sitracker.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ImportEvent extends FBAEvent {

    public ImportEvent(boolean cancelled, Integer count) {
        super(Constants.GA_EVENT_AUTHOR_IMPORT);
        if (cancelled) {
            getParamMap().put("status", Constants.GA_EVENT_IMPORT_CANCELED);
        } else {
            getParamMap().put("status", Constants.GA_EVENT_IMPORT_COMPLETE);
            if (count != null) {
                getParamMap().put(FirebaseAnalytics.Param.VALUE, count.toString());
            }
        }


    }
}
