package com.ayaan.chiragfarmer.utils

import java.util.regex.Pattern

/**
 * Utility object for input validation using regex patterns.
 *
 * Provides validation functions for:
 * - Mobile number validation (Indian format)
 * - Email address validation
 *
 * Usage Examples:
 * ```kotlin
 * // Validate mobile number
 * val isValid = ValidationUtils.isValidMobileNumber("9876543210")
 *
 * // Validate email
 * val isValidEmail = ValidationUtils.isValidEmail("john@example.com")
 * ```
 */
object ValidationUtils {

    /**
     * Regex pattern for Indian mobile number validation.
     *
     * Rules:
     * - Must be exactly 10 digits
     * - Should start with 6, 7, 8, or 9
     * - Only numeric characters allowed
     *
     * Valid examples: 9876543210, 8123456789, 7012345678
     * Invalid examples: 5123456789, 98765432, 98765432109, +919876543210
     */
    private const val MOBILE_NUMBER_PATTERN = "^[6-9][0-9]{9}$"

    /**
     * Regex pattern for email validation.
     *
     * Rules:
     * - Must have a local part (before @)
     * - Must have @ symbol
     * - Must have a domain name
     * - Must have a valid TLD (top-level domain)
     *
     * Valid examples: john@example.com, user.name@domain.co.in, test123@test-domain.com
     * Invalid examples: invalid.email, @domain.com, user@, user@domain
     */
    private const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"

    /**
     * Validates if the given mobile number is in valid Indian format.
     *
     * @param mobileNumber The mobile number string to validate
     * @return true if the mobile number is valid, false otherwise
     *
     * @example
     * ```kotlin
     * ValidationUtils.isValidMobileNumber("9876543210") // returns true
     * ValidationUtils.isValidMobileNumber("123456789")  // returns false (less than 10 digits)
     * ValidationUtils.isValidMobileNumber("5876543210") // returns false (starts with 5)
     * ValidationUtils.isValidMobileNumber("98765432109") // returns false (more than 10 digits)
     * ```
     */
    fun isValidMobileNumber(mobileNumber: String?): Boolean {
        if (mobileNumber.isNullOrBlank()) return false
        return Pattern.matches(MOBILE_NUMBER_PATTERN, mobileNumber.trim())
    }

    /**
     * Validates if the given email address is in valid format.
     *
     * @param email The email address string to validate
     * @return true if the email is valid, false otherwise
     *
     * @example
     * ```kotlin
     * ValidationUtils.isValidEmail("john@example.com")     // returns true
     * ValidationUtils.isValidEmail("user.name@domain.co.in") // returns true
     * ValidationUtils.isValidEmail("invalid.email")        // returns false
     * ValidationUtils.isValidEmail("@domain.com")          // returns false
     * ValidationUtils.isValidEmail("user@")                // returns false
     * ```
     */
    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return Pattern.matches(EMAIL_PATTERN, email.trim())
    }

    /**
     * Formats a mobile number by removing any spaces, hyphens, or special characters.
     * Useful for cleaning user input before validation.
     *
     * @param mobileNumber The mobile number string to clean
     * @return Cleaned mobile number with only digits
     *
     * @example
     * ```kotlin
     * ValidationUtils.cleanMobileNumber("987-654-3210")  // returns "9876543210"
     * ValidationUtils.cleanMobileNumber("987 654 3210")  // returns "9876543210"
     * ValidationUtils.cleanMobileNumber("+91 9876543210") // returns "919876543210"
     * ```
     */
    fun cleanMobileNumber(mobileNumber: String?): String {
        if (mobileNumber.isNullOrBlank()) return ""
        return mobileNumber.replace(Regex("[^0-9]"), "")
    }

    /**
     * Validates and returns error message for mobile number.
     *
     * @param mobileNumber The mobile number to validate
     * @return Error message if invalid, null if valid
     *
     * @example
     * ```kotlin
     * val error = ValidationUtils.getMobileNumberError("123")
     * // returns "Mobile number must be 10 digits"
     * ```
     */
    fun getMobileNumberError(mobileNumber: String?): String? {
        return when {
            mobileNumber.isNullOrBlank() -> "Mobile number is required"
            mobileNumber.length < 10 -> "Mobile number must be 10 digits"
            mobileNumber.length > 10 -> "Mobile number must be 10 digits"
            !mobileNumber[0].isDigit() || mobileNumber[0] !in '6'..'9' ->
                "Mobile number must start with 6, 7, 8, or 9"
            !isValidMobileNumber(mobileNumber) -> "Please enter a valid mobile number"
            else -> null
        }
    }

    /**
     * Validates and returns error message for email.
     *
     * @param email The email to validate
     * @return Error message if invalid, null if valid
     *
     * @example
     * ```kotlin
     * val error = ValidationUtils.getEmailError("invalid")
     * // returns "Please enter a valid email address"
     * ```
     */
    fun getEmailError(email: String?): String? {
        return when {
            email.isNullOrBlank() -> "Email is required"
            !email.contains("@") -> "Email must contain @"
            !email.contains(".") -> "Email must contain a domain"
            !isValidEmail(email) -> "Please enter a valid email address"
            else -> null
        }
    }

    /**
     * Extension function for String to validate as mobile number.
     *
     * @example
     * ```kotlin
     * "9876543210".isValidMobile() // returns true
     * ```
     */
    fun String.isValidMobile(): Boolean = isValidMobileNumber(this)

    /**
     * Extension function for String to validate as email.
     *
     * @example
     * ```kotlin
     * "john@example.com".isValidEmail() // returns true
     * ```
     */
    fun String.isValidEmailAddress(): Boolean = isValidEmail(this)
}

