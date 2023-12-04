package tcc.enterprise.fakenewsbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tcc.enterprise.fakenewsbot.Model.Medias.MediaUrl;
import tcc.enterprise.fakenewsbot.Model.Message;
import tcc.enterprise.fakenewsbot.Model.MessageCallBack;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageAvulsa;
import tcc.enterprise.fakenewsbot.Model.Messages.Text;
import tcc.enterprise.fakenewsbot.util.enums.MessageTypes;

import java.io.*;
import java.net.*;
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
        Message message = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0);

        if (message.getType() != MessageTypes.TEXT.getDescription()) {
            MediaUrl mediaUrl = getWhatsAppMediaUrl(message.getAudio().getId());
            byte[] media = downloadWhatsAppMedia(mediaUrl);
            logger.info("mediaaaaaaaaaa" + media);


            String percentual = String.format("%.2f", sendMediaToRedeNeural(media));

            //TODO REMOVER QUANDO FOR IMPLEMENTANDO REDE NEURAL
            String phonenumberReciever = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id();

            logger.info("[phonenumberReciever]--phonenumberReciever: " + phonenumberReciever.toString());

            sendWhatsappMessage(phonenumberReciever, percentual);

            return "ok";
        }
        return "notMedia";

    }

    public MediaUrl getWhatsAppMediaUrl(String mediaId) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v17.0/" + mediaId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<MediaUrl> result = restTemplate.exchange(uri, HttpMethod.GET, entity, MediaUrl.class);

        return result.getBody();
    }

    public byte[] downloadWhatsAppMedia(MediaUrl mediaUrl) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(mediaUrl.getUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> result = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);

        return result.getBody();
    }

    public String sendMediaToRedeNeural(byte[] media) throws IOException {

        byte[] fileBytes = media;
        String uploadUrl = "http://67.205.179.72:5000/";

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
        return line;
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
    public String sendWhatsappMessage(String phoneNumberReciever, String percentual) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("https://graph.facebook.com/v17.0/" + phoneNumberSender + "/messages");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + permanentAcessToken2);
        headers.set("Content-Type", "application/json");

        MessageAvulsa messageAvulsa = new MessageAvulsa();
        Text text = new Text();
        text.setPreview_url(false);
        text.setBody("Percentual de acerto: " + percentual + "%");


        messageAvulsa.setText(text);
        messageAvulsa.setMessaging_product("whatsapp");
        messageAvulsa.setRecipient_type("individual");
        messageAvulsa.setTo(phoneNumberReciever);
        messageAvulsa.setType("text");

        HttpEntity<MessageAvulsa> entity = new HttpEntity<>(messageAvulsa, headers);


        ResponseEntity<MessageAvulsa> result = restTemplate.exchange(uri, HttpMethod.POST, entity, MessageAvulsa.class);

        return "ok";
    }
}