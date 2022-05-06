package com.goody.utils.baihao.util;

/**
 * basic utils
 *
 * @author Goody
 * @version 1.0, 2022/5/6
 * @since 1.0.0
 */
public class CommonUtils {

    /**
     * check if input array is empty
     *
     * @param params array
     * @param <T>    any type
     * @return true -> empty ; false -> not empty
     */
    public static <T> boolean isArrayEmpty(T[] params) {
        return params == null || params.length == 0;
    }

    /**
     * check if input string is blank
     *
     * @param str string
     * @return true -> blank ; false -> not blank
     */
    public static boolean isStringBlank(String str) {
        return null == str || str.length() == 0;
    }

    /**
     * <p>Uncapitalizes a String, changing the first character to lower case as
     * per {@link Character#toLowerCase(int)}. No other characters are changed.</p>
     *
     * <p>For a word based algorithm, see {@link org.apache.commons.lang3.text.WordUtils#uncapitalize(String)}.
     * A {@code null} input String returns {@code null}.</p>
     *
     * <pre>
     * StringUtils.uncapitalize(null)  = null
     * StringUtils.uncapitalize("")    = ""
     * StringUtils.uncapitalize("cat") = "cat"
     * StringUtils.uncapitalize("Cat") = "cat"
     * StringUtils.uncapitalize("CAT") = "cAT"
     * </pre>
     *
     * @param str the String to uncapitalize, may be null
     * @return the uncapitalized String, {@code null} if null String input
     * @see org.apache.commons.lang3.text.WordUtils#uncapitalize(String)
     * @see #capitalize(String)
     * @since 2.0
     */
    public static String uncapitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toLowerCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int newCodePoints[] = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }
}
