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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class TracyGraph {
    private DirectedGraph<TracyEvent,DefaultEdge> graph = new DefaultDirectedGraph<TracyEvent, DefaultEdge>(DefaultEdge.class);
    private HashMap<String, TracyEvent> optIdToTracyEventMap = new HashMap<String, TracyEvent>();
    private List<String> duplicateOptIds = new ArrayList<String>();
    private Set<String> taskIds = new HashSet<String>();
    private TracyEvent rootTracyEvent = null;
    
    public void add(TracyEvent tracyEvent)  {
        graph.addVertex(tracyEvent);
        if (optIdToTracyEventMap.containsKey(tracyEvent.getOptId()))    {
            duplicateOptIds.add(tracyEvent.getOptId());
        }
        taskIds.add(tracyEvent.getTaskId());
        optIdToTracyEventMap.put(tracyEvent.getOptId(), tracyEvent);
    }
    
    public void build() {
        Iterator<TracyEvent> iterator = graph.vertexSet().iterator();
        while (iterator.hasNext())  {
            TracyEvent tracyEvent = iterator.next();
            TracyEvent parentOptTracyEvent = optIdToTracyEventMap.get(tracyEvent.getParentOptId());
            if (parentOptTracyEvent != null)    {
                graph.addEdge(parentOptTracyEvent, tracyEvent);
            }
            else {
                rootTracyEvent = tracyEvent;
            }
        }
        // TODO: throwException if graph !isValid()
    }
  
    public boolean isValid()    {
        int nodesWithoutParentCount = 0;
        int nodesWithoutParentOrChildrenCount = 0;
        
        for (TracyEvent tracyEvent : optIdToTracyEventMap.values()) {
            boolean hasParent = graph.incomingEdgesOf(tracyEvent).size() > 0;
            boolean hasChildren = graph.outgoingEdgesOf(tracyEvent).size() > 0;
            if (false == hasParent)    {
                nodesWithoutParentCount++;
            }
            if (false == hasParent && false == hasChildren) {
                nodesWithoutParentOrChildrenCount++;
            }
        }
        // Single taskId
        // No duplicate optIds
        // Has a single root
        // Each node has either parent or children
        return (
                1 == taskIds.size()
                && 0 == duplicateOptIds.size()
                && 1 == nodesWithoutParentCount && rootTracyEvent != null
                && 0 == nodesWithoutParentOrChildrenCount);
    }
    
    public String toString()  {
        return graph.toString();
    }
    
}
