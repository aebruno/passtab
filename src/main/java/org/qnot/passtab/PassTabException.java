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

public class PassTabException extends Exception {

    private static final long serialVersionUID = 2451696483282692692L;

    public PassTabException() {
    }

    public PassTabException(String message) {
        super(message);
    }

    public PassTabException(Throwable cause) {
        super(cause);
    }

    public PassTabException(String message, Throwable cause) {
        super(message, cause);
    }

}
