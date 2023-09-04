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
import tcc.enterprise.fakenewsbot.Model.MessageCallBackModel;
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

    public MessageCallBackModel parseJson(String inputJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        MessageCallBackModel callBackJson = objectMapper.readValue(inputJson, MessageCallBackModel.class);
        return callBackJson;
    }

    public String callBackHandler(MessageCallBackModel messageCallBackModel) throws URISyntaxException {
        Message message = messageCallBackModel.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0);

        if (message.getType() != MessageTypes.TEXT.getDescription()) {
            MediaUrl mediaUrl = getWhatsAppMediaUrl(message.getAudio().getId());
            byte[] media = downloadWhatsAppMedia(mediaUrl);
            sendMediaToRedeNeural(media);
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

    //TODO: TESTAR
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

        //Assertions.assertEquals(201, result.getStatusCodeValue());
        //Assertions.assertNotNull(result.getBody().getId());

        return "ok";
    }
    //   private List<Travel> travels;

//    public void createTravelFactory() {
//        if(factory == null) {
//            factory = new TravelFactoryImpl();
//        }
//    }
//
//    public void createTravelList() {
//        if(travels == null) {
//            travels = new ArrayList<>();
//        }
//    }
//
//    public boolean isJSONValid(String jsonInString) {
//        try {
//            return new ObjectMapper().readTree(jsonInString) != null;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//
//    private long parseId(JSONObject travel) {
//        return Long.valueOf((int) travel.get("id"));
//    }
//
//    private BigDecimal parseAmount(JSONObject travel) {
//        return new BigDecimal((String) travel.get("amount"));
//    }
//
//    private LocalDateTime parseStartDate(JSONObject travel) {
//        var startDate = (String) travel.get("startDate");
//        return ZonedDateTime.parse(startDate).toLocalDateTime();
//    }
//
//    private LocalDateTime parseEndDate(JSONObject travel) {
//        var endDate = (String) travel.get("endDate");
//        return ZonedDateTime.parse(endDate).toLocalDateTime();
//    }
//
//    public boolean isStartDateGreaterThanEndDate(Travel travel) {
//        if (travel.getEndDate() == null) return false;
//        return travel.getStartDate().isAfter(travel.getEndDate());
//    }
//
//    private void setTravelValues(JSONObject jsonTravel, Travel travel) {
//
//        String orderNumber = (String) jsonTravel.get("orderNumber");
//        String type = (String) jsonTravel.get("type");
//
//        travel.setOrderNumber(orderNumber != null ? orderNumber : travel.getOrderNumber());
//        travel.setAmount(jsonTravel.get("amount") != null ? parseAmount(jsonTravel) : travel.getAmount());
//        travel.setStartDate(jsonTravel.get("initialDate") != null ? parseStartDate(jsonTravel) : travel.getStartDate());
//        travel.setEndDate(jsonTravel.get("finalDate") != null ? parseEndDate(jsonTravel) : travel.getEndDate());
//        travel.setType(type != null ? TravelTypeEnum.getEnum(type) : travel.getType());
//    }
//
//    public Travel create(JSONObject jsonTravel) {
//
//        createFactory();
//
//        Travel travel = factory.createTravel((String) jsonTravel.get("type"));
//        travel.setId(parseId(jsonTravel));
//        setTravelValues(jsonTravel, travel);
//
//        return travel;
//    }
//
//    public Travel update(Travel travel, JSONObject jsonTravel) {
//
//        setTravelValues(jsonTravel, travel);
//        return travel;
//    }
//
//    public void add(Travel travel) {
//        createTravelList();
//        travels.add(travel);
//    }
//    public List<Travel> find() {
//        createTravelList();
//        return travels;
//    }
//    public Travel findById(long id) {
//        return travels.stream().filter(t -> id == t.getId()).collect(Collectors.toList()).get(0);
//    }
//
//    public void delete() {
//        travels.clear();
//    }
//
//    public void clearObjects() {
//        travels = null;
//        factory = null;
//    }
}