package com.sgebs.eventmanager.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-eventmanagerApp-alert", message);
        headers.add("X-eventmanagerApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("eventmanagerApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("eventmanagerApp." + entityName + ".updated", param);
    }
    public static HttpHeaders createEntityUpdateWithActionAlert(String entityName,String action, String param) {
        return createAlert("eventmanagerApp." + entityName + "." + action, param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("eventmanagerApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-eventmanagerApp-error", "error." + errorKey);
        headers.add("X-eventmanagerApp-params", entityName);
        return headers;
    }
}
