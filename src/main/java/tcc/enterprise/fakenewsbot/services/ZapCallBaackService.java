package tcc.enterprise.fakenewsbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tcc.enterprise.fakenewsbot.Model.Medias.MediaUrl;
import tcc.enterprise.fakenewsbot.Model.Message;
import tcc.enterprise.fakenewsbot.Model.MessageCallBack;
import tcc.enterprise.fakenewsbot.Model.Messages.MessageAvulsa;
import tcc.enterprise.fakenewsbot.Model.Messages.Text;
import tcc.enterprise.fakenewsbot.util.enums.MessageTypes;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ZapCallBaackService {
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

    public String callBackHandler(MessageCallBack messageCallBack) throws URISyntaxException {
        Message message = messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0);

        if (message.getType() != MessageTypes.TEXT.getDescription()) {
            MediaUrl mediaUrl = getWhatsAppMediaUrl(message.getAudio().getId());
            byte[] media = downloadWhatsAppMedia(mediaUrl);
            sendMediaToRedeNeural(media);

            sendWhatsappMessage(messageCallBack.getEntry().get(0).getChanges().get(0).getValue().getContacts().get(0).getWa_id(), "95");

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
    private void sendMediaToRedeNeural(byte[] media) {
        //TODO: REST CALL TO REDE NEURAL.
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