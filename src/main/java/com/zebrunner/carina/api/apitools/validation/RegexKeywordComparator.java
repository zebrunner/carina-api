/*******************************************************************************
 * Copyright 2020-2022 Zebrunner Inc (https://www.zebrunner.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zebrunner.carina.api.apitools.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class RegexKeywordComparator implements JsonKeywordComparator {

    @Override
    public void compare(String prefix, Object expectedValue, Object actualValue, JsonCompareResultWrapper result) {
        if (actualValue instanceof Number || actualValue instanceof String) {
            String actualStr = actualValue.toString();
            String regex = expectedValue.toString().replace(JsonCompareKeywords.REGEX.getKey(), "");
            Matcher m = Pattern.compile(regex).matcher(actualStr);
            if (!m.find()) {
                result.fail(String.format("%s\nActual value '%s' doesn't match to expected regex '%s'\n", prefix, actualStr, regex));
            }
        } else {
            result.compareByDefault(prefix, expectedValue, actualValue);
        }
    }

    @Override
    public boolean isMatch(Object expectedValue) {
        return expectedValue.toString().startsWith(JsonCompareKeywords.REGEX.getKey());
    }
}
