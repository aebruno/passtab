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

public class SequenceParseException extends PasswordRectaException {

    private static final long serialVersionUID = -8532492297318885298L;

    public SequenceParseException() {
    }

    public SequenceParseException(String message) {
        super(message);
    }

    public SequenceParseException(Throwable cause) {
        super(cause);
    }

    public SequenceParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
