package tcc.enterprise.fakenewsbot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcc.enterprise.fakenewsbot.Model.MessageCallBackModel;
import tcc.enterprise.fakenewsbot.services.ZapCallBaackService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/whatsApp-integration")
public class ZapCallBaackController {

    private static final Logger logger = Logger.getLogger(String.valueOf(ZapCallBaackController.class));

    @Autowired
    private ZapCallBaackService zapCallBaackService;

    @PostMapping("/webhook")
    public ResponseEntity<String> read(@RequestBody String inputJson) throws JsonProcessingException {

        MessageCallBackModel retornoCallback = zapCallBaackService.parseJson(inputJson);
        //logger.info(zapCallBaackService.find());
        logger.info("[JSON CALLBACK:]" + retornoCallback);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/webhook")
    public ResponseEntity<Integer> webhookAuthentication(@RequestParam(name = "hub.mode") String hubMode,
                                                        @RequestParam(name = "hub.challenge") Integer hubChallenge,
                                                        @RequestParam(name = "hub.verify_token") String hubVerifyToken) {

        logger.info("[WhatApp-HandShake]");
        Boolean handshake = zapCallBaackService.verifyHandShake(hubVerifyToken);
        if(handshake) {
            return ResponseEntity.ok(hubChallenge);
        }else{
            return ResponseEntity.status(403).body(-1);

        }
    }

//    @DeleteMapping
//    public ResponseEntity<Boolean> delete() {
//        try {
//            travelService.delete();
//            return ResponseEntity.noContent().build();
//        }catch(Exception e) {
//            logger.error(e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PostMapping
//    @ResponseBody
//    public ResponseEntity<Travel> create(@RequestBody JSONObject travel) {
//        try {
//            if(travelService.isJSONValid(travel.toString())) {
//                Travel travelCreated = travelService.create(travel);
//                var uri = ServletUriComponentsBuilder.fromCurrentRequest().path(travelCreated.getOrderNumber()).build().toUri();
//
//                if(travelService.isStartDateGreaterThanEndDate(travelCreated)){
//                    logger.error("The start date is greater than end date.");
//                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
//                }else {
//                    travelService.add(travelCreated);
//                    return ResponseEntity.created(uri).body(null);
//                }
//            }else {
//                return ResponseEntity.badRequest().body(null);
//            }
//        }catch(Exception e) {
//            logger.error("JSON fields are not parsable. " + e);
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
//        }
//    }
//
//    @PutMapping(path = "/{id}", produces = { "application/json" })
//    public ResponseEntity<Travel> update(@PathVariable("id") long id, @RequestBody JSONObject travel) {
//        try {
//            if(travelService.isJSONValid(travel.toString())) {
//                Travel travelToUpdate = travelService.findById(id);
//                if(travelToUpdate == null){
//                    logger.error("Travel not found.");
//                    return ResponseEntity.notFound().build();
//                }else {
//                    Travel travelToUpdate = travelService.update(travelToUpdate, travel);
//                    return ResponseEntity.ok(travelToUpdate);
//                }
//            }else {
//                return ResponseEntity.badRequest().body(null);
//            }
//        }catch(Exception e) {
//            logger.error("JSON fields are not parsable." + e);
//            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
//        }
//    }
}