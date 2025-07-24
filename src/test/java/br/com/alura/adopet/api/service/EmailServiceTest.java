package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    @DisplayName("Deve enviar EMAIL com dados corretos")
    void enviarEmail01() {
        // Arrange
        String to = "destinatario@teste.com";
        String subject = "Assunto do Teste";
        String body = "Corpo da mensagem";

        // Act
        emailService.enviarEmail(to, subject, body);

        // Assert
        verify(emailSender).send(messageCaptor.capture());

        SimpleMailMessage emailEnviado = messageCaptor.getValue();
        assertEquals("adopet@email.com.br", emailEnviado.getFrom());
        assertEquals(to, emailEnviado.getTo()[0]);
        assertEquals(subject, emailEnviado.getSubject());
        assertEquals(body, emailEnviado.getText());
    }
}