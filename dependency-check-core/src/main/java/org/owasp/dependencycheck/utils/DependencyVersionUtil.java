/*
 * This file is part of dependency-check-core.
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
 *
 * Copyright (c) 2013 Jeremy Long. All Rights Reserved.
 */
package org.owasp.dependencycheck.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * A utility class to extract version numbers from file names (or other strings containing version numbers.</p>
 *
 * @author Jeremy Long <jeremy.long@owasp.org>
 */
public final class DependencyVersionUtil {

    /**
     * Regular expression to extract version numbers from file names.
     */
    private static final Pattern RX_VERSION = Pattern.compile("\\d+(\\.\\d{1,6})+(\\.?([_-](release|beta|alpha)|[a-zA-Z_-]{1,3}\\d{1,8}))?");
    /**
     * Regular expression to extract a single version number without periods. This is a last ditch effort just to check
     * in case we are missing a version number using the previous regex.
     */
    private static final Pattern RX_SINGLE_VERSION = Pattern.compile("\\d+(\\.?([_-](release|beta|alpha)|[a-zA-Z_-]{1,3}\\d{1,8}))?");

    /**
     * Private constructor for utility class.
     */
    private DependencyVersionUtil() {
    }

    /**
     * <p>
     * A utility class to extract version numbers from file names (or other strings containing version numbers.<br/>
     * Example:<br/>
     * Give the file name: library-name-1.4.1r2-release.jar<br/>
     * This function would return: 1.4.1.r2</p>
     *
     * @param text the text being analyzed
     * @return a DependencyVersion containing the version
     */
    public static DependencyVersion parseVersion(String text) {
        if (text == null) {
            return null;
        }
        //'-' is a special case used within the CVE entries, just include it as the version.
        if ("-".equals(text)) {
            final DependencyVersion dv = new DependencyVersion();
            final ArrayList<String> list = new ArrayList<String>();
            list.add(text);
            dv.setVersionParts(list);
            return dv;
        }
        String version = null;
        Matcher matcher = RX_VERSION.matcher(text);
        if (matcher.find()) {
            version = matcher.group();
        }
        //throw away the results if there are two things that look like version numbers
        if (matcher.find()) {
            return null;
        }
        if (version == null) {
            matcher = RX_SINGLE_VERSION.matcher(text);
            if (matcher.find()) {
                version = matcher.group();
            } else {
                return null;
            }
            //throw away the results if there are two things that look like version numbers
            if (matcher.find()) {
                return null;
            }
        }
        return new DependencyVersion(version);
    }
}
