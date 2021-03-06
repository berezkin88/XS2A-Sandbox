package de.adorsys.psd2.sandbox.tpp.rest.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.ledgers.middleware.api.domain.account.AccountDetailsTO;
import de.adorsys.ledgers.middleware.api.domain.payment.PaymentTO;
import de.adorsys.ledgers.middleware.api.domain.um.UserTO;
import de.adorsys.ledgers.middleware.client.rest.UserMgmtRestClient;
import de.adorsys.psd2.sandbox.tpp.rest.server.exception.TppException;
import de.adorsys.psd2.sandbox.tpp.rest.server.model.AccountBalance;
import de.adorsys.psd2.sandbox.tpp.rest.server.model.DataPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestsDataGenerationServiceTest {
    private static final String TPP_ID = "DE_12345678";
    private static final String USER_IBAN = "DE89000000115555555555";
    private static final Currency CURRENCY = Currency.getInstance("EUR");

    @InjectMocks
    private TestsDataGenerationService generationService;
    @Mock
    private ParseService parseService;
    @Mock
    private RestExecutionService executionService;
    @Mock
    private UserMgmtRestClient userMgmtRestClient;
    @Mock
    private IbanGenerationService ibanGenerationService;

    private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    @Test
    void generate() throws JsonProcessingException {
        // Given
        when(userMgmtRestClient.getUser()).thenReturn(ResponseEntity.ok(new UserTO(null, null, null, null, null, emptyList(), null, TPP_ID, false, false)));
        when(ibanGenerationService.generateIbanForNisp(any(), any())).thenReturn(USER_IBAN);
        when(parseService.generateFileByPayload(any())).thenReturn(getBytes());
        when(parseService.getDefaultData()).thenReturn(Optional.of(getPayload()));

        // When
        byte[] generatedData = generationService.generate(true, CURRENCY);

        // Then
        assertNotEquals(0, generatedData.length);
    }

    @Test
    void generate_noBranch() {
        //given
        when(userMgmtRestClient.getUser()).thenReturn(ResponseEntity.ok(new UserTO(null, null, null, null, null, emptyList(), null, null, false, false)));

        // Then
        assertThrows(TppException.class, () -> generationService.generate(true, CURRENCY));
    }

    private byte[] getBytes() throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(getPayload());
    }

    private DataPayload getPayload() {
        List<UserTO> users = singletonList(new UserTO("login", "email", "pin"));
        List<AccountDetailsTO> accounts = singletonList(new AccountDetailsTO());
        List<AccountBalance> balances = singletonList(new AccountBalance());
        List<PaymentTO> payments = singletonList(new PaymentTO());
        return new DataPayload(users, accounts, balances, payments, false, TPP_ID, new HashMap<>());
    }
}
