package tcc.enterprise.fakenewsbot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcc.enterprise.fakenewsbot.services.ZapCallBaackService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/webhook")
public class ZapCallBaackController {

    private static final Logger logger = Logger.getLogger(String.valueOf(ZapCallBaackController.class));

    @Autowired
    private ZapCallBaackService zapCallBaackService;

    @PostMapping
    public ResponseEntity<String> find() {

        //logger.info(zapCallBaackService.find());
        return ResponseEntity.ok("ok");
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