package tcc.enterprise.fakenewsbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tcc.enterprise.fakenewsbot.Model.Medias.MediaUrl;
import tcc.enterprise.fakenewsbot.Model.Message;
import tcc.enterprise.fakenewsbot.Model.MessageCallBack;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageAvulsa;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageInterativa.*;
import tcc.enterprise.fakenewsbot.Model.Text;
import tcc.enterprise.fakenewsbot.util.enums.MessageIndex;
import tcc.enterprise.fakenewsbot.util.enums.MessageTypes;

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
    @Value("${permanent-acess-token}")
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

    public String callBackHandler(MessageCallBack messageCallBack) throws URISyntaxException, IOException {
        // message = "true";
        Message message = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0);
        String phonenumberReciever = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();
        String messagemEnviar = null;
        String caseTexto = null;

        logger.info("message-padrao: " + message.toString());
        if (!message.getType().equals(MessageTypes.TEXT.getDescription())) {
            caseTexto = "4";
            logger.info("media-id: " +message.getAudio().getId());
            MediaUrl mediaUrl = getWhatsAppMediaUrl(message.getAudio().getId());

            byte[] media = downloadWhatsAppMedia(mediaUrl);
            logger.info("media" + media);

            Double percentual = sendMediaToRedeNeural(media);

            messagemEnviar = String.format(messageHandler(caseTexto), percentual);
            sendWhatsappMessage(phonenumberReciever, messagemEnviar);
            logger.info("[phonenumberReciever]--phonenumberReciever: " + phonenumberReciever.toString());
            //String percentualFormated = String.format("%.2f", percentual);
            return "enviouMedia";
        }
        if(message.getType().equals(MessageTypes.TEXT.getDescription())){
            caseTexto = message.getText().getBody();
            logger.info("message: " +message.getText().getBody());
            messagemEnviar =  messageHandler(caseTexto);
            sendWhatsappInteractiveMessage(phonenumberReciever, messagemEnviar);
        }


        return "ok";

    }

    private String messageHandler(String messageRecieved) throws URISyntaxException {
        StringBuilder stringBuilder = new StringBuilder();

        if(messageRecieved.equals(MessageIndex.ONE.getText())) {
            System.out.println("ASD");
        } else if (messageRecieved.equals(MessageIndex.TWO.getText())){
            System.out.println("ASD");
        } else if(messageRecieved.equals(MessageIndex.THREE.getText())){
            System.out.println("ASD");
        }else if(messageRecieved.equals(MessageIndex.FOUR.getText())){
            stringBuilder.append("Percentual de acerto: %.2f%%");
        }
        else {
            stringBuilder.append("Olá seja bem vindo ao MediaGuard! por favor selecione uma das opções abaixo! ou envie um áudio para verificação");
        }
        return stringBuilder.toString();
    }


   public String sendWhatsappInteractiveMessage(String phoneNumberReciever, String message) throws URISyntaxException {
       RestTemplate restTemplate = new RestTemplate();
       URI uri = new URI("https://graph.facebook.com/v17.0/" + phoneNumberSender + "/messages");

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
       bodyInteractive.setText("teste");

       interactive.setBody(bodyInteractive);

       Action action = new Action();
       List<Button> buttons = new ArrayList<>();

       Button button = new Button();

       button.setType("reply");

       Reply reply = new Reply();
       reply.setId("teste2");
       reply.setTitle("teste3");

       button.setReply(reply);
       buttons.add(button);

       action.setButtons(buttons);

       interactive.setAction(action);
       messageInterativa.setInteractive(interactive);

       HttpEntity<MessageInterativa> entity = new HttpEntity<>(messageInterativa, headers);


       ResponseEntity<MessageAvulsa> result = restTemplate.exchange(uri, HttpMethod.POST, entity, MessageAvulsa.class);

       return "ok";
   }
    public String sendWhatsappMessage(String phoneNumberReciever, String message) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v17.0/" + phoneNumberSender + "/messages");

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

        return "ok";
    }

    public MediaUrl getWhatsAppMediaUrl(String mediaId) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v17.0/" + mediaId);

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


    public Double sendMediaToRedeNeural(byte[] media) throws IOException {
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

        writeFormField(outputStream, "audio", "uploaded_audio.ogg", fileBytes);

        // Write the byte array to the output stream
        outputStream.write(fileBytes);

        // Close the output stream
        outputStream.close();

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        String line = null;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //while (reader.readLine() != null) {
             line = reader.readLine();
                System.out.println(reader.readLine());
          //  }

            reader.close();
        } else {
            System.out.println("Error in HTTP request: " + responseCode);
        }


        // Close the connection
        connection.disconnect();

        try {
            // Convert the string to a double using Double.parseDouble
            double percentual = Double.parseDouble(line);
            System.out.println("Converted to double: " + percentual);
            return percentual;
            // Print the result
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
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
    //TODO: MODULARIZAR O PARAMETRO DE TEXTO, AVERIGAR SE O NUMERO de telefone DEVERIA VIR DA REDE NEURAL
}