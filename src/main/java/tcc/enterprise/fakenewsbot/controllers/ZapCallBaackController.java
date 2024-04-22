package tcc.enterprise.fakenewsbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tcc.enterprise.fakenewsbot.Model.MessageCallBack;
import tcc.enterprise.fakenewsbot.services.ZapCallBaackService;

import java.io.IOException;
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
    public ResponseEntity<String> read(@RequestBody String inputJson) throws IOException, URISyntaxException {

        MessageCallBack retornoCallback = zapCallBaackService.parseJson(inputJson);
        logger.info("retornoCallback: " +  retornoCallback);
        String retornoHandler = zapCallBaackService.callBackHandler(retornoCallback);
        logger.info("retornoHandler: " + retornoHandler);

        //logger.info(zapCallBaackService.find());
        logger.info("[JSON CALLBACK]--CallBack: " + retornoCallback.toString());
        logger.info("[JSON retorno HANDLER]--HANDLER:" + retornoHandler);
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


    @PostMapping("/teste-rede-neural")
    public ResponseEntity<String> read(@RequestParam("audio") MultipartFile media) throws IOException {
        media.getName();
        byte[] strToBytes = media.getBytes();

        String percentual = String.format("%.2f", zapCallBaackService.sendMediaToRedeNeural(strToBytes));
        // String percentual =  zapCallBaackService.sendMediaToRedeNeural(strToBytes);

        return ResponseEntity.ok(percentual);
    }
//    @GetMapping("/rede-neural-retorno")
//    public ResponseEntity<String> retornoRedeNeural(@RequestParam(name = "percentual") String percentual,
//                                                    @RequestParam(name = "phoneNumber") String phoneNumberReciever)
//            throws URISyntaxException {
//        //zapCallBaackService.getWhatsAppMediaUrl()
//        zapCallBaackService.sendWhatsappMessage(phoneNumberReciever, percentual);
//        return ResponseEntity.ok("ok");
//    }
}