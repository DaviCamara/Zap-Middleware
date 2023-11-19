package tcc.enterprise.fakenewsbot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.enterprise.fakenewsbot.Model.MessageCallBack;
import tcc.enterprise.fakenewsbot.services.ZapCallBaackService;

import java.net.URISyntaxException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/whatsApp-integration")
public class ZapCallBaackController {

    private static final Logger logger = Logger.getLogger(String.valueOf(ZapCallBaackController.class));


    @GetMapping("/hello-world")
    public ResponseEntity<String> hellowWorld() {

        logger.info("hello-world");
        return ResponseEntity.ok("hello-world");
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> read(@RequestBody String inputJson) throws JsonProcessingException, URISyntaxException {

        MessageCallBack retornoCallback = zapCallBaackService.parseJson(inputJson);
        String retornoHandler = zapCallBaackService.callBackHandler(retornoCallback);

        //logger.info(zapCallBaackService.find());
        logger.info("[JSON CALLBACK]--CallBack: " + retornoCallback.toString());
        logger.info("[JSON retorno HANDLER]--HANDLER:" +retornoHandler);
        return ResponseEntity.ok("ok");
    }

    @Autowired
    private ZapCallBaackService zapCallBaackService;


    @GetMapping("/webhook")
    public ResponseEntity<Integer> webhookAuthentication(@RequestParam(name = "hub.mode") String hubMode,
                                                         @RequestParam(name = "hub.challenge") Integer hubChallenge,
                                                         @RequestParam(name = "hub.verify_token") String hubVerifyToken) {

        logger.info("[WhatApp-HandShake]--Challenge: " + hubChallenge);
        Boolean handshake = zapCallBaackService.verifyHandShake(hubVerifyToken);
        if (handshake) {
            return ResponseEntity.ok(hubChallenge);
        } else {
            return ResponseEntity.status(403).body(-1);

        }
    }

    @GetMapping("/rede-neural-retorno")
    public ResponseEntity<String> retornoRedeNeural(@RequestParam(name = "percentual") String percentual,
                                                    @RequestParam(name = "phoneNumber") String phoneNumberReciever)
            throws URISyntaxException {
        //zapCallBaackService.getWhatsAppMediaUrl()
        zapCallBaackService.sendWhatsappMessage(phoneNumberReciever, percentual);
        return ResponseEntity.ok("ok");
    }
}