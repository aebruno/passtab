/*
 * Copyright (C) 2011  Andrew E. Bruno <aeb@qnot.org>
 *
 * This file is part of passtab.
 *
 * passtab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * passtab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with passtab.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.qnot.passtab;

import java.util.ArrayList;
import java.util.List;

/**
 * The Sequence used for generating a password. A Sequence is list of SequenceItem's
 * 
 * @author Andrew E. Bruno <aeb@qnot.org>
 */
public class Sequence {
    public static String DEFAULT_SEQUENCE = "12:SE";
    
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
