/*
 * Copyright 2015 Joao Vicente
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apm4all.tracy;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TracyEventParser {
    private String tracyEventString;

    public TracyEventParser(String tracyEventString) {
        this.tracyEventString = tracyEventString;
    }
    
    public TracyEvent parse()  {
        Map<String,String> map = new HashMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();
        TracyEvent tracyEvent = null;
     
        try {
            //convert JSON string to Map
            map = mapper.readValue(tracyEventString, new TypeReference<HashMap<String,String>>(){});
            if (isValidTracyEvent(map))    {
                tracyEvent = extractTracyEvent(map);
            }
        } catch (Exception e) {
            // Don't care why can't parse. Just return null for now.
            tracyEvent = null;
        }
        return tracyEvent;
    }

    private TracyEvent extractTracyEvent(Map<String, String> map) {
        //TODO: Refactor TracyEvent to include a builder
        //TODO: Add TracyEvent constants like TASK_ID_KEY = "taskId", etc
        TracyEvent tracyEvent = new TracyEvent(
            map.get("taskId"),
            map.get("label"), 
            map.get("parentOptId"), 
            map.get("optId"), 
            Long.parseLong(map.get("msecBefore")));
        map.remove("taskId");
        map.remove("label");
        map.remove("parentOptId");
        map.remove("optId");
        tracyEvent.setMsecAfter(Long.parseLong(map.get("msecAfter")));
        map.remove("msecAfter");
        tracyEvent.setMsecElapsed(Long.parseLong(map.get("msecElapsed")));
        map.remove("msecElapsed");
        tracyEvent.addAnnotation("host", map.get("host"));
        map.remove("host");
        tracyEvent.addAnnotation("host", map.get("component"));
        map.remove("component");
        return tracyEvent;
    }

    private boolean isValidTracyEvent(Map<String, String> map) {
        boolean validEvent = 
                map.containsKey("taskId")
                && map.containsKey("parentOptId")
                && map.containsKey("label")
                && map.containsKey("optId")
                && map.containsKey("msecBefore")
                && map.containsKey("msecAfter")
                && map.containsKey("msecElapsed")
                && map.containsKey("host")
                && map.containsKey("component");
        return validEvent;
    }
}
