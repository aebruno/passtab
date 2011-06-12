/*
 * Copyright 2011 Andrew E. Bruno <aeb@qnot.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.qnot.passtab;

import java.util.ArrayList;
import java.util.List;

public class Sequence {
    public static String DEFAULT_SEQUENCE = "12:SW";
    
    private List<SequenceItem> items;
    
    public Sequence() {
        this.items = new ArrayList<SequenceItem>();
    }
    
    public void addItem(SequenceItem item) {
        this.items.add(item);
    }
    
    public List<SequenceItem> getItemList() {
        return this.items;
    }
    
    public static Sequence fromString(String seq) throws SequenceParseException {
        Sequence sequence = new Sequence();
        if(seq == null || seq.length() == 0) {
            throw new SequenceParseException("Empty sequence string");
        }
        
        String[] items = seq.split(",");
        if (items == null || items.length == 0) {
            throw new SequenceParseException("No sequence items found. Please be sure to separate items with a ','");
        }
        
        for(String i : items) {
            String[] spec = i.split(":");
            if (spec == null || spec.length != 2) {
                throw new SequenceParseException("Invalid sequence item. Format should be [length]:[direction]");
            }

            Direction direction = Direction.fromString(spec[1]);
            Integer len = null;
            try {
                len = Integer.valueOf(spec[0]);
            } catch(NumberFormatException e) {
                throw new SequenceParseException("Invalid sequence length. Please provide an integer");
            }
            
            sequence.addItem(new SequenceItem(direction, len.intValue()));
        }
        
        return sequence;
    }
}
