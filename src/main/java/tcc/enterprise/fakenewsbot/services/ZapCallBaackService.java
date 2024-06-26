package tcc.enterprise.fakenewsbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tcc.enterprise.fakenewsbot.Model.Medias.MediaUrl;
import tcc.enterprise.fakenewsbot.Model.Message;
import tcc.enterprise.fakenewsbot.Model.MessageCallBack;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageAvulsa;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa.*;
import tcc.enterprise.fakenewsbot.Model.Text;
import tcc.enterprise.fakenewsbot.util.enums.Buttons;
import tcc.enterprise.fakenewsbot.util.enums.MessageIndex;
import tcc.enterprise.fakenewsbot.util.enums.MessageTypes;
import tcc.enterprise.fakenewsbot.util.enums.Respostas;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ZapCallBaackService {
    private static final Logger logger = Logger.getLogger(String.valueOf(ZapCallBaackService.class));
    @Value("${verify-token}")
    String envVerifyToken;
    @Value("${phone-number-sender}")
    String phoneNumberSender;
    @Value("${permanent-acess-token2}")
    String permanentAcessToken2;

    public boolean verifyHandShake(String hubVerifyToken) {

        if (hubVerifyToken.equals(envVerifyToken)) {
            return true;

        } else {
            return false;
        }
    }

    public MessageCallBack parseJson(String inputJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        MessageCallBack callBackJson = objectMapper.readValue(inputJson, MessageCallBack.class);
        return callBackJson;
    }
    @Async
    public void callBackHandler(MessageCallBack messageCallBack) throws URISyntaxException, IOException {
        // message = "true";
        Message message = null;
        String phonenumberReciever = null;
        if (messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getMessages() == null) {
            return;
        }
        if (messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getMessages() != null) {
            message = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0);
        }
        if (messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getContacts() != null) {
            phonenumberReciever = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();
        }
        String messagemEnviar = null;
        String messagemAguarde = null;
        String caseTexto = null;

        if (message != null) {
            logger.info("message-padrao: " + message.toString());
        }
        if (message.getType().equals(MessageTypes.INTERACTIVE.getDescription())) {
            caseTexto = message.getInteractive().getButton_reply().getId();
            logger.info("message Interactive: " + message);
            messagemEnviar = messageHandler(caseTexto);
            sendWhatsappMessage(phonenumberReciever, messagemEnviar);
            return;
        } else if (message.getType().equals(MessageTypes.TEXT.getDescription())) {
            caseTexto = message.getText().getBody();
            logger.info("messageText: " + message);
            messagemEnviar = messageHandler(caseTexto);
            sendWhatsappInteractiveMessage(phonenumberReciever, messagemEnviar);

        } else {
            caseTexto = "aguarde";
            messagemAguarde = messageHandler(caseTexto);
            sendWhatsappMessage(phonenumberReciever, messagemAguarde);
            caseTexto = "rede-neural";

            MediaUrl mediaUrl = null;
            String fileFormat = null;
            if (message.getType().equals(MessageTypes.DOCUMENT.getDescription())) {
                logger.info("media-id-document: " + message.getDocument().getId());
                mediaUrl = getWhatsAppMediaUrl(message.getDocument().getId());
                String[] parts = message.getDocument().getFilename().split("\\.");
                fileFormat = parts[parts.length - 1];
                logger.info("fileFormat: " + fileFormat);

            } else if (message.getType().equals(MessageTypes.VIDEO.getDescription())) {
                logger.info("media-id-video: " + message.getVideo().getId());
                mediaUrl = getWhatsAppMediaUrl(message.getVideo().getId());
                String[] parts = message.getVideo().getMime_type().split("/");
                fileFormat = parts[parts.length - 1];
                logger.info("fileFormat: " + fileFormat);

            } else {
                logger.info("media-id-audio: " + message.getAudio().getId());
                mediaUrl = getWhatsAppMediaUrl(message.getAudio().getId());
                if (message.getAudio().getMime_type().contains(";")) {
                    String[] mainTypeParts = message.getAudio().getMime_type().split(";"); // Split by semicolon to separate parameters
                    logger.info("mainTypeParts: " + mainTypeParts);
                    String[] typeSubtype = mainTypeParts[0].split("/"); // Split the actual MIME type by slash
                    logger.info("typeSubtype: " + typeSubtype);
                    fileFormat = typeSubtype[1];
                    logger.info("typeSubtype[1]: " + typeSubtype[1]);
                } else {
                    String[] parts = message.getAudio().getMime_type().split("/");
                    fileFormat = parts[parts.length - 1];
                }
                // fileFormat = "ogg";
                if (fileFormat.equals("mpeg")) {
                    fileFormat = "mp3";
                }
                logger.info("fileFormat-final: " + fileFormat);
            }

            byte[] media = downloadWhatsAppMedia(mediaUrl);
            logger.info("media" + media);

            Double percentual = sendMediaToRedeNeural(media, fileFormat);
            messagemEnviar = String.format(messageHandler(caseTexto), percentual);
            sendWhatsappMessage(phonenumberReciever, messagemEnviar);
            logger.info("[phonenumberReciever]--phonenumberReciever: " + phonenumberReciever.toString());
            //String percentualFormated = String.format("%.2f", percentual);
            return;
        }


        return;

    }

    private String messageHandler(String messageRecieved) throws URISyntaxException {
        StringBuilder stringBuilder = new StringBuilder();

        if (messageRecieved.equals(MessageIndex.ONE.getId())) {
            stringBuilder.append(Respostas.SITE.getText());
        } else if (messageRecieved.equals(MessageIndex.TWO.getId())) {
            stringBuilder.append(Respostas.SUPPORT.getText());
        } else if (messageRecieved.equals(MessageIndex.NINENINENINE.getId())) {
            stringBuilder.append(Respostas.REDE_NEURAL.getText());
        } else if (messageRecieved.equals(MessageIndex.NINENINEEIGTH.getId())) {
            stringBuilder.append(Respostas.AGUARDE.getText());
        } else {
            stringBuilder.append("Olá seja bem vindo ao MediaGuard! por favor selecione uma das opções abaixo! ou envie um áudio para verificação. " +
                    "Evite utilizar o microfone padrão do seu celular para enviar áudios a serem verificados pois isso pode afetar a performance.");
        }
        return stringBuilder.toString();
    }


    public ResponseEntity<Object> sendWhatsappInteractiveMessage(String phoneNumberReciever, String message) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v19.0/" + phoneNumberSender + "/messages");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);
        headers.set("Content-Type", "application/json");

        MessageInterativa messageInterativa = new MessageInterativa();
        messageInterativa.setMessaging_product("whatsapp");
        messageInterativa.setRecipient_type("individual");
        messageInterativa.setTo(phoneNumberReciever);
        messageInterativa.setType("interactive");

        Interactive interactive = new Interactive();

        interactive.setType("button");

        Body bodyInteractive = new Body();
        bodyInteractive.setText(message);

        interactive.setBody(bodyInteractive);

        Action action = new Action();
        List<Button> buttons = new ArrayList<>();

        for (Buttons button : Buttons.values()) {
            Button buttonStub = new Button();
            buttonStub.setType("reply");
            Reply reply = new Reply();
            reply.setId(button.getNome());
            reply.setTitle(button.getText());
            buttonStub.setReply(reply);
            buttons.add(buttonStub);
        }

        action.setButtons(buttons);

        interactive.setAction(action);
        messageInterativa.setInteractive(interactive);

        HttpEntity<MessageInterativa> entity = new HttpEntity<>(messageInterativa, headers);


        ResponseEntity<MessageInterativa> result = restTemplate.exchange(uri, HttpMethod.POST, entity, MessageInterativa.class);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> sendWhatsappMessage(String phoneNumberReciever, String message) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v19.0/" + phoneNumberSender + "/messages");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);
        headers.set("Content-Type", "application/json");

        MessageAvulsa messageAvulsa = new MessageAvulsa();
        Text text = new Text();

        text.setPreview_url(false);
        text.setBody(message);


        messageAvulsa.setText(text);
        messageAvulsa.setMessaging_product("whatsapp");
        messageAvulsa.setRecipient_type("individual");
        messageAvulsa.setTo(phoneNumberReciever);
        messageAvulsa.setType("text");

        HttpEntity<MessageAvulsa> entity = new HttpEntity<>(messageAvulsa, headers);


        ResponseEntity<MessageAvulsa> result = restTemplate.exchange(uri, HttpMethod.POST, entity, MessageAvulsa.class);

        return ResponseEntity.ok().build();
    }

    public MediaUrl getWhatsAppMediaUrl(String mediaId) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v19.0/" + mediaId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<MediaUrl> result = restTemplate.exchange(uri, HttpMethod.GET, entity, MediaUrl.class);
        logger.info("body de retorno de buscar url de media:" + result.getBody());
        return result.getBody();
    }

    public byte[] downloadWhatsAppMedia(MediaUrl mediaUrl) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(mediaUrl.getUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> result = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
        logger.info("body de retorno de download da media:" + result.getBody());
        return result.getBody();
    }


    public Double sendMediaToRedeNeural(byte[] media, String fileFormat) throws IOException {
        byte[] fileBytes = media;
        String uploadUrl = "http://104.131.190.85:5000/";

        // Create connection
        URL url = new URL(uploadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set connection properties
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=boundary");

        // Get the output stream of the connection
        OutputStream outputStream = connection.getOutputStream();


        writeFormField(outputStream, "audio", "uploaded_audio." + fileFormat, fileBytes);

        // Write the byte array to the output stream
        outputStream.write(fileBytes);

        // Close the output stream
        outputStream.close();

        // Get the response code
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: " + responseCode);

        String line = null;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //while (reader.readLine() != null) {
            line = reader.readLine();
            logger.info(reader.readLine());
            //  }

            reader.close();
        } else {
            logger.info("Error in HTTP request: " + responseCode);
        }


        // Close the connection
        connection.disconnect();

        try {
            // Convert the string to a double using Double.parseDouble
            double percentual = Double.parseDouble(line);
            logger.info("Converted to double: " + percentual);
            return percentual;
            // Print the result
        } catch (NumberFormatException e) {
            logger.info("Invalid number format");
        }
        return null;
    }

    private static void writeFormField(OutputStream outputStream, String fieldName, String fileName, byte[] fileBytes) throws IOException {
        String boundary = "boundary";
        String delimiter = "--" + boundary + "\r\n";
        String endDelimiter = "\r\n--" + boundary + "--\r\n";

        // Write the form field header
        outputStream.write(delimiter.getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        outputStream.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());

        // Write the file content
        outputStream.write(fileBytes);

        // Write the end delimiter
        outputStream.write(endDelimiter.getBytes());
    }
}