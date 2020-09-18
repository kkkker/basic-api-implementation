package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.exception.EventIndexException;
import com.thoughtworks.rslist.exception.EventRangeException;
import com.thoughtworks.rslist.exception.ExceptionMessage;
import com.thoughtworks.rslist.service.RsEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    RsEventService rsEventService;

    @GetMapping("/rs/event/{index}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) throws EventIndexException {
        return ResponseEntity.ok().body(rsEventService.getRsEventById(index));
    }

    @GetMapping("/rs/event")
    public ResponseEntity<List<RsEvent>> getRsEventByRange(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) throws EventRangeException {
        List<RsEvent> rsList = rsEventService.getRsEventByRange(start, end);
        return ResponseEntity.ok().body(rsList.subList(start - 1, end));
    }

    @PostMapping("/rs/add/event")
    public ResponseEntity<Object> addOneRsEvent(@Valid @RequestBody RsEvent rsEvent) {

        int index = rsEventService.addOneRsEvent(rsEvent);
        if (index <= 0) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.status(201)
                .header("index", String.valueOf(index))
                .build();
    }

    @PutMapping("/rs/update/event/{id}")
    public ResponseEntity<Object> updateRsEventByIndex(@PathVariable Integer id, @NotEmpty @RequestBody RsEvent rsEvent) {
        if (!rsEventService.updateRsEventByIndex(id, rsEvent)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/rs/delete/event/{index}")
    public ResponseEntity<String> deleteRsEventByIndex(@PathVariable Integer index) {
        if (!rsEventService.deleteRsEventByIndex(index)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionMessage> handleException(Exception ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage();
        Logger logger = LoggerFactory.getLogger(RsController.class);
        if (ex instanceof MethodArgumentNotValidException) {
            exceptionMessage.setError("invalid param");
            logger.error(exceptionMessage.getError());
            return ResponseEntity.status(400).body(exceptionMessage);
        }
        logger.error(exceptionMessage.getError());
        return ResponseEntity.status(400).body(null);
    }
}
