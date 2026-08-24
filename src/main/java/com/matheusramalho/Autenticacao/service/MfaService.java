package com.matheusramalho.Autenticacao.service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import org.springframework.stereotype.Service;

/**
 * MFA/TOTP (B6), obrigatorio so para o papel ADMIN. Usa a biblioteca
 * dev.samstevens.totp -- o mesmo algoritmo do Google Authenticator/Authy.
 *
 * Fluxo: gerarNovoSecret() + gerarQrCodeBase64() no POST /mfa/setup ->
 * usuario escaneia no app -> POST /mfa/ativar confirma com o primeiro
 * codigo -> so entao mfaAtivo vira true. Depois disso, todo login desse
 * usuario passa por verificarCodigo().
 */
@Service
public class MfaService {

    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeVerifier codeVerifier =
            new DefaultCodeVerifier(new DefaultCodeGenerator(HashingAlgorithm.SHA1), timeProvider);

    public String gerarNovoSecret(){
        return secretGenerator.generate();
    }

    public String gerarQrCodeBase64(String username, String secret) {
        QrData data = new QrData.Builder()
                .label(username)
                .secret(secret)
                .issuer("BatataAuth")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        try {
            byte[] imagemPng = qrGenerator.generate(data);
            return Utils.getDataUriForImage(imagemPng, qrGenerator.getImageMimeType())
                    .replaceFirst("^data:image/png;base64,", "");
        } catch (QrGenerationException e) {
            throw new RuntimeException("Falha ao gerar QR Code do MFA.", e);
        }
    }

    public boolean verificarCodigo(String secret, String codigo) {
        if (secret == null || codigo == null || codigo.isBlank()) {
            return false;
        }
        return codeVerifier.isValidCode(secret, codigo);
    }
}
