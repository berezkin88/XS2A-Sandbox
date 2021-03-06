package de.adorsys.ledgers.oba.rest.server.resource;

import de.adorsys.ledgers.middleware.api.domain.um.AccessTokenTO;
import de.adorsys.ledgers.oba.rest.server.config.cors.CookieConfigProperties;
import de.adorsys.ledgers.oba.service.api.domain.ConsentReference;
import de.adorsys.ledgers.oba.service.api.domain.OnlineBankingResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ResponseUtils {
    private static final String LOCATION_HEADER_NAME = "Location";
    public static final String CONSENT_COOKIE_NAME = "CONSENT";
    public static final String ACCESS_TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private final CookieConfigProperties cookieConfigProperties;

    /*
     * Set both access token cookie and consent cookie.
     *
     * @param response
     *
     * @param consentReference
     *
     * @param accessTokenString
     *
     * @param accessTokenTO
     */
    public void setCookies(HttpServletResponse response, ConsentReference consentReference, String accessTokenString, AccessTokenTO accessTokenTO) {

        int validity = cookieConfigProperties.getMaxAge();// default to five seconds.
        if (StringUtils.isNoneBlank(accessTokenString) && accessTokenTO != null) {
            long diffInMillies = Math.abs(new Date().getTime() - accessTokenTO.getExp().getTime());
            validity = ((Long) TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS)).intValue();
            // Set Cookie. Access Token
            Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessTokenString);
            accessTokenCookie.setHttpOnly(cookieConfigProperties.isHttpOnly());
            accessTokenCookie.setSecure(cookieConfigProperties.isSecure());
            accessTokenCookie.setMaxAge(validity);
            accessTokenCookie.setPath(cookieConfigProperties.getPath());
            response.addCookie(accessTokenCookie);
        } else {
            removeCookie(response, ACCESS_TOKEN_COOKIE_NAME);
        }

        if (consentReference != null && StringUtils.isNoneBlank(consentReference.getCookieString())) {
            // Set cookie consent
            Cookie consentCookie = new Cookie(CONSENT_COOKIE_NAME, consentReference.getCookieString());
            consentCookie.setHttpOnly(true);
            consentCookie.setSecure(cookieConfigProperties.isSecure());
            consentCookie.setMaxAge(validity);
            consentCookie.setPath(cookieConfigProperties.getPath());
            response.addCookie(consentCookie);
        }
    }

    public void removeCookies(HttpServletResponse response) {
        removeCookie(response, ACCESS_TOKEN_COOKIE_NAME);
        removeCookie(response, CONSENT_COOKIE_NAME);
    }

    private void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(cookieConfigProperties.isHttpOnly());
        cookie.setSecure(cookieConfigProperties.isSecure());
        cookie.setPath(cookieConfigProperties.getPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    public <T extends OnlineBankingResponse> ResponseEntity<T> redirect(String locationURI, HttpServletResponse httpResp) {
        HttpHeaders headers = new HttpHeaders();

        if (!UrlUtils.isAbsoluteUrl(locationURI)) {
            locationURI = "http://" + locationURI;
        }

        headers.add(LOCATION_HEADER_NAME, locationURI);
        removeCookies(httpResp);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    public String consentCookie(String cookieString) {
        return cookie(cookieString, CONSENT_COOKIE_NAME);
    }

    private String cookie(String cookieStringIn, String name) {
        String cookieString = cookieStringIn;
        if (cookieString == null) {
            return null;
        }

        String cookieParamName = name + "=";

        // Fix Java: rfc2965 want cookie to be separated by comma.
        // SOmehow i am receiving some semicolon separated cookies.
        // Quick Fix: First strip the preceeding cookies if not the first.
        if (!StringUtils.startsWithIgnoreCase(cookieString, cookieParamName)) {
            int indexOfIgnoreCase = StringUtils.indexOfIgnoreCase(cookieString, cookieParamName);
            cookieString = cookieString.substring(indexOfIgnoreCase);
        }
        // The proce
        List<HttpCookie> cookies = HttpCookie.parse(cookieString);
        for (HttpCookie httpCookie : cookies) {
            if (StringUtils.equalsIgnoreCase(httpCookie.getName(), name)) {
                return httpCookie.getValue();
            }
        }
        return null;
    }
}
